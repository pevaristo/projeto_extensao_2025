/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `CompetenciaQuestionarioDAO`, segue o padrão de projeto Data Access Object
 * (DAO). Sua responsabilidade é encapsular toda a lógica de acesso a dados para a
 * entidade `CompetenciaQuestionario`, separando as regras de negócio da lógica de
 * persistência.
 *
 * Ela herda de uma classe `GenericDAO`, que provavelmente contém métodos comuns de
 * CRUD (create, findById, update, delete), e adiciona métodos específicos para as
 * necessidades de busca da entidade `CompetenciaQuestionario`.
 *
 * O fluxo de operação é o seguinte:
 * 1.  **Gerenciamento de EntityManager:** Cada método obtém uma instância de `EntityManager`
 * para interagir com o banco de dados e garante seu fechamento em um bloco `finally`,
 * prevenindo vazamento de recursos.
 * 2.  **Execução de Consultas:** A classe utiliza JPQL (Jakarta Persistence Query Language)
 * para construir consultas que são executadas contra o banco de dados.
 * 3.  **Métodos Específicos:**
 * - `findByQuestionario`: Busca todos os itens de um questionário específico.
 * - `findByQuestionarioAndFilters`: Método principal, usado pela camada de controle
 * (Servlet), para buscar itens de um questionário com um filtro de texto dinâmico.
 * - `findByIdWithQuestionario`: Resolve o problema comum de `LazyInitializationException`
 * ao carregar a competência e seu questionário associado em uma única consulta
 * usando `JOIN FETCH`.
 * 4.  **Tratamento de Exceções:** Uma exceção `RuntimeException` genérica é lançada
 * se ocorrer um erro durante o acesso ao banco, simplificando o tratamento de erros
 * na camada de serviço ou controle.
 * =================================================================================================
 */
package com.unifae.med.dao;

import com.unifae.med.entity.CompetenciaQuestionario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.NoResultException;
import java.util.Optional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Classe DAO para a entidade CompetenciaQuestionario. Contém métodos para
 * realizar operações de persistência específicas para as competências de um
 * questionário.
 */
@ApplicationScoped
public class CompetenciaQuestionarioDAO extends GenericDAO<CompetenciaQuestionario, Integer> {

    /**
     * Construtor que passa a classe da entidade para o GenericDAO.
     */
    public CompetenciaQuestionarioDAO() {
        super(CompetenciaQuestionario.class);
    }

    /**
     * Busca competências específicas de um questionário
     *
     * @param questionarioId ID do questionário
     * @return Lista de competências do questionário específico
     */
    public List<CompetenciaQuestionario> findByQuestionario(Integer questionarioId) {
        EntityManager em = getEntityManager();
        try {
            // JPQL para selecionar competências (cq) onde o ID do questionário associado é igual ao parâmetro.
            String jpql = "SELECT cq FROM CompetenciaQuestionario cq "
                    + "WHERE cq.questionario.idQuestionario = :questionarioId "
                    + "ORDER BY cq.idCompetenciaQuestionario";
            TypedQuery<CompetenciaQuestionario> query = em.createQuery(jpql, CompetenciaQuestionario.class);
            query.setParameter("questionarioId", questionarioId);
            return query.getResultList();
        } catch (Exception e) {
            // Encapsula exceções de persistência em uma RuntimeException.
            throw new RuntimeException("Erro ao buscar competências por questionário: " + e.getMessage(), e);
        } finally {
            // Garante que o EntityManager seja fechado, mesmo se ocorrer um erro.
            em.close();
        }
    }

