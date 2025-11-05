/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `UsuarioResource`, é um recurso JAX-RS que define a API REST para o
 * gerenciamento de Usuários do sistema. Ela fornece um conjunto completo de endpoints
 * para as operações de CRUD (Criar, Ler, Atualizar, Deletar) sobre os usuários,
 * acessível pelo caminho base "/usuarios".
 *
 * A classe atua como a camada controladora da API, com as seguintes responsabilidades:
 * 1.  **Expor os Endpoints:** Mapeia os verbos HTTP (GET, POST, PUT, DELETE) para os
 * métodos que manipulam os dados dos usuários.
 * 2.  **Interagir com a Camada de Dados:** Utiliza o `UsuarioDAO` para realizar as
 * operações de persistência no banco de dados.
 * 3.  **Garantir a Segurança dos Dados:** Um ponto crucial nesta classe é o uso do
 * método `toDTO`. Ao receber uma entidade `Usuario` para criar ou atualizar, a classe
 * pode receber dados sensíveis (como a senha). No entanto, ao retornar uma resposta,
 * ela SEMPRE converte a entidade para um `UsuarioDTO`, que é um "filtro" seguro que
 * omite campos críticos (como a senha), garantindo que essa informação nunca seja
 * exposta pela API.
 * =================================================================================================
 */
package com.unifae.med.rest;

import com.unifae.med.dao.UsuarioDAO;
import com.unifae.med.entity.Usuario;
import com.unifae.med.rest.dto.UsuarioDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    // Instância do DAO para interagir com a tabela de usuários no banco de dados.
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Endpoint para listar todos os usuários. Mapeado para: GET /usuarios
     *
     * @return Uma lista de UsuarioDTO, que representa uma visão segura dos
     * usuários.
     */
    @GET
    public List<UsuarioDTO> getAllUsuarios() {
        return usuarioDAO.findAll().stream()
                .map(this::toDTO) // Converte cada entidade para um DTO seguro
                .collect(Collectors.toList());
    }

    /**
     * Endpoint para buscar um usuário pelo seu ID. Mapeado para: GET
     * /usuarios/{id}
     *
     * @param id O ID do usuário a ser buscado.
     * @return Resposta 200 OK com o DTO do usuário se encontrado, ou 404 Not
     * Found.
     */
    @GET
    @Path("/{id}")
    public Response getUsuarioById(@PathParam("id") Integer id) {
        return usuarioDAO.findById(id)
                .map(this::toDTO)
                .map(dto -> Response.ok(dto).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * Endpoint para criar um novo usuário. Mapeado para: POST /usuarios
     *
     * @param usuario A entidade Usuario completa recebida no corpo da
     * requisição (pode incluir a senha).
     * @return Resposta 201 Created com o DTO do usuário recém-criado (sem a
     * senha).
     */
    @POST
    public Response createUsuario(Usuario usuario) {
        Usuario novoUsuario = usuarioDAO.save(usuario);
        return Response.status(Response.Status.CREATED).entity(toDTO(novoUsuario)).build();
    }

    /**
     * Endpoint para atualizar um usuário existente. Mapeado para: PUT
     * /usuarios/{id}
     *
     * @param id O ID do usuário a ser atualizado.
     * @param usuario A entidade com os novos dados do usuário.
     * @return Resposta 200 OK com o DTO atualizado, ou 400 Bad Request se os
     * IDs não baterem.
     */
    @PUT
    @Path("/{id}")
    public Response updateUsuario(@PathParam("id") Integer id, Usuario usuario) {
        // Validação para garantir que o ID da URL corresponde ao ID do objeto no corpo.
        if (!id.equals(usuario.getIdUsuario())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ID do usuário no corpo não corresponde ao ID na URL").build();
        }
        Usuario usuarioAtualizado = usuarioDAO.save(usuario);
        return Response.ok(toDTO(usuarioAtualizado)).build();
    }

    /**
     * Endpoint para deletar um usuário. Mapeado para: DELETE /usuarios/{id}
     *
     * @param id O ID do usuário a ser deletado.
     * @return Resposta 204 No Content, indicando sucesso na remoção.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteUsuario(@PathParam("id") Integer id) {
        usuarioDAO.deleteById(id);
        return Response.noContent().build();
    }

    /**
     * Converte uma entidade Usuario para seu DTO correspondente. Este método é
     * crucial para a segurança, pois garante que dados sensíveis (como a senha)
     * da entidade NUNCA sejam incluídos na resposta da API.
     *
     * @param usuario A entidade de persistência.
     * @return O DTO preenchido com dados seguros.
     */
    private UsuarioDTO toDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getNomeCompleto(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getMatriculaRA(),
                usuario.getTipoUsuario(),
                usuario.getAtivo()
        );
    }
}
