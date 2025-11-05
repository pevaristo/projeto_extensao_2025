package com.unifae.med.dao;

import com.unifae.med.entity.Nota;
import com.unifae.med.entity.Usuario;
import com.unifae.med.entity.Disciplina;
import com.unifae.med.entity.Turma;
import com.unifae.med.entity.TipoAvaliacao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * NOTA DAO - DATA ACCESS OBJECT PARA NOTAS (CORRIGIDO)
 * ====================================================
 * 
 * DAO responsável pelas operações de banco de dados relacionadas às notas.
 * Estende GenericDAO e adiciona métodos específicos para notas.
 * 
 * CORREÇÃO: Adicionado FETCH JOIN para resolver LazyInitializationException
 * 
 * FUNCIONALIDADES:
 * - CRUD básico de notas
 * - Busca por aluno, disciplina, turma
 * - Cálculo de médias
 * - Filtros por tipo de avaliação
 * - Relatórios e estatísticas
 * 
 * RELACIONAMENTO COM OUTROS ARQUIVOS:
 * - GenericDAO.java: Classe pai com operações básicas
 * - Nota.java: Entidade JPA manipulada
 * - NotaServlet.java: Controlador que usa este DAO
 * 
 * @author Sistema de Avaliação UNIFAE
 * @version 1.1 - Corrigido LazyInitializationException
 */

@ApplicationScoped
public class NotaDAO extends GenericDAO<Nota, Integer> {

    /**
     * CONSTRUTOR
     * ==========
     * Inicializa o DAO com a classe Nota.
     */
    public NotaDAO() {
        super(Nota.class);
    }

    // ========================================
    // MÉTODOS DE BUSCA POR RELACIONAMENTOS
    // ========================================

    /**
     * BUSCAR NOTAS POR ALUNO
     * ======================
     * Busca todas as notas de um aluno específico.
     * CORRIGIDO: Adicionado FETCH JOIN para carregar relacionamentos.
     */
    public List<Nota> findByAluno(Usuario aluno) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT n FROM Nota n " +
                         "LEFT JOIN FETCH n.aluno " +
                         "LEFT JOIN FETCH n.disciplina " +
                         "LEFT JOIN FETCH n.turma " +
                         "LEFT JOIN FETCH n.professor " +
                         "WHERE n.aluno = :aluno AND n.ativo = true " +
                         "ORDER BY n.dataAvaliacao DESC";
            TypedQuery<Nota> query = em.createQuery(jpql, Nota.class);
            query.setParameter("aluno", aluno);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar notas por aluno: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * BUSCAR NOTAS POR ALUNO E DISCIPLINA
     * ===================================
     * Busca notas de um aluno em uma disciplina específica.
     * CORRIGIDO: Adicionado FETCH JOIN para carregar relacionamentos.
     */
    public List<Nota> findByAlunoAndDisciplina(Usuario aluno, Disciplina disciplina) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT n FROM Nota n " +
                         "LEFT JOIN FETCH n.aluno " +
                         "LEFT JOIN FETCH n.disciplina " +
                         "LEFT JOIN FETCH n.turma " +
                         "LEFT JOIN FETCH n.professor " +
                         "WHERE n.aluno = :aluno AND n.disciplina = :disciplina AND n.ativo = true " +
                         "ORDER BY n.dataAvaliacao DESC";
            TypedQuery<Nota> query = em.createQuery(jpql, Nota.class);
            query.setParameter("aluno", aluno);
            query.setParameter("disciplina", disciplina);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar notas por aluno e disciplina: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * BUSCAR NOTAS POR DISCIPLINA
     * ===========================
     * Busca todas as notas de uma disciplina específica.
     * CORRIGIDO: Adicionado FETCH JOIN para carregar relacionamentos.
     */
    public List<Nota> findByDisciplina(Disciplina disciplina) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT n FROM Nota n " +
                         "LEFT JOIN FETCH n.aluno " +
                         "LEFT JOIN FETCH n.disciplina " +
                         "LEFT JOIN FETCH n.turma " +
                         "LEFT JOIN FETCH n.professor " +
                         "WHERE n.disciplina = :disciplina AND n.ativo = true " +
                         "ORDER BY n.aluno.nomeCompleto, n.dataAvaliacao DESC";
            TypedQuery<Nota> query = em.createQuery(jpql, Nota.class);
            query.setParameter("disciplina", disciplina);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar notas por disciplina: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * BUSCAR NOTAS POR TURMA
     * ======================
     * Busca todas as notas de uma turma específica.
     * CORRIGIDO: Adicionado FETCH JOIN para carregar relacionamentos.
     */
    public List<Nota> findByTurma(Turma turma) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT n FROM Nota n " +
                         "LEFT JOIN FETCH n.aluno " +
                         "LEFT JOIN FETCH n.disciplina " +
                         "LEFT JOIN FETCH n.turma " +
                         "LEFT JOIN FETCH n.professor " +
                         "WHERE n.turma = :turma AND n.ativo = true " +
                         "ORDER BY n.disciplina.nomeDisciplina, n.aluno.nomeCompleto";
            TypedQuery<Nota> query = em.createQuery(jpql, Nota.class);
            query.setParameter("turma", turma);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar notas por turma: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * BUSCAR NOTAS POR PROFESSOR
     * ==========================
     * Busca todas as notas lançadas por um professor.
     * CORRIGIDO: Adicionado FETCH JOIN para carregar relacionamentos.
     */
    public List<Nota> findByProfessor(Usuario professor) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT n FROM Nota n " +
                         "LEFT JOIN FETCH n.aluno " +
                         "LEFT JOIN FETCH n.disciplina " +
                         "LEFT JOIN FETCH n.turma " +
                         "LEFT JOIN FETCH n.professor " +
                         "WHERE n.professor = :professor AND n.ativo = true " +
                         "ORDER BY n.dataLancamento DESC";
            TypedQuery<Nota> query = em.createQuery(jpql, Nota.class);
            query.setParameter("professor", professor);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar notas por professor: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    // ========================================
    // MÉTODOS DE BUSCA POR TIPO E PERÍODO
    // ========================================

