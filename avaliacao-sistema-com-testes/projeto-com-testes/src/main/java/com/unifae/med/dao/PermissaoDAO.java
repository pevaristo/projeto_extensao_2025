package com.unifae.med.dao;

import com.unifae.med.entity.Permissao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * PERMISSAODAO - DATA ACCESS OBJECT PARA A ENTIDADE PERMISSAO
 * ============================================================ * Especializa o
 * GenericDAO para a entidade Permissao. Implementa consultas específicas para
 * buscar permissões por nome e com filtros.
 *
 * * @author Sistema de Avaliação UNIFAE
 * @version 2.0 - Adicionado método de busca com filtros e estatísticas
 */

@ApplicationScoped
public class PermissaoDAO extends GenericDAO<Permissao, Integer> {

    /**
     * Construtor padrão que define a classe da entidade para o GenericDAO.
     */
    public PermissaoDAO() {
        super(Permissao.class);
    }

    /**
     * Busca permissões com filtros dinâmicos e retorna estatísticas para a
     * listagem.
     *
     * @param search Termo de busca para nome ou descrição.
     * @return Um Map contendo a lista de permissões ("list") e as estatísticas
     * ("stats").
     */
    public Map<String, Object> findWithFiltersAndStats(String search) {
        EntityManager em = getEntityManager();
        try {
            // 1. Consulta principal para a lista filtrada
            StringBuilder jpql = new StringBuilder("SELECT p FROM Permissao p WHERE 1=1");
            Map<String, Object> parameters = new HashMap<>();

            if (search != null && !search.trim().isEmpty()) {
                jpql.append(" AND (LOWER(p.nomePermissao) LIKE LOWER(:search) OR LOWER(p.descricaoPermissao) LIKE LOWER(:search))");
                parameters.put("search", "%" + search + "%");
            }
            jpql.append(" ORDER BY p.nomePermissao");

            TypedQuery<Permissao> query = em.createQuery(jpql.toString(), Permissao.class);
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            List<Permissao> list = query.getResultList();

            // 2. Consulta para estatísticas
            Long totalPermissoes = em.createQuery("SELECT COUNT(p) FROM Permissao p", Long.class).getSingleResult();

            Map<String, Long> stats = new HashMap<>();
            stats.put("totalPermissoes", totalPermissoes);

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
     * Busca uma permissão pelo seu nome exato.
     *
     * @param nomePermissao O nome da permissão a ser buscada.
     * @return Um Optional contendo a permissão se encontrada.
     */
    public Optional<Permissao> findByNome(String nomePermissao) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT p FROM Permissao p WHERE p.nomePermissao = :nomePermissao";
            TypedQuery<Permissao> query = em.createQuery(jpql, Permissao.class);
            query.setParameter("nomePermissao", nomePermissao);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
}
