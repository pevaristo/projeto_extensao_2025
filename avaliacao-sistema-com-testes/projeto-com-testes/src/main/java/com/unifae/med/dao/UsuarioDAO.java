package com.unifae.med.dao;

import com.unifae.med.entity.Usuario;
import com.unifae.med.entity.TipoUsuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioDAO extends GenericDAO<Usuario, Integer> {

    public UsuarioDAO() {
        super(Usuario.class);
    }

    public Optional<Usuario> findByEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT u FROM Usuario u WHERE u.email = :email";
            TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
            query.setParameter("email", email);
            Usuario usuario = query.getSingleResult();
            return Optional.of(usuario);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usuário por email: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public Optional<Usuario> findByMatriculaRA(String matriculaRA) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT u FROM Usuario u WHERE u.matriculaRA = :matriculaRA";
            TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
            query.setParameter("matriculaRA", matriculaRA);
            Usuario usuario = query.getSingleResult();
            return Optional.of(usuario);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usuário por matrícula/RA: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public List<Usuario> findByTipoUsuario(TipoUsuario tipoUsuario) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT u FROM Usuario u WHERE u.tipoUsuario = :tipoUsuario AND u.ativo = true";
            TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
            query.setParameter("tipoUsuario", tipoUsuario);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usuários por tipo: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public List<Usuario> findAtivos() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT u FROM Usuario u WHERE u.ativo = true ORDER BY u.nomeCompleto";
            TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usuários ativos: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public List<Usuario> findByNomeContaining(String nome) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT u FROM Usuario u WHERE LOWER(u.nomeCompleto) LIKE LOWER(:nome) AND u.ativo = true ORDER BY u.nomeCompleto";
            TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
            query.setParameter("nome", "%" + nome + "%");
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usuários por nome: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public boolean existsByEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT COUNT(u) FROM Usuario u WHERE u.email = :email";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("email", email);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar existência de email: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public boolean existsByMatriculaRA(String matriculaRA) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT COUNT(u) FROM Usuario u WHERE u.matriculaRA = :matriculaRA";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("matriculaRA", matriculaRA);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar existência de matrícula/RA: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Usuario> findAll() {
        EntityManager em = getEntityManager();
        try {
            // Usando JOIN FETCH para carregar a permissão e evitar N+1 queries na listagem
            return em.createQuery("SELECT u FROM Usuario u LEFT JOIN FETCH u.permissao ORDER BY u.nomeCompleto", Usuario.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Retorna uma lista de todos os usuários ativos que são professores. 
     * Essencial para o formulário de vínculo Disciplina-Turma.     *
     * @return Lista de Usuários que são professores.
     */
    public List<Usuario> findProfessoresAtivos() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT u FROM Usuario u WHERE u.tipoUsuario = :tipo AND u.ativo = true ORDER BY u.nomeCompleto";
            TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
            query.setParameter("tipo", TipoUsuario.PROFESSOR);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
