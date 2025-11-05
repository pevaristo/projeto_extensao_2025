package com.unifae.med.dao;

import com.unifae.med.entity.Questionario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class QuestionarioDAO extends GenericDAO<Questionario, Integer> {

    public QuestionarioDAO() {
        super(Questionario.class);
    }

    /**
     * Busca questionários com filtros dinâmicos e retorna estatísticas.
     *
     * @param search Termo de busca para nome ou descrição.
     * @return Um Map contendo a lista de questionários ("list") e as
     * estatísticas ("stats").
     */
    public Map<String, Object> findWithFiltersAndStats(String search) {
        EntityManager em = getEntityManager();
        try {
            // 1. Consulta principal para a lista filtrada
            StringBuilder jpql = new StringBuilder("SELECT q FROM Questionario q WHERE 1=1");
            Map<String, Object> parameters = new HashMap<>();

            if (search != null && !search.trim().isEmpty()) {
                jpql.append(" AND (LOWER(q.nomeModelo) LIKE LOWER(:search) OR LOWER(q.descricao) LIKE LOWER(:search))");
                parameters.put("search", "%" + search + "%");
            }
            jpql.append(" ORDER BY q.nomeModelo");

            TypedQuery<Questionario> query = em.createQuery(jpql.toString(), Questionario.class);
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            List<Questionario> list = query.getResultList();

            // 2. Consulta para estatísticas
            Long totalQuestionarios = em.createQuery("SELECT COUNT(q) FROM Questionario q", Long.class).getSingleResult();

            Map<String, Long> stats = new HashMap<>();
            stats.put("totalQuestionarios", totalQuestionarios);

            // 3. Monta o resultado final
            Map<String, Object> result = new HashMap<>();
            result.put("list", list);
            result.put("stats", stats);

            return result;
        } finally {
            em.close();
        }
    }

    // Métodos existentes mantidos
    public Optional<Questionario> findByNomeModelo(String nomeModelo) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT q FROM Questionario q WHERE q.nomeModelo = :nomeModelo";
            TypedQuery<Questionario> query = em.createQuery(jpql, Questionario.class);
            query.setParameter("nomeModelo", nomeModelo);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
}
