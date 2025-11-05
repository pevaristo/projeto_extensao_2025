package com.unifae.med.dao;

import com.unifae.med.entity.LocalEvento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LocalEventoDAO extends GenericDAO<LocalEvento, Integer> {

    public LocalEventoDAO() {
        super(LocalEvento.class);
    }

    /**
     * Busca todos os locais de eventos ordenados por nome
     */
    @Override
    public List<LocalEvento> findAll() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT l FROM LocalEvento l ORDER BY l.nomeLocal";
            TypedQuery<LocalEvento> query = em.createQuery(jpql, LocalEvento.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar todos os locais de eventos: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * Busca locais por tipo
     */
    public List<LocalEvento> findByTipo(String tipoLocal) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT l FROM LocalEvento l WHERE l.tipoLocal = :tipoLocal ORDER BY l.nomeLocal";
            TypedQuery<LocalEvento> query = em.createQuery(jpql, LocalEvento.class);
            query.setParameter("tipoLocal", tipoLocal);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar locais por tipo: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * Busca locais por cidade
     */
    public List<LocalEvento> findByCidade(String cidade) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT l FROM LocalEvento l WHERE l.cidade = :cidade ORDER BY l.nomeLocal";
            TypedQuery<LocalEvento> query = em.createQuery(jpql, LocalEvento.class);
            query.setParameter("cidade", cidade);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar locais por cidade: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * Busca locais por nome (busca parcial)
     */
    public List<LocalEvento> findByNomeContaining(String nome) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT l FROM LocalEvento l WHERE LOWER(l.nomeLocal) LIKE LOWER(:nome) ORDER BY l.nomeLocal";
            TypedQuery<LocalEvento> query = em.createQuery(jpql, LocalEvento.class);
            query.setParameter("nome", "%" + nome + "%");
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar locais por nome: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}
