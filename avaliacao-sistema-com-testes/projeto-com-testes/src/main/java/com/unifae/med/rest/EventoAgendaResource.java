/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `EventoAgendaResource`, é um recurso JAX-RS que define os endpoints da
 * API REST para o gerenciamento completo de "Eventos da Agenda". Ela implementa todas
 * as operações de CRUD (Criar, Ler, Atualizar, Deletar) para os eventos.
 *
 * A complexidade desta classe reside na sua responsabilidade de orquestrar múltiplos
 * DAOs (Data Access Objects). Como um `EventoAgenda` possui relacionamentos com outras
 * entidades (Local, Disciplina, Turma, Usuário), esta classe precisa:
 *
 * 1.  **Ao Criar/Atualizar (Método `fromDTO`):** Receber um `EventoAgendaDTO` com apenas os IDs
 * das entidades relacionadas, usar os DAOs correspondentes (`localDAO`, `disciplinaDAO`, etc.)
 * para buscar as entidades completas no banco de dados e, então, montar a entidade
 * `EventoAgenda` final para ser salva.
 *
 * 2.  **Ao Ler (Método `toDTO`):** Receber uma entidade `EventoAgenda` completa do banco de
 * dados e convertê-la em um `EventoAgendaDTO`, extraindo apenas os IDs das entidades
 * relacionadas e separando `LocalDateTime` em `LocalDate` e `LocalTime` para
 * facilitar o consumo pelo frontend.
 *
 * Este padrão de "montagem" e "desmontagem" de objetos é central para manter a API
 * desacoplada e eficiente, evitando que dados desnecessários trafeguem pela rede.
 * =================================================================================================
 */
package com.unifae.med.rest;

