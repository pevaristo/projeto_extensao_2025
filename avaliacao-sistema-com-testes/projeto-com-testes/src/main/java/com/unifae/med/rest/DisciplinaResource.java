/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `DisciplinaResource`, é um recurso JAX-RS que define os endpoints de uma
 * API REST para o gerenciamento de Disciplinas acadêmicas. Ela implementa todas as
 * operações básicas de um CRUD (Criar, Ler, Atualizar, Deletar).
 *
 * A classe serve como a camada de "Controller" da aplicação, responsável por:
 * 1.  **Expor os Endpoints:** Mapeia os caminhos (URLs) e os verbos HTTP (GET, POST, etc.)
 * para métodos Java específicos, utilizando as anotações JAX-RS como `@Path`, `@GET`,
 * `@POST`, etc. O caminho base para este recurso é "/disciplinas".
 * 2.  **Orquestrar a Lógica:** Recebe as requisições, utiliza o `DisciplinaDAO` para
 * interagir com o banco de dados e processar a lógica de negócio.
 * 3.  **Formatar a Resposta:** Converte as entidades de persistência (`Disciplina`) em
 * Data Transfer Objects (`DisciplinaDTO`) antes de enviá-las como resposta em formato
 * JSON. Este passo é crucial para desacoplar o modelo de dados interno da
 * representação exposta pela API.
 * =================================================================================================
 */
package com.unifae.med.rest;

import com.unifae.med.dao.DisciplinaDAO;
import com.unifae.med.entity.Disciplina;
import com.unifae.med.rest.dto.DisciplinaDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Define o caminho (path) base para todos os endpoints desta classe. Indica
 * que, por padrão, os métodos produzirão respostas no formato JSON. Indica que,
 * por padrão, os métodos esperam receber dados no formato JSON.
 */
@Path("/disciplinas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DisciplinaResource {

    // Instância do DAO para realizar as operações de acesso ao banco de dados.
    private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();

    /**
     * Endpoint para listar todas as disciplinas cadastradas. Mapeado para: GET
     * /disciplinas
     *
     * @return Uma lista contendo todas as disciplinas como DTOs.
     */
    @GET
    public List<DisciplinaDTO> getAllDisciplinas() {
        return disciplinaDAO.findAll().stream()
                .map(this::toDTO) // Converte cada entidade para DTO
                .collect(Collectors.toList());
    }

    /**
     * Endpoint para buscar uma única disciplina pelo seu ID. Mapeado para: GET
     * /disciplinas/{id}
     *
     * @param id O ID da disciplina, extraído da URL.
     * @return Uma Resposta HTTP. Se a disciplina for encontrada, retorna 200 OK
     * com o DTO. Caso contrário, retorna 404 Not Found.
     */
    @GET
    @Path("/{id}")
    public Response getDisciplinaById(@PathParam("id") Integer id) {
        return disciplinaDAO.findById(id)
                .map(this::toDTO)
                .map(dto -> Response.ok(dto).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * Endpoint para criar uma nova disciplina. Mapeado para: POST /disciplinas
     *
     * @param disciplina A entidade Disciplina, criada a partir do JSON no corpo
     * da requisição.
     * @return Uma Resposta HTTP 201 Created, contendo o DTO da disciplina
     * recém-criada.
     */
    @POST
    public Response createDisciplina(Disciplina disciplina) {
        Disciplina novaDisciplina = disciplinaDAO.save(disciplina);
        return Response.status(Response.Status.CREATED).entity(toDTO(novaDisciplina)).build();
    }

    /**
     * Endpoint para atualizar uma disciplina existente. Mapeado para: PUT
     * /disciplinas/{id}
     *
     * @param id O ID da disciplina a ser atualizada.
     * @param disciplina Os novos dados da disciplina.
     * @return Uma Resposta HTTP. Se bem-sucedido, retorna 200 OK com o DTO
     * atualizado. Se os IDs não forem consistentes, retorna 400 Bad Request.
     */
    @PUT
    @Path("/{id}")
    public Response updateDisciplina(@PathParam("id") Integer id, Disciplina disciplina) {
        // Validação para garantir que o ID na URL corresponde ao ID no corpo da requisição.
        if (!id.equals(disciplina.getIdDisciplina())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ID da disciplina no corpo não corresponde ao ID na URL").build();
        }
        Disciplina disciplinaAtualizada = disciplinaDAO.save(disciplina);
        return Response.ok(toDTO(disciplinaAtualizada)).build();
    }

    /**
     * Endpoint para deletar uma disciplina pelo seu ID. Mapeado para: DELETE
     * /disciplinas/{id}
     *
     * @param id O ID da disciplina a ser deletada.
     * @return Uma Resposta HTTP 204 No Content, que indica sucesso na operação
     * sem retornar corpo.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteDisciplina(@PathParam("id") Integer id) {
        disciplinaDAO.deleteById(id);
        return Response.noContent().build();
    }

    /**
     * Método utilitário privado para mapear uma entidade `Disciplina` para um
     * `DisciplinaDTO`. Centraliza a lógica de conversão, garantindo
     * consistência e promovendo o desacoplamento.
     *
     * @param disciplina A entidade de persistência.
     * @return O objeto de transferência de dados (DTO).
     */
    private DisciplinaDTO toDTO(Disciplina disciplina) {
        DisciplinaDTO dto = new DisciplinaDTO();
        dto.setIdDisciplina(disciplina.getIdDisciplina());
        dto.setNomeDisciplina(disciplina.getNomeDisciplina());
        dto.setCodigoDisciplina(disciplina.getCodigoDisciplina());
        dto.setSiglaDisciplina(disciplina.getSiglaDisciplina());
        dto.setAtiva(disciplina.getAtiva());
        return dto;
    }
}
