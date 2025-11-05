/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `QuestionarioDTO`, é um DTO (Data Transfer Object) que representa as
 * informações essenciais de um modelo de questionário ou formulário de avaliação.
 *
 * Sua finalidade é servir como um "contrato" de dados para as operações da API REST
 * relacionadas a questionários. Por exemplo, ao listar os modelos de avaliação
 * disponíveis, ao criar um novo modelo ou ao exibir os detalhes de um questionário
 * específico, é um objeto com esta estrutura que será transmitido.
 *
 * O uso de um DTO para representar o questionário na camada de API é uma boa prática
 * que promove o desacoplamento. Ele permite que a entidade de banco de dados
 * (`Questionario.java`) contenha mais informações (como relacionamentos com perguntas,
 * etc.) que não precisam ser expostas na API, mantendo a comunicação mais simples,
 * segura e eficiente.
 * =================================================================================================
 */
package com.unifae.med.rest.dto;

public class QuestionarioDTO {

    // ============================================================================================
    // ATRIBUTOS
    // ============================================================================================
    /**
     * Identificador único do questionário (chave primária).
     */
    private Integer idQuestionario;

    /**
     * Nome do modelo ou título do questionário (ex: "Avaliação de Desempenho
     * Semestral").
     */
    private String nomeModelo;

    /**
     * Texto descritivo com detalhes sobre a finalidade ou o conteúdo do
     * questionário.
     */
    private String descricao;

    /**
     * Construtor padrão sem argumentos. É obrigatório para que frameworks de
     * serialização/desserialização (como o Jackson) possam instanciar o objeto
     * automaticamente.
     */
    public QuestionarioDTO() {
    }

    /**
     * Construtor de conveniência com todos os argumentos. Útil para criar e
     * inicializar uma instância do DTO de forma programática em uma única
     * linha.
     *
     * @param idQuestionario O ID do questionário.
     * @param nomeModelo O nome do modelo do questionário.
     * @param descricao A descrição do questionário.
     */
    public QuestionarioDTO(Integer idQuestionario, String nomeModelo, String descricao) {
        this.idQuestionario = idQuestionario;
        this.nomeModelo = nomeModelo;
        this.descricao = descricao;
    }

    // ============================================================================================
    // MÉTODOS GETTERS E SETTERS
    // ============================================================================================
    // Fornecem acesso encapsulado para ler e modificar os atributos da classe.
    // ============================================================================================
    public Integer getIdQuestionario() {
        return idQuestionario;
    }

    public void setIdQuestionario(Integer idQuestionario) {
        this.idQuestionario = idQuestionario;
    }

    public String getNomeModelo() {
        return nomeModelo;
    }

    public void setNomeModelo(String nomeModelo) {
        this.nomeModelo = nomeModelo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
