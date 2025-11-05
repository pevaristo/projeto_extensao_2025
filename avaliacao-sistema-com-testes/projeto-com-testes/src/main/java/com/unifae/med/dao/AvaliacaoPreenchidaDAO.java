package com.unifae.med.dao;

import com.unifae.med.entity.AvaliacaoPreenchida;
import com.unifae.med.entity.Usuario;
import com.unifae.med.entity.Questionario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AvaliacaoPreenchidaDAO extends GenericDAO<AvaliacaoPreenchida, Integer> {

    public AvaliacaoPreenchidaDAO() {
        super(AvaliacaoPreenchida.class);
    }

    /**
     * Sobrescreve findAll() para usar eager loading e evitar
     * LazyInitializationException
     */
    @Override
    public List<AvaliacaoPreenchida> findAll() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT DISTINCT a FROM AvaliacaoPreenchida a "
                    + "LEFT JOIN FETCH a.questionario "
                    + "LEFT JOIN FETCH a.alunoAvaliado "
                    + "LEFT JOIN FETCH a.avaliador "
                    + "LEFT JOIN FETCH a.respostasItens "
                    + "ORDER BY a.idAvaliacaoPreenchida DESC";
            TypedQuery<AvaliacaoPreenchida> query = em.createQuery(jpql, AvaliacaoPreenchida.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar todas as avaliações: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * Sobrescreve findById() para usar eager loading
     */
    @Override
    public Optional<AvaliacaoPreenchida> findById(Integer id) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT a FROM AvaliacaoPreenchida a "
                    + "LEFT JOIN FETCH a.questionario "
                    + "LEFT JOIN FETCH a.alunoAvaliado "
                    + "LEFT JOIN FETCH a.avaliador "
                    + "LEFT JOIN FETCH a.respostasItens "
                    + "WHERE a.idAvaliacaoPreenchida = :id";
            TypedQuery<AvaliacaoPreenchida> query = em.createQuery(jpql, AvaliacaoPreenchida.class);
            query.setParameter("id", id);
            List<AvaliacaoPreenchida> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar avaliação por ID: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public List<AvaliacaoPreenchida> findByAlunoAvaliado(Usuario alunoAvaliado) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT DISTINCT a FROM AvaliacaoPreenchida a "
                    + "LEFT JOIN FETCH a.questionario "
                    + "LEFT JOIN FETCH a.alunoAvaliado "
                    + "LEFT JOIN FETCH a.avaliador "
                    + "WHERE a.alunoAvaliado = :alunoAvaliado "
                    + "ORDER BY a.idAvaliacaoPreenchida DESC";
            TypedQuery<AvaliacaoPreenchida> query = em.createQuery(jpql, AvaliacaoPreenchida.class);
            query.setParameter("alunoAvaliado", alunoAvaliado);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar avaliações por aluno avaliado: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public List<AvaliacaoPreenchida> findByAvaliador(Usuario avaliador) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT DISTINCT a FROM AvaliacaoPreenchida a "
                    + "LEFT JOIN FETCH a.questionario "
                    + "LEFT JOIN FETCH a.alunoAvaliado "
                    + "LEFT JOIN FETCH a.avaliador "
                    + "WHERE a.avaliador = :avaliador "
                    + "ORDER BY a.idAvaliacaoPreenchida DESC";
            TypedQuery<AvaliacaoPreenchida> query = em.createQuery(jpql, AvaliacaoPreenchida.class);
            query.setParameter("avaliador", avaliador);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar avaliações por avaliador: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public List<AvaliacaoPreenchida> findByQuestionario(Questionario questionario) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT DISTINCT a FROM AvaliacaoPreenchida a "
                    + "LEFT JOIN FETCH a.questionario "
                    + "LEFT JOIN FETCH a.alunoAvaliado "
                    + "LEFT JOIN FETCH a.avaliador "
                    + "WHERE a.questionario = :questionario "
                    + "ORDER BY a.idAvaliacaoPreenchida DESC";
            TypedQuery<AvaliacaoPreenchida> query = em.createQuery(jpql, AvaliacaoPreenchida.class);
            query.setParameter("questionario", questionario);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar avaliações por questionário: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public List<AvaliacaoPreenchida> findByDataRealizacao(LocalDate dataInicio, LocalDate dataFim) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT DISTINCT a FROM AvaliacaoPreenchida a "
                    + "LEFT JOIN FETCH a.questionario "
                    + "LEFT JOIN FETCH a.alunoAvaliado "
                    + "LEFT JOIN FETCH a.avaliador "
                    + "WHERE a.dataRealizacao BETWEEN :dataInicio AND :dataFim "
                    + "ORDER BY a.idAvaliacaoPreenchida DESC";
            TypedQuery<AvaliacaoPreenchida> query = em.createQuery(jpql, AvaliacaoPreenchida.class);
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar avaliações por período: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public List<AvaliacaoPreenchida> findByAlunoAvaliadoAndQuestionario(Usuario alunoAvaliado, Questionario questionario) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT DISTINCT a FROM AvaliacaoPreenchida a "
                    + "LEFT JOIN FETCH a.questionario "
                    + "LEFT JOIN FETCH a.alunoAvaliado "
                    + "LEFT JOIN FETCH a.avaliador "
                    + "WHERE a.alunoAvaliado = :alunoAvaliado AND a.questionario = :questionario "
                    + "ORDER BY a.idAvaliacaoPreenchida DESC";
            TypedQuery<AvaliacaoPreenchida> query = em.createQuery(jpql, AvaliacaoPreenchida.class);
            query.setParameter("alunoAvaliado", alunoAvaliado);
            query.setParameter("questionario", questionario);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar avaliações por aluno e questionário: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public List<AvaliacaoPreenchida> findByTipoAvaliadorNaoUsuario(String tipoAvaliadorNaoUsuario) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT DISTINCT a FROM AvaliacaoPreenchida a "
                    + "LEFT JOIN FETCH a.questionario "
                    + "LEFT JOIN FETCH a.alunoAvaliado "
                    + "LEFT JOIN FETCH a.avaliador "
                    + "WHERE a.tipoAvaliadorNaoUsuario = :tipoAvaliadorNaoUsuario "
                    + "ORDER BY a.idAvaliacaoPreenchida DESC";
            TypedQuery<AvaliacaoPreenchida> query = em.createQuery(jpql, AvaliacaoPreenchida.class);
            query.setParameter("tipoAvaliadorNaoUsuario", tipoAvaliadorNaoUsuario);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar avaliações por tipo de avaliador não usuário: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public long countByAlunoAvaliado(Usuario alunoAvaliado) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT COUNT(a) FROM AvaliacaoPreenchida a WHERE a.alunoAvaliado = :alunoAvaliado";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("alunoAvaliado", alunoAvaliado);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao contar avaliações por aluno: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}