    /**
     * BUSCAR NOTAS POR TIPO DE AVALIAÇÃO
     * ==================================
     * Busca notas por tipo de avaliação.
     * CORRIGIDO: Adicionado FETCH JOIN para carregar relacionamentos.
     */
    public List<Nota> findByTipoAvaliacao(TipoAvaliacao tipoAvaliacao) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT n FROM Nota n " +
                         "LEFT JOIN FETCH n.aluno " +
                         "LEFT JOIN FETCH n.disciplina " +
                         "LEFT JOIN FETCH n.turma " +
                         "LEFT JOIN FETCH n.professor " +
                         "WHERE n.tipoAvaliacao = :tipoAvaliacao AND n.ativo = true " +
                         "ORDER BY n.dataAvaliacao DESC";
            TypedQuery<Nota> query = em.createQuery(jpql, Nota.class);
            query.setParameter("tipoAvaliacao", tipoAvaliacao);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar notas por tipo de avaliação: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * BUSCAR NOTAS POR PERÍODO
     * ========================
     * Busca notas em um período específico.
     * CORRIGIDO: Adicionado FETCH JOIN para carregar relacionamentos.
     */
    public List<Nota> findByPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT n FROM Nota n " +
                         "LEFT JOIN FETCH n.aluno " +
                         "LEFT JOIN FETCH n.disciplina " +
                         "LEFT JOIN FETCH n.turma " +
                         "LEFT JOIN FETCH n.professor " +
                         "WHERE n.dataAvaliacao BETWEEN :dataInicio AND :dataFim AND n.ativo = true " +
                         "ORDER BY n.dataAvaliacao DESC";
            TypedQuery<Nota> query = em.createQuery(jpql, Nota.class);
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar notas por período: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    // ========================================
    // MÉTODOS DE CÁLCULO DE MÉDIAS
    // ========================================

    /**
     * CALCULAR MÉDIA DO ALUNO NA DISCIPLINA
     * =====================================
     * Calcula a média ponderada de um aluno em uma disciplina.
     */
    public BigDecimal calcularMediaAlunoDisciplina(Usuario aluno, Disciplina disciplina) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT SUM(n.valorNota * n.pesoNota) / SUM(n.pesoNota) FROM Nota n " +
                         "WHERE n.aluno = :aluno AND n.disciplina = :disciplina AND n.ativo = true";
            TypedQuery<BigDecimal> query = em.createQuery(jpql, BigDecimal.class);
            query.setParameter("aluno", aluno);
            query.setParameter("disciplina", disciplina);
            
            BigDecimal media = query.getSingleResult();
            return media != null ? media : BigDecimal.ZERO;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular média do aluno na disciplina: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * CALCULAR MÉDIA GERAL DO ALUNO
     * =============================
     * Calcula a média geral de um aluno em todas as disciplinas.
     */
    public BigDecimal calcularMediaGeralAluno(Usuario aluno) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT AVG(n.valorNota) FROM Nota n WHERE n.aluno = :aluno AND n.ativo = true";
            TypedQuery<BigDecimal> query = em.createQuery(jpql, BigDecimal.class);
            query.setParameter("aluno", aluno);
            
