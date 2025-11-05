package com.unifae.med.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * USUARIO - ENTIDADE JPA PARA USUÁRIOS DO SISTEMA
 * ================================================
 * 
 * Representa todos os tipos de usuários do Sistema de Avaliação UNIFAE:
 * estudantes, professores, coordenadores e administradores.
 * 
 * RESPONSABILIDADES:
 * - Armazenar dados pessoais e de acesso
 * - Definir tipo e permissões do usuário
 * - Controlar status ativo/inativo
 * - Relacionar com permissões específicas
 * 
 * MAPEAMENTO DE BANCO:
 * - Tabela: usuarios
 * - Chave primária: id_usuario (auto incremento)
 * - Índices únicos: email, matricula_RA
 * 
 * RELACIONAMENTOS:
 * - ManyToOne com Permissao: Define permissões específicas
 * - Referenciado por AvaliacaoPreenchida: Avaliações realizadas
 * - Usado em formulários de avaliação como avaliador/avaliado
 * 
 * VALIDAÇÕES:
 * - Bean Validation para campos obrigatórios
 * - Validação de formato de email
 * - Limites de tamanho para todos os campos
 * 
 * RELACIONAMENTO COM OUTROS ARQUIVOS:
 * - TipoUsuario.java: Enum que define tipos de usuário
 * - Permissao.java: Entidade relacionada para permissões
 * - UsuarioDAO.java: DAO para operações de banco
 * - Servlets: Usam esta entidade para autenticação e autorização
 * - JSPs: Exibem dados do usuário nas interfaces
 * 
 * @author Sistema de Avaliação UNIFAE
 * @version 1.0
 */
@Entity                           // Marca como entidade JPA
@Table(name = "usuarios")         // Mapeia para tabela 'usuarios'
public class Usuario {

    /**
     * CHAVE PRIMÁRIA
     * ==============
     * Identificador único do usuário no sistema.
     * 
     * ANOTAÇÕES:
     * @Id: Define como chave primária
     * @GeneratedValue: Valor gerado automaticamente pelo banco
     * @Column: Mapeia para coluna específica
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto incremento
    @Column(name = "id_usuario")
    private Integer idUsuario;

    /**
     * NOME COMPLETO
     * =============
     * Nome completo do usuário para identificação.
     * 
     * VALIDAÇÕES:
     * - Obrigatório (não pode ser vazio)
     * - Máximo 100 caracteres
     * - Campo não nulo no banco
     */
    @NotBlank(message = "Nome completo é obrigatório")
    @Size(max = 100, message = "Nome completo deve ter no máximo 100 caracteres")
    @Column(name = "nome_completo", nullable = false, length = 100)
    private String nomeCompleto;

    /**
     * EMAIL
     * =====
     * Email único para login e comunicação.
     * 
     * VALIDAÇÕES:
     * - Obrigatório
     * - Formato de email válido
     * - Único no sistema (constraint de banco)
     * - Máximo 254 caracteres (padrão RFC)
     */
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter formato válido")
    @Size(max = 254, message = "Email deve ter no máximo 254 caracteres")
    @Column(name = "email", nullable = false, unique = true, length = 254)
    private String email;

    /**
     * TELEFONE
     * ========
     * Telefone de contato (opcional).
     * Armazena apenas números, máximo 15 dígitos (padrão brasileiro).
     */
    @Size(max = 15, message = "Telefone deve ter no máximo 15 caracteres")
    @Column(name = "telefone", length = 15)
    private String telefone;

    /**
     * MATRÍCULA/RA
     * ============
     * Registro Acadêmico único do estudante ou código do funcionário.
     * 
     * CARACTERÍSTICAS:
     * - Único no sistema
     * - Máximo 6 caracteres
     * - Opcional (nem todos usuários têm)
     */
    @Size(max = 6, message = "Matrícula/RA deve ter no máximo 6 caracteres")
    @Column(name = "matricula_RA", unique = true, length = 6)
    private String matriculaRA;

    /**
     * SENHA HASH
     * ==========
     * Hash da senha para autenticação segura.
     * 
     * SEGURANÇA:
     * - Nunca armazena senha em texto plano
     * - Usa algoritmo de hash (bcrypt, SHA-256, etc.)
     * - Campo obrigatório
     */
    @NotBlank(message = "Senha é obrigatória")
    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    /**
     * TIPO DE USUÁRIO
     * ===============
     * Define o papel do usuário no sistema.
     * 
     * TIPOS POSSÍVEIS (ver TipoUsuario.java):
     * - ESTUDANTE: Aluno que recebe avaliações
     * - PROFESSOR: Docente que avalia estudantes
     * - COORDENADOR: Gerencia avaliações e relatórios
     * - ADMINISTRADOR: Acesso total ao sistema
     * 
     * MAPEAMENTO:
     * @Enumerated(EnumType.STRING): Armazena nome do enum como string
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false)
    private TipoUsuario tipoUsuario;

    /**
     * FOTO DE PERFIL
     * ==============
     * Caminho para arquivo de foto do usuário (opcional).
     * Armazena apenas o path, não o arquivo binário.
     */
    @Column(name = "foto_perfil_path")
    private String fotoPerfilPath;