    /**
     * Busca competências com filtros dinâmicos, sempre vinculadas a um
     * questionário específico. Este é o método que o servlet irá chamar.
     *
     * @param questionarioId O ID do questionário pai.
     * @param search Termo de busca para o nome ou descrição da competência.
     * @return Uma lista de competências que correspondem aos critérios de
     * busca.
     */
    public List<CompetenciaQuestionario> findByQuestionarioAndFilters(Integer questionarioId, String search) {
        EntityManager em = getEntityManager();
        try {
            // Usa StringBuilder para construir a consulta dinamicamente.
            // A consulta base sempre filtra pelo ID do questionário pai.
            StringBuilder jpql = new StringBuilder("SELECT c FROM CompetenciaQuestionario c WHERE c.questionario.idQuestionario = :questionarioId");
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("questionarioId", questionarioId);

            // Adiciona a condição de busca (search) se ela for fornecida e não estiver vazia.
            if (search != null && !search.trim().isEmpty()) {
                // Adiciona a cláusula AND para filtrar por nome ou descrição, usando LOWER para busca case-insensitive.
                jpql.append(" AND (LOWER(c.nomeCompetencia) LIKE LOWER(:search) OR LOWER(c.descricaoPrompt) LIKE LOWER(:search))");
                parameters.put("search", "%" + search + "%");
            }

            // Ordena os resultados pela ordem de exibição e, em seguida, pelo nome para consistência.
            jpql.append(" ORDER BY c.ordemExibicao, c.nomeCompetencia");

            TypedQuery<CompetenciaQuestionario> query = em.createQuery(jpql.toString(), CompetenciaQuestionario.class);

            // Define os parâmetros na query a partir do mapa.
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Busca todas as competências de um determinado tipo.
     *
     * @param tipoItem O tipo de item a ser buscado.
     * @return Lista de competências do tipo especificado.
     */
    public List<CompetenciaQuestionario> findByTipoItem(String tipoItem) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT c FROM CompetenciaQuestionario c WHERE c.tipoItem = :tipoItem ORDER BY c.nomeCompetencia";
            TypedQuery<CompetenciaQuestionario> query = em.createQuery(jpql, CompetenciaQuestionario.class);
            query.setParameter("tipoItem", tipoItem);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar competências por tipo de item: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * Busca competências cujo nome contém o termo de busca fornecido.
     *
     * @param nome Parte do nome da competência a ser buscado.
     * @return Lista de competências que correspondem ao critério.
     */
    public List<CompetenciaQuestionario> findByNomeCompetenciaContaining(String nome) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT c FROM CompetenciaQuestionario c WHERE LOWER(c.nomeCompetencia) LIKE LOWER(:nome) ORDER BY c.nomeCompetencia";
            TypedQuery<CompetenciaQuestionario> query = em.createQuery(jpql, CompetenciaQuestionario.class);
            query.setParameter("nome", "%" + nome + "%");
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar competências por nome: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * Verifica se já existe uma competência com um nome exato.
     *
     * @param nomeCompetencia O nome a ser verificado.
     * @return `true` se existir, `false` caso contrário.
     */
    public boolean existsByNomeCompetencia(String nomeCompetencia) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT COUNT(c) FROM CompetenciaQuestionario c WHERE c.nomeCompetencia = :nomeCompetencia";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("nomeCompetencia", nomeCompetencia);
            // Retorna true se a contagem for maior que 0.
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar existência de competência: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * Busca uma competência pelo seu ID, garantindo que o Questionário
     * associado seja carregado na mesma consulta (Eager loading via JOIN
     * FETCH). Isso previne a LazyInitializationException na camada de visão.
     *
     * @param id O ID da competência a ser buscada.
     * @return Um Optional contendo a competência com seu questionário
     * inicializado.
     */
    public Optional<CompetenciaQuestionario> findByIdWithQuestionario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            // "JOIN FETCH c.questionario" força a inicialização da propriedade 'questionario'.
            String jpql = "SELECT c FROM CompetenciaQuestionario c JOIN FETCH c.questionario WHERE c.idCompetenciaQuestionario = :id";
            TypedQuery<CompetenciaQuestionario> query = em.createQuery(jpql, CompetenciaQuestionario.class);
            query.setParameter("id", id);
            // Usa Optional para encapsular o resultado, que pode existir ou não.
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            // Se nenhum resultado for encontrado, retorna um Optional vazio.
            return Optional.empty();
        } finally {
            em.close();
        }
    }
}
