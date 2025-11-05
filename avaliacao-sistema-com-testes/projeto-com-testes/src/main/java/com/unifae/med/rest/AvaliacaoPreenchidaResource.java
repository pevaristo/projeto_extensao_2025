/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `AvaliacaoPreenchidaResource`, é um recurso JAX-RS, o que significa que ela
 * define os endpoints (URLs) de uma API REST para gerenciar "Avaliações Preenchidas".
 * Ela é a porta de entrada para todas as operações de CRUD (Criar, Ler, Atualizar, Deletar)
 * relacionadas a este recurso.
 *
 * A classe segue um padrão de arquitetura bem definido:
 * 1.  **Camada de API (@Path):** Recebe as requisições HTTP (GET, POST, PUT, DELETE) no
 * caminho base "/avaliacoes". Ela lida com os dados no formato JSON.
 * 2.  **Camada de Acesso a Dados (DAO):** Utiliza uma instância de `AvaliacaoPreenchidaDAO`
 * para realizar as operações de persistência (salvar, buscar, deletar) no banco de
 * dados.
 * 3.  **Camada de Transferência de Dados (DTO):** Converte as entidades do banco de dados
 * (`AvaliacaoPreenchida`) em DTOs (`AvaliacaoPreenchidaDTO`) antes de enviá-las como
 * resposta. Isso desacopla a representação interna dos dados daquela exposta
 * pela API, uma prática essencial para segurança e manutenibilidade.
 * =================================================================================================
 */
package com.unifae.med.rest;

import com.unifae.med.dao.AvaliacaoPreenchidaDAO;
import com.unifae.med.entity.AvaliacaoPreenchida;
import com.unifae.med.rest.dto.AvaliacaoPreenchidaDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Define o caminho base para todos os endpoints nesta classe. Todas as
 * operações começarão com "/avaliacoes". Indica que, por padrão, os métodos
 * produzirão respostas em JSON. Indica que, por padrão, os métodos consumirão
 * dados em JSON.
 */
