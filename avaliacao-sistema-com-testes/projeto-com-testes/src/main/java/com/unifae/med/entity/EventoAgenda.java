package com.unifae.med.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * EVENTO_AGENDA - ENTIDADE JPA (Java Persistence API) PARA EVENTOS DA AGENDA
 * ==========================================================================
 *
 * Esta classe é um "molde" para os objetos que representam os eventos da
 * agenda. Ela é mapeada diretamente para a tabela `eventos_agenda` no banco de
 * dados. Cada instância desta classe corresponde a uma linha na tabela.
 *
 * Conceito Chave: Entidade JPA ---------------------------- Uma Entidade JPA é
 * uma classe Java simples (POJO - Plain Old Java Object) que o Hibernate (nossa
 * implementação JPA) sabe como salvar, buscar, atualizar e deletar no banco de
 * dados. As anotações como `@Entity` e `@Table` são as "instruções" que dizem
 * ao Hibernate como fazer isso.
 */
@Entity                           // Anotação que marca esta classe como uma entidade gerenciável pelo JPA.
@Table(name = "eventos_agenda")   // Especifica que esta entidade está mapeada para a tabela chamada "eventos_agenda".
public class EventoAgenda {

    /**
     * CHAVE PRIMÁRIA (ID) =================== Identificador único e numérico
     * para cada evento no banco de dados.
     *
     * Anotações JPA Explicadas: -------------------------
     *
     * @Id: Marca este campo como a chave primária da tabela.
     * @GeneratedValue(strategy = GenerationType.IDENTITY): Instrui o banco de
     * dados a gerar automaticamente o valor para esta coluna (geralmente usando
     * auto-incremento). É a melhor estratégia para MariaDB/MySQL.
     * @Column(name = "id_evento"): Mapeia este atributo para a coluna
     * "id_evento" na tabela.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Integer idEvento;

    /**
     * TÍTULO DO EVENTO ================ Um nome claro e conciso para o evento.
     *
     * Anotações de Validação (Bean Validation) Explicadas:
     * ----------------------------------------------------
     *
     * @NotBlank: Garante que o valor não seja nulo e nem contenha apenas
     * espaços em branco.
     * @Size(max = 255): Define o tamanho máximo permitido para a string.
     *
     * Anotações JPA Explicadas: -------------------------
     * @Column(nullable = false): Define que a coluna no banco de dados não pode
     * ser nula.
     */
    @NotBlank(message = "Título é obrigatório")
    @Size(max = 255, message = "Título deve ter no máximo 255 caracteres")
    @Column(name = "titulo", nullable = false, length = 255)
    private String titulo;

