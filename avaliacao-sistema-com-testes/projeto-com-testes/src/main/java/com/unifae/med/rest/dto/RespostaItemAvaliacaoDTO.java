/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `RespostaItemAvaliacaoDTO`, é um DTO (Data Transfer Object) que representa
 * a resposta a um item específico dentro de uma avaliação já preenchida. Cada instância
 * desta classe corresponde a uma única "linha" de resposta em um formulário.
 *
 * Seu principal propósito é transportar os dados de uma resposta individual para a API REST.
 * Ao submeter uma avaliação completa, por exemplo, o cliente da API normalmente envia uma
 * lista de objetos `RespostaItemAvaliacaoDTO`, cada um contendo a resposta para uma
 * competência ou pergunta do questionário.
 *
 * Principais características deste DTO:
 * 1.  **Granularidade:** É um objeto altamente específico, focado em uma única resposta,
 * ligando a avaliação geral (`idAvaliacaoPreenchida`) à pergunta específica
 * (`idCompetenciaQuestionario`).
 * 2.  **Flexibilidade:** Suporta tanto respostas numéricas (`respostaValorNumerico`),
 * ideais para escalas (ex: 1 a 5), quanto respostas textuais (`respostaTexto`),
 * para comentários ou questões abertas.
 * 3.  **Tratamento de Exceção:** O campo `naoAvaliado` permite que um item seja
 * explicitamente marcado como "Não se Aplica" ou "Não Avaliado", o que é
 * importante para a integridade dos dados.
 * =================================================================================================
 */
package com.unifae.med.rest.dto;

import java.math.BigDecimal;

public class RespostaItemAvaliacaoDTO {

    // ============================================================================================
    // ATRIBUTOS
    // ============================================================================================
    /**
     * Identificador único desta resposta específica (chave primária).
     */
    private Integer idRespostaAvaliacao;

    /**
     * ID da avaliação geral (cabeçalho) à qual esta resposta pertence.
     */
    private Integer idAvaliacaoPreenchida;

    /**
     * ID do item do questionário (a competência ou pergunta) que está sendo
     * respondido.
     */
    private Integer idCompetenciaQuestionario;

    /**
     * Campo para armazenar uma resposta numérica, como uma nota ou um valor em
     * uma escala. O uso de BigDecimal garante precisão nos valores.
     */
    private BigDecimal respostaValorNumerico;

    /**
     * Campo para armazenar uma resposta textual, como um comentário ou uma
     * justificativa.
     */
    private String respostaTexto;

    /**
     * Flag booleana que indica se o item foi marcado como 'Não Avaliado' ou
     * 'Não se Aplica'. `true` se não foi avaliado, `false` ou `null` caso
     * contrário.
     */
    private Boolean naoAvaliado;

    /**
     * Construtor padrão. Necessário para que frameworks como o Jackson (para
     * JSON) possam instanciar o objeto automaticamente durante a
     * desserialização.
     */
    public RespostaItemAvaliacaoDTO() {
    }

    // ============================================================================================
    // MÉTODOS GETTERS E SETTERS
    // ============================================================================================
    // Fornecem acesso encapsulado para ler e modificar os atributos da classe.
    // ============================================================================================
    public Integer getIdRespostaAvaliacao() {
        return idRespostaAvaliacao;
    }

    public void setIdRespostaAvaliacao(Integer idRespostaAvaliacao) {
        this.idRespostaAvaliacao = idRespostaAvaliacao;
    }

    public Integer getIdAvaliacaoPreenchida() {
        return idAvaliacaoPreenchida;
    }

    public void setIdAvaliacaoPreenchida(Integer idAvaliacaoPreenchida) {
        this.idAvaliacaoPreenchida = idAvaliacaoPreenchida;
    }

    public Integer getIdCompetenciaQuestionario() {
        return idCompetenciaQuestionario;
    }

    public void setIdCompetenciaQuestionario(Integer idCompetenciaQuestionario) {
        this.idCompetenciaQuestionario = idCompetenciaQuestionario;
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
}
