/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `TurmaDTO`, é um DTO (Data Transfer Object) que representa as
 * informações essenciais de uma turma acadêmica. Uma turma é um agrupamento de
 * alunos que compartilham um mesmo período letivo e currículo.
 *
 * O objetivo deste DTO é servir como um "contrato" de dados para a API REST,
 * definindo a estrutura de informações que será enviada e recebida ao se manipular
 * dados de turmas. Ele é utilizado em operações como cadastrar uma nova turma,
 * listar as turmas existentes ou atualizar os dados de uma turma específica.
 *
 * Ao utilizar este DTO, a aplicação desacopla a representação da turma na API da
 * sua representação no banco de dados. Isso significa que a entidade interna
 * (`Turma.java`) pode conter relacionamentos complexos (como a lista de alunos)
 * que não são necessários ao se listar ou cadastrar uma turma, tornando a
 * - * comunicação da API mais leve, segura e eficiente.
 * =================================================================================================
 */
package com.unifae.med.rest.dto;

public class TurmaDTO {

    // ============================================================================================
    // ATRIBUTOS
    // ============================================================================================
    /**
     * Identificador único da turma (chave primária).
     */
    private Integer idTurma;

    /**
     * Nome descritivo e completo da turma (ex: "Turma de Medicina - Ingresso
     * 2024").
     */
    private String nomeTurma;

    /**
     * Código alfanumérico único para identificação rápida da turma (ex:
     * "MED2024").
     */
    private String codigoTurma;

    /**
     * O ano letivo de ingresso ou referência da turma (ex: 2024).
     */
    private Integer anoLetivo;

    /**
     * O semestre de ingresso da turma (ex: 1 para o primeiro semestre, 2 para o
     * segundo).
     */
    private Integer semestre;

    /**
     * Flag booleana que indica o status da turma. `true` se a turma está ativa,
     * `false` se já foi concluída ou está inativa.
     */
    private Boolean ativo;

    /**
     * Construtor padrão sem argumentos. Essencial para que frameworks de
     * serialização, como o Jackson, possam instanciar o objeto ao converter
     * dados JSON recebidos pela API.
     */
    public TurmaDTO() {
    }

    // ============================================================================================
    // MÉTODOS GETTERS E SETTERS
    // ============================================================================================
    // Fornecem acesso encapsulado para ler e modificar os atributos da classe.
    // ============================================================================================
    public Integer getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(Integer idTurma) {
        this.idTurma = idTurma;
    }

    public String getNomeTurma() {
        return nomeTurma;
    }

    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }

    public String getCodigoTurma() {
        return codigoTurma;
    }

    public void setCodigoTurma(String codigoTurma) {
        this.codigoTurma = codigoTurma;
    }

    public Integer getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(Integer anoLetivo) {
        this.anoLetivo = anoLetivo;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