    /**
     * PERÍODO ATUAL (ESTUDANTES)
     * ==========================
     * Período/semestre atual do estudante.
     * Usado apenas para usuários do tipo ESTUDANTE.
     * Máximo 2 caracteres (ex: "1", "2", "10").
     */
    @Size(max = 2, message = "Período atual deve ter no máximo 2 caracteres")
    @Column(name = "periodo_atual_aluno", length = 2)
    private String periodoAtualAluno;

    /**
     * OBSERVAÇÕES GERAIS (ESTUDANTES)
     * ===============================
     * Campo de texto livre para observações sobre o estudante.
     * Usado por coordenadores e professores para anotações.
     * 
     * TIPO DE COLUNA:
     * TEXT: Permite textos longos (mais que VARCHAR)
     */
    @Column(name = "observacoes_gerais_aluno", columnDefinition = "TEXT")
    private String observacoesGeraisAluno;

    /**
     * RELACIONAMENTO COM PERMISSÕES
     * =============================
     * Relacionamento Many-to-One com entidade Permissao.
     * Define permissões específicas além do tipo de usuário.
     * 
     * CARACTERÍSTICAS:
     * - FetchType.LAZY: Carrega permissão apenas quando acessada
     * - JoinColumn: Define chave estrangeira
     * - Opcional: Usuário pode não ter permissão específica
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_permissao")
    private Permissao permissao;

    /**
     * STATUS ATIVO
     * ============
     * Controla se usuário está ativo no sistema.
     * 
     * COMPORTAMENTO:
     * - true: Usuário pode fazer login e usar sistema
     * - false: Usuário desabilitado (soft delete)
     * - Padrão: true (ativo)
     */
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    // ========================================
    // CONSTRUTORES
    // ========================================

    /**
     * CONSTRUTOR PADRÃO
     * =================
     * Necessário para JPA criar instâncias.
     * JPA usa reflexão e precisa de construtor sem parâmetros.
     */
    public Usuario() {
    }

    /**
     * CONSTRUTOR COM CAMPOS ESSENCIAIS
     * ================================
     * Facilita criação de usuários com dados mínimos necessários.
     * 
     * @param nomeCompleto Nome completo do usuário
     * @param email Email único para login
     * @param senhaHash Hash da senha
     * @param tipoUsuario Tipo/papel do usuário
     */
    public Usuario(String nomeCompleto, String email, String senhaHash, TipoUsuario tipoUsuario) {
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.senhaHash = senhaHash;
        this.tipoUsuario = tipoUsuario;
        this.ativo = true;  // Novo usuário sempre ativo
    }

    // ========================================
    // GETTERS E SETTERS
    // ========================================
    // Métodos de acesso necessários para JPA e frameworks web

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

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getFotoPerfilPath() {
        return fotoPerfilPath;
    }

    public void setFotoPerfilPath(String fotoPerfilPath) {
        this.fotoPerfilPath = fotoPerfilPath;
    }

    public String getPeriodoAtualAluno() {
        return periodoAtualAluno;
    }

    public void setPeriodoAtualAluno(String periodoAtualAluno) {
        this.periodoAtualAluno = periodoAtualAluno;
    }

    public String getObservacoesGeraisAluno() {
        return observacoesGeraisAluno;
    }

    public void setObservacoesGeraisAluno(String observacoesGeraisAluno) {
        this.observacoesGeraisAluno = observacoesGeraisAluno;
    }

    public Permissao getPermissao() {
        return permissao;
    }

    public void setPermissao(Permissao permissao) {
        this.permissao = permissao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    /**
     * REPRESENTAÇÃO TEXTUAL
     * =====================
     * Método toString() para debug e logging.
     * Inclui apenas campos essenciais para identificação.
     * 
     * SEGURANÇA:
     * - NÃO inclui senha hash
     * - Útil para logs e debug
     */
    @Override
    public String toString() {
        return "Usuario{"
                + "idUsuario=" + idUsuario
                + ", nomeCompleto='" + nomeCompleto + '\''
                + ", email='" + email + '\''
                + ", tipoUsuario=" + tipoUsuario
                + ", ativo=" + ativo
                + '}';
    }
}

