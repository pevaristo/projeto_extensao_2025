package com.unifae.med.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * JPAUTIL - CLASSE UTILITÁRIA PARA GERENCIAMENTO JPA
 * ===================================================
 * 
 * Esta classe implementa o padrão Singleton para gerenciar o EntityManagerFactory
 * e fornecer EntityManagers para acesso ao banco de dados em todo o sistema.
 * 
 * RESPONSABILIDADES:
 * - Inicializar EntityManagerFactory uma única vez (Singleton)
 * - Fornecer EntityManagers para DAOs e Servlets
 * - Gerenciar ciclo de vida da conexão JPA
 * - Tratar erros de inicialização
 * - Suportar injeção de EntityManagerFactory para testes
 * 
 * PADRÕES IMPLEMENTADOS:
 * - Singleton: Uma única instância de EntityManagerFactory
 * - Factory: Cria EntityManagers sob demanda
 * - Resource Management: Controla abertura/fechamento de recursos
 * - Dependency Injection: Permite injeção de EMF para testes
 * 
 * RELACIONAMENTO COM OUTROS ARQUIVOS:
 * - persistence.xml: Configuração lida na inicialização
 * - DAOs (dao/*.java): Usam getEntityManager() para acesso a dados
 * - Servlets (servlet/*.java): Podem usar para transações manuais
 * - Entidades (entity/*.java): Gerenciadas pelos EntityManagers criados aqui
 * - BaseIntegrationTest.java: Injeta EMF de testes via setEntityManagerFactory()
 * 
 * FLUXO DE USO TÍPICO:
 * 1. Aplicação inicia → static block cria EntityManagerFactory
 * 2. DAO precisa de dados → chama JPAUtil.getEntityManager()
 * 3. DAO executa operações → fecha EntityManager
 * 4. Aplicação termina → chama closeEntityManagerFactory()
 * 
 * FLUXO DE USO EM TESTES:
 * 1. Teste inicia → cria EntityManagerFactory de testes
 * 2. Teste chama JPAUtil.setEntityManagerFactory(emfDeTeste)
 * 3. DAOs usam o EMF de testes automaticamente
 * 4. Teste termina → fecha EntityManagerFactory
 * 
 * @author Sistema de Avaliação UNIFAE
 * @version 1.1 (com suporte a testes)
 */
public class JPAUtil {
    
    /**
     * NOME DA UNIDADE DE PERSISTÊNCIA
     * ================================
     * Deve corresponder ao nome definido em persistence.xml.
     * Usado para localizar a configuração JPA correta.
     */
    private static final String PERSISTENCE_UNIT_NAME = "unifae-med-pu";
    
    /**
     * FACTORY DE ENTITYMANAGERS (SINGLETON)
     * ======================================
     * Instância única compartilhada por toda aplicação.
     * Criação é custosa, por isso mantemos uma única instância.
     * Thread-safe por design do JPA.
     * 
     * IMPORTANTE: Pode ser substituído por setEntityManagerFactory() em testes.
     */
    private static EntityManagerFactory entityManagerFactory;
    
    /**
     * BLOCO DE INICIALIZAÇÃO ESTÁTICA
     * ================================
     * Executado uma única vez quando a classe é carregada pela JVM.
     * Tenta criar EntityManagerFactory, mas não falha se houver erro
     * (permite que testes injetem seu próprio EMF).
     * 
     * TRATAMENTO DE ERROS:
     * - Captura exceções de inicialização
     * - Registra erro no console para debug
     * - Permite que aplicação continue (EMF pode ser injetado depois)
     */
    static {
        try {
            // Cria factory usando configuração do persistence.xml
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        } catch (Exception e) {
            // Log do erro para facilitar debug
            System.err.println("AVISO: Não foi possível criar EntityManagerFactory na inicialização: " + e.getMessage());
            System.err.println("Isso é normal em ambiente de testes. O EMF será injetado pelos testes.");
            // NÃO lança exceção - permite que testes injetem seu próprio EMF
        }
    }
    
    /**
     * OBTER ENTITYMANAGER
     * ===================
     * Método principal usado pelos DAOs para obter acesso ao banco.
     * 
     * IMPORTANTE:
     * - Cada EntityManager deve ser fechado após uso
     * - Não é thread-safe - cada thread deve ter seu próprio EM
     * - Representa uma sessão de trabalho com o banco
     * 
     * EXEMPLO DE USO:
     * EntityManager em = JPAUtil.getEntityManager();
     * try {
     *     em.getTransaction().begin();
     *     // operações com banco
     *     em.getTransaction().commit();
     * } finally {
     *     em.close();
     * }
     * 
     * @return EntityManager novo para operações de banco
     * @throws IllegalStateException se EntityManagerFactory não foi inicializado
     */
    public static EntityManager getEntityManager() {
        // Validação de segurança
        if (entityManagerFactory == null) {
            throw new IllegalStateException(
                "EntityManagerFactory não foi inicializado. " +
                "Em testes, use JPAUtil.setEntityManagerFactory() antes de chamar getEntityManager()."
            );
        }
        
        // Cria novo EntityManager para esta sessão
        return entityManagerFactory.createEntityManager();
    }
    
    /**
     * CONFIGURAR ENTITYMANAGERFACTORY (PARA TESTES)
     * ==============================================
     * Permite que testes injetem seu próprio EntityManagerFactory.
     * Isso garante que DAOs e testes usem o mesmo contexto de persistência.
     * 
     * QUANDO USAR:
     * - Em @BeforeAll de classes de teste de integração
     * - Antes de executar qualquer operação de DAO em testes
     * 
     * EXEMPLO DE USO EM TESTES:
     * @BeforeAll
     * public static void setUpClass() {
     *     emf = Persistence.createEntityManagerFactory("unifae-med-pu");
     *     JPAUtil.setEntityManagerFactory(emf);
     * }
     * 
     * IMPORTANTE:
     * - Fecha o EMF anterior se existir
     * - Não deve ser usado em produção
     * - Deve ser chamado antes de qualquer getEntityManager()
     * 
     * @param emf EntityManagerFactory a ser usado (geralmente de testes)
     */
    public static void setEntityManagerFactory(EntityManagerFactory emf) {
        // Fecha o EMF anterior se existir (evita vazamento de recursos)
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
        
        // Define o novo EMF
        entityManagerFactory = emf;
    }
    
    /**
     * FECHAR ENTITYMANAGERFACTORY
     * ============================
     * Método para limpeza de recursos quando aplicação termina.
     * 
     * QUANDO USAR:
     * - Shutdown da aplicação
     * - Testes unitários (cleanup em @AfterAll)
     * - Reconfiguração do sistema
     * 
     * EFEITOS:
     * - Fecha todas as conexões de banco
     * - Libera recursos do pool de conexões
     * - Invalida todos os EntityManagers criados
     */
    public static void closeEntityManagerFactory() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
    
    /**
     * VERIFICAR STATUS DO ENTITYMANAGERFACTORY
     * =========================================
     * Método utilitário para verificar se o factory ainda está ativo.
     * 
     * USOS:
     * - Diagnóstico de problemas
     * - Validação em testes
     * - Monitoramento de saúde da aplicação
     * 
     * @return true se EntityManagerFactory está aberto e funcional
     */
    public static boolean isEntityManagerFactoryOpen() {
        return entityManagerFactory != null && entityManagerFactory.isOpen();
    }
}