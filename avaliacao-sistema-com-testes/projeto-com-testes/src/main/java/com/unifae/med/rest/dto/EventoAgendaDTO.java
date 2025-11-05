/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `EventoAgendaDTO`, é um DTO (Data Transfer Object) projetado para
 * representar um evento da agenda. Sua principal função é servir como um contêiner de
 * dados limpo e estruturado para a comunicação com a API REST, seja para enviar
 * informações para criar/atualizar um evento ou para receber os detalhes de um evento
 * já existente.
 *
 * Principais características do design deste DTO:
 * 1.  **Simplicidade:** Em vez de aninhar objetos complexos (como a entidade Disciplina
 * ou Turma inteira), ele utiliza apenas os IDs (ex: `idDisciplina`, `idTurma`).
 * Isso torna o payload (carga de dados) da API mais leve e simples de consumir.
 * 2.  **Facilidade de Uso:** A separação de data (`LocalDate`) e hora (`LocalTime`)
 * facilita a manipulação desses dados em interfaces de usuário (frontend), onde
 * componentes de calendário e seletor de horas são comuns.
 * 3.  **Desacoplamento:** Ele cria uma camada de abstração entre o modelo de dados
 * interno (a entidade JPA `EventoAgenda`) e a representação externa exposta pela API,
 * permitindo que um evolua sem impactar diretamente o outro.
 * =================================================================================================
 */
package com.unifae.med.rest.dto;

import com.unifae.med.entity.StatusEvento;
import com.unifae.med.entity.TipoEvento;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventoAgendaDTO {

    // ============================================================================================
    // ATRIBUTOS
    // ============================================================================================
    /**
     * Identificador único do evento na agenda (chave primária).
     */
    private Integer idEvento;

    /**
     * Título ou nome principal do evento (ex: "Aula de Anatomia").
     */
    private String titulo;

    /**
     * Descrição mais detalhada ou informações adicionais sobre o evento.
     */
    private String descricao;

    /**
     * A data de início do evento.
     */
    private LocalDate dataInicio;

    /**
     * O horário em que o evento se inicia.
     */
    private LocalTime horarioInicio;

    /**
     * A data de término do evento (pode ser a mesma da data de início).
     */
    private LocalDate dataFim;

    /**
     * O horário em que o evento termina.
     */
    private LocalTime horarioFim;

    /**
     * ID da entidade Local onde o evento ocorrerá.
     */
    private Integer idLocal;

    /**
     * ID da entidade Disciplina associada ao evento.
     */
    private Integer idDisciplina;

    /**
     * ID da entidade Turma para a qual o evento se destina.
     */
    private Integer idTurma;

    /**
     * ID do usuário (ex: Professor) responsável pelo evento.
     */
    private Integer idResponsavel;

    /**
     * O tipo do evento, utilizando um Enum para valores definidos (ex: AULA,
     * PROVA, PALESTRA).
     */
    private TipoEvento tipoEvento;

    /**
     * O status atual do evento, utilizando um Enum para valores definidos (ex:
     * AGENDADO, REALIZADO, CANCELADO).
     */
    private StatusEvento statusEvento;

    /**
     * Construtor padrão. Necessário para que frameworks de desserialização
     * JSON, como o Jackson, possam instanciar o objeto ao receber dados pela
     * API.
     */
    public EventoAgendaDTO() {
    }

    // ============================================================================================
    // MÉTODOS GETTERS E SETTERS
    // ============================================================================================
    // Permitem o acesso e a modificação controlada dos atributos privados da classe.
    // ============================================================================================
    public Integer getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Integer idEvento) {
        this.idEvento = idEvento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public LocalTime getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(LocalTime horarioFim) {
        this.horarioFim = horarioFim;
    }

    public Integer getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(Integer idLocal) {
        this.idLocal = idLocal;
    }

    public Integer getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(Integer idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    public Integer getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(Integer idTurma) {
        this.idTurma = idTurma;
    }

    public Integer getIdResponsavel() {
        return idResponsavel;
    }

    public void setIdResponsavel(Integer idResponsavel) {
        this.idResponsavel = idResponsavel;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public StatusEvento getStatusEvento() {
        return statusEvento;
    }

    public void setStatusEvento(StatusEvento statusEvento) {
        this.statusEvento = statusEvento;
    }
}