            BigDecimal media = query.getSingleResult();
            return media != null ? media : BigDecimal.ZERO;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular média geral do aluno: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * CALCULAR MÉDIA DA TURMA NA DISCIPLINA
     * =====================================
     * Calcula a média da turma em uma disciplina específica.
     */
    public BigDecimal calcularMediaTurmaDisciplina(Turma turma, Disciplina disciplina) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT AVG(n.valorNota) FROM Nota n " +
                         "WHERE n.turma = :turma AND n.disciplina = :disciplina AND n.ativo = true";
            TypedQuery<BigDecimal> query = em.createQuery(jpql, BigDecimal.class);
            query.setParameter("turma", turma);
            query.setParameter("disciplina", disciplina);
            
            BigDecimal media = query.getSingleResult();
            return media != null ? media : BigDecimal.ZERO;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular média da turma na disciplina: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    // ========================================
    // MÉTODOS DE ESTATÍSTICAS
    // ========================================

    /**
     * CONTAR NOTAS POR ALUNO
     * ======================
     * Conta o número de notas de um aluno.
     */
    public Long contarNotasAluno(Usuario aluno) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT COUNT(n) FROM Nota n WHERE n.aluno = :aluno AND n.ativo = true";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("aluno", aluno);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao contar notas do aluno: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * CONTAR ALUNOS APROVADOS NA DISCIPLINA
     * =====================================
     * Conta quantos alunos estão aprovados (média >= 6.0) em uma disciplina.
     */
    public Long contarAlunosAprovadosDisciplina(Disciplina disciplina) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT COUNT(DISTINCT n.aluno) FROM Nota n " +
                         "WHERE n.disciplina = :disciplina AND n.ativo = true " +
                         "GROUP BY n.aluno " +
                         "HAVING AVG(n.valorNota) >= 6.0";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("disciplina", disciplina);
            
            List<Long> resultados = query.getResultList();
            return (long) resultados.size();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao contar alunos aprovados na disciplina: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * BUSCAR NOTAS ATIVAS (MÉTODO PRINCIPAL CORRIGIDO)
     * ================================================
     * Busca apenas notas ativas (não excluídas).
     * CORREÇÃO PRINCIPAL: Adicionado FETCH JOIN para resolver LazyInitializationException.
     */
    public List<Nota> findAtivas() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT n FROM Nota n " +
                         "LEFT JOIN FETCH n.aluno " +
                         "LEFT JOIN FETCH n.disciplina " +
                         "LEFT JOIN FETCH n.turma " +
                         "LEFT JOIN FETCH n.professor " +
                         "WHERE n.ativo = true " +
                         "ORDER BY n.dataLancamento DESC";
            TypedQuery<Nota> query = em.createQuery(jpql, Nota.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar notas ativas: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * BUSCAR TODAS AS NOTAS (SOBRESCRITO PARA INCLUIR FETCH JOIN)
     * ===========================================================
     * Sobrescreve o método findAll() do GenericDAO para incluir FETCH JOIN.
     */
    @Override
    public List<Nota> findAll() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT n FROM Nota n " +
                         "LEFT JOIN FETCH n.aluno " +
                         "LEFT JOIN FETCH n.disciplina " +
                         "LEFT JOIN FETCH n.turma " +
                         "LEFT JOIN FETCH n.professor " +
                         "ORDER BY n.dataLancamento DESC";
            TypedQuery<Nota> query = em.createQuery(jpql, Nota.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar todas as notas: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * BUSCAR POR ID (SOBRESCRITO PARA INCLUIR FETCH JOIN)
     * ===================================================
     * Sobrescreve o método findById() do GenericDAO para incluir FETCH JOIN.
     */
    @Override
    public Optional<Nota> findById(Integer id) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT n FROM Nota n " +
                         "LEFT JOIN FETCH n.aluno " +
                         "LEFT JOIN FETCH n.disciplina " +
                         "LEFT JOIN FETCH n.turma " +
                         "LEFT JOIN FETCH n.professor " +
                         "WHERE n.idNota = :id";
            TypedQuery<Nota> query = em.createQuery(jpql, Nota.class);
            query.setParameter("id", id);
            
            try {
                return Optional.of(query.getSingleResult());
            } catch (NoResultException e) {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar nota por ID: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * VERIFICAR EXISTÊNCIA DE NOTA
     * ============================
     * Verifica se já existe uma nota para aluno, disciplina e tipo específicos.
     */
    public boolean existeNota(Usuario aluno, Disciplina disciplina, TipoAvaliacao tipoAvaliacao, LocalDate dataAvaliacao) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT COUNT(n) FROM Nota n WHERE n.aluno = :aluno AND n.disciplina = :disciplina " +
                         "AND n.tipoAvaliacao = :tipoAvaliacao AND n.dataAvaliacao = :dataAvaliacao AND n.ativo = true";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("aluno", aluno);
            query.setParameter("disciplina", disciplina);
            query.setParameter("tipoAvaliacao", tipoAvaliacao);
            query.setParameter("dataAvaliacao", dataAvaliacao);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar existência de nota: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}
