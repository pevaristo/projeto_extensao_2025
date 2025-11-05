/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `AvaliacaoPreenchidaDTO`, é um DTO (Data Transfer Object). Seu principal objetivo
 * é servir como um "contêiner" de dados para transportar informações sobre uma avaliação
 * preenchida entre as diferentes camadas da aplicação (por exemplo, da interface do usuário para
 * o servidor ou do servidor para o banco de dados) de forma organizada.
 *
 * Ela representa a "fotografia" de uma avaliação que já foi respondida, contendo todas as
 * informações relevantes, como quem avaliou quem, qual questionário foi usado, quando e onde
 * a avaliação ocorreu, e os feedbacks dados.
 *
 * O uso de um DTO é uma boa prática pois desacopla a representação dos dados da lógica de
 * negócio e da estrutura do banco de dados, tornando o sistema mais flexível e fácil de manter.
 * =================================================================================================
 */
package com.unifae.med.rest.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AvaliacaoPreenchidaDTO {

    /**
     * Identificador único da avaliação que foi preenchida. Chave primária.
     */
    private Integer idAvaliacaoPreenchida;

    /**
     * Identificador do questionário que foi utilizado como base para esta
     * avaliação.
     */
    private Integer idQuestionario;

    /**
     * Identificador do aluno que foi o alvo da avaliação.
     */
    private Integer idAlunoAvaliado;

    /**
     * Identificador do usuário do sistema (ex: professor, preceptor) que
     * realizou a avaliação.
     */
    private Integer idAvaliador;

    /**
     * Campo para especificar o tipo do avaliador quando ele não é um usuário
     * cadastrado no sistema (ex: 'Paciente', 'Enfermeiro', 'Residente').
     */
    private String tipoAvaliadorNaoUsuario;

    /**
     * Nome do avaliador externo (não usuário) que realizou a avaliação.
     */
    private String nomeAvaliadorNaoUsuario;

    /**
     * Data em que a avaliação foi efetivamente realizada.
     */
    private LocalDate dataRealizacao;

    /**
     * Horário de início da avaliação.
     */
    private LocalTime horarioInicio;

    /**
     * Horário de término da avaliação.
     */
    private LocalTime horarioFim;

    /**
     * Descrição do local onde a avaliação ocorreu (ex: 'Enfermaria 2', 'Centro
     * Cirúrgico').
     */
    private String localRealizacao;

    /**
     * Campo de texto para registrar os pontos positivos observados durante a
     * avaliação.
     */
    private String feedbackPositivo;

    /**
     * Campo de texto para registrar os pontos que necessitam de melhoria.
     */
    private String feedbackMelhoria;

    /**
     * Campo de texto para descrever o plano ou acordo de aprendizado definido
     * após a avaliação.
     */
    private String contratoAprendizagem;

    /**
     * Construtor padrão sem argumentos. É necessário para que frameworks como o
     * Jackson (para conversão de JSON) e o JPA (para persistência de dados)
     * possam instanciar o objeto automaticamente.
     */
    public AvaliacaoPreenchidaDTO() {
    }

    // ============================================================================================
    // MÉTODOS GETTERS E SETTERS
    // ============================================================================================
    // Fornecem acesso controlado aos atributos privados da classe, seguindo o princípio
    // de encapsulamento da Programação Orientada a Objetos.
    // ============================================================================================
    public Integer getIdAvaliacaoPreenchida() {
        return idAvaliacaoPreenchida;
    }

    public void setIdAvaliacaoPreenchida(Integer idAvaliacaoPreenchida) {
        // A CORREÇÃO FOI APLICADA NESTA LINHA
        this.idAvaliacaoPreenchida = idAvaliacaoPreenchida;
    }

    public Integer getIdQuestionario() {
        return idQuestionario;
    }

    public void setIdQuestionario(Integer idQuestionario) {
        this.idQuestionario = idQuestionario;
    }

    public Integer getIdAlunoAvaliado() {
        return idAlunoAvaliado;
    }

    public void setIdAlunoAvaliado(Integer idAlunoAvaliado) {
        this.idAlunoAvaliado = idAlunoAvaliado;
    }

    public Integer getIdAvaliador() {
        return idAvaliador;
    }

    public void setIdAvaliador(Integer idAvaliador) {
        this.idAvaliador = idAvaliador;
    }

    public String getTipoAvaliadorNaoUsuario() {
        return tipoAvaliadorNaoUsuario;
    }

    public void setTipoAvaliadorNaoUsuario(String tipoAvaliadorNaoUsuario) {
        this.tipoAvaliadorNaoUsuario = tipoAvaliadorNaoUsuario;
    }

    public String getNomeAvaliadorNaoUsuario() {
        return nomeAvaliadorNaoUsuario;
    }

    public void setNomeAvaliadorNaoUsuario(String nomeAvaliadorNaoUsuario) {
        this.nomeAvaliadorNaoUsuario = nomeAvaliadorNaoUsuario;
    }

    public LocalDate getDataRealizacao() {
        return dataRealizacao;
    }

    public void setDataRealizacao(LocalDate dataRealizacao) {
        this.dataRealizacao = dataRealizacao;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalTime getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(LocalTime horarioFim) {
        this.horarioFim = horarioFim;
    }

    public String getLocalRealizacao() {
        return localRealizacao;
    }

    public void setLocalRealizacao(String localRealizacao) {
        this.localRealizacao = localRealizacao;
    }

    public String getFeedbackPositivo() {
        return feedbackPositivo;
    }

    public void setFeedbackPositivo(String feedbackPositivo) {
        this.feedbackPositivo = feedbackPositivo;
    }

    public String getFeedbackMelhoria() {
        return feedbackMelhoria;
    }

    public void setFeedbackMelhoria(String feedbackMelhoria) {
        this.feedbackMelhoria = feedbackMelhoria;
    }

    public String getContratoAprendizagem() {
        return contratoAprendizagem;
    }

    public void setContratoAprendizagem(String contratoAprendizagem) {
        this.contratoAprendizagem = contratoAprendizagem;
    }
}
