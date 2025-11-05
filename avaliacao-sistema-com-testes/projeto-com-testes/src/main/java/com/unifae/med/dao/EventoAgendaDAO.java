package com.unifae.med.dao;

import com.unifae.med.entity.EventoAgenda;
import com.unifae.med.entity.TipoEvento;
import com.unifae.med.entity.StatusEvento;
import com.unifae.med.entity.Usuario;
import com.unifae.med.entity.Disciplina;
import com.unifae.med.entity.Turma;
import com.unifae.med.entity.LocalEvento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * EVENTO_AGENDA_DAO - DAO (Data Access Object) PARA EVENTOS DA AGENDA
 * ==================================================================== Esta
 * classe é responsável por toda a comunicação entre a aplicação e o banco de
 * dados referente à entidade 'EventoAgenda'. Ela abstrai a complexidade das
 * consultas SQL/JPQL, oferecendo métodos Java simples para buscar, salvar e
 * manipular eventos.
 *
 * Conceito Chave: DAO (Data Access Object)
 * --------------------------------------- É um padrão de projeto que separa a
 * lógica de negócio da lógica de persistência de dados. Isso torna o código
 * mais organizado, testável e fácil de manter.
 */

@ApplicationScoped
public class EventoAgendaDAO extends GenericDAO<EventoAgenda, Integer> {

    /**
     * CONSTRUTOR ========== Inicializa o DAO informando à classe pai
     * (GenericDAO) que esta classe irá gerenciar a entidade `EventoAgenda`.
     */
    public EventoAgendaDAO() {
        super(EventoAgenda.class);
    }

