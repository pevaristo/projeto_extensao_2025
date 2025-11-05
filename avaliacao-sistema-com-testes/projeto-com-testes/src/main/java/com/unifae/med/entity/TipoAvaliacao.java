package com.unifae.med.entity;

/**
 * TIPO AVALIAÇÃO - ENUM PARA TIPOS DE AVALIAÇÃO
 * =============================================
 * 
 * Enum que define os tipos de avaliação disponíveis no sistema de notas.
 * Complementa o sistema de avaliações formativas existente.
 * 
 * RESPONSABILIDADES:
 * - Definir tipos padronizados de avaliação
 * - Facilitar categorização de notas
 * - Permitir filtros e relatórios por tipo
 * - Manter consistência no sistema
 * 
 * TIPOS DISPONÍVEIS:
 * - PROVA: Avaliações escritas formais
 * - TRABALHO: Trabalhos e projetos
 * - SEMINARIO: Apresentações e seminários
 * - PRATICA: Atividades práticas e laboratoriais
 * - PARTICIPACAO: Participação em aula
 * - PROJETO: Projetos de maior duração
 * - ESTAGIO: Avaliações de estágio
 * - OUTROS: Outros tipos não categorizados
 * 
 * RELACIONAMENTO COM OUTROS ARQUIVOS:
 * - Nota.java: Entidade que usa este enum
 * - NotaServlet.java: Controlador que manipula tipos
 * - notas/form.jsp: Formulário que exibe opções
 * 
 * @author Sistema de Avaliação UNIFAE
 * @version 1.0
 */
public enum TipoAvaliacao {
    
    /**
     * PROVA
     * =====
     * Avaliações escritas formais, exames.
     */
    PROVA("Prova", "Avaliações escritas formais e exames"),
    
    /**
     * TRABALHO
     * ========
     * Trabalhos individuais ou em grupo.
     */
    TRABALHO("Trabalho", "Trabalhos individuais ou em grupo"),
    
    /**
     * SEMINÁRIO
     * =========
     * Apresentações orais e seminários.
     */
    SEMINARIO("Seminário", "Apresentações orais e seminários"),
    
    /**
     * PRÁTICA
     * =======
     * Atividades práticas e laboratoriais.
     */
    PRATICA("Prática", "Atividades práticas e laboratoriais"),
    
    /**
     * PARTICIPAÇÃO
     * ============
     * Participação em aula e atividades.
     */
    PARTICIPACAO("Participação", "Participação em aula e atividades"),
    
    /**
     * PROJETO
     * =======
     * Projetos de maior duração e complexidade.
     */
    PROJETO("Projeto", "Projetos de maior duração e complexidade"),
    
    /**
     * ESTÁGIO
     * =======
     * Avaliações de estágio supervisionado.
     */
    ESTAGIO("Estágio", "Avaliações de estágio supervisionado"),
    
    /**
     * OUTROS
     * ======
     * Outros tipos de avaliação não categorizados.
     */
    OUTROS("Outros", "Outros tipos de avaliação não categorizados");

    // ========================================
    // ATRIBUTOS DO ENUM
    // ========================================

    /**
     * NOME AMIGÁVEL
     * =============
     * Nome legível para exibição na interface.
     */
    private final String nomeAmigavel;

    /**
     * DESCRIÇÃO
     * =========
     * Descrição detalhada do tipo de avaliação.
     */
    private final String descricao;

    // ========================================
    // CONSTRUTOR
    // ========================================

    /**
     * CONSTRUTOR DO ENUM
     * ==================
     * Define nome amigável e descrição para cada tipo.
     */
    TipoAvaliacao(String nomeAmigavel, String descricao) {
        this.nomeAmigavel = nomeAmigavel;
        this.descricao = descricao;
    }

    // ========================================
    // GETTERS
    // ========================================

    /**
     * OBTER NOME AMIGÁVEL
     * ===================
     * Retorna o nome legível do tipo de avaliação.
     */
    public String getNomeAmigavel() {
        return nomeAmigavel;
    }

    /**
     * OBTER DESCRIÇÃO
     * ===============
     * Retorna a descrição detalhada do tipo.
     */
    public String getDescricao() {
        return descricao;
    }

    // ========================================
    // MÉTODOS AUXILIARES
    // ========================================

    /**
     * BUSCAR POR NOME
     * ===============
     * Busca um tipo de avaliação pelo nome amigável.
     * 
     * @param nomeAmigavel Nome amigável a buscar
     * @return TipoAvaliacao encontrado ou null
     */
    public static TipoAvaliacao buscarPorNome(String nomeAmigavel) {
        if (nomeAmigavel == null) {
            return null;
        }
        
        for (TipoAvaliacao tipo : values()) {
            if (tipo.getNomeAmigavel().equalsIgnoreCase(nomeAmigavel)) {
                return tipo;
            }
        }
        return null;
    }

    /**
     * VERIFICAR SE É AVALIAÇÃO FORMAL
     * ===============================
     * Verifica se o tipo representa uma avaliação formal (prova, trabalho, projeto).
     */
    public boolean isAvaliacaoFormal() {
        return this == PROVA || this == TRABALHO || this == PROJETO || this == ESTAGIO;
    }

    /**
     * VERIFICAR SE É AVALIAÇÃO CONTÍNUA
     * =================================
     * Verifica se o tipo representa avaliação contínua (participação, prática).
     */
    public boolean isAvaliacaoContinua() {
        return this == PARTICIPACAO || this == PRATICA || this == SEMINARIO;
    }

    /**
     * OBTER PESO SUGERIDO
     * ===================
     * Retorna um peso sugerido baseado no tipo de avaliação.
     */
    public double getPesoSugerido() {
        switch (this) {
            case PROVA:
                return 3.0; // Provas têm peso maior
            case TRABALHO:
            case PROJETO:
                return 2.0; // Trabalhos e projetos têm peso médio-alto
            case SEMINARIO:
            case ESTAGIO:
                return 2.0; // Seminários e estágios têm peso médio-alto
            case PRATICA:
                return 1.5; // Práticas têm peso médio
            case PARTICIPACAO:
                return 1.0; // Participação tem peso normal
            case OUTROS:
            default:
                return 1.0; // Outros têm peso normal
        }
    }

    /**
     * REPRESENTAÇÃO TEXTUAL
     * =====================
     * Retorna o nome amigável como representação textual.
     */
    @Override
    public String toString() {
        return nomeAmigavel;
    }
}
