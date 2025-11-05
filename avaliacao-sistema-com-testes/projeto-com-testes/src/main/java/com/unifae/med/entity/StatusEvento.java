package com.unifae.med.entity;

/**
 * STATUS_EVENTO - ENUM PARA STATUS DE EVENTOS DA AGENDA
 * =====================================================
 * 
 * Define os status possíveis dos eventos agendados
 * no Sistema de Avaliação UNIFAE.
 * 
 * RESPONSABILIDADES:
 * - Controlar o ciclo de vida dos eventos
 * - Facilitar filtros por status na agenda
 * - Padronizar estados dos eventos
 * 
 * VALORES POSSÍVEIS:
 * - AGENDADO: Evento programado, aguardando realização
 * - EM_ANDAMENTO: Evento em execução no momento
 * - CONCLUIDO: Evento finalizado com sucesso
 * - CANCELADO: Evento cancelado por algum motivo
 * 
 * FLUXO TÍPICO:
 * AGENDADO → EM_ANDAMENTO → CONCLUIDO
 * AGENDADO → CANCELADO
 * 
 * MAPEAMENTO DE BANCO:
 * - Armazenado como STRING na coluna status_evento
 * - Valores correspondem aos nomes dos enums
 * 
 * RELACIONAMENTO COM OUTROS ARQUIVOS:
 * - EventoAgenda.java: Usa este enum no campo statusEvento
 * - EventoAgendaDAO.java: Usa para filtros por status
 * - Servlets: Usam para controle de fluxo e validação
 * - JSPs: Exibem status com cores e ícones apropriados
 * 
 * @author Sistema de Avaliação UNIFAE
 * @version 1.0
 */
public enum StatusEvento {
    
    /**
     * AGENDADO - Evento programado
     * ============================
     * Status inicial de todos os eventos.
     * Indica que o evento foi criado e está aguardando realização.
     * 
     * CARACTERÍSTICAS:
     * - Status padrão para novos eventos
     * - Permite edição completa do evento
     * - Pode ser alterado para EM_ANDAMENTO ou CANCELADO
     */
    AGENDADO("Agendado", "primary"),
    
    /**
     * EM_ANDAMENTO - Evento em execução
     * =================================
     * Indica que o evento está acontecendo no momento.
     * Usado para controle de presença e acompanhamento.
     * 
     * CARACTERÍSTICAS:
     * - Alterado manualmente ou automaticamente
     * - Permite algumas edições limitadas
     * - Deve ser alterado para CONCLUIDO ao final
     */
    EM_ANDAMENTO("Em Andamento", "warning"),
    
    /**
     * CONCLUIDO - Evento finalizado
     * =============================
     * Indica que o evento foi realizado com sucesso.
     * Status final para eventos que ocorreram normalmente.
     * 
     * CARACTERÍSTICAS:
     * - Status final positivo
     * - Edições muito limitadas ou bloqueadas
     * - Usado para relatórios e histórico
     */
    CONCLUIDO("Concluído", "success"),
    
    /**
     * CANCELADO - Evento cancelado
     * ============================
     * Indica que o evento foi cancelado e não será realizado.
     * Status final para eventos que não ocorreram.
     * 
     * CARACTERÍSTICAS:
     * - Status final negativo
     * - Requer justificativa do cancelamento
     * - Mantido para histórico e controle
     */
    CANCELADO("Cancelado", "danger");

    /**
     * DESCRIÇÃO AMIGÁVEL
     * ==================
     * Texto descritivo para exibição nas interfaces.
     * Usado em formulários, relatórios e listagens.
     */
    private final String descricao;

    /**
     * CLASSE CSS PARA ESTILIZAÇÃO
     * ===========================
     * Classe CSS Bootstrap para colorir badges e botões.
     * Facilita a identificação visual dos status.
     * 
     * CORES:
     * - primary: Azul (AGENDADO)
     * - warning: Amarelo (EM_ANDAMENTO)
     * - success: Verde (CONCLUIDO)
     * - danger: Vermelho (CANCELADO)
     */
    private final String cssClass;

    /**
     * CONSTRUTOR DO ENUM
     * ==================
     * Define a descrição e classe CSS para cada status.
     * 
     * @param descricao Texto descritivo do status
     * @param cssClass Classe CSS Bootstrap para estilização
     */
    StatusEvento(String descricao, String cssClass) {
        this.descricao = descricao;
        this.cssClass = cssClass;
    }

    /**
     * OBTER DESCRIÇÃO
     * ===============
     * Retorna a descrição amigável do status do evento.
     * Usado nas interfaces para exibição ao usuário.
     * 
     * @return Descrição amigável do status
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * OBTER CLASSE CSS
     * ================
     * Retorna a classe CSS Bootstrap para estilização.
     * Usado nas JSPs para colorir badges e indicadores.
     * 
     * @return Classe CSS Bootstrap
     */
    public String getCssClass() {
        return cssClass;
    }

    /**
     * VERIFICAR SE É STATUS FINAL
     * ===========================
     * Verifica se o status é final (não pode mais ser alterado).
     * Usado para controle de edição de eventos.
     * 
     * @return true se for status final (CONCLUIDO ou CANCELADO)
     */
    public boolean isFinal() {
        return this == CONCLUIDO || this == CANCELADO;
    }

    /**
     * VERIFICAR SE PERMITE EDIÇÃO
     * ===========================
     * Verifica se o status permite edição do evento.
     * Usado para controlar formulários e operações.
     * 
     * @return true se permitir edição
     */
    public boolean permiteEdicao() {
        return this == AGENDADO || this == EM_ANDAMENTO;
    }

    /**
     * REPRESENTAÇÃO TEXTUAL
     * =====================
     * Retorna a descrição amigável como representação padrão.
     * Útil para logs e debug.
     */
    @Override
    public String toString() {
        return descricao;
    }
}

