package com.unifae.med.dao;

import com.unifae.med.entity.Turma;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class TurmaDAO extends GenericDAO<Turma, Integer> {

    public TurmaDAO() {
        super(Turma.class);
    }

    /**
     * Busca turmas com filtros dinâmicos de texto e status, e também retorna
     * estatísticas. Necessário para popular a nova listagem padronizada.
     *
     * @param search Termo de busca para nome ou código.
     * @param status Filtro de status ("ativo" ou "inativo").
     * @return Um Map contendo a lista de turmas ("list") e as estatísticas
     * ("stats").
     */
    public Map<String, Object> findWithFiltersAndStats(String search, String status) {
        EntityManager em = getEntityManager();
        try {
            // 1. Consulta principal para a lista filtrada
            StringBuilder jpql = new StringBuilder("SELECT t FROM Turma t WHERE 1=1");
            Map<String, Object> parameters = new HashMap<>();

            if (search != null && !search.trim().isEmpty()) {
                jpql.append(" AND (LOWER(t.nomeTurma) LIKE LOWER(:search) OR LOWER(t.codigoTurma) LIKE LOWER(:search))");
                parameters.put("search", "%" + search + "%");
            }

            if (status != null && !status.trim().isEmpty()) {
                if ("ativo".equalsIgnoreCase(status)) {
                    jpql.append(" AND t.ativo = :status");
                    parameters.put("status", true);
                } else if ("inativo".equalsIgnoreCase(status)) {
                    jpql.append(" AND t.ativo = :status");
                    parameters.put("status", false);
                }
            }
            jpql.append(" ORDER BY t.anoLetivo DESC, t.nomeTurma");

            TypedQuery<Turma> query = em.createQuery(jpql.toString(), Turma.class);
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            List<Turma> list = query.getResultList();

            // 2. Consultas para estatísticas
            Long totalTurmas = em.createQuery("SELECT COUNT(t) FROM Turma t", Long.class).getSingleResult();
            Long turmasAtivas = em.createQuery("SELECT COUNT(t) FROM Turma t WHERE t.ativo = true", Long.class).getSingleResult();

            Map<String, Long> stats = new HashMap<>();
            stats.put("totalTurmas", totalTurmas);
            stats.put("turmasAtivas", turmasAtivas);

            // 3. Monta o resultado final
            Map<String, Object> result = new HashMap<>();
            result.put("list", list);
            result.put("stats", stats);

            return result;
        } finally {
            em.close();
        }
    }

    /**
     * Busca todas as turmas que estão ativas. 
     * Útil para popular formulários (como o de Notas) apenas com opções relevantes.
     * @return Uma lista de turmas ativas.
     */
    public List<Turma> findAtivas() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT t FROM Turma t WHERE t.ativo = true ORDER BY t.anoLetivo DESC, t.nomeTurma";
            TypedQuery<Turma> query = em.createQuery(jpql, Turma.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Turma> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT t FROM Turma t ORDER BY t.anoLetivo DESC, t.nomeTurma", Turma.class).getResultList();
        } finally {
            em.close();
        }
    }
}
