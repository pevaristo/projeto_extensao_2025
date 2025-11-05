/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `LocalEventoResource`, é um recurso JAX-RS que expõe uma API REST para
 * o gerenciamento de "Locais de Evento". Ela é responsável por fornecer os endpoints
 * para todas as operações de CRUD (Criar, Ler, Atualizar, Deletar) relacionadas aos locais.
 *
 * A classe segue um padrão arquitetural robusto e desacoplado:
 * 1.  **Camada de API (@Path):** Define o caminho base "/locais" e mapeia os verbos HTTP
 * (GET, POST, PUT, DELETE) para os métodos correspondentes. A comunicação é padronizada
 * para o formato JSON.
 * 2.  **Camada de Acesso a Dados (DAO):** Utiliza o `LocalEventoDAO` para abstrair toda a
 * interação com o banco de dados.
 * 3.  **Mapeamento DTO <-> Entidade:** A classe implementa dois métodos privados
 * essenciais: - `toDTO`: Converte uma entidade `LocalEvento` (do banco) para um
 * `LocalEventoDTO` (para a API), garantindo que apenas os dados necessários
 * sejam expostos. - `fromDTO`: Faz o caminho inverso, atualizando uma entidade
 * `LocalEvento` com os dados recebidos de um `LocalEventoDTO` em requisições de
 * criação ou atualização.
 *
 * Este design promove a reutilização de código e uma clara separação de
 * responsabilidades.
 * =================================================================================================
 */
package com.unifae.med.rest;

import com.unifae.med.dao.LocalEventoDAO;
import com.unifae.med.entity.LocalEvento;
import com.unifae.med.rest.dto.LocalEventoDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/locais")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LocalEventoResource {

    // Instância do DAO para interagir com a tabela de locais no banco de dados.
    private final LocalEventoDAO localEventoDAO = new LocalEventoDAO();

    /**
     * Endpoint para listar todos os locais de evento. Mapeado para: GET /locais
     *
     * @return Uma lista de LocalEventoDTO.
     */
    @GET
    public List<LocalEventoDTO> getAllLocais() {
        return localEventoDAO.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Endpoint para buscar um local de evento pelo seu ID. Mapeado para: GET
     * /locais/{id}
     *
     * @param id O ID do local a ser buscado.
     * @return Resposta 200 OK com o DTO se encontrado, ou 404 Not Found.
     */
    @GET
    @Path("/{id}")
    public Response getLocalById(@PathParam("id") Integer id) {
        return localEventoDAO.findById(id)
                .map(this::toDTO)
                .map(dto -> Response.ok(dto).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * Endpoint para criar um novo local de evento. Mapeado para: POST /locais
     *
     * @param dto O DTO com os dados do local a ser criado.
     * @return Resposta 201 Created com o DTO do local recém-criado.
     */
    @POST
    public Response createLocal(LocalEventoDTO dto) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("O corpo da requisição não pode ser vazio.").build();
        }
        // Converte o DTO para uma nova entidade
        LocalEvento local = fromDTO(new LocalEvento(), dto);
        // Salva a entidade no banco
        LocalEvento novoLocal = localEventoDAO.save(local);
        // Retorna o DTO do novo local com status 201
        return Response.status(Response.Status.CREATED).entity(toDTO(novoLocal)).build();
    }

    /**
     * Endpoint para atualizar um local de evento existente. Mapeado para: PUT
     * /locais/{id}
     *
     * @param id O ID do local a ser atualizado.
     * @param dto O DTO com os novos dados.
     * @return Resposta 200 OK com o DTO atualizado, ou 404 Not Found.
     */
    @PUT
    @Path("/{id}")
    public Response updateLocal(@PathParam("id") Integer id, LocalEventoDTO dto) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("O corpo da requisição não pode ser vazio.").build();
        }
        // Busca o local pelo ID e, se encontrar, executa a lógica de atualização.
        return localEventoDAO.findById(id).map(localExistente -> {
            // Atualiza a entidade existente com os dados do DTO
            LocalEvento localParaAtualizar = fromDTO(localExistente, dto);
            // Salva as alterações
            LocalEvento localAtualizado = localEventoDAO.save(localParaAtualizar);
            // Retorna a resposta de sucesso
            return Response.ok(toDTO(localAtualizado)).build();
        }).orElse(Response.status(Response.Status.NOT_FOUND).entity("Local com ID " + id + " não encontrado.").build()); // Se não encontrar, retorna 404
    }

    /**
     * Endpoint para deletar um local de evento. Mapeado para: DELETE
     * /locais/{id}
     *
     * @param id O ID do local a ser deletado.
     * @return Resposta 204 No Content em caso de sucesso, ou 404 Not Found.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteLocal(@PathParam("id") Integer id) {
        // Verifica se o local existe antes de deletar para prover uma resposta 404 mais consistente.
        if (localEventoDAO.findById(id).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        localEventoDAO.deleteById(id);
        return Response.noContent().build();
    }

    /**
     * Converte uma entidade LocalEvento para seu DTO correspondente.
     *
     * @param local A entidade JPA a ser convertida.
     * @return O DTO preenchido com os dados da entidade.
     */
    private LocalEventoDTO toDTO(LocalEvento local) {
        if (local == null) {
            return null;
        }
        LocalEventoDTO dto = new LocalEventoDTO();
        dto.setIdLocalEvento(local.getIdLocalEvento());
        dto.setNomeLocal(local.getNomeLocal());
        dto.setTipoLocal(local.getTipoLocal());
        dto.setEndereco(local.getEndereco());
        dto.setCidade(local.getCidade());
        dto.setEstado(local.getEstado());
        return dto;
    }

    /**
     * Preenche/atualiza uma entidade LocalEvento a partir de um DTO.
     *
     * @param local A entidade (nova ou existente) a ser preenchida.
     * @param dto O DTO contendo os dados.
     * @return A entidade preenchida/atualizada.
     */
    private LocalEvento fromDTO(LocalEvento local, LocalEventoDTO dto) {
        local.setNomeLocal(dto.getNomeLocal());
        local.setTipoLocal(dto.getTipoLocal());
        local.setEndereco(dto.getEndereco());
        local.setCidade(dto.getCidade());
        local.setEstado(dto.getEstado());
        return local;
    }
}
