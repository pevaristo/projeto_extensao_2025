package com.unifae.med.entity;

/**
 * TIPO_EVENTO - ENUM PARA TIPOS DE EVENTOS DA AGENDA
 * ==================================================
 * 
 * Define os tipos possíveis de eventos que podem ser agendados
 * no Sistema de Avaliação UNIFAE.
 * 
 * RESPONSABILIDADES:
 * - Categorizar eventos por tipo/finalidade
 * - Facilitar filtros e buscas na agenda
 * - Padronizar nomenclatura dos tipos de evento
 * 
 * VALORES POSSÍVEIS:
 * - AULA: Aulas regulares das disciplinas
 * - PROVA: Avaliações, exames e provas
 * - SEMINARIO: Apresentações e seminários
 * - AVALIACAO: Avaliações práticas (Mini-CEX, 360°)
 * - REUNIAO: Reuniões administrativas e acadêmicas
 * - EVENTO: Eventos gerais, palestras, workshops
 * 
 * MAPEAMENTO DE BANCO:
 * - Armazenado como STRING na coluna tipo_evento
 * - Valores correspondem aos nomes dos enums
 * 
 * RELACIONAMENTO COM OUTROS ARQUIVOS:
 * - EventoAgenda.java: Usa este enum no campo tipoEvento
 * - EventoAgendaDAO.java: Usa para filtros por tipo
 * - Servlets: Usam para validação e processamento
 * - JSPs: Exibem descrições amigáveis dos tipos
 * 
 * @author Sistema de Avaliação UNIFAE
 * @version 1.0
 */
public enum TipoEvento {
    
    /**
     * AULA - Aulas regulares
     * ======================
     * Representa aulas normais das disciplinas.
     * Tipo mais comum de evento na agenda.
     */
    AULA("Aula"),
    
    /**
     * PROVA - Avaliações e exames
     * ===========================
     * Representa provas, exames e avaliações escritas.
     * Eventos importantes que requerem preparação especial.
     */
    PROVA("Prova"),
    
    /**
     * SEMINARIO - Apresentações
     * =========================
     * Representa seminários, apresentações de trabalhos,
     * defesas e eventos similares.
     */
    SEMINARIO("Seminário"),
    
    /**
     * AVALIACAO - Avaliações práticas
     * ===============================
     * Representa avaliações práticas como Mini-CEX,
     * avaliações 360° e outras avaliações formativas.
     */
    AVALIACAO("Avaliação"),
    
    /**
     * REUNIAO - Reuniões
     * ==================
     * Representa reuniões administrativas, acadêmicas,
     * de coordenação e similares.
     */
    REUNIAO("Reunião"),
    
    /**
     * EVENTO - Eventos gerais
     * =======================
     * Representa eventos gerais como palestras, workshops,
     * congressos, simpósios e outros eventos especiais.
     */
    EVENTO("Evento");

    /**
     * DESCRIÇÃO AMIGÁVEL
     * ==================
     * Texto descritivo para exibição nas interfaces.
     * Usado em formulários, relatórios e listagens.
     */
    private final String descricao;

    /**
     * CONSTRUTOR DO ENUM
     * ==================
     * Define a descrição amigável para cada tipo.
     * 
     * @param descricao Texto descritivo do tipo
     */
    TipoEvento(String descricao) {
        this.descricao = descricao;
    }

    /**
     * OBTER DESCRIÇÃO
     * ===============
     * Retorna a descrição amigável do tipo de evento.
     * Usado nas interfaces para exibição ao usuário.
     * 
     * @return Descrição amigável do tipo
     */
    public String getDescricao() {
        return descricao;
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

