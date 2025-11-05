/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `DisciplinaDTO`, é um DTO (Data Transfer Object), um padrão de projeto
 * utilizado para transferir dados entre subsistemas de uma aplicação de software.
 *
 * O seu propósito principal é encapsular e agrupar os dados de uma "Disciplina"
 * para serem enviados ou recebidos através de uma API REST. Ela atua como um "contrato",
 * definindo exatamente quais campos de dados de uma disciplina são expostos externamente,
 * como para um aplicativo web ou móvel.
 *
 * Utilizar um DTO como este é uma excelente prática pois desacopla a representação
 * interna da entidade no banco de dados (a classe @Entity) da representação externa
 * na API. Isso permite que o modelo do banco de dados evolua sem quebrar os
 * "contratos" estabelecidos com os clientes da API, tornando o sistema mais robusto e
 * fácil de manter.
 * =================================================================================================
 */
package com.unifae.med.rest.dto;

public class DisciplinaDTO {

    // ============================================================================================
    // ATRIBUTOS
    // ============================================================================================
    /**
     * Identificador único da disciplina (chave primária).
     */
    private Integer idDisciplina;

    /**
     * Nome completo e descritivo da disciplina (ex: "Cardiologia").
     */
    private String nomeDisciplina;

    /**
     * Código alfanumérico único associado à disciplina (ex: "MED-045").
     */
    private String codigoDisciplina;

    /**
     * Sigla ou nome curto para a disciplina (ex: "CARDIO").
     */
    private String siglaDisciplina;

    /**
     * Indicador booleano para o status da disciplina. `true` se a disciplina
     * está ativa e `false` se está inativa.
     */
    private Boolean ativa;

    /**
     * Construtor padrão sem argumentos. É fundamental para que frameworks de
     * serialização/desserialização, como o Jackson (usado para JSON), possam
     * instanciar o objeto automaticamente ao receber os dados.
     */
    public DisciplinaDTO() {
    }

    // ============================================================================================
    // MÉTODOS GETTERS E SETTERS
    // ============================================================================================
    // Fornecem acesso controlado e encapsulado aos atributos privados da classe.
    // ============================================================================================
    public Integer getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(Integer idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public String getCodigoDisciplina() {
        return codigoDisciplina;
    }

    public void setCodigoDisciplina(String codigoDisciplina) {
        this.codigoDisciplina = codigoDisciplina;
    }

    public String getSiglaDisciplina() {
        return siglaDisciplina;
    }

    public void setSiglaDisciplina(String siglaDisciplina) {
        this.siglaDisciplina = siglaDisciplina;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }
}
