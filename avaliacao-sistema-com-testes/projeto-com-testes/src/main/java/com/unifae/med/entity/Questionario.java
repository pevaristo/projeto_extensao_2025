/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `Questionario`, é uma entidade JPA que representa o modelo de um
 * questionário ou formulário de avaliação. Ela atua como um container ou "pai" para
 * um conjunto de itens de `CompetenciaQuestionario`.
 *
 * Cada instância desta classe corresponde a uma linha na tabela `questionarios` do
 * banco de dados. Ela armazena informações gerais sobre o modelo, como seu nome
 * e descrição.
 *
 * O fluxo de operação é o seguinte:
 * 1.  **Mapeamento Objeto-Relacional (ORM):** A anotação `@Entity` e outras anotações
 * JPA (`@Table`, `@Id`, `@Column`, etc.) configuram o mapeamento desta classe
 * para a tabela correspondente no banco de dados.
 * 2.  **Relacionamentos:**
 * - `@OneToMany`: Define a relação com `AvaliacaoPreenchida`. Um único modelo de
 * questionário (`Questionario`) pode ser preenchido várias vezes, gerando
 * múltiplas instâncias de `AvaliacaoPreenchida`.
 * - A relação com `CompetenciaQuestionario` é implícita aqui, mas é definida
 * na classe `CompetenciaQuestionario` através do atributo `mappedBy`.
 * 3.  **Validação:** Anotações como `@NotBlank` e `@Size` garantem que os dados
 * sejam válidos antes da persistência.
 * 4.  **Estrutura:** A classe segue o padrão JavaBean com construtores, getters,
 * setters e um método `toString()` para facilitar a depuração.
 * =================================================================================================
 */
package com.unifae.med.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * Representa um modelo de questionário ou formulário de avaliação. É a entidade
 * "pai" que agrupa um conjunto de CompetenciaQuestionario.
 */
@Entity
@Table(name = "questionarios")
public class Questionario {

    // Chave primária da tabela, gerada automaticamente.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_questionario")
    private Integer idQuestionario;

    // Nome do modelo do questionário, como "Avaliação de Desempenho 360".
    @NotBlank(message = "Nome do modelo é obrigatório")
    @Size(max = 255, message = "Nome do modelo deve ter no máximo 255 caracteres")
    @Column(name = "nome_modelo", nullable = false, length = 255)
    private String nomeModelo;

    // Descrição opcional com mais detalhes sobre o propósito do questionário.
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    // Relacionamento: Um Questionário pode ter várias AvaliacoesPreenchidas.
    // cascade = CascadeType.ALL: Se um questionário for removido, todas as avaliações preenchidas associadas também serão.
    // fetch = FetchType.LAZY: A lista de avaliações preenchidas só será carregada do banco sob demanda.
    @OneToMany(mappedBy = "questionario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AvaliacaoPreenchida> avaliacoesPreenchidas;

    // Construtores
    /**
     * Construtor padrão sem argumentos, exigido pelo JPA.
     */
    public Questionario() {
    }

    /**
     * Construtor para criar uma nova instância com nome e descrição.
     *
     * @param nomeModelo O nome do questionário.
     * @param descricao A descrição do questionário.
     */
    public Questionario(String nomeModelo, String descricao) {
        this.nomeModelo = nomeModelo;
        this.descricao = descricao;
    }

    // Getters e Setters
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

    public List<AvaliacaoPreenchida> getAvaliacoesPreenchidas() {
        return avaliacoesPreenchidas;
    }

    public void setAvaliacoesPreenchidas(List<AvaliacaoPreenchida> avaliacoesPreenchidas) {
        this.avaliacoesPreenchidas = avaliacoesPreenchidas;
    }

    /**
     * Retorna uma representação em String do objeto para fins de logging e
     * depuração.
     */
    @Override
    public String toString() {
        return "Questionario{"
                + "idQuestionario=" + idQuestionario
                + ", nomeModelo='" + nomeModelo + '\''
                + ", descricao='" + descricao + '\''
                + '}';
    }
}
