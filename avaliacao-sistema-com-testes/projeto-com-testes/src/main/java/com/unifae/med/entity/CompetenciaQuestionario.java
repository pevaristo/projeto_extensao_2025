/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `CompetenciaQuestionario`, é uma entidade JPA que representa um item
 * individual (uma "competência" ou pergunta) dentro de um Questionário. Ela funciona
 * como o "Model" na arquitetura MVC.
 *
 * Cada instância desta classe corresponde a uma linha na tabela `competencias_questionario`
 * do banco de dados. Ela define as propriedades de uma pergunta, como seu texto,
 * o tipo de resposta esperada (escala, texto, múltipla escolha), a ordem de exibição,
 * e se é de preenchimento obrigatório.
 *
 * O fluxo de operação é o seguinte:
 * 1.  **Mapeamento Objeto-Relacional (ORM):** A anotação `@Entity` marca esta classe
 * para ser gerenciada pelo provedor de persistência (como Hibernate). As anotações
 * `@Table`, `@Id`, `@Column`, etc., configuram como os atributos da classe
 * se conectam às colunas da tabela do banco de dados.
 * 2.  **Relacionamentos:**
 * - `@ManyToOne`: Estabelece que muitas competências (perguntas) pertencem a um
 * único `Questionario`. Este é o lado "filho" do relacionamento.
 * - `@OneToMany`: Define que uma competência pode ter múltiplas
 * `RespostaItemAvaliacao`, ou seja, várias respostas associadas a ela em
 * diferentes avaliações preenchidas.
 * 3.  **Validação de Dados:** As anotações do pacote `jakarta.validation.constraints`
 * (como `@NotBlank`, `@NotNull`, `@Size`) são usadas para garantir a integridade
 * dos dados antes de serem persistidos no banco.
 * 4.  **Tipos de Itens:** Um `enum` interno chamado `TipoItem` define de forma segura
 * os tipos de perguntas permitidos, evitando erros de digitação e inconsistências.
 * =================================================================================================
 */
package com.unifae.med.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * Representa uma competência ou item específico de um questionário. Cada
 * competência é uma pergunta que compõe o modelo de avaliação.
 */
@Entity
@Table(name = "competencias_questionario")
public class CompetenciaQuestionario {

    // Chave primária da tabela, gerada automaticamente.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_competencia_questionario")
    private Integer idCompetenciaQuestionario;

    // O nome ou o texto da pergunta/competência a ser avaliada.
    @NotBlank(message = "Nome da competência é obrigatório")
    @Size(max = 255, message = "Nome da competência deve ter no máximo 255 caracteres")
    @Column(name = "nome_competencia", nullable = false, length = 255)
    private String nomeCompetencia;

    // Define o tipo de input que será usado para responder a esta competência.
    @NotNull(message = "O tipo do item é obrigatório.")
    @Enumerated(EnumType.STRING) // Grava o nome do enum ('escala_numerica', etc.) no banco.
    @Column(name = "tipo_item", nullable = false, columnDefinition = "enum('escala_numerica','texto_livre','multipla_escolha','checkbox')")
    private TipoItem tipoItem;

    // Texto de ajuda ou instrução adicional que pode aparecer para o usuário.
    @Column(name = "descricao_prompt", columnDefinition = "TEXT")
    private String descricaoPrompt;

    // Define a posição em que esta pergunta aparecerá no questionário.
    @NotNull(message = "A ordem de exibição é obrigatória.")
    @Column(name = "ordem_exibicao", nullable = false, columnDefinition = "int default 0")
    private Integer ordemExibicao = 0;

    // Flag que indica se a resposta para este item é obrigatória.
    @NotNull
    @Column(name = "obrigatorio", nullable = false, columnDefinition = "tinyint(1) default 1")
    private boolean obrigatorio = true;

    // Flag para ativação ou desativação lógica do item, sem removê-lo do banco.
    @NotNull
    @Column(name = "ativo", nullable = false, columnDefinition = "tinyint(1) default 1")
    private boolean ativo = true;

    // Relacionamento com Questionario: Muitas competências pertencem a um questionário.
    // FetchType.LAZY: O questionário só será carregado do banco quando for explicitamente acessado.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_questionario", nullable = false) // Chave estrangeira
    private Questionario questionario;

    // Relacionamento com RespostaItemAvaliacao: Uma competência pode ter muitas respostas (em diferentes avaliações).
    // mappedBy: Indica que o lado `competenciaQuestionario` na classe RespostaItemAvaliacao é o dono do relacionamento.
    // cascade: Operações de persistência nesta entidade serão propagadas para as respostas associadas.
    @OneToMany(mappedBy = "competenciaQuestionario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RespostaItemAvaliacao> respostasItens;

    // Enum para o campo tipo_item, representando os valores permitidos no banco de dados.
    public enum TipoItem {
        escala_numerica,
        texto_livre,
        multipla_escolha,
        checkbox
    }

    // Construtores
    public CompetenciaQuestionario() {
    }

    // Getters e Setters para todos os campos
    public Integer getIdCompetenciaQuestionario() {
        return idCompetenciaQuestionario;
    }

    public void setIdCompetenciaQuestionario(Integer idCompetenciaQuestionario) {
        this.idCompetenciaQuestionario = idCompetenciaQuestionario;
    }

    public String getNomeCompetencia() {
        return nomeCompetencia;
    }

    public void setNomeCompetencia(String nomeCompetencia) {
        this.nomeCompetencia = nomeCompetencia;
    }

    public TipoItem getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(TipoItem tipoItem) {
        this.tipoItem = tipoItem;
    }

    public String getDescricaoPrompt() {
        return descricaoPrompt;
    }

    public void setDescricaoPrompt(String descricaoPrompt) {
        this.descricaoPrompt = descricaoPrompt;
    }

    public Integer getOrdemExibicao() {
        return ordemExibicao;
    }

    public void setOrdemExibicao(Integer ordemExibicao) {
        this.ordemExibicao = ordemExibicao;
    }

    public boolean isObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Questionario getQuestionario() {
        return questionario;
    }

    public void setQuestionario(Questionario questionario) {
        this.questionario = questionario;
    }

    public List<RespostaItemAvaliacao> getRespostasItens() {
        return respostasItens;
    }

    public void setRespostasItens(List<RespostaItemAvaliacao> respostasItens) {
        this.respostasItens = respostasItens;
    }

    @Override
    public String toString() {
        return "CompetenciaQuestionario{"
                + "idCompetenciaQuestionario=" + idCompetenciaQuestionario
                + ", nomeCompetencia='" + nomeCompetencia + '\''
                + ", tipoItem=" + tipoItem
                + ", ordemExibicao=" + ordemExibicao
                + ", obrigatorio=" + obrigatorio
                + ", ativo=" + ativo
                + '}';
    }
}
