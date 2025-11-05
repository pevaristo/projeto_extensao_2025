package com.unifae.med.rest;

import com.unifae.med.dao.CompetenciaQuestionarioDAO;
import com.unifae.med.dao.QuestionarioDAO;
import com.unifae.med.entity.CompetenciaQuestionario;
import com.unifae.med.entity.Questionario;
import com.unifae.med.rest.dto.CompetenciaQuestionarioDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/questionarios/{questionarioId}/competencias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompetenciaQuestionarioResource {

    private final CompetenciaQuestionarioDAO competenciaDAO = new CompetenciaQuestionarioDAO();
    private final QuestionarioDAO questionarioDAO = new QuestionarioDAO();

    @GET
    public List<CompetenciaQuestionarioDTO> listarCompetenciasPorQuestionario(@PathParam("questionarioId") Integer questionarioId) {
        return competenciaDAO.findByQuestionarioAndFilters(questionarioId, null).stream()
                .map(CompetenciaQuestionarioDTO::new)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{competenciaId}")
    public Response getCompetenciaById(@PathParam("questionarioId") Integer questionarioId,
            @PathParam("competenciaId") Integer competenciaId) {
        return competenciaDAO.findById(competenciaId)
                .filter(c -> c.getQuestionario().getIdQuestionario().equals(questionarioId))
                .map(CompetenciaQuestionarioDTO::new)
                .map(dto -> Response.ok(dto).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response createCompetencia(@PathParam("questionarioId") Integer questionarioId, CompetenciaQuestionarioDTO dto) {
        Questionario questionarioPai = questionarioDAO.findById(questionarioId)
                .orElseThrow(() -> new WebApplicationException("Questionário não encontrado", Response.Status.NOT_FOUND));

        CompetenciaQuestionario novaCompetencia = toEntity(dto, questionarioPai);
        competenciaDAO.save(novaCompetencia);

        return Response.status(Response.Status.CREATED).entity(new CompetenciaQuestionarioDTO(novaCompetencia)).build();
    }

    @PUT
    @Path("/{competenciaId}")
    public Response updateCompetencia(@PathParam("questionarioId") Integer questionarioId,
            @PathParam("competenciaId") Integer competenciaId,
            CompetenciaQuestionarioDTO dto) {

        if (questionarioDAO.findById(questionarioId).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Questionário não encontrado.").build();
        }

        return competenciaDAO.findById(competenciaId)
                .map(competenciaExistente -> {
                    if (!competenciaExistente.getQuestionario().getIdQuestionario().equals(questionarioId)) {
                        throw new WebApplicationException("Conflito: A competência não pertence ao questionário informado.", Response.Status.CONFLICT);
                    }

                    competenciaExistente.setNomeCompetencia(dto.getNomeCompetencia());
                    competenciaExistente.setTipoItem(dto.getTipoItem());
                    competenciaExistente.setDescricaoPrompt(dto.getDescricaoPrompt());
                    competenciaExistente.setOrdemExibicao(dto.getOrdemExibicao());
                    competenciaExistente.setObrigatorio(dto.isObrigatorio());
                    competenciaExistente.setAtivo(dto.isAtivo());

                    competenciaDAO.save(competenciaExistente);
                    return Response.ok(new CompetenciaQuestionarioDTO(competenciaExistente)).build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).entity("Competência não encontrada.").build());
    }

    @DELETE
    @Path("/{competenciaId}")
    public Response deleteCompetencia(@PathParam("questionarioId") Integer questionarioId,
            @PathParam("competenciaId") Integer competenciaId) {

        competenciaDAO.findById(competenciaId)
                .filter(c -> c.getQuestionario().getIdQuestionario().equals(questionarioId))
                .ifPresentOrElse(
                        c -> competenciaDAO.deleteById(competenciaId),
                        () -> {
                            throw new WebApplicationException(Response.Status.NOT_FOUND);
                        }
                );

        return Response.noContent().build();
    }

    private CompetenciaQuestionario toEntity(CompetenciaQuestionarioDTO dto, Questionario questionarioPai) {
        CompetenciaQuestionario entity = new CompetenciaQuestionario();
        entity.setNomeCompetencia(dto.getNomeCompetencia());
        entity.setTipoItem(dto.getTipoItem());
        entity.setDescricaoPrompt(dto.getDescricaoPrompt());
        entity.setOrdemExibicao(dto.getOrdemExibicao());
        entity.setObrigatorio(dto.isObrigatorio());
        entity.setAtivo(dto.isAtivo());
        entity.setQuestionario(questionarioPai);
        return entity;
    }
}
