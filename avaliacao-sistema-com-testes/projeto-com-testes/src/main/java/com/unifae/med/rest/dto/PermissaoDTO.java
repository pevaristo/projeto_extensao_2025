/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `PermissaoDTO`, é um DTO (Data Transfer Object) que representa uma
 * permissão de acesso dentro do sistema. Permissões são usadas em mecanismos de
 * segurança e autorização para definir o que um determinado usuário ou grupo de
 * usuários pode fazer na aplicação (ex: "CADASTRAR_ALUNOS", "EDITAR_NOTAS").
 *
 * O objetivo deste DTO é fornecer uma representação de dados simples e clara de uma
 * permissão para ser usada na API REST. Ele funciona como o "contrato" de dados
 * para operações de consulta, criação ou atualização de permissões.
 *
 * Ao desacoplar a representação da API da entidade de banco de dados, este DTO
 * permite que o modelo de segurança interno evolua sem quebrar a compatibilidade
 * com os clientes que consomem a API, o que é uma prática fundamental para a
 * manutenibilidade de sistemas de software.
 * =================================================================================================
 */
package com.unifae.med.rest.dto;

public class PermissaoDTO {

    // ============================================================================================
    // ATRIBUTOS
    // ============================================================================================
    /**
     * Identificador único da permissão (chave primária).
     */
    private Integer idPermissao;

    /**
     * Nome técnico da permissão, geralmente em maiúsculas e sem espaços. É este
     * campo que o sistema utiliza internamente para verificar as autorizações.
     * (ex: "ROLE_ADMIN", "CADASTRAR_USUARIO").
     */
    private String nomePermissao;

    /**
     * Descrição amigável e clara do que a permissão concede ao usuário. (ex:
     * "Permite cadastrar, editar e excluir usuários do sistema.").
     */
    private String descricaoPermissao;

    /**
     * Construtor padrão sem argumentos. Necessário para que frameworks de
     * serialização, como o Jackson, possam instanciar o objeto ao converter
     * dados (ex: de JSON para o objeto Java).
     */
    public PermissaoDTO() {
    }

    // ============================================================================================
    // MÉTODOS GETTERS E SETTERS
    // ============================================================================================
    // Fornecem acesso controlado aos atributos privados da classe.
    // ============================================================================================
    public Integer getIdPermissao() {
        return idPermissao;
    }

    public void setIdPermissao(Integer idPermissao) {
        this.idPermissao = idPermissao;
    }

    public String getNomePermissao() {
        return nomePermissao;
    }

    public void setNomePermissao(String nomePermissao) {
        this.nomePermissao = nomePermissao;
    }

    public String getDescricaoPermissao() {
        return descricaoPermissao;
    }

    public void setDescricaoPermissao(String descricaoPermissao) {
        this.descricaoPermissao = descricaoPermissao;
    }
}
