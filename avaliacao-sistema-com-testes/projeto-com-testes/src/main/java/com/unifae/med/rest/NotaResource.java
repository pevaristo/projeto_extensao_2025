/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `NotaResource`, é um recurso JAX-RS que define a API REST para o
 * gerenciamento de Notas acadêmicas. Ela implementa as operações de CRUD (Criar, Ler,
 * Atualizar, Deletar) para as notas, servindo como a camada controladora que recebe as
 * requisições HTTP no caminho base "/notas".
 *
 * A principal complexidade e responsabilidade desta classe é orquestrar múltiplos DAOs
 * para montar e desmontar corretamente a entidade `Nota`. Como uma nota está
 * relacionada a várias outras entidades (Aluno, Disciplina, Turma, Professor), a classe
 * precisa:
 *
 * 1.  **Ao Criar/Atualizar (Método `fromDTO`):** Receber um `NotaDTO` com os IDs das
 * entidades relacionadas, usar os DAOs (`usuarioDAO`, `disciplinaDAO`, etc.) para
 * buscar as entidades completas no banco e associá-las à entidade `Nota` antes de
 * persistir. O método lança exceções se os IDs obrigatórios não forem encontrados,
 * garantindo a integridade dos dados.
 *
 * 2.  **Ao Ler (Método `toDTO`):** Converter a entidade `Nota`, vinda do banco, em um
 * `NotaDTO`, extraindo apenas os IDs das entidades relacionadas para enviar uma
 * resposta limpa e leve para o cliente da API.
 *
 * Este padrão de conversão e validação de relacionamentos é crucial para o correto
 * funcionamento de uma entidade que depende de muitas outras.
 * =================================================================================================
 */
package com.unifae.med.rest;

