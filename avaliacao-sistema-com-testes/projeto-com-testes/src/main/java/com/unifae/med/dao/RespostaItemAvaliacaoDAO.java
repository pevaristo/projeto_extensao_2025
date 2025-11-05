package com.unifae.med.dao;

import com.unifae.med.entity.RespostaItemAvaliacao;
import com.unifae.med.entity.AvaliacaoPreenchida;
import com.unifae.med.entity.CompetenciaQuestionario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RespostaItemAvaliacaoDAO extends GenericDAO<RespostaItemAvaliacao, Integer> {

    public RespostaItemAvaliacaoDAO() {
        super(RespostaItemAvaliacao.class);
    }

    public List<RespostaItemAvaliacao> findByAvaliacaoPreenchida(AvaliacaoPreenchida avaliacaoPreenchida) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT r FROM RespostaItemAvaliacao r "
                    + "LEFT JOIN FETCH r.competenciaQuestionario "
                    + "WHERE r.avaliacaoPreenchida = :avaliacaoPreenchida "
                    + "ORDER BY r.competenciaQuestionario.nomeCompetencia";
            TypedQuery<RespostaItemAvaliacao> query = em.createQuery(jpql, RespostaItemAvaliacao.class);
            query.setParameter("avaliacaoPreenchida", avaliacaoPreenchida);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar respostas por avaliação preenchida: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public List<RespostaItemAvaliacao> findByCompetenciaQuestionario(CompetenciaQuestionario competenciaQuestionario) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT r FROM RespostaItemAvaliacao r WHERE r.competenciaQuestionario = :competenciaQuestionario ORDER BY r.avaliacaoPreenchida.dataRealizacao DESC";
            TypedQuery<RespostaItemAvaliacao> query = em.createQuery(jpql, RespostaItemAvaliacao.class);
            query.setParameter("competenciaQuestionario", competenciaQuestionario);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar respostas por competência: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public List<RespostaItemAvaliacao> findByAvaliacaoPreenchidaAndCompetenciaQuestionario(
            AvaliacaoPreenchida avaliacaoPreenchida, CompetenciaQuestionario competenciaQuestionario) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT r FROM RespostaItemAvaliacao r WHERE r.avaliacaoPreenchida = :avaliacaoPreenchida AND r.competenciaQuestionario = :competenciaQuestionario";
            TypedQuery<RespostaItemAvaliacao> query = em.createQuery(jpql, RespostaItemAvaliacao.class);
            query.setParameter("avaliacaoPreenchida", avaliacaoPreenchida);
            query.setParameter("competenciaQuestionario", competenciaQuestionario);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar resposta específica: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public List<RespostaItemAvaliacao> findNaoAvaliadas() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT r FROM RespostaItemAvaliacao r WHERE r.naoAvaliado = true ORDER BY r.avaliacaoPreenchida.dataRealizacao DESC";
            TypedQuery<RespostaItemAvaliacao> query = em.createQuery(jpql, RespostaItemAvaliacao.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar respostas não avaliadas: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public long countByAvaliacaoPreenchida(AvaliacaoPreenchida avaliacaoPreenchida) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT COUNT(r) FROM RespostaItemAvaliacao r WHERE r.avaliacaoPreenchida = :avaliacaoPreenchida";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("avaliacaoPreenchida", avaliacaoPreenchida);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao contar respostas por avaliação: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public void deleteByAvaliacaoPreenchida(AvaliacaoPreenchida avaliacaoPreenchida) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            String jpql = "DELETE FROM RespostaItemAvaliacao r WHERE r.avaliacaoPreenchida = :avaliacaoPreenchida";
            em.createQuery(jpql)
                    .setParameter("avaliacaoPreenchida", avaliacaoPreenchida)
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao deletar respostas por avaliação: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}
