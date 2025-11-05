/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `RespostaItemAvaliacaoResource`, é um recurso JAX-RS que define a API REST
 * para o gerenciamento de "Respostas de Itens de Avaliação". Cada "resposta" é um
 * registro granular que conecta uma avaliação preenchida a um item específico do
 * questionário respondido.
 *
 * A classe é a camada controladora para o caminho base "/respostas-avaliacao" e
 * implementa um CRUD completo. Sua principal responsabilidade é orquestrar a interação
 * entre múltiplos DAOs para garantir a integridade referencial dos dados:
 *
 * 1.  **`RespostaItemAvaliacaoDAO`:** Para persistir a própria resposta.
 * 2.  **`AvaliacaoPreenchidaDAO`:** Para validar a existência da avaliação "pai".
 * 3.  **`CompetenciaQuestionarioDAO`:** Para validar a existência da competência/pergunta "pai".
 *
 * O método `fromDTO` é particularmente crítico, pois ele não apenas converte os dados,
 * mas também impõe regras de negócio, como a obrigatoriedade dos IDs da avaliação e
 * da competência, lançando exceções se essas regras não forem cumpridas. Isso torna
 * a API mais robusta e segura.
 * =================================================================================================
 */
package com.unifae.med.rest;

import com.unifae.med.dao.AvaliacaoPreenchidaDAO;
import com.unifae.med.dao.CompetenciaQuestionarioDAO;
import com.unifae.med.dao.RespostaItemAvaliacaoDAO;
import com.unifae.med.entity.RespostaItemAvaliacao;
import com.unifae.med.rest.dto.RespostaItemAvaliacaoDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/respostas-avaliacao")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RespostaItemAvaliacaoResource {

    // DAOs para gerenciar a própria entidade e suas dependências obrigatórias.
    private final RespostaItemAvaliacaoDAO respostaDAO = new RespostaItemAvaliacaoDAO();
    private final AvaliacaoPreenchidaDAO avaliacaoPreenchidaDAO = new AvaliacaoPreenchidaDAO();
    private final CompetenciaQuestionarioDAO competenciaDAO = new CompetenciaQuestionarioDAO();

    /**
     * Endpoint para listar todas as respostas de itens de avaliação. Mapeado
     * para: GET /respostas-avaliacao
     *
     * @return Uma lista de RespostaItemAvaliacaoDTO.
     */
    @GET
    public List<RespostaItemAvaliacaoDTO> getAllRespostas() {
        return respostaDAO.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Endpoint para buscar uma resposta específica pelo seu ID. Mapeado para:
     * GET /respostas-avaliacao/{id}
     *
     * @param id O ID da resposta a ser buscada.
     * @return Resposta 200 OK com o DTO da resposta se encontrada, ou 404 Not
     * Found.
     */
    @GET
    @Path("/{id}")
    public Response getRespostaById(@PathParam("id") Integer id) {
        return respostaDAO.findById(id)
                .map(this::toDTO)
                .map(dto -> Response.ok(dto).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * Endpoint para criar uma nova resposta. Mapeado para: POST
     * /respostas-avaliacao
     *
     * @param dto O DTO com os dados da resposta a ser criada.
     * @return Resposta 201 Created com o DTO da resposta criada, ou 400 Bad
     * Request em caso de erro.
     */
    @POST
    public Response createResposta(RespostaItemAvaliacaoDTO dto) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("O corpo da requisição não pode ser vazio.").build();
        }

        try {
            // Converte o DTO em uma nova entidade, validando e buscando os relacionamentos.
            RespostaItemAvaliacao resposta = fromDTO(new RespostaItemAvaliacao(), dto);
            RespostaItemAvaliacao novaResposta = respostaDAO.save(resposta);
            return Response.status(Response.Status.CREATED).entity(toDTO(novaResposta)).build();
        } catch (Exception e) {
            // Captura exceções do fromDTO (ex: AvaliacaoPreenchida não encontrada) e retorna um erro claro.
            return Response.status(Response.Status.BAD_REQUEST).entity("Erro ao criar resposta: " + e.getMessage()).build();
        }
    }

    /**
     * Endpoint para atualizar uma resposta existente. Mapeado para: PUT
     * /respostas-avaliacao/{id}
     *
     * @param id O ID da resposta a ser atualizada.
     * @param dto O DTO com os novos dados.
     * @return Resposta 200 OK com o DTO atualizado, 404 Not Found ou 400 Bad
     * Request.
     */
    @PUT
    @Path("/{id}")
    public Response updateResposta(@PathParam("id") Integer id, RespostaItemAvaliacaoDTO dto) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("O corpo da requisição não pode ser vazio.").build();
        }

        return respostaDAO.findById(id).map(respostaExistente -> {
            try {
                // Atualiza a entidade existente com os dados do DTO.
                RespostaItemAvaliacao respostaParaAtualizar = fromDTO(respostaExistente, dto);
                RespostaItemAvaliacao respostaAtualizada = respostaDAO.save(respostaParaAtualizar);
                return Response.ok(toDTO(respostaAtualizada)).build();
            } catch (Exception e) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Erro ao atualizar resposta: " + e.getMessage()).build();
            }
        }).orElse(Response.status(Response.Status.NOT_FOUND).entity("Resposta com ID " + id + " não encontrada.").build());
    }

    /**
     * Endpoint para deletar uma resposta. Mapeado para: DELETE
     * /respostas-avaliacao/{id}
     *
     * @param id O ID da resposta a ser deletada.
     * @return Resposta 204 No Content em caso de sucesso, ou 404 Not Found.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteResposta(@PathParam("id") Integer id) {
        if (respostaDAO.findById(id).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        respostaDAO.deleteById(id);
        return Response.noContent().build();
    }

    /**
     * Converte uma entidade RespostaItemAvaliacao para seu DTO correspondente.
     *
     * @param resposta A entidade a ser convertida.
     * @return O DTO preenchido.
     */
    private RespostaItemAvaliacaoDTO toDTO(RespostaItemAvaliacao resposta) {
        if (resposta == null) {
            return null;
        }

        RespostaItemAvaliacaoDTO dto = new RespostaItemAvaliacaoDTO();
        dto.setIdRespostaAvaliacao(resposta.getIdRespostaAvaliacao());
        dto.setRespostaValorNumerico(resposta.getRespostaValorNumerico());
        dto.setRespostaTexto(resposta.getRespostaTexto());
        dto.setNaoAvaliado(resposta.getNaoAvaliado());

        // Extrai os IDs das entidades relacionadas, com verificação de nulidade.
        if (resposta.getAvaliacaoPreenchida() != null) {
            dto.setIdAvaliacaoPreenchida(resposta.getAvaliacaoPreenchida().getIdAvaliacaoPreenchida());
        }
        if (resposta.getCompetenciaQuestionario() != null) {
            dto.setIdCompetenciaQuestionario(resposta.getCompetenciaQuestionario().getIdCompetenciaQuestionario());
        }

        return dto;
    }

    /**
     * Preenche/atualiza uma entidade RespostaItemAvaliacao a partir de um DTO.
     *
     * @param resposta A entidade (nova ou existente) a ser preenchida.
     * @param dto O DTO com os dados.
     * @return A entidade preenchida/atualizada.
     * @throws Exception se as entidades relacionadas obrigatórias não forem
     * encontradas.
     */
    private RespostaItemAvaliacao fromDTO(RespostaItemAvaliacao resposta, RespostaItemAvaliacaoDTO dto) throws Exception {
        resposta.setRespostaValorNumerico(dto.getRespostaValorNumerico());
        resposta.setRespostaTexto(dto.getRespostaTexto());
        // Garante que 'naoAvaliado' tenha um valor padrão (false) se for nulo no DTO.
        resposta.setNaoAvaliado(dto.getNaoAvaliado() != null ? dto.getNaoAvaliado() : false);

        // Valida e busca a AvaliacaoPreenchida associada. Lança exceção se for nula ou não encontrada.
        if (dto.getIdAvaliacaoPreenchida() != null) {
            resposta.setAvaliacaoPreenchida(avaliacaoPreenchidaDAO.findById(dto.getIdAvaliacaoPreenchida())
                    .orElseThrow(() -> new Exception("AvaliacaoPreenchida com ID " + dto.getIdAvaliacaoPreenchida() + " não encontrada.")));
        } else {
            throw new Exception("ID da AvaliacaoPreenchida é obrigatório.");
        }

        // Valida e busca a CompetenciaQuestionario associada. Lança exceção se for nula ou não encontrada.
        if (dto.getIdCompetenciaQuestionario() != null) {
            resposta.setCompetenciaQuestionario(competenciaDAO.findById(dto.getIdCompetenciaQuestionario())
                    .orElseThrow(() -> new Exception("CompetenciaQuestionario com ID " + dto.getIdCompetenciaQuestionario() + " não encontrada.")));
        } else {
            throw new Exception("ID da CompetenciaQuestionario é obrigatório.");
        }

        return resposta;
    }
}
