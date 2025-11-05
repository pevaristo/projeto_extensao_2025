package com.unifae.med.dao;

import com.unifae.med.entity.Disciplina;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DisciplinaDAO extends GenericDAO<Disciplina, Integer> {

    public DisciplinaDAO() {
        super(Disciplina.class);
    }

    /**
     * Busca uma disciplina pelo seu nome exato.
     *
     * @param nomeDisciplina O nome da disciplina.
     * @return Um Optional contendo a disciplina se encontrada.
     */
    public Optional<Disciplina> findByNomeDisciplina(String nomeDisciplina) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT d FROM Disciplina d WHERE d.nomeDisciplina = :nomeDisciplina";
            TypedQuery<Disciplina> query = em.createQuery(jpql, Disciplina.class);
            query.setParameter("nomeDisciplina", nomeDisciplina);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    /**
     * Busca uma disciplina pela sua sigla exata.
     *
     * @param sigla A sigla da disciplina.
     * @return Um Optional contendo a disciplina se encontrada.
     */
    public Optional<Disciplina> findBySiglaDisciplina(String sigla) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT d FROM Disciplina d WHERE d.siglaDisciplina = :sigla";
            TypedQuery<Disciplina> query = em.createQuery(jpql, Disciplina.class);
            query.setParameter("sigla", sigla);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    /**
     * Busca todas as disciplinas que estão ativas.
     *
     * @return Uma lista de disciplinas ativas.
     */
    public List<Disciplina> findAtivas() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT d FROM Disciplina d WHERE d.ativa = true ORDER BY d.nomeDisciplina";
            TypedQuery<Disciplina> query = em.createQuery(jpql, Disciplina.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Disciplina> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT d FROM Disciplina d ORDER BY d.nomeDisciplina", Disciplina.class).getResultList();
        } finally {
            em.close();
        }
    }

}
