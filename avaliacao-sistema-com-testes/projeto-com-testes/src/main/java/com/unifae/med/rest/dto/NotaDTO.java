/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `NotaDTO`, é um DTO (Data Transfer Object) que representa a nota de um
 * aluno em uma avaliação específica. Seu papel é servir como um "pacote" de dados
 * estruturado para ser trocado com a API REST, facilitando operações como lançar,
 * consultar ou atualizar notas.
 *
 * Principais aspectos deste DTO:
 * 1.  **Granularidade:** Contém todas as informações pertinentes a uma nota, incluindo
 * não apenas o valor, mas também seu peso, o tipo de avaliação e as datas
 * relevantes.
 * 2.  **Relacionamentos por ID:** Utiliza identificadores numéricos (ex: `idAluno`,
 * `idDisciplina`) para se referir a outras entidades, mantendo o objeto leve e
 * focado, sem a necessidade de carregar objetos complexos aninhados.
 * 3.  **Precisão:** O uso de `BigDecimal` para `valorNota` e `pesoNota` é uma excelente
 * prática para garantir a precisão em cálculos acadêmicos, evitando os problemas
 * de arredondamento comuns com tipos de ponto flutuante (`float` ou `double`).
 * 4.  **Desacoplamento:** Separa a representação da nota na API da sua estrutura no
 * banco de dados, o que confere maior flexibilidade e robustez ao sistema.
 * =================================================================================================
 */
package com.unifae.med.rest.dto;

import com.unifae.med.entity.TipoAvaliacao;
import java.math.BigDecimal;
import java.time.LocalDate;

public class NotaDTO {

    // ============================================================================================
    // ATRIBUTOS
    // ============================================================================================
    /**
     * Identificador único da nota (chave primária).
     */
    private Integer idNota;

    /**
     * ID do aluno que recebeu a nota.
     */
    private Integer idAluno;

    /**
     * ID da disciplina à qual esta nota pertence.
     */
    private Integer idDisciplina;

    /**
     * ID da turma na qual a avaliação foi aplicada.
     */
    private Integer idTurma;

    /**
     * ID do professor que lançou a nota.
     */
    private Integer idProfessor;

    /**
     * O valor numérico da nota obtida pelo aluno (ex: 8.5).
     */
    private BigDecimal valorNota;

    /**
     * O peso que esta nota tem no cálculo da média final (ex: 0.4 para 40%).
     */
    private BigDecimal pesoNota;

    /**
     * O tipo da avaliação, utilizando um Enum (ex: PROVA, TRABALHO, SEMINARIO).
     */
    private TipoAvaliacao tipoAvaliacao;

    /**
     * Uma breve descrição da avaliação (ex: "Prova Parcial 1", "Trabalho sobre
     * Sistema Nervoso").
     */
    private String descricaoAvaliacao;

    /**
     * A data em que a avaliação foi realizada.
     */
    private LocalDate dataAvaliacao;

    /**
     * A data em que a nota foi oficialmente lançada no sistema.
     */
    private LocalDate dataLancamento;

    /**
     * Campo para quaisquer observações ou comentários adicionais sobre a nota.
     */
    private String observacoes;

    /**
     * Flag para controle de status. `true` indica que a nota é válida/ativa,
     * `false` pode ser usado para "excluir" a nota de forma lógica (soft
     * delete).
     */
    private Boolean ativo;

    /**
     * Construtor padrão. Necessário para que frameworks como o Jackson possam
     * instanciar o objeto ao converter um JSON para este tipo de objeto.
     */
    public NotaDTO() {
    }

    // ============================================================================================
    // MÉTODOS GETTERS E SETTERS
    // ============================================================================================
    // Fornecem acesso encapsulado para ler e modificar os atributos da classe.
    // ============================================================================================
    public Integer getIdNota() {
        return idNota;
    }

    public void setIdNota(Integer idNota) {
        this.idNota = idNota;
    }

    public Integer getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(Integer idAluno) {
        this.idAluno = idAluno;
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

    public Integer getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(Integer idProfessor) {
        this.idProfessor = idProfessor;
    }

    public BigDecimal getValorNota() {
        return valorNota;
    }

    public void setValorNota(BigDecimal valorNota) {
        this.valorNota = valorNota;
    }

    public BigDecimal getPesoNota() {
        return pesoNota;
    }

    public void setPesoNota(BigDecimal pesoNota) {
        this.pesoNota = pesoNota;
    }

    public TipoAvaliacao getTipoAvaliacao() {
        return tipoAvaliacao;
    }

    public void setTipoAvaliacao(TipoAvaliacao tipoAvaliacao) {
        this.tipoAvaliacao = tipoAvaliacao;
    }

    public String getDescricaoAvaliacao() {
        return descricaoAvaliacao;
    }

    public void setDescricaoAvaliacao(String descricaoAvaliacao) {
        this.descricaoAvaliacao = descricaoAvaliacao;
    }

    public LocalDate getDataAvaliacao() {
        return dataAvaliacao;
    }

    public void setDataAvaliacao(LocalDate dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