@Path("/avaliacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AvaliacaoPreenchidaResource {

    // Instância do DAO para interagir com o banco de dados.
    private final AvaliacaoPreenchidaDAO avaliacaoDAO = new AvaliacaoPreenchidaDAO();

    /**
     * Endpoint para listar todas as avaliações preenchidas. Mapeado para: GET
     * /avaliacoes
     *
     * @return Uma lista de AvaliacaoPreenchidaDTO.
     */
    @GET
    public List<AvaliacaoPreenchidaDTO> getAllAvaliacoes() {
        // Busca todas as entidades do banco
        return avaliacaoDAO.findAll().stream()
                // Mapeia cada entidade para seu respectivo DTO
                .map(this::toDTO)
                // Coleta os DTOs em uma lista
                .collect(Collectors.toList());
    }

    /**
     * Endpoint para buscar uma avaliação preenchida pelo seu ID. Mapeado para:
     * GET /avaliacoes/{id}
     *
     * @param id O ID da avaliação a ser buscada, extraído da URL.
     * @return Uma Resposta HTTP. Se encontrada, retorna 200 OK com o DTO. Se
     * não, retorna 404 Not Found.
     */
    @GET
    @Path("/{id}")
    public Response getAvaliacaoById(@PathParam("id") Integer id) {
        return avaliacaoDAO.findById(id)
                .map(this::toDTO) // Converte a entidade para DTO
                .map(dto -> Response.ok(dto).build()) // Cria uma resposta 200 OK com o DTO
                .orElse(Response.status(Response.Status.NOT_FOUND).build()); // Cria uma resposta 404
    }

    /**
     * Endpoint para criar uma nova avaliação preenchida. Mapeado para: POST
     * /avaliacoes
     *
     * @param avaliacao A entidade AvaliacaoPreenchida recebida no corpo da
     * requisição.
     * @return Uma Resposta HTTP 201 Created com o DTO da avaliação
     * recém-criada.
     */
    @POST
    public Response createAvaliacao(AvaliacaoPreenchida avaliacao) {
        AvaliacaoPreenchida novaAvaliacao = avaliacaoDAO.save(avaliacao);
        // Retorna o status 201 (Created) e a representação DTO do recurso criado.
        return Response.status(Response.Status.CREATED).entity(toDTO(novaAvaliacao)).build();
    }

    /**
     * Endpoint para atualizar uma avaliação preenchida existente. Mapeado para:
     * PUT /avaliacoes/{id}
     *
     * @param id O ID da avaliação a ser atualizada.
     * @param avaliacao Os dados atualizados da avaliação.
     * @return Uma Resposta HTTP. Se bem-sucedido, retorna 200 OK com o DTO
     * atualizado. Se os IDs não baterem, retorna 400 Bad Request.
     */
    @PUT
    @Path("/{id}")
    public Response updateAvaliacao(@PathParam("id") Integer id, AvaliacaoPreenchida avaliacao) {
        // Validação para garantir que o ID da URL é o mesmo do objeto no corpo da requisição
        if (!id.equals(avaliacao.getIdAvaliacaoPreenchida())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ID da avaliação no corpo não corresponde ao ID na URL").build();
        }
        AvaliacaoPreenchida avaliacaoAtualizada = avaliacaoDAO.save(avaliacao);
        return Response.ok(toDTO(avaliacaoAtualizada)).build();
    }

    /**
     * Endpoint para deletar uma avaliação preenchida. Mapeado para: DELETE
     * /avaliacoes/{id}
     *
     * @param id O ID da avaliação a ser deletada.
     * @return Uma Resposta HTTP 204 No Content, indicando sucesso na remoção.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteAvaliacao(@PathParam("id") Integer id) {
        avaliacaoDAO.deleteById(id);
        // O status 204 (No Content) é o padrão para respostas de DELETE bem-sucedidas.
        return Response.noContent().build();
    }

    /**
     * Método utilitário privado para converter uma entidade AvaliacaoPreenchida
     * em um DTO. Este método é o coração do desacoplamento entre a camada de
     * persistência e a camada de API.
     *
     * @param avaliacao A entidade a ser convertida.
     * @return O DTO correspondente.
     */
    private AvaliacaoPreenchidaDTO toDTO(AvaliacaoPreenchida avaliacao) {
        AvaliacaoPreenchidaDTO dto = new AvaliacaoPreenchidaDTO();
        dto.setIdAvaliacaoPreenchida(avaliacao.getIdAvaliacaoPreenchida());
        // Verifica se os objetos relacionados não são nulos antes de pegar seus IDs para evitar NullPointerException
        dto.setIdQuestionario(avaliacao.getQuestionario() != null ? avaliacao.getQuestionario().getIdQuestionario() : null);
        dto.setIdAlunoAvaliado(avaliacao.getAlunoAvaliado() != null ? avaliacao.getAlunoAvaliado().getIdUsuario() : null);
        dto.setIdAvaliador(avaliacao.getAvaliador() != null ? avaliacao.getAvaliador().getIdUsuario() : null);
        dto.setTipoAvaliadorNaoUsuario(avaliacao.getTipoAvaliadorNaoUsuario());
        dto.setNomeAvaliadorNaoUsuario(avaliacao.getNomeAvaliadorNaoUsuario());
        dto.setDataRealizacao(avaliacao.getDataRealizacao());
        dto.setHorarioInicio(avaliacao.getHorarioInicio());
        dto.setHorarioFim(avaliacao.getHorarioFim());
        dto.setLocalRealizacao(avaliacao.getLocalRealizacao());
        dto.setFeedbackPositivo(avaliacao.getFeedbackPositivo());
        dto.setFeedbackMelhoria(avaliacao.getFeedbackMelhoria());
        dto.setContratoAprendizagem(avaliacao.getContratoAprendizagem());
        return dto;
    }
}