import com.unifae.med.dao.DisciplinaDAO;
import com.unifae.med.dao.NotaDAO;
import com.unifae.med.dao.TurmaDAO;
import com.unifae.med.dao.UsuarioDAO;
import com.unifae.med.entity.Nota;
import com.unifae.med.rest.dto.NotaDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Path("/notas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotaResource {

    // DAOs necessários para persistir a nota e buscar suas entidades relacionadas.
    private final NotaDAO notaDAO = new NotaDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    private final TurmaDAO turmaDAO = new TurmaDAO();

    /**
     * Endpoint para listar todas as notas. Mapeado para: GET /notas
     *
     * @return Uma lista de NotaDTO.
     */
    @GET
    public List<NotaDTO> getAllNotas() {
        return notaDAO.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Endpoint para buscar uma nota pelo seu ID. Mapeado para: GET /notas/{id}
     *
     * @param id O ID da nota a ser buscada.
     * @return Resposta 200 OK com o DTO da nota se encontrada, ou 404 Not
     * Found.
     */
    @GET
    @Path("/{id}")
    public Response getNotaById(@PathParam("id") Integer id) {
        return notaDAO.findById(id)
                .map(this::toDTO)
                .map(dto -> Response.ok(dto).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * Endpoint para criar uma nova nota. Mapeado para: POST /notas
     *
     * @param dto O DTO com os dados da nota a ser criada.
     * @return Resposta 201 Created com o DTO da nota criada, ou 400 Bad Request
     * em caso de erro.
     */
    @POST
    public Response createNota(NotaDTO dto) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("O corpo da requisição não pode ser vazio.").build();
        }

        try {
            // Converte o DTO para uma nova entidade, validando e buscando os relacionamentos.
            Nota nota = fromDTO(new Nota(), dto);
            nota.setDataLancamento(LocalDate.now()); // Regra de negócio: data de lançamento é definida no momento da criação.
            Nota novaNota = notaDAO.save(nota);
            return Response.status(Response.Status.CREATED).entity(toDTO(novaNota)).build();
        } catch (Exception e) {
            // Captura exceções do fromDTO (ex: Aluno não encontrado) e retorna um erro claro.
            return Response.status(Response.Status.BAD_REQUEST).entity("Erro ao criar nota: " + e.getMessage()).build();
        }
    }

    /**
     * Endpoint para atualizar uma nota existente. Mapeado para: PUT /notas/{id}
     *
     * @param id O ID da nota a ser atualizada.
     * @param dto O DTO com os novos dados.
     * @return Resposta 200 OK com o DTO atualizado, 404 Not Found ou 400 Bad
     * Request.
     */
    @PUT
    @Path("/{id}")
    public Response updateNota(@PathParam("id") Integer id, NotaDTO dto) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("O corpo da requisição não pode ser vazio.").build();
        }

        return notaDAO.findById(id).map(notaExistente -> {
            try {
                // Atualiza a entidade existente com os dados do DTO.
                Nota notaParaAtualizar = fromDTO(notaExistente, dto);
                Nota notaAtualizada = notaDAO.save(notaParaAtualizar);
                return Response.ok(toDTO(notaAtualizada)).build();
            } catch (Exception e) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Erro ao atualizar nota: " + e.getMessage()).build();
            }
        }).orElse(Response.status(Response.Status.NOT_FOUND).entity("Nota com ID " + id + " não encontrada.").build());
    }

    /**
     * Endpoint para deletar uma nota. Mapeado para: DELETE /notas/{id}
     *
     * @param id O ID da nota a ser deletada.
     * @return Resposta 204 No Content em caso de sucesso, ou 404 Not Found.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteNota(@PathParam("id") Integer id) {
        if (notaDAO.findById(id).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        notaDAO.deleteById(id);
        return Response.noContent().build();
    }

    /**
     * Converte uma entidade Nota para seu DTO correspondente.
     *
     * @param nota A entidade a ser convertida.
     * @return O DTO preenchido.
     */
    private NotaDTO toDTO(Nota nota) {
        if (nota == null) {
            return null;
        }

        NotaDTO dto = new NotaDTO();
        dto.setIdNota(nota.getIdNota());
        dto.setValorNota(nota.getValorNota());
        // ... (mapeamento dos outros campos diretos)
        dto.setPesoNota(nota.getPesoNota());
        dto.setTipoAvaliacao(nota.getTipoAvaliacao());
        dto.setDescricaoAvaliacao(nota.getDescricaoAvaliacao());
        dto.setDataAvaliacao(nota.getDataAvaliacao());
        dto.setDataLancamento(nota.getDataLancamento());
        dto.setObservacoes(nota.getObservacoes());
        dto.setAtivo(nota.getAtivo());

        // Extrai os IDs das entidades relacionadas, com verificação de nulidade.
        if (nota.getAluno() != null) {
            dto.setIdAluno(nota.getAluno().getIdUsuario());
        }
        if (nota.getDisciplina() != null) {
            dto.setIdDisciplina(nota.getDisciplina().getIdDisciplina());
        }
        if (nota.getTurma() != null) {
            dto.setIdTurma(nota.getTurma().getIdTurma());
        }
        if (nota.getProfessor() != null) {
            dto.setIdProfessor(nota.getProfessor().getIdUsuario());
        }

        return dto;
    }

    /**
     * Preenche/atualiza uma entidade Nota a partir de um DTO.
     *
     * @param nota A entidade (nova ou existente) a ser preenchida.
     * @param dto O DTO com os dados.
     * @return A entidade preenchida/atualizada.
     * @throws Exception se uma entidade obrigatória (Aluno, Disciplina) não for
     * encontrada.
     */
    private Nota fromDTO(Nota nota, NotaDTO dto) throws Exception {
        nota.setValorNota(dto.getValorNota());
        // ... (mapeamento dos outros campos diretos)
        nota.setPesoNota(dto.getPesoNota());
        nota.setTipoAvaliacao(dto.getTipoAvaliacao());
        nota.setDescricaoAvaliacao(dto.getDescricaoAvaliacao());
        nota.setDataAvaliacao(dto.getDataAvaliacao());
        nota.setObservacoes(dto.getObservacoes());
        nota.setAtivo(dto.getAtivo());

        // Busca e associa as entidades relacionadas a partir dos IDs.
        // Lança uma exceção se entidades obrigatórias não forem encontradas.
        if (dto.getIdAluno() != null) {
            nota.setAluno(usuarioDAO.findById(dto.getIdAluno())
                    .orElseThrow(() -> new Exception("Aluno com ID " + dto.getIdAluno() + " não encontrado.")));
        }
        if (dto.getIdDisciplina() != null) {
            nota.setDisciplina(disciplinaDAO.findById(dto.getIdDisciplina())
                    .orElseThrow(() -> new Exception("Disciplina com ID " + dto.getIdDisciplina() + " não encontrada.")));
        }

        // Para entidades não obrigatórias, define como nulo se não encontrar.
        if (dto.getIdTurma() != null) {
            nota.setTurma(turmaDAO.findById(dto.getIdTurma()).orElse(null));
        } else {
            nota.setTurma(null);
        }
        if (dto.getIdProfessor() != null) {
            nota.setProfessor(usuarioDAO.findById(dto.getIdProfessor()).orElse(null));
        } else {
            nota.setProfessor(null);
        }

        return nota;
    }
}
