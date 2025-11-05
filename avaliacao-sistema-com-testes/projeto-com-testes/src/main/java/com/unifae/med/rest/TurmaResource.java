/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `TurmaResource`, é um recurso JAX-RS que define a API REST para o
 * gerenciamento de Turmas acadêmicas. Ela fornece um conjunto completo de endpoints
 * para as operações de CRUD (Criar, Ler, Atualizar, Deletar) sobre as turmas.
 *
 * A classe serve como a camada controladora da API, acessível pelo caminho base "/turmas",
 * e se comunica utilizando o formato JSON. Suas principais características são:
 *
 * 1.  **Listagem com Filtros:** O método `getAllTurmas` é particularmente flexível,
 * aceitando dois parâmetros de consulta (`@QueryParam`): `search` para busca por
 * texto e `status` para filtrar por turmas ativas ou inativas. Isso torna a API
 * poderosa para listagens dinâmicas em interfaces de usuário.
 *
 * 2.  **Padrão DAO e DTO:** A classe utiliza o `TurmaDAO` para abstrair o acesso ao
 * banco de dados e o `TurmaDTO` para definir o "contrato" de dados com os clientes
 * da API.
 *
 * 3.  **Conversores `toDTO` e `fromDTO`:** A lógica de conversão entre a entidade de
 * persistência (`Turma`) e o objeto de transferência de dados (`TurmaDTO`) é
 * centralizada em métodos privados, o que promove código limpo, reutilizável e
 * um claro desacoplamento entre as camadas da aplicação.
 * =================================================================================================
 */
package com.unifae.med.rest;

import com.unifae.med.dao.TurmaDAO;
import com.unifae.med.entity.Turma;
import com.unifae.med.rest.dto.TurmaDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/turmas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TurmaResource {

    // Instância do DAO para interagir com a tabela de turmas no banco de dados.
    private final TurmaDAO turmaDAO = new TurmaDAO();

    /**
     * Endpoint para listar todas as turmas, com suporte a filtros de busca e
     * status. Mapeado para: GET /turmas?search=termo&status=ativo
     *
     * @param search Um termo opcional de busca para filtrar por nome ou código
     * da turma.
     * @param status Um filtro opcional para o status da turma (ex: "ativo",
     * "inativo").
     * @return Uma lista de TurmaDTO.
     */
    @GET
    public List<TurmaDTO> getAllTurmas(@QueryParam("search") String search, @QueryParam("status") String status) {
        // Delega a lógica de filtragem para o método do DAO.
        Map<String, Object> result = turmaDAO.findWithFiltersAndStats(search, status);
        // Extrai a lista de entidades do mapa retornado pelo DAO.
        List<Turma> turmas = (List<Turma>) result.get("list");
        return turmas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Endpoint para buscar uma turma pelo seu ID. Mapeado para: GET
     * /turmas/{id}
     *
     * @param id O ID da turma a ser buscada.
     * @return Resposta 200 OK com o DTO da turma se encontrada, ou 404 Not
     * Found.
     */
    @GET
    @Path("/{id}")
    public Response getTurmaById(@PathParam("id") Integer id) {
        return turmaDAO.findById(id)
                .map(this::toDTO)
                .map(dto -> Response.ok(dto).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * Endpoint para criar uma nova turma. Mapeado para: POST /turmas
     *
     * @param dto O DTO com os dados da turma a ser criada.
     * @return Resposta 201 Created com o DTO da turma recém-criada.
     */
    @POST
    public Response createTurma(TurmaDTO dto) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("O corpo da requisição não pode ser vazio.").build();
        }

        Turma turma = fromDTO(new Turma(), dto);
        Turma novaTurma = turmaDAO.save(turma);

        return Response.status(Response.Status.CREATED).entity(toDTO(novaTurma)).build();
    }

    /**
     * Endpoint para atualizar uma turma existente. Mapeado para: PUT
     * /turmas/{id}
     *
     * @param id O ID da turma a ser atualizada.
     * @param dto O DTO com os novos dados.
     * @return Resposta 200 OK com o DTO atualizado, ou 404 Not Found.
     */
    @PUT
    @Path("/{id}")
    public Response updateTurma(@PathParam("id") Integer id, TurmaDTO dto) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("O corpo da requisição não pode ser vazio.").build();
        }

        return turmaDAO.findById(id).map(turmaExistente -> {
            Turma turmaParaAtualizar = fromDTO(turmaExistente, dto);
            Turma turmaAtualizada = turmaDAO.save(turmaParaAtualizar);
            return Response.ok(toDTO(turmaAtualizada)).build();
        }).orElse(Response.status(Response.Status.NOT_FOUND).entity("Turma com ID " + id + " não encontrada.").build());
    }

    /**
     * Endpoint para deletar uma turma. Mapeado para: DELETE /turmas/{id}
     *
     * @param id O ID da turma a ser deletada.
     * @return Resposta 204 No Content em caso de sucesso, ou 404 Not Found.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteTurma(@PathParam("id") Integer id) {
        if (turmaDAO.findById(id).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        turmaDAO.deleteById(id);
        return Response.noContent().build();
    }

    /**
     * Converte uma entidade Turma para seu DTO correspondente.
     *
     * @param turma A entidade a ser convertida.
     * @return O DTO preenchido.
     */
    private TurmaDTO toDTO(Turma turma) {
        if (turma == null) {
            return null;
        }

        TurmaDTO dto = new TurmaDTO();
        dto.setIdTurma(turma.getIdTurma());
        dto.setNomeTurma(turma.getNomeTurma());
        dto.setCodigoTurma(turma.getCodigoTurma());
        dto.setAnoLetivo(turma.getAnoLetivo());
        dto.setSemestre(turma.getSemestre());
        dto.setAtivo(turma.getAtivo());

        return dto;
    }

    /**
     * Preenche/atualiza uma entidade Turma a partir de um DTO.
     *
     * @param turma A entidade (nova ou existente) a ser preenchida.
     * @param dto O DTO com os dados.
     * @return A entidade preenchida/atualizada.
     */
    private Turma fromDTO(Turma turma, TurmaDTO dto) {
        turma.setNomeTurma(dto.getNomeTurma());
        turma.setCodigoTurma(dto.getCodigoTurma());
        turma.setAnoLetivo(dto.getAnoLetivo());
        turma.setSemestre(dto.getSemestre());
        turma.setAtivo(dto.getAtivo());

        return turma;
    }
}
