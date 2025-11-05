/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `LocalEventoDTO`, é um DTO (Data Transfer Object) que serve para
 * transportar os dados referentes a um local onde um evento pode ocorrer.
 *
 * Seu principal objetivo é definir uma estrutura de dados clara e concisa para ser
 * utilizada na comunicação com a API REST. Quando um cliente (como uma aplicação web)
 * precisa criar, visualizar ou atualizar um local, ele envia ou recebe um objeto
 * com este formato.
 *
 * O uso deste DTO permite que a representação do "Local do Evento" na API seja
 * independente da sua representação no banco de dados (a entidade JPA). Isso significa
 * que podemos adicionar ou remover campos na entidade do banco de dados para fins
 * internos sem quebrar a integração com os sistemas que consomem a API, garantindo
 * - * maior flexibilidade e manutenibilidade ao projeto.
 * =================================================================================================
 */
package com.unifae.med.rest.dto;

public class LocalEventoDTO {

    // ============================================================================================
    // ATRIBUTOS
    // ============================================================================================
    /**
     * Identificador único do local do evento (chave primária).
     */
    private Integer idLocalEvento;

    /**
     * Nome descritivo do local (ex: "Anfiteatro A", "Laboratório de Habilidades
     * Cirúrgicas").
     */
    private String nomeLocal;

    /**
     * Categoria ou tipo do local (ex: "Sala de Aula", "Hospital", "Auditório").
     */
    private String tipoLocal;

    /**
     * Endereço completo do local, incluindo rua, número e complemento, se
     * houver.
     */
    private String endereco;

    /**
     * Cidade onde o local está situado.
     */
    private String cidade;

    /**
     * Estado (UF) da federação onde a cidade do local se encontra.
     */
    private String estado;

    /**
     * Construtor padrão sem argumentos. Essencial para que frameworks de
     * serialização JSON, como o Jackson, possam criar uma instância do objeto
     * ao processar dados de uma requisição.
     */
    public LocalEventoDTO() {
    }

    // ============================================================================================
    // MÉTODOS GETTERS E SETTERS
    // ============================================================================================
    // Permitem o acesso e a modificação dos atributos privados da classe de forma controlada.
    // ============================================================================================
    public Integer getIdLocalEvento() {
        return idLocalEvento;
    }

    public void setIdLocalEvento(Integer idLocalEvento) {
        this.idLocalEvento = idLocalEvento;
    }

    public String getNomeLocal() {
        return nomeLocal;
    }

    public void setNomeLocal(String nomeLocal) {
        this.nomeLocal = nomeLocal;
    }

    public String getTipoLocal() {
        return tipoLocal;
    }

    public void setTipoLocal(String tipoLocal) {
        this.tipoLocal = tipoLocal;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
