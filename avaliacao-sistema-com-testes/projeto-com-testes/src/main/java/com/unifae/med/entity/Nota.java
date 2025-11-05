package com.unifae.med.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * NOTA - ENTIDADE JPA PARA NOTAS DOS ALUNOS
 * =========================================
 * 
 * Representa as notas dos alunos nas disciplinas do Sistema UNIFAE.
 * Complementa o sistema de avaliações existente com controle de notas numéricas.
 * 
 * RESPONSABILIDADES:
 * - Armazenar notas numéricas dos alunos
 * - Relacionar aluno, disciplina e turma
 * - Controlar tipos de avaliação (prova, trabalho, etc.)
 * - Registrar datas e observações
 * - Calcular médias e status de aprovação
 * 
 * MAPEAMENTO DE BANCO:
 * - Tabela: notas
 * - Chave primária: id_nota (auto incremento)
 * - Relacionamentos: aluno, disciplina, turma
 * 
 * RELACIONAMENTOS:
 * - ManyToOne com Usuario (aluno): Aluno que recebeu a nota
 * - ManyToOne com Disciplina: Disciplina da nota
 * - ManyToOne com Turma: Turma onde foi aplicada a avaliação
 * - ManyToOne com Usuario (professor): Professor que atribuiu a nota
 * 
 * VALIDAÇÕES:
 * - Bean Validation para campos obrigatórios
 * - Validação de faixa de notas (0.0 a 10.0)
 * - Limites de tamanho para todos os campos
 * 
 * RELACIONAMENTO COM OUTROS ARQUIVOS:
 * - Usuario.java: Entidade relacionada (aluno e professor)
 * - Disciplina.java: Entidade relacionada
 * - Turma.java: Entidade relacionada
 * - NotaDAO.java: DAO para operações de banco
 * - NotaServlet.java: Controlador web
 * 
 * @author Sistema de Avaliação UNIFAE
 * @version 1.0
 */
@Entity
@Table(name = "notas")
public class Nota {

    /**
     * CHAVE PRIMÁRIA
     * ==============
     * Identificador único da nota no sistema.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nota")
    private Integer idNota;

    /**
     * RELACIONAMENTO COM ALUNO
     * ========================
     * Aluno que recebeu a nota.
     * Relacionamento obrigatório Many-to-One.
     */
    @NotNull(message = "Aluno é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aluno", nullable = false)
    private Usuario aluno;

    /**
     * RELACIONAMENTO COM DISCIPLINA
     * =============================
     * Disciplina na qual a nota foi atribuída.
     * Relacionamento obrigatório Many-to-One.
     */
    @NotNull(message = "Disciplina é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_disciplina", nullable = false)
    private Disciplina disciplina;

    /**
     * RELACIONAMENTO COM TURMA
     * ========================
     * Turma onde a avaliação foi aplicada.
     * Relacionamento opcional Many-to-One.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_turma")
    private Turma turma;

    /**
     * RELACIONAMENTO COM PROFESSOR
     * ============================
     * Professor que atribuiu a nota.
     * Relacionamento opcional Many-to-One.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_professor")
    private Usuario professor;

    /**
     * VALOR DA NOTA
     * =============
     * Nota numérica do aluno (0.0 a 10.0).
     * Campo obrigatório com validação de faixa.
     */
    @NotNull(message = "Valor da nota é obrigatório")
    @DecimalMin(value = "0.0", message = "Nota deve ser maior ou igual a 0.0")
    @DecimalMax(value = "10.0", message = "Nota deve ser menor ou igual a 10.0")
    @Column(name = "valor_nota", nullable = false, precision = 4, scale = 2)
    private BigDecimal valorNota;

    /**
     * PESO DA NOTA
     * ============
     * Peso da nota para cálculo de média.
     * Padrão: 1.0 (peso normal).
     */
    @DecimalMin(value = "0.1", message = "Peso deve ser maior que 0.1")
    @DecimalMax(value = "10.0", message = "Peso deve ser menor ou igual a 10.0")
    @Column(name = "peso_nota", precision = 4, scale = 2)
    private BigDecimal pesoNota = BigDecimal.ONE;

