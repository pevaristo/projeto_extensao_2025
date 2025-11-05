package com.unifae.med.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

/**
 * Classe base para testes de integração com JPA/Hibernate.
 *
 * Fornece infraestrutura comum para todos os testes de integração: -
 * Configuração do EntityManagerFactory com H2 - Criação e limpeza de
 * EntityManager - Gerenciamento de transações - Integração com JPAUtil para que
 * DAOs usem o mesmo contexto de persistência
 *
 * Classes de teste devem estender esta classe para herdar a configuração.
 *
 * CORREÇÕES IMPLEMENTADAS (v1.3): - Injeção do EntityManagerFactory nos DAOs
 * via JPAUtil.setEntityManagerFactory() - Limpeza do banco movida para
 * @AfterEach (após cada teste) - Método clearDatabase() simplificado usando
 * JPQL (sem unwrap) - Melhor tratamento de transações e conexões - Limpeza de
 * tabelas na ordem correta (respeitando foreign keys) - Uso de DELETE com JPQL
 * ao invés de SQL nativo
 */
public abstract class BaseIntegrationTest {

    protected static EntityManagerFactory emf;
    protected EntityManager em;

    /**
     * Configuração executada uma vez antes de todos os testes da classe.
     *
     * IMPORTANTE: Injeta o EntityManagerFactory no JPAUtil para que os DAOs
     * usem o mesmo contexto de persistência dos testes.
     */
    @BeforeAll
    public static void setUpClass() {
        // Cria EntityManagerFactory para testes (H2 em memória)
        emf = Persistence.createEntityManagerFactory("unifae-med-pu");

        // CORREÇÃO: Injeta o EMF no JPAUtil para que DAOs usem o mesmo contexto
        JPAUtil.setEntityManagerFactory(emf);
    }

    /**
     * Limpeza executada uma vez após todos os testes da classe.
     */
    @AfterAll
    public static void tearDownClass() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    /**
     * Configuração executada antes de cada teste. Cria um novo EntityManager
     * para o teste.
     */
    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
    }

    /**
     * Limpeza executada após cada teste.
     *
     * CORREÇÃO: A limpeza do banco foi movida para cá (após o teste). Isso
     * evita conflitos com a criação inicial do schema e garante que cada teste
     * comece com um banco limpo.
     */
    @AfterEach
    void tearDown() {
        // Limpa o banco de dados após cada teste
        clearDatabase();

        // Fecha o EntityManager
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    /**
     * Limpa todas as tabelas do banco de dados de forma robusta.
     *
     * CORREÇÃO v1.3: Este método agora: - Usa JPQL ao invés de SQL nativo
     * (evita problemas com unwrap) - Usa um EntityManager separado (evita
     * conflitos com transações do teste) - Limpa entidades na ORDEM CORRETA
     * (respeitando foreign keys) - Garante que o EntityManager local seja
     * SEMPRE fechado - Não lança exceção em caso de erro (apenas loga aviso)
     */
    protected void clearDatabase() {
        // Só tenta limpar se o EntityManagerFactory está aberto
        if (emf == null || !emf.isOpen()) {
            return;
        }

        EntityManager localEm = null;
        try {
            // Usa um EntityManager separado para evitar conflitos com transações do teste
            localEm = emf.createEntityManager();
            localEm.getTransaction().begin();

            // CORREÇÃO v1.3: Limpa entidades usando JPQL na ordem correta
            // Ordem: entidades dependentes primeiro, depois as independentes
            // Nível 1: Entidades mais dependentes (referenciam outras)
            deleteAllFromEntity(localEm, "RespostaItemAvaliacao");
            deleteAllFromEntity(localEm, "AvaliacaoPreenchida");
            deleteAllFromEntity(localEm, "CompetenciaQuestionario");
            deleteAllFromEntity(localEm, "Nota");
            deleteAllFromEntity(localEm, "DisciplinaTurma");
            deleteAllFromEntity(localEm, "EventoAgenda");

            // Nível 2: Entidades intermediárias
            deleteAllFromEntity(localEm, "Questionario");
            deleteAllFromEntity(localEm, "Turma");
            deleteAllFromEntity(localEm, "Disciplina");
            deleteAllFromEntity(localEm, "LocalEvento");

            // Nível 3: Entidades que referenciam Permissao
            deleteAllFromEntity(localEm, "Usuario");

            // Nível 4: Entidades mais independentes
            deleteAllFromEntity(localEm, "Permissao");

            localEm.getTransaction().commit();
        } catch (RuntimeException e) {
            // Em caso de erro, faz rollback
            if (localEm != null && localEm.getTransaction().isActive()) {
                try {
                    localEm.getTransaction().rollback();
                } catch (Exception rollbackEx) {
                    System.err.println("Erro ao fazer rollback: " + rollbackEx.getMessage());
                }
            }

            // Log do erro, mas não lança exceção (permite que próximo teste execute)
            System.err.println("Aviso: Falha ao limpar o banco de dados: " + e.getMessage());
            // Não imprime stack trace completo para não poluir o log
        } finally {
            // CORREÇÃO: Garante que o EntityManager local seja SEMPRE fechado
            if (localEm != null && localEm.isOpen()) {
                try {
                    localEm.close();
                } catch (Exception closeEx) {
                    System.err.println("Erro ao fechar EntityManager local: " + closeEx.getMessage());
                }
            }
        }
    }

    /**
     * Deleta todas as entidades de um tipo específico usando JPQL.
     *
     * @param em EntityManager a ser usado
     * @param entityName Nome da entidade (classe) a ser limpa
     */
    private void deleteAllFromEntity(EntityManager em, String entityName) {
        try {
            String jpql = "DELETE FROM " + entityName;
            em.createQuery(jpql).executeUpdate();
        } catch (Exception e) {
            // Ignora erros (entidade pode não existir ou não ter dados)
            // Não loga para evitar poluir o console
        }
    }

    /**
     * Executa uma operação dentro de uma transação. Útil para testes que
     * precisam persistir dados.
     *
     * @param operation Operação a ser executada dentro da transação
     */
    protected void executeInTransaction(Runnable operation) {
        em.getTransaction().begin();
        try {
            operation.run();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    /**
     * Força a sincronização do contexto de persistência com o banco e limpa o
     * cache de primeiro nível.
     */
    protected void flushAndClear() {
        if (em != null && em.isOpen()) {
            em.flush();
            em.clear();
        }
    }
}
