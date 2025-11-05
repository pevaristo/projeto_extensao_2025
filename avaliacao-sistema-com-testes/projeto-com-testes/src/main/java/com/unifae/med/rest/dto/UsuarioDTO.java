/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `UsuarioDTO`, é um DTO (Data Transfer Object) que representa um usuário
 * do sistema. Seu principal objetivo é servir como um contêiner de dados para transportar
 * informações de usuários de forma segura e estruturada através da API REST.
 *
 * Ele é fundamental para operações como criar um novo usuário, exibir detalhes de um
 * perfil, ou atualizar informações cadastrais.
 *
 * Uma característica importante de segurança neste DTO é a **ausência de campos
 * sensíveis**, como a senha (`password`). Ao expor apenas os dados necessários e não
 * sensíveis, o DTO atua como uma "fachada" segura para a entidade de usuário do
 * banco de dados (`Usuario.java`), que por sua vez armazena informações críticas como
 * a senha criptografada. Este desacoplamento é uma prática essencial para a segurança
 * e manutenibilidade de aplicações.
 * =================================================================================================
 */
package com.unifae.med.rest.dto;

import com.unifae.med.entity.TipoUsuario;

public class UsuarioDTO {

    // ============================================================================================
    // ATRIBUTOS
    // ============================================================================================
    /**
     * Identificador único do usuário (chave primária).
     */
    private Integer idUsuario;

    /**
     * Nome completo do usuário.
     */
    private String nomeCompleto;

    /**
     * Endereço de e-mail do usuário, frequentemente utilizado para login e
     * notificações.
     */
    private String email;

    /**
     * Número de telefone de contato do usuário.
     */
    private String telefone;

    /**
     * Matrícula ou Registro Acadêmico (RA) do usuário, se aplicável.
     */
    private String matriculaRA;

    /**
     * O tipo ou perfil do usuário no sistema, utilizando um Enum para garantir
     * valores consistentes (ex: ALUNO, PROFESSOR, ADMINISTRADOR).
     */
    private TipoUsuario tipoUsuario;

    /**
     * Flag booleana que indica o status da conta do usuário. `true` para uma
     * conta ativa, `false` para uma conta inativa ou bloqueada.
     */
    private Boolean ativo;

    /**
     * Construtor padrão sem argumentos. Indispensável para que frameworks de
     * serialização (como o Jackson) possam instanciar o objeto ao converter um
     * corpo de requisição JSON.
     */
    public UsuarioDTO() {
    }

    /**
     * Construtor de conveniência com todos os argumentos. Facilita a criação de
     * instâncias do DTO em outras partes do código, como em services ou
     * mappers, permitindo a inicialização de todos os campos de uma só vez.
     */
    public UsuarioDTO(Integer idUsuario, String nomeCompleto, String email, String telefone, String matriculaRA, TipoUsuario tipoUsuario, Boolean ativo) {
        this.idUsuario = idUsuario;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.telefone = telefone;
        this.matriculaRA = matriculaRA;
        this.tipoUsuario = tipoUsuario;
        this.ativo = ativo;
    }

    // ============================================================================================
    // MÉTODOS GETTERS E SETTERS
    // ============================================================================================
    // Permitem o acesso e a modificação controlada dos atributos privados da classe.
    // ============================================================================================
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getMatriculaRA() {
        return matriculaRA;
    }

    public void setMatriculaRA(String matriculaRA) {
        this.matriculaRA = matriculaRA;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
