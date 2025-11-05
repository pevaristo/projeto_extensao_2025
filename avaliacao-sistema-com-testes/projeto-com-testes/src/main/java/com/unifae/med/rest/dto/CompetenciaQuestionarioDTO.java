// CompetenciaQuestionarioDTO.java (versão corrigida e completa)
package com.unifae.med.rest.dto;

import com.unifae.med.entity.CompetenciaQuestionario;

public class CompetenciaQuestionarioDTO {

    private Integer id;
    private String nomeCompetencia;
    private CompetenciaQuestionario.TipoItem tipoItem;
    private String descricaoPrompt;
    private Integer ordemExibicao;
    private boolean obrigatorio;
    private boolean ativo;
    private Integer questionarioId;

    // Construtor padrão para frameworks como Jackson
    public CompetenciaQuestionarioDTO() {
    }

    // Construtor para facilitar a conversão da Entidade para DTO
    public CompetenciaQuestionarioDTO(CompetenciaQuestionario entity) {
        this.id = entity.getIdCompetenciaQuestionario();
        this.nomeCompetencia = entity.getNomeCompetencia();
        this.tipoItem = entity.getTipoItem();
        this.descricaoPrompt = entity.getDescricaoPrompt();
        this.ordemExibicao = entity.getOrdemExibicao();
        this.obrigatorio = entity.isObrigatorio();
        this.ativo = entity.isAtivo();
        this.questionarioId = entity.getQuestionario() != null ? entity.getQuestionario().getIdQuestionario() : null;
    }

    // Getters e Setters para todos os campos...
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeCompetencia() {
        return nomeCompetencia;
    }

    public void setNomeCompetencia(String nomeCompetencia) {
        this.nomeCompetencia = nomeCompetencia;
    }

    public CompetenciaQuestionario.TipoItem getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(CompetenciaQuestionario.TipoItem tipoItem) {
        this.tipoItem = tipoItem;
    }

    public String getDescricaoPrompt() {
        return descricaoPrompt;
    }

    public void setDescricaoPrompt(String descricaoPrompt) {
        this.descricaoPrompt = descricaoPrompt;
    }

    public Integer getOrdemExibicao() {
        return ordemExibicao;
    }

    public void setOrdemExibicao(Integer ordemExibicao) {
        this.ordemExibicao = ordemExibicao;
    }

    public boolean isObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Integer getQuestionarioId() {
        return questionarioId;
    }

    public void setQuestionarioId(Integer questionarioId) {
        this.questionarioId = questionarioId;
    }
}