    /**
     * TIPO DE AVALIAÇÃO
     * =================
     * Tipo da avaliação que gerou a nota.
     * Enum com valores pré-definidos.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_avaliacao", nullable = false)
    private TipoAvaliacao tipoAvaliacao;

    /**
     * DESCRIÇÃO DA AVALIAÇÃO
     * ======================
     * Descrição detalhada da avaliação.
     * Campo opcional para contexto adicional.
     */
    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    @Column(name = "descricao_avaliacao", length = 500)
    private String descricaoAvaliacao;

    /**
     * DATA DA AVALIAÇÃO
     * =================
     * Data em que a avaliação foi realizada.
     * Campo obrigatório.
     */
    @NotNull(message = "Data da avaliação é obrigatória")
    @Column(name = "data_avaliacao", nullable = false)
    private LocalDate dataAvaliacao;

    /**
     * DATA DE LANÇAMENTO
     * ==================
     * Data em que a nota foi lançada no sistema.
     * Preenchida automaticamente.
     */
    @Column(name = "data_lancamento", nullable = false)
    private LocalDate dataLancamento = LocalDate.now();

    /**
     * OBSERVAÇÕES
     * ===========
     * Observações adicionais sobre a nota.
     * Campo opcional para feedback.
     */
    @Size(max = 1000, message = "Observações devem ter no máximo 1000 caracteres")
    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    /**
     * STATUS ATIVO
     * ============
     * Controla se a nota está ativa no sistema.
     * Permite "soft delete" de notas.
     */
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    // ========================================
    // CONSTRUTORES
    // ========================================

    /**
     * CONSTRUTOR PADRÃO
     * =================
     * Necessário para JPA.
     */
    public Nota() {
    }

    /**
     * CONSTRUTOR COM CAMPOS ESSENCIAIS
     * ================================
     * Facilita criação de notas com dados mínimos.
     */
    public Nota(Usuario aluno, Disciplina disciplina, BigDecimal valorNota, 
                TipoAvaliacao tipoAvaliacao, LocalDate dataAvaliacao) {
        this.aluno = aluno;
        this.disciplina = disciplina;
        this.valorNota = valorNota;
        this.tipoAvaliacao = tipoAvaliacao;
        this.dataAvaliacao = dataAvaliacao;
        this.dataLancamento = LocalDate.now();
        this.ativo = true;
    }

    // ========================================
    // GETTERS E SETTERS
    // ========================================

    public Integer getIdNota() {
        return idNota;
    }

    public void setIdNota(Integer idNota) {
        this.idNota = idNota;
    }

    public Usuario getAluno() {
        return aluno;
    }

    public void setAluno(Usuario aluno) {
        this.aluno = aluno;
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

    public Usuario getProfessor() {
        return professor;
    }

    public void setProfessor(Usuario professor) {
        this.professor = professor;
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

    // ========================================
    // MÉTODOS AUXILIARES
    // ========================================

    /**
     * CALCULAR NOTA PONDERADA
     * =======================
     * Calcula o valor da nota multiplicado pelo peso.
     */
    public BigDecimal calcularNotaPonderada() {
        if (valorNota == null || pesoNota == null) {
            return BigDecimal.ZERO;
        }
        return valorNota.multiply(pesoNota);
    }

    /**
     * VERIFICAR SE APROVADO
     * =====================
     * Verifica se a nota é suficiente para aprovação (>= 6.0).
     */
    public boolean isAprovado() {
        if (valorNota == null) {
            return false;
        }
        return valorNota.compareTo(new BigDecimal("6.0")) >= 0;
    }

    /**
     * REPRESENTAÇÃO TEXTUAL
     * =====================
     * Método toString() para debug e logging.
     */
    @Override
    public String toString() {
        return "Nota{" +
                "idNota=" + idNota +
                ", aluno=" + (aluno != null ? aluno.getNomeCompleto() : null) +
                ", disciplina=" + (disciplina != null ? disciplina.getNomeDisciplina() : null) +
                ", valorNota=" + valorNota +
                ", tipoAvaliacao=" + tipoAvaliacao +
                ", dataAvaliacao=" + dataAvaliacao +
                ", ativo=" + ativo +
                '}';
    }
}
