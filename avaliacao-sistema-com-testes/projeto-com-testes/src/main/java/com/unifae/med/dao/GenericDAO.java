package com.unifae.med.dao;

import com.unifae.med.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * GENERICDAO - CLASSE BASE PARA ACESSO A DADOS
 * =============================================
 * 
 * Implementa o padrão DAO (Data Access Object) genérico com operações CRUD básicas.
 * Serve como classe pai para todos os DAOs específicos do sistema, evitando
 * duplicação de código e garantindo consistência nas operações de banco.
 * 
 * PADRÕES IMPLEMENTADOS:
 * - DAO (Data Access Object): Encapsula acesso a dados
 * - Template Method: Define estrutura comum para operações CRUD
 * - Generic Programming: Funciona com qualquer tipo de entidade
 * - Repository Pattern: Abstrai persistência de dados
 * 
 * RESPONSABILIDADES:
 * - Operações CRUD básicas (Create, Read, Update, Delete)
 * - Gerenciamento de transações
 * - Tratamento de exceções de persistência
 * - Controle de ciclo de vida do EntityManager
 * 
 * RELACIONAMENTO COM OUTROS ARQUIVOS:
 * - JPAUtil.java: Obtém EntityManagers para operações
 * - Entidades (entity/*.java): Tipos gerenciados pelos DAOs filhos
 * - DAOs específicos: Herdam desta classe (UsuarioDAO, QuestionarioDAO, etc.)
 * - Servlets: Usam DAOs para operações de negócio
 * 
 * HIERARQUIA DE HERANÇA:
 * GenericDAO (esta classe)
 *   ├── UsuarioDAO
 *   ├── QuestionarioDAO
 *   ├── AvaliacaoPreenchidaDAO
 *   └── outros DAOs específicos
 * 
 * @param <T> Tipo da entidade gerenciada (ex: Usuario, Questionario)
 * @param <ID> Tipo da chave primária (ex: Long, Integer, String)
 * 
 * @author Sistema de Avaliação UNIFAE
 * @version 1.0
 */
public abstract class GenericDAO<T, ID> {

    /**
     * CLASSE DA ENTIDADE GERENCIADA
     * ==============================
     * Armazena o tipo da entidade para operações reflexivas.
     * Necessário para criar queries JPQL dinâmicas e operações find().
     * Definido no construtor pelas classes filhas.
     */
    private final Class<T> entityClass;

    /**
     * CONSTRUTOR PROTEGIDO
     * ====================
     * Recebe a classe da entidade que será gerenciada.
     * Protegido para garantir que apenas classes filhas possam instanciar.
     * 
     * @param entityClass Classe da entidade JPA a ser gerenciada
     */
    protected GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * SALVAR ENTIDADE (CREATE/UPDATE)
     * ================================
     * Persiste uma entidade no banco de dados usando merge().
     * Funciona tanto para inserção (entidade nova) quanto atualização (existente).
     * 
     * COMPORTAMENTO:
     * - Se entidade não tem ID: INSERT (nova entidade)
     * - Se entidade tem ID: UPDATE (entidade existente)
     * - Usa transação para garantir consistência
     * - Retorna entidade gerenciada (attached)
     * 
     * TRATAMENTO DE TRANSAÇÃO:
     * 1. Inicia transação
     * 2. Executa merge()
     * 3. Commit se sucesso
     * 4. Rollback se erro
     * 5. Sempre fecha EntityManager
     * 
     * @param entity Entidade a ser salva
     * @return Entidade salva e gerenciada pelo JPA
     * @throws RuntimeException se erro na operação
     */
    public T save(T entity) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            
            // merge() funciona para INSERT e UPDATE
            // Retorna entidade gerenciada (attached ao contexto)
            T savedEntity = em.merge(entity);
            
            transaction.commit();
            return savedEntity;
        } catch (Exception e) {
            // Rollback em caso de erro
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Erro ao salvar entidade: " + e.getMessage(), e);
        } finally {
            // Sempre fecha EntityManager para liberar recursos
            em.close();
        }
    }

    /**
     * BUSCAR POR ID (READ)
     * ====================
     * Localiza uma entidade pela sua chave primária.
     * Retorna Optional para tratamento seguro de valores nulos.
     * 
     * VANTAGENS DO OPTIONAL:
     * - Evita NullPointerException
     * - Força tratamento explícito de "não encontrado"
     * - Código mais legível e seguro
     * 
     * EXEMPLO DE USO:
     * Optional<Usuario> usuario = usuarioDAO.findById(1L);
     * if (usuario.isPresent()) {
     *     // usar usuario.get()
     * } else {
     *     // tratar caso não encontrado
     * }
     * 
     * @param id Chave primária da entidade
     * @return Optional contendo entidade se encontrada
     * @throws RuntimeException se erro na operação
     */
    public Optional<T> findById(ID id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // find() retorna null se não encontrar
            T entity = em.find(entityClass, id);
            
            // Converte null em Optional vazio
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar entidade por ID: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * BUSCAR TODAS AS ENTIDADES (READ ALL)
     * =====================================
     * Retorna lista com todas as instâncias da entidade no banco.
     * Usa JPQL dinâmico baseado no nome da classe.
     * 
     * ATENÇÃO:
     * - Pode retornar muitos registros
     * - Considere paginação para tabelas grandes
     * - Útil para listas de seleção e relatórios pequenos
     * 
     * JPQL GERADO:
     * Para Usuario.class: "SELECT e FROM Usuario e"
     * Para Questionario.class: "SELECT e FROM Questionario e"
     * 
     * @return Lista com todas as entidades (pode ser vazia)
     * @throws RuntimeException se erro na operação
     */
    public List<T> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Constrói JPQL dinâmico usando nome da classe
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
            TypedQuery<T> query = em.createQuery(jpql, entityClass);
            
            // getResultList() nunca retorna null (lista vazia se nenhum resultado)
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar todas as entidades: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * DELETAR ENTIDADE (DELETE)
     * ==========================
     * Remove uma entidade do banco de dados.
     * Requer que entidade esteja gerenciada (attached).
     * 
     * PROCESSO:
     * 1. Merge para garantir que entidade está gerenciada
     * 2. Remove entidade gerenciada
     * 3. Commit da transação
     * 
     * IMPORTANTE:
     * - Entidade deve existir no banco
     * - Cuidado com restrições de integridade referencial
     * - Operação irreversível
     * 
     * @param entity Entidade a ser removida
     * @throws RuntimeException se erro na operação
     */
    public void delete(T entity) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            
            // merge() garante que entidade está gerenciada
            T managedEntity = em.merge(entity);
            
            // remove() só funciona com entidades gerenciadas
            em.remove(managedEntity);
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Erro ao deletar entidade: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * DELETAR POR ID (DELETE BY ID)
     * ==============================
     * Remove entidade localizando-a primeiro pelo ID.
     * Conveniência para quando só temos a chave primária.
     * 
     * PROCESSO:
     * 1. Busca entidade por ID
     * 2. Se encontrada, deleta
     * 3. Se não encontrada, lança exceção
     * 
     * @param id Chave primária da entidade a ser removida
     * @throws RuntimeException se entidade não encontrada ou erro na operação
     */
    public void deleteById(ID id) {
        Optional<T> entity = findById(id);
        if (entity.isPresent()) {
            delete(entity.get());
        } else {
            throw new RuntimeException("Entidade não encontrada para o ID: " + id);
        }
    }

    /**
     * CONTAR ENTIDADES (COUNT)
     * =========================
     * Retorna número total de registros da entidade no banco.
     * Útil para paginação e estatísticas.
     * 
     * JPQL GERADO:
     * Para Usuario.class: "SELECT COUNT(e) FROM Usuario e"
     * 
     * PERFORMANCE:
     * - Mais eficiente que findAll().size()
     * - Executa COUNT no banco (não traz dados)
     * 
     * @return Número total de entidades
     * @throws RuntimeException se erro na operação
     */
    public long count() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            
            // getSingleResult() para queries que retornam um único valor
            return query.getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao contar entidades: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * OBTER ENTITYMANAGER (MÉTODO PROTEGIDO)
     * =======================================
     * Permite que classes filhas obtenham EntityManager para operações customizadas.
     * Útil para queries específicas que não estão no GenericDAO.
     * 
     * RESPONSABILIDADE:
     * - Classes filhas devem fechar o EntityManager
     * - Usar em métodos específicos dos DAOs filhos
     * 
     * EXEMPLO DE USO EM DAO FILHO:
     * public List<Usuario> findByNome(String nome) {
     *     EntityManager em = getEntityManager();
     *     try {
     *         // query específica
     *     } finally {
     *         em.close();
     *     }
     * }
     * 
     * @return EntityManager novo para operações customizadas
     */
    protected EntityManager getEntityManager() {
        return JPAUtil.getEntityManager();
    }
}

