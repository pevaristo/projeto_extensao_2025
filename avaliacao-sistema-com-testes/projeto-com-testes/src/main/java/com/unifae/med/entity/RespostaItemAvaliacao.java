package com.unifae.med.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "respostas_itens_avaliacao")
public class RespostaItemAvaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resposta_avaliacao")
    private Integer idRespostaAvaliacao;

    @NotNull(message = "Avaliação preenchida é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_avaliacao_preenchida", nullable = false)
    private AvaliacaoPreenchida avaliacaoPreenchida;

    @NotNull(message = "Competência do questionário é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_competencia_questionario", nullable = false)
    private CompetenciaQuestionario competenciaQuestionario;

    @Column(name = "resposta_valor_numerico", precision = 4, scale = 1)
    private BigDecimal respostaValorNumerico;

    @Column(name = "resposta_texto", columnDefinition = "TEXT")
    private String respostaTexto;

    @Column(name = "nao_avaliado", nullable = false)
    private Boolean naoAvaliado = false;

    // Construtores
    public RespostaItemAvaliacao() {
    }

    public RespostaItemAvaliacao(AvaliacaoPreenchida avaliacaoPreenchida,
            CompetenciaQuestionario competenciaQuestionario) {
        this.avaliacaoPreenchida = avaliacaoPreenchida;
        this.competenciaQuestionario = competenciaQuestionario;
        this.naoAvaliado = false;
    }

    public RespostaItemAvaliacao(AvaliacaoPreenchida avaliacaoPreenchida,
            CompetenciaQuestionario competenciaQuestionario,
            BigDecimal respostaValorNumerico) {
        this.avaliacaoPreenchida = avaliacaoPreenchida;
        this.competenciaQuestionario = competenciaQuestionario;
        this.respostaValorNumerico = respostaValorNumerico;
        this.naoAvaliado = false;
    }

    // Getters e Setters
    public Integer getIdRespostaAvaliacao() {
        return idRespostaAvaliacao;
    }

    public void setIdRespostaAvaliacao(Integer idRespostaAvaliacao) {
        this.idRespostaAvaliacao = idRespostaAvaliacao;
    }

    public AvaliacaoPreenchida getAvaliacaoPreenchida() {
        return avaliacaoPreenchida;
    }

    public void setAvaliacaoPreenchida(AvaliacaoPreenchida avaliacaoPreenchida) {
        this.avaliacaoPreenchida = avaliacaoPreenchida;
    }

    public CompetenciaQuestionario getCompetenciaQuestionario() {
        return competenciaQuestionario;
    }

    public void setCompetenciaQuestionario(CompetenciaQuestionario competenciaQuestionario) {
        this.competenciaQuestionario = competenciaQuestionario;
    }

    public BigDecimal getRespostaValorNumerico() {
        return respostaValorNumerico;
    }

    public void setRespostaValorNumerico(BigDecimal respostaValorNumerico) {
        this.respostaValorNumerico = respostaValorNumerico;
    }

    public String getRespostaTexto() {
        return respostaTexto;
    }

    public void setRespostaTexto(String respostaTexto) {
        this.respostaTexto = respostaTexto;
    }

    public Boolean getNaoAvaliado() {
        return naoAvaliado;
    }

    public void setNaoAvaliado(Boolean naoAvaliado) {
        this.naoAvaliado = naoAvaliado;
    }

    @Override
    public String toString() {
        return "RespostaItemAvaliacao{"
                + "idRespostaAvaliacao=" + idRespostaAvaliacao
                + ", competenciaQuestionario=" + (competenciaQuestionario != null ? competenciaQuestionario.getNomeCompetencia() : null)
                + ", respostaValorNumerico=" + respostaValorNumerico
                + ", naoAvaliado=" + naoAvaliado
                + '}';
    }
}