    /**
     * DESCRIÇÃO DO EVENTO =================== Campo opcional para fornecer mais
     * detalhes sobre o evento.
     *
     * Anotações JPA Explicadas: -------------------------
     *
     * @Column(columnDefinition = "TEXT"): Instrui o Hibernate a usar um tipo de
     * dado no banco que suporte textos longos (como TEXT ou CLOB), em vez de um
     * VARCHAR limitado.
     */
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    /**
     * DATA E HORA DE INÍCIO ===================== Momento exato em que o evento
     * começa.
     *
     * Tipo de Dado: java.time.LocalDateTime
     * ------------------------------------- Usamos LocalDateTime (do Java 8+)
     * porque ele armazena data e hora sem fuso horário, sendo o tipo ideal para
     * mapear colunas DATETIME do banco de dados.
     *
     * Anotações de Validação Explicadas: ----------------------------------
     *
     * @NotNull: Garante que o valor não seja nulo.
     */
    @NotNull(message = "Data de início é obrigatória")
    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    /**
     * DATA E HORA DE FIM ================== Momento exato em que o evento
     * termina. Este campo é opcional.
     */
    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    /**
     * RELACIONAMENTO COM LOCAL DO EVENTO ==================================
     * Representa a associação entre um evento e seu local.
     *
     * Conceito Chave: Relacionamento Many-to-One
     * ------------------------------------------ Muitos eventos (@Many) podem
     * ocorrer em um mesmo local (@ToOne). No banco, isso é representado por uma
     * chave estrangeira (`id_local_evento`) na tabela `eventos_agenda`.
     *
     * Anotações JPA Explicadas: -------------------------
     *
     * @ManyToOne: Define o tipo de relacionamento.
     * @JoinColumn(name = "id_local_evento"): Especifica qual coluna nesta
     * tabela (`eventos_agenda`) é a chave estrangeira que se conecta à tabela
     * `locais_eventos`.
     * @FetchType.LAZY: Estratégia de otimização. "LAZY" (preguiçoso) significa
     * que o Hibernate só carregará os dados completos do `LocalEvento` do banco
     * quando ele for explicitamente acessado pela primeira vez (ex:
     * `evento.getLocalEvento().getNome()`). Isso evita carregar dados
     * desnecessários e melhora o desempenho.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_local_evento")
    private LocalEvento localEvento;

    /**
     * RELACIONAMENTO COM DISCIPLINA ============================= Associa o
     * evento a uma disciplina acadêmica. (Muitos Eventos para Uma Disciplina)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_disciplina")
    private Disciplina disciplina;

    /**
     * RELACIONAMENTO COM TURMA ======================== Associa o evento a uma
     * turma de alunos. (Muitos Eventos para Uma Turma)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_turma")
    private Turma turma;

    /**
     * RELACIONAMENTO COM RESPONSÁVEL (USUÁRIO)
     * ======================================== Associa o evento a um usuário
     * responsável. (Muitos Eventos para Um Responsável)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_responsavel")
    private Usuario responsavel;

    /**
     * TIPO DO EVENTO ============== Categoriza o evento usando um tipo
     * pré-definido (Enum).
     *
     * Conceito Chave: Enum (Enumeração) ---------------------------------
     * `TipoEvento` é um Enum, que é um tipo especial que representa um conjunto
     * fixo de constantes (AULA, PROVA, etc.). Usar Enums torna o código mais
     * seguro e legível do que usar Strings ou números mágicos.
     *
     * Anotações JPA Explicadas: -------------------------
     *
     * @Enumerated(EnumType.STRING): Instrui o Hibernate a salvar o nome do Enum
     * (ex: "AULA") como uma String no banco de dados. A alternativa,
     * `EnumType.ORDINAL`, salvaria um número (0, 1, 2...), o que é perigoso se
     * a ordem do Enum mudar no futuro. `STRING` é a prática recomendada.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_evento", nullable = false)
    private TipoEvento tipoEvento = TipoEvento.AULA; // Valor padrão

    /**
     * STATUS DO EVENTO ================ Representa o estado atual do evento no
     * seu ciclo de vida (ex: AGENDADO, CONCLUIDO). Mapeado como um Enum do tipo
     * String, assim como o `tipoEvento`.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_evento", nullable = false)
    private StatusEvento statusEvento = StatusEvento.AGENDADO; // Valor padrão

    /**
     * DATA DE CRIAÇÃO =============== Armazena o timestamp de quando o registro
     * do evento foi criado.
     *
     * Ponto Didático: Campos de Auditoria -----------------------------------
     * Campos como `dataCriacao` e `dataAtualizacao` são chamados de campos de
     * auditoria. `updatable = false` garante que, uma vez definido, este campo
     * não possa ser alterado em operações de atualização (UPDATE).
     */
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    /**
     * DATA DE ATUALIZAÇÃO =================== Armazena o timestamp da última
     * vez que o registro do evento foi modificado. Geralmente, é gerenciado por
     * gatilhos no banco de dados (`ON UPDATE CURRENT_TIMESTAMP`) ou por
     * interceptadores do JPA (`@PrePersist`, `@PreUpdate`).
     */
    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    // ========================================
    // HOOKS DE CICLO DE VIDA (LIFECYCLE HOOKS)
    // ========================================
    /**
     * MÉTODO EXECUTADO ANTES DE PERSISTIR ==================================
     * Este método será chamado automaticamente pelo Hibernate um pouco antes de
     * um novo evento ser salvo no banco de dados pela primeira vez (INSERT). É
     * o local perfeito para definir valores padrão, como datas de
     * criação/atualização.
     */
    @PrePersist
    protected void onCreate() {
        dataCriacao = dataAtualizacao = LocalDateTime.now();
    }

    /**
     * MÉTODO EXECUTADO ANTES DE ATUALIZAR ===================================
     * Este método será chamado automaticamente pelo Hibernate um pouco antes de
     * um evento existente ser atualizado no banco de dados (UPDATE). É o local
     * ideal para atualizar o timestamp de modificação.
     */
    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }

    // ========================================
    // CONSTRUTORES
    // ========================================
    /**
     * CONSTRUTOR PADRÃO (SEM ARGUMENTOS) ================================== O
     * JPA exige que toda entidade tenha um construtor público ou protegido sem
     * argumentos. Ele o utiliza internamente para criar instâncias da entidade
     * ao buscar dados do banco.
     */
    public EventoAgenda() {
    }

    // ========================================
    // GETTERS E SETTERS
    // ========================================
    // Métodos públicos para acessar e modificar os atributos da entidade.
    // São essenciais para que o JPA e outras bibliotecas (como as de renderização de JSPs)
    // possam ler e escrever nos campos privados da classe.
    public Integer getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Integer idEvento) {
        this.idEvento = idEvento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public LocalEvento getLocalEvento() {
        return localEvento;
    }

    public void setLocalEvento(LocalEvento localEvento) {
        this.localEvento = localEvento;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Usuario getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Usuario responsavel) {
        this.responsavel = responsavel;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public StatusEvento getStatusEvento() {
        return statusEvento;
    }

    public void setStatusEvento(StatusEvento statusEvento) {
        this.statusEvento = statusEvento;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    /**
     * MÉTODO toString =============== Gera uma representação textual do objeto.
     * É extremamente útil para depuração (debugging) e para registrar logs,
     * permitindo visualizar rapidamente o estado de um objeto.
     */
    @Override
    public String toString() {
        return "EventoAgenda{"
                + "idEvento=" + idEvento
                + ", titulo='" + titulo + '\''
                + ", dataInicio=" + dataInicio
                + ", tipoEvento=" + tipoEvento
                + ", statusEvento=" + statusEvento
                + '}';
    }
}
