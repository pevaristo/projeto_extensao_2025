/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `QuestionarioResource`, é um recurso JAX-RS que define a API REST para
 * o gerenciamento de modelos de Questionário. Ela fornece um conjunto completo de
 * endpoints para as operações de CRUD (Criar, Ler, Atualizar, Deletar) sobre os
 * questionários.
 *
 * A classe atua como a camada de "Controller" para o recurso de questionários,
 * acessível pelo caminho base "/questionarios" e comunicando-se via JSON. Suas
 * responsabilidades são:
 *
 * 1.  **Receber Requisições HTTP:** Mapeia os verbos HTTP (GET, POST, PUT, DELETE) para
 * métodos Java específicos que executam a lógica de negócio.
 * 2.  **Interagir com a Camada de Dados:** Utiliza o `QuestionarioDAO` para realizar as
 * operações de persistência no banco de dados.
 * 3.  **Formatar Respostas:** Emprega o método `toDTO` para converter a entidade de
 * persistência `Questionario` em um `QuestionarioDTO`. Isso garante que a API
 * exponha uma representação de dados consistente e desacoplada do modelo interno
 * do banco de dados.
 * =================================================================================================
 */
package com.unifae.med.rest;

import com.unifae.med.dao.QuestionarioDAO;
import com.unifae.med.entity.Questionario;
import com.unifae.med.rest.dto.QuestionarioDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/questionarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QuestionarioResource {

    // Instância do DAO para interagir com a tabela de questionários no banco de dados.
    private final QuestionarioDAO questionarioDAO = new QuestionarioDAO();

    /**
     * Endpoint para listar todos os modelos de questionário. Mapeado para: GET
     * /questionarios
     *
     * @return Uma lista de QuestionarioDTO.
     */
    @GET
    public List<QuestionarioDTO> getAllQuestionarios() {
        return questionarioDAO.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Endpoint para buscar um modelo de questionário pelo seu ID. Mapeado para:
     * GET /questionarios/{id}
     *
     * @param id O ID do questionário a ser buscado.
     * @return Resposta 200 OK com o DTO se encontrado, ou 404 Not Found.
     */
    @GET
    @Path("/{id}")
    public Response getQuestionarioById(@PathParam("id") Integer id) {
        return questionarioDAO.findById(id)
                .map(this::toDTO)
                .map(dto -> Response.ok(dto).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * Endpoint para criar um novo modelo de questionário. Mapeado para: POST
     * /questionarios
     *
     * @param questionario A entidade Questionario recebida no corpo da
     * requisição.
     * @return Resposta 201 Created com o DTO do questionário recém-criado.
     */
    @POST
    public Response createQuestionario(Questionario questionario) {
        Questionario novoQuestionario = questionarioDAO.save(questionario);
        return Response.status(Response.Status.CREATED).entity(toDTO(novoQuestionario)).build();
    }

    /**
     * Endpoint para atualizar um modelo de questionário existente. Mapeado
     * para: PUT /questionarios/{id}
     *
     * @param id O ID do questionário a ser atualizado.
     * @param questionario A entidade com os novos dados.
     * @return Resposta 200 OK com o DTO atualizado, ou 400 Bad Request se os
     * IDs não baterem.
     */
    @PUT
    @Path("/{id}")
    public Response updateQuestionario(@PathParam("id") Integer id, Questionario questionario) {
        // Validação para garantir que o ID da URL corresponde ao ID do objeto enviado.
        if (!id.equals(questionario.getIdQuestionario())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ID do questionário no corpo não corresponde ao ID na URL").build();
        }
        Questionario questionarioAtualizado = questionarioDAO.save(questionario);
        return Response.ok(toDTO(questionarioAtualizado)).build();
    }

    /**
     * Endpoint para deletar um modelo de questionário. Mapeado para: DELETE
     * /questionarios/{id}
     *
     * @param id O ID do questionário a ser deletado.
     * @return Resposta 204 No Content, indicando sucesso na remoção.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteQuestionario(@PathParam("id") Integer id) {
        questionarioDAO.deleteById(id);
        return Response.noContent().build();
    }

    /**
     * Converte uma entidade Questionario para seu DTO correspondente.
     *
     * @param questionario A entidade a ser convertida.
     * @return O DTO preenchido.
     */
    private QuestionarioDTO toDTO(Questionario questionario) {
        // Utiliza o construtor do DTO para uma conversão direta e limpa.
        return new QuestionarioDTO(
                questionario.getIdQuestionario(),
                questionario.getNomeModelo(),
                questionario.getDescricao()
        );
    }
}