    /**
     * BUSCAR EVENTOS POR PERÍODO ========================== Busca todos os
     * eventos cuja data de início esteja dentro de um intervalo específico.
     * Este método é fundamental para as visualizações de calendário
     * (mês/semana).
     *
     * Ponto Didático: LazyInitializationException e a solução com JOIN FETCH
     * -------------------------------------------------------------------- As
     * entidades relacionadas (LocalEvento, Disciplina, etc.) são configuradas
     * como LAZY (carregamento preguiçoso). Isso significa que o Hibernate não
     * as carrega do banco de dados na consulta inicial para economizar
     * recursos. O problema: quando a página JSP tenta acessar um dado de uma
     * entidade relacionada (ex: evento.localEvento.nome), a conexão com o banco
     * (EntityManager) já foi fechada por este método (no bloco `finally`). Isso
     * causa a `LazyInitializationException`.
     *
     * A solução: Usamos `LEFT JOIN FETCH`. Esta instrução diz ao Hibernate para
     * buscar e carregar ("fetch") os dados das entidades relacionadas na mesma
     * consulta inicial, enquanto a conexão ainda está aberta. Usamos `LEFT
     * JOIN` para garantir que, se um evento não tiver um local ou disciplina,
     * ele ainda seja retornado.
     *
     * @param dataInicio A data e hora de início do intervalo da busca.
     * @param dataFim A data e hora de fim do intervalo da busca.
     * @return Uma lista de eventos encontrados no período, já com suas
     * entidades relacionadas carregadas.
     */
    public List<EventoAgenda> findByPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        EntityManager em = getEntityManager();
        try {
            // Consulta JPQL (Java Persistence Query Language) para buscar os eventos.
            // A cláusula "LEFT JOIN FETCH" é a chave para evitar a LazyInitializationException.
            String jpql = """
            SELECT e FROM EventoAgenda e 
            LEFT JOIN FETCH e.localEvento
            LEFT JOIN FETCH e.disciplina
            LEFT JOIN FETCH e.turma
            LEFT JOIN FETCH e.responsavel
            WHERE FUNCTION('DATE', e.dataInicio) BETWEEN :dataInicioDia AND :dataFimDia
            ORDER BY e.dataInicio ASC
            """;
            TypedQuery<EventoAgenda> query = em.createQuery(jpql, EventoAgenda.class);

            // Define os parâmetros da consulta. Usamos toLocalDate() para comparar apenas a data,
            // ignorando a hora, conforme a lógica da cláusula WHERE.
            query.setParameter("dataInicioDia", dataInicio.toLocalDate());
            query.setParameter("dataFimDia", dataFim.toLocalDate());

            return query.getResultList();
        } catch (Exception e) {
            // Em caso de erro, lança uma exceção mais genérica para a camada de serviço/servlet tratar.
            throw new RuntimeException("Erro ao buscar eventos por período: " + e.getMessage(), e);
        } finally {
            // Bloco 'finally' garante que o EntityManager (conexão com o banco) seja sempre fechado,
            // mesmo que ocorra um erro, evitando vazamento de recursos.
            em.close();
        }
    }

    /**
     * BUSCAR EVENTOS POR DATA ======================= Método de conveniência
     * que utiliza o `findByPeriodo` para buscar eventos de um único dia. Define
     * o intervalo como do início (00:00:00) ao fim (23:59:59) do dia informado.
     *
     * @param data A data específica para a busca.
     * @return Uma lista de eventos que ocorrem na data especificada.
     */
    public List<EventoAgenda> findByData(LocalDate data) {
        LocalDateTime inicioData = data.atStartOfDay();
        LocalDateTime fimData = data.atTime(23, 59, 59);
        return findByPeriodo(inicioData, fimData);
    }

    /**
     * BUSCAR EVENTOS POR TIPO ======================= Retorna todos os eventos
     * que correspondem a um tipo específico (ex: AULA, PROVA).
     *
     * @param tipoEvento O tipo de evento a ser filtrado.
     * @return Uma lista de eventos do tipo especificado.
     */
    public List<EventoAgenda> findByTipoEvento(TipoEvento tipoEvento) {
        EntityManager em = getEntityManager();
        try {
            String jpql = """
                SELECT e FROM EventoAgenda e 
                WHERE e.tipoEvento = :tipoEvento 
                ORDER BY e.dataInicio ASC
                """;
            TypedQuery<EventoAgenda> query = em.createQuery(jpql, EventoAgenda.class);
            query.setParameter("tipoEvento", tipoEvento);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar eventos por tipo: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * BUSCAR EVENTOS POR STATUS ========================= Retorna todos os
     * eventos que correspondem a um status específico (ex: AGENDADO,
     * CONCLUIDO).
     *
     * @param statusEvento O status a ser filtrado.
     * @return Uma lista de eventos com o status especificado.
     */
    public List<EventoAgenda> findByStatusEvento(StatusEvento statusEvento) {
        EntityManager em = getEntityManager();
        try {
            String jpql = """
                SELECT e FROM EventoAgenda e 
                WHERE e.statusEvento = :statusEvento 
                ORDER BY e.dataInicio ASC
                """;
            TypedQuery<EventoAgenda> query = em.createQuery(jpql, EventoAgenda.class);
            query.setParameter("statusEvento", statusEvento);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar eventos por status: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * BUSCAR EVENTOS POR RESPONSÁVEL ============================== Retorna
     * todos os eventos associados a um usuário responsável.
     *
     * @param responsavel O objeto do tipo Usuario que é o responsável.
     * @return Uma lista de eventos do responsável.
     */
    public List<EventoAgenda> findByResponsavel(Usuario responsavel) {
        EntityManager em = getEntityManager();
        try {
            String jpql = """
                SELECT e FROM EventoAgenda e 
                WHERE e.responsavel = :responsavel 
                ORDER BY e.dataInicio ASC
                """;
            TypedQuery<EventoAgenda> query = em.createQuery(jpql, EventoAgenda.class);
            query.setParameter("responsavel", responsavel);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar eventos por responsável: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * BUSCAR EVENTOS POR DISCIPLINA ============================= Retorna todos
     * os eventos associados a uma disciplina específica.
     *
     * @param disciplina O objeto do tipo Disciplina.
     * @return Uma lista de eventos da disciplina.
     */
    public List<EventoAgenda> findByDisciplina(Disciplina disciplina) {
        EntityManager em = getEntityManager();
        try {
            String jpql = """
                SELECT e FROM EventoAgenda e 
                WHERE e.disciplina = :disciplina 
                ORDER BY e.dataInicio ASC
                """;
            TypedQuery<EventoAgenda> query = em.createQuery(jpql, EventoAgenda.class);
            query.setParameter("disciplina", disciplina);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar eventos por disciplina: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * BUSCAR EVENTOS POR TURMA ======================== Retorna todos os
     * eventos associados a uma turma específica.
     *
     * @param turma O objeto do tipo Turma.
     * @return Uma lista de eventos da turma.
     */
    public List<EventoAgenda> findByTurma(Turma turma) {
        EntityManager em = getEntityManager();
        try {
            String jpql = """
                SELECT e FROM EventoAgenda e 
                WHERE e.turma = :turma 
                ORDER BY e.dataInicio ASC
                """;
            TypedQuery<EventoAgenda> query = em.createQuery(jpql, EventoAgenda.class);
            query.setParameter("turma", turma);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar eventos por turma: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * BUSCAR EVENTOS POR LOCAL ======================== Retorna todos os
     * eventos que ocorrem em um local específico.
     *
     * @param localEvento O objeto do tipo LocalEvento.
     * @return Uma lista de eventos no local.
     */
    public List<EventoAgenda> findByLocal(LocalEvento localEvento) {
        EntityManager em = getEntityManager();
        try {
            String jpql = """
                SELECT e FROM EventoAgenda e 
                WHERE e.localEvento = :localEvento 
                ORDER BY e.dataInicio ASC
                """;
            TypedQuery<EventoAgenda> query = em.createQuery(jpql, EventoAgenda.class);
            query.setParameter("localEvento", localEvento);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar eventos por local: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * BUSCAR EVENTOS COM FILTROS MÚLTIPLOS ====================================
     * Realiza uma busca dinâmica, construindo a consulta JPQL com base nos
     * filtros que foram fornecidos (não nulos). Essencial para a tela de
     * listagem de eventos.
     *
     * Ponto Didático: Construção Dinâmica de Query
     * --------------------------------------------- Usamos um `StringBuilder`
     * para montar a string da consulta passo a passo. Começamos com uma
     * consulta base (`WHERE 1=1`) que é sempre verdadeira, facilitando a adição
     * das próximas cláusulas com `AND` sem nos preocuparmos se é a primeira ou
     * não. Para cada filtro que não for nulo, adicionamos a condição
     * correspondente na query.
     *
     * Também aplicamos a correção com `LEFT JOIN FETCH` aqui, para garantir que
     * os dados relacionados sejam carregados e evitar a
     * `LazyInitializationException` na página de lista. O `DISTINCT` é usado
     * para evitar duplicatas que podem surgir devido aos JOINs.
     *
     * @param dataInicio Filtro opcional por data de início.
     * @param dataFim Filtro opcional por data de fim.
     * @param tipoEvento Filtro opcional por tipo de evento.
     * @param statusEvento Filtro opcional por status.
     * @param responsavel Filtro opcional por responsável.
     * @param disciplina Filtro opcional por disciplina.
     * @param turma Filtro opcional por turma.
     * @return Uma lista de eventos que correspondem a todos os filtros
     * fornecidos.
     */
    public List<EventoAgenda> findWithFilters(LocalDateTime dataInicio, LocalDateTime dataFim,
            TipoEvento tipoEvento, StatusEvento statusEvento,
            Usuario responsavel, Disciplina disciplina, Turma turma) {
        EntityManager em = getEntityManager();
        try {
            // Inicia a construção da query com JOIN FETCH para carregar os dados relacionados
            StringBuilder jpql = new StringBuilder("""
                SELECT DISTINCT e FROM EventoAgenda e 
                LEFT JOIN FETCH e.localEvento
                LEFT JOIN FETCH e.disciplina
                LEFT JOIN FETCH e.turma
                LEFT JOIN FETCH e.responsavel
                WHERE 1=1 
                """);

            // Adiciona as condições à query dinamicamente
            if (dataInicio != null) {
                jpql.append(" AND e.dataInicio >= :dataInicio");
            }
            if (dataFim != null) {
                jpql.append(" AND e.dataInicio <= :dataFim");
            }
            if (tipoEvento != null) {
                jpql.append(" AND e.tipoEvento = :tipoEvento");
            }
            if (statusEvento != null) {
                jpql.append(" AND e.statusEvento = :statusEvento");
            }
            if (responsavel != null) {
                jpql.append(" AND e.responsavel = :responsavel");
            }
            if (disciplina != null) {
                jpql.append(" AND e.disciplina = :disciplina");
            }
            if (turma != null) {
                jpql.append(" AND e.turma = :turma");
            }

            jpql.append(" ORDER BY e.dataInicio ASC");

            TypedQuery<EventoAgenda> query = em.createQuery(jpql.toString(), EventoAgenda.class);

            // Define os parâmetros apenas para os filtros que foram fornecidos
            if (dataInicio != null) {
                query.setParameter("dataInicio", dataInicio);
            }
            if (dataFim != null) {
                query.setParameter("dataFim", dataFim);
            }
            if (tipoEvento != null) {
                query.setParameter("tipoEvento", tipoEvento);
            }
            if (statusEvento != null) {
                query.setParameter("statusEvento", statusEvento);
            }
            if (responsavel != null) {
                query.setParameter("responsavel", responsavel);
            }
            if (disciplina != null) {
                query.setParameter("disciplina", disciplina);
            }
            if (turma != null) {
                query.setParameter("turma", turma);
            }

            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar eventos com filtros: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * VERIFICAR CONFLITO DE HORÁRIO ============================= Verifica se
     * já existe outro evento (que não seja o próprio evento sendo editado)
     * agendado para o mesmo local e com sobreposição de horário.
     *
     * @param localEvento O local onde se deseja agendar o evento.
     * @param dataInicio A data e hora de início do novo evento.
     * @param dataFim A data e hora de fim do novo evento.
     * @param idEvento O ID do evento que está sendo editado (null se for um
     * novo evento).
     * @return `true` se houver conflito, `false` caso contrário.
     */
    public boolean hasConflito(LocalEvento localEvento, LocalDateTime dataInicio,
            LocalDateTime dataFim, Integer idEvento) {
        EntityManager em = getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("""
                SELECT COUNT(e) FROM EventoAgenda e 
                WHERE e.localEvento = :localEvento 
                AND e.statusEvento != :statusCancelado
                AND (
                    (e.dataInicio <= :dataInicio AND e.dataFim > :dataInicio) OR
                    (e.dataInicio < :dataFim AND e.dataFim >= :dataFim) OR
                    (e.dataInicio >= :dataInicio AND e.dataFim <= :dataFim)
                )
                """);

            // Se estivermos editando um evento, devemos excluí-lo da verificação de conflito.
            if (idEvento != null) {
                jpql.append(" AND e.idEvento != :idEvento");
            }

            TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);
            query.setParameter("localEvento", localEvento);
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);
            query.setParameter("statusCancelado", StatusEvento.CANCELADO);

            if (idEvento != null) {
                query.setParameter("idEvento", idEvento);
            }

            // Se a contagem de eventos conflitantes for maior que 0, retorna true.
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar conflito de horário: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * BUSCAR PRÓXIMOS EVENTOS ======================= Retorna uma lista
     * limitada dos próximos eventos que ainda não ocorreram. Útil para painéis
     * (dashboards) ou seções de "próximos eventos".
     *
     * @param limite O número máximo de eventos a serem retornados.
     * @return Uma lista dos próximos eventos agendados.
     */
    public List<EventoAgenda> findProximosEventos(int limite) {
        EntityManager em = getEntityManager();
        try {
            String jpql = """
                SELECT e FROM EventoAgenda e 
                WHERE e.dataInicio >= :agora 
                AND e.statusEvento = :statusAgendado
                ORDER BY e.dataInicio ASC
                """;
            TypedQuery<EventoAgenda> query = em.createQuery(jpql, EventoAgenda.class);
            query.setParameter("agora", LocalDateTime.now());
            query.setParameter("statusAgendado", StatusEvento.AGENDADO);
            query.setMaxResults(limite);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar próximos eventos: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * BUSCAR EVENTOS EM ANDAMENTO =========================== Retorna todos os
     * eventos que estão atualmente com o status "EM_ANDAMENTO".
     *
     * @return Uma lista de eventos em andamento.
     */
    public List<EventoAgenda> findEventosEmAndamento() {
        EntityManager em = getEntityManager();
        try {
            String jpql = """
                SELECT e FROM EventoAgenda e 
                WHERE e.statusEvento = :statusEmAndamento
                ORDER BY e.dataInicio ASC
                """;
            TypedQuery<EventoAgenda> query = em.createQuery(jpql, EventoAgenda.class);
            query.setParameter("statusEmAndamento", StatusEvento.EM_ANDAMENTO);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar eventos em andamento: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
        /**
     * BUSCAR EVENTO POR ID COM RELACIONAMENTOS CARREGADOS
     * ====================================================
     * Sobrescreve o método findById genérico para garantir que todas as entidades
     * relacionadas (lazy-loaded) sejam carregadas na mesma consulta, evitando
     * a LazyInitializationException na tela de edição.
     *
     * @param id O ID do evento a ser buscado.
     * @return Um Optional contendo o EventoAgenda com todos os seus relacionamentos
     *         (local, disciplina, turma, responsavel) já inicializados.
     */
    @Override
    public Optional<EventoAgenda> findById(Integer id) {
        EntityManager em = getEntityManager();
        try {
            // Consulta JPQL que usa LEFT JOIN FETCH para carregar todos os dados necessários de uma só vez.
            String jpql = """
                SELECT e FROM EventoAgenda e
                LEFT JOIN FETCH e.localEvento
                LEFT JOIN FETCH e.disciplina
                LEFT JOIN FETCH e.turma
                LEFT JOIN FETCH e.responsavel
                WHERE e.idEvento = :id
            """;
            TypedQuery<EventoAgenda> query = em.createQuery(jpql, EventoAgenda.class);
            query.setParameter("id", id);

            // Usa getSingleResult, mas dentro de um bloco try-catch para NoResultException,
            // que é o comportamento esperado quando o ID não existe, retornando um Optional vazio.
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty(); // Retorna um Optional vazio se nenhum evento for encontrado
        } catch (Exception e) {
            // Lança uma exceção genérica para outros tipos de erro de banco de dados.
            throw new RuntimeException("Erro ao buscar evento por ID com relacionamentos: " + e.getMessage(), e);
        } finally {
            // Garante que o EntityManager seja sempre fechado.
            em.close();
        }
    }
}