import com.unifae.med.dao.*;
import com.unifae.med.entity.EventoAgenda;
import com.unifae.med.rest.dto.EventoAgendaDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Path("/eventos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventoAgendaResource {

    // DAOs necessários para persistir o evento e buscar suas entidades relacionadas.
    private final EventoAgendaDAO eventoDAO = new EventoAgendaDAO();
    private final LocalEventoDAO localDAO = new LocalEventoDAO();
    private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    private final TurmaDAO turmaDAO = new TurmaDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Endpoint para listar todos os eventos da agenda. Mapeado para: GET
     * /eventos
     *
     * @return Uma lista de EventoAgendaDTO.
     */
    @GET
    public List<EventoAgendaDTO> getAllEventos() {
        return eventoDAO.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Endpoint para buscar um evento pelo seu ID. Mapeado para: GET
     * /eventos/{id}
     *
     * @param id O ID do evento a ser buscado.
     * @return Resposta 200 OK com o DTO do evento se encontrado, ou 404 Not
     * Found.
     */
    @GET
    @Path("/{id}")
    public Response getEventoById(@PathParam("id") Integer id) {
        return eventoDAO.findById(id)
                .map(this::toDTO)
                .map(dto -> Response.ok(dto).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * Endpoint para criar um novo evento. Mapeado para: POST /eventos
     *
     * @param dto O DTO com os dados do evento a ser criado.
     * @return Resposta 201 Created com o DTO do evento recém-criado.
     */
    @POST
    public Response createEvento(EventoAgendaDTO dto) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("O corpo da requisição não pode ser vazio.").build();
        }

        // Converte o DTO recebido para uma nova entidade JPA, buscando os objetos relacionados.
        EventoAgenda evento = fromDTO(new EventoAgenda(), dto);

        // Salva a nova entidade no banco.
        EventoAgenda novoEvento = eventoDAO.save(evento);

        // Retorna o status 201 e o DTO do objeto criado.
        return Response.status(Response.Status.CREATED).entity(toDTO(novoEvento)).build();
    }

    /**
     * Endpoint para atualizar um evento existente. Mapeado para: PUT
     * /eventos/{id}
     *
     * @param id O ID do evento a ser atualizado.
     * @param dto O DTO com os novos dados do evento.
     * @return Resposta 200 OK com o DTO atualizado, ou 404 Not Found se o
     * evento não existir.
     */
    @PUT
    @Path("/{id}")
    public Response updateEvento(@PathParam("id") Integer id, EventoAgendaDTO dto) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("O corpo da requisição não pode ser vazio.").build();
        }

        // Busca a entidade existente no banco de dados.
        EventoAgenda eventoExistente = eventoDAO.findById(id).orElse(null);

        if (eventoExistente == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Evento com ID " + id + " não encontrado.").build();
        }

        // Atualiza a entidade existente com os dados do DTO.
        EventoAgenda eventoParaAtualizar = fromDTO(eventoExistente, dto);

        // Salva as alterações.
        EventoAgenda eventoAtualizado = eventoDAO.save(eventoParaAtualizar);

        return Response.ok(toDTO(eventoAtualizado)).build();
    }

    /**
     * Endpoint para deletar um evento. Mapeado para: DELETE /eventos/{id}
     *
     * @param id O ID do evento a ser deletado.
     * @return Resposta 204 No Content em caso de sucesso, ou 404 Not Found.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteEvento(@PathParam("id") Integer id) {
        // Verifica se o evento existe antes de tentar deletar para retornar um 404 mais claro.
        if (eventoDAO.findById(id).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Evento com ID " + id + " não encontrado.").build();
        }
        eventoDAO.deleteById(id);
        return Response.noContent().build();
    }

    /**
     * Converte uma entidade EventoAgenda para seu respectivo DTO. Este método
     * "desmonta" a entidade para uma representação segura e simplificada para a
     * API.
     *
     * @param evento A entidade a ser convertida.
     * @return O DTO preenchido.
     */
    private EventoAgendaDTO toDTO(EventoAgenda evento) {
        if (evento == null) {
            return null;
        }

        EventoAgendaDTO dto = new EventoAgendaDTO();
        dto.setIdEvento(evento.getIdEvento());
        dto.setTitulo(evento.getTitulo());
        dto.setDescricao(evento.getDescricao());

        // Separa LocalDateTime da entidade em LocalDate e LocalTime para o DTO.
        if (evento.getDataInicio() != null) {
            dto.setDataInicio(evento.getDataInicio().toLocalDate());
            dto.setHorarioInicio(evento.getDataInicio().toLocalTime());
        }
        if (evento.getDataFim() != null) {
            dto.setDataFim(evento.getDataFim().toLocalDate());
            dto.setHorarioFim(evento.getDataFim().toLocalTime());
        }

        // Mapeia os IDs das entidades relacionadas, verificando a nulidade.
        if (evento.getLocalEvento() != null) {
            dto.setIdLocal(evento.getLocalEvento().getIdLocalEvento());
        }
        if (evento.getDisciplina() != null) {
            dto.setIdDisciplina(evento.getDisciplina().getIdDisciplina());
        }
        if (evento.getTurma() != null) {
            dto.setIdTurma(evento.getTurma().getIdTurma());
        }
        if (evento.getResponsavel() != null) {
            dto.setIdResponsavel(evento.getResponsavel().getIdUsuario());
        }

        dto.setTipoEvento(evento.getTipoEvento());
        dto.setStatusEvento(evento.getStatusEvento());

        return dto;
    }

    /**
     * Atualiza uma entidade EventoAgenda a partir de um DTO. Este método
     * "monta" a entidade, buscando os objetos relacionados no banco de dados a
     * partir dos IDs fornecidos no DTO.
     *
     * @param evento A entidade (nova ou existente) a ser preenchida.
     * @param dto O DTO com os dados.
     * @return A entidade preenchida e pronta para ser persistida.
     */
    private EventoAgenda fromDTO(EventoAgenda evento, EventoAgendaDTO dto) {
        evento.setTitulo(dto.getTitulo());
        evento.setDescricao(dto.getDescricao());

        // Combina data e hora do DTO em um único LocalDateTime para a entidade.
        if (dto.getDataInicio() != null && dto.getHorarioInicio() != null) {
            evento.setDataInicio(LocalDateTime.of(dto.getDataInicio(), dto.getHorarioInicio()));
        }
        if (dto.getDataFim() != null && dto.getHorarioFim() != null) {
            evento.setDataFim(LocalDateTime.of(dto.getDataFim(), dto.getHorarioFim()));
        } else {
            evento.setDataFim(null); // Garante que o campo seja nulo se não for fornecido.
        }

        // Para cada ID no DTO, busca a entidade correspondente no banco e a associa ao evento.
        if (dto.getIdLocal() != null) {
            localDAO.findById(dto.getIdLocal()).ifPresent(evento::setLocalEvento);
        } else {
            evento.setLocalEvento(null);
        }

        if (dto.getIdDisciplina() != null) {
            disciplinaDAO.findById(dto.getIdDisciplina()).ifPresent(evento::setDisciplina);
        } else {
            evento.setDisciplina(null);
        }

        if (dto.getIdTurma() != null) {
            turmaDAO.findById(dto.getIdTurma()).ifPresent(evento::setTurma);
        } else {
            evento.setTurma(null);
        }

        if (dto.getIdResponsavel() != null) {
            usuarioDAO.findById(dto.getIdResponsavel()).ifPresent(evento::setResponsavel);
        } else {
            evento.setResponsavel(null);
        }

        evento.setTipoEvento(dto.getTipoEvento());
        evento.setStatusEvento(dto.getStatusEvento());

        return evento;
    }
}
