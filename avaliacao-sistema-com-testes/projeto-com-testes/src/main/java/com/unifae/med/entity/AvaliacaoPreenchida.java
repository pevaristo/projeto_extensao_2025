package com.unifae.med.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "avaliacoes_preenchidas")
public class AvaliacaoPreenchida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avaliacao_preenchida")
    private Integer idAvaliacaoPreenchida;

    @NotNull(message = "Questionário é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_questionario", nullable = false)
    private Questionario questionario;

    @NotNull(message = "Aluno avaliado é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aluno_avaliado", nullable = false)
    private Usuario alunoAvaliado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_avaliador")
    private Usuario avaliador;

    @Size(max = 50, message = "Tipo de avaliador não usuário deve ter no máximo 50 caracteres")
    @Column(name = "tipo_avaliador_nao_usuario", length = 50)
    private String tipoAvaliadorNaoUsuario;

    @Size(max = 100, message = "Nome do avaliador não usuário deve ter no máximo 100 caracteres")
    @Column(name = "nome_avaliador_nao_usuario", length = 100)
    private String nomeAvaliadorNaoUsuario;

    @NotNull(message = "Data de realização é obrigatória")
    @Column(name = "data_realizacao", nullable = false)
    private LocalDate dataRealizacao;

    @Column(name = "horario_inicio")
    private LocalTime horarioInicio;

    @Column(name = "horario_fim")
    private LocalTime horarioFim;

    @Size(max = 255, message = "Local de realização deve ter no máximo 255 caracteres")
    @Column(name = "local_realizacao", length = 255)
    private String localRealizacao;

    @Column(name = "feedback_positivo", columnDefinition = "TEXT")
    private String feedbackPositivo;

    @Column(name = "feedback_melhoria", columnDefinition = "TEXT")
    private String feedbackMelhoria;

    @Column(name = "contrato_aprendizagem", columnDefinition = "TEXT")
    private String contratoAprendizagem;

    @OneToMany(mappedBy = "avaliacaoPreenchida", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RespostaItemAvaliacao> respostasItens;

    // Construtores
    public AvaliacaoPreenchida() {
    }

    public AvaliacaoPreenchida(Questionario questionario, Usuario alunoAvaliado, LocalDate dataRealizacao) {
        this.questionario = questionario;
        this.alunoAvaliado = alunoAvaliado;
        this.dataRealizacao = dataRealizacao;
    }

    // Getters e Setters
    public Integer getIdAvaliacaoPreenchida() {
        return idAvaliacaoPreenchida;
    }

    public void setIdAvaliacaoPreenchida(Integer idAvaliacaoPreenchida) {
        this.idAvaliacaoPreenchida = idAvaliacaoPreenchida;
    }

    public Questionario getQuestionario() {
        return questionario;
    }

    public void setQuestionario(Questionario questionario) {
        this.questionario = questionario;
    }

    public Usuario getAlunoAvaliado() {
        return alunoAvaliado;
    }

    public void setAlunoAvaliado(Usuario alunoAvaliado) {
        this.alunoAvaliado = alunoAvaliado;
    }

    public Usuario getAvaliador() {
        return avaliador;
    }

    public void setAvaliador(Usuario avaliador) {
        this.avaliador = avaliador;
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

    public List<RespostaItemAvaliacao> getRespostasItens() {
        return respostasItens;
    }

    public void setRespostasItens(List<RespostaItemAvaliacao> respostasItens) {
        this.respostasItens = respostasItens;
    }

    @Override
    public String toString() {
        return "AvaliacaoPreenchida{"
                + "idAvaliacaoPreenchida=" + idAvaliacaoPreenchida
                + ", questionario=" + (questionario != null ? questionario.getNomeModelo() : null)
                + ", alunoAvaliado=" + (alunoAvaliado != null ? alunoAvaliado.getNomeCompleto() : null)
                + ", dataRealizacao=" + dataRealizacao
                + '}';
    }
}
