/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `PermissaoResource`, é um recurso JAX-RS que define a API REST para o
 * gerenciamento de Permissões do sistema. Permissões são a base do controle de acesso
 * e autorização, definindo o que um usuário pode ou não fazer na aplicação.
 *
 * A classe implementa um CRUD (Criar, Ler, Atualizar, Deletar) completo para o recurso
 * de permissões, acessível através do caminho base "/permissoes", com comunicação em JSON.
 *
 * Um destaque nesta classe é o método `getAllPermissoes`, que aceita um parâmetro de
 * busca (`@QueryParam("search")`). Isso permite que a API filtre as permissões
 * retornadas, tornando-a mais flexível e eficiente (ex: `/permissoes?search=admin`).
 *
 * A arquitetura utiliza o padrão DAO para acesso aos dados e DTO para a camada de API,
 * com métodos `toDTO` e `fromDTO` para realizar as conversões, garantindo um bom
 * desacoplamento e uma clara separação de responsabilidades.
 * =================================================================================================
 */
package com.unifae.med.rest;

import com.unifae.med.dao.PermissaoDAO;
import com.unifae.med.entity.Permissao;
import com.unifae.med.rest.dto.PermissaoDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/permissoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PermissaoResource {

    // Instância do DAO para interagir com a tabela de permissões no banco de dados.
    private final PermissaoDAO permissaoDAO = new PermissaoDAO();

    /**
     * Endpoint para listar todas as permissões, com suporte a filtro. Mapeado
     * para: GET /permissoes e GET /permissoes?search=termo
     *
     * @param search Um termo opcional de busca para filtrar as permissões.
     * @return Uma lista de PermissaoDTO.
     */
    @GET
    public List<PermissaoDTO> getAllPermissoes(@QueryParam("search") String search) {
        // Delega a lógica de busca para o DAO e extrai a lista do resultado.
        // O cast para (List<Permissao>) é necessário pois o método do DAO retorna um Map.
        return ((List<Permissao>) permissaoDAO.findWithFiltersAndStats(search).get("list"))
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Endpoint para buscar uma permissão pelo seu ID. Mapeado para: GET
     * /permissoes/{id}
     *
     * @param id O ID da permissão a ser buscada.
     * @return Resposta 200 OK com o DTO da permissão se encontrada, ou 404 Not
     * Found.
     */
    @GET
    @Path("/{id}")
    public Response getPermissaoById(@PathParam("id") Integer id) {
        return permissaoDAO.findById(id)
                .map(this::toDTO)
                .map(dto -> Response.ok(dto).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * Endpoint para criar uma nova permissão. Mapeado para: POST /permissoes
     *
     * @param dto O DTO com os dados da permissão a ser criada.
     * @return Resposta 201 Created com o DTO da permissão recém-criada.
     */
    @POST
    public Response createPermissao(PermissaoDTO dto) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("O corpo da requisição não pode ser vazio.").build();
        }
        Permissao permissao = fromDTO(new Permissao(), dto);
        Permissao novaPermissao = permissaoDAO.save(permissao);
        return Response.status(Response.Status.CREATED).entity(toDTO(novaPermissao)).build();
    }

    /**
     * Endpoint para atualizar uma permissão existente. Mapeado para: PUT
     * /permissoes/{id}
     *
     * @param id O ID da permissão a ser atualizada.
     * @param dto O DTO com os novos dados.
     * @return Resposta 200 OK com o DTO atualizado, ou 404 Not Found.
     */
    @PUT
    @Path("/{id}")
    public Response updatePermissao(@PathParam("id") Integer id, PermissaoDTO dto) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("O corpo da requisição não pode ser vazio.").build();
        }
        return permissaoDAO.findById(id).map(permissaoExistente -> {
            Permissao permissaoParaAtualizar = fromDTO(permissaoExistente, dto);
            Permissao permissaoAtualizada = permissaoDAO.save(permissaoParaAtualizar);
            return Response.ok(toDTO(permissaoAtualizada)).build();
        }).orElse(Response.status(Response.Status.NOT_FOUND).entity("Permissão com ID " + id + " não encontrada.").build());
    }

    /**
     * Endpoint para deletar uma permissão. Mapeado para: DELETE
     * /permissoes/{id}
     *
     * @param id O ID da permissão a ser deletada.
     * @return Resposta 204 No Content em caso de sucesso, ou 404 Not Found.
     */
    @DELETE
    @Path("/{id}")
    public Response deletePermissao(@PathParam("id") Integer id) {
        if (permissaoDAO.findById(id).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        permissaoDAO.deleteById(id);
        return Response.noContent().build();
    }

    /**
     * Converte uma entidade Permissao para seu DTO correspondente.
     *
     * @param permissao A entidade a ser convertida.
     * @return O DTO preenchido.
     */
    private PermissaoDTO toDTO(Permissao permissao) {
        if (permissao == null) {
            return null;
        }

        PermissaoDTO dto = new PermissaoDTO();
        dto.setIdPermissao(permissao.getIdPermissao());
        dto.setNomePermissao(permissao.getNomePermissao());
        dto.setDescricaoPermissao(permissao.getDescricaoPermissao());
        return dto;
    }

    /**
     * Preenche/atualiza uma entidade Permissao a partir de um DTO.
     *
     * @param permissao A entidade (nova ou existente) a ser preenchida.
     * @param dto O DTO com os dados.
     * @return A entidade preenchida/atualizada.
     */
    private Permissao fromDTO(Permissao permissao, PermissaoDTO dto) {
        permissao.setNomePermissao(dto.getNomePermissao());
        permissao.setDescricaoPermissao(dto.getDescricaoPermissao());
        return permissao;
    }
}
