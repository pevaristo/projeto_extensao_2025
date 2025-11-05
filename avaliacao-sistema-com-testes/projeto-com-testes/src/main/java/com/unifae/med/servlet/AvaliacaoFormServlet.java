/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `AvaliacaoFormServlet`, é um Controller altamente especializado dentro da
 * arquitetura MVC, responsável por toda a lógica de renderização e processamento dos
 * complexos formulários de avaliação. Mapeado para `/avaliacao/form`, ele atua como
 * um orquestrador central para uma das funcionalidades mais críticas do sistema.
 *
 * Suas principais responsabilidades e complexidades são:
 * 1.  **Renderização Dinâmica de Formulários:** O método `doGet`, com a ajuda de
 * `determinarJSP`, seleciona e encaminha a requisição para um arquivo JSP
 * específico (ex: `minicex-form.jsp`, `avaliacao360-professor-form.jsp`) com base
 * no ID do `Questionario` escolhido pelo usuário.
 *
 * 2.  **Agregação de Dados para a View:** Antes de exibir um formulário, o `doGet`
 * utiliza múltiplos DAOs para carregar todas as informações necessárias para
 * popular os campos, como listas de alunos, professores, locais e os próprios
 * modelos de questionário.
 *
 * 3.  **Processamento de Submissões Complexas:** O método `doPost` executa uma
 * transação de múltiplos passos: salva o "cabeçalho" da avaliação, itera sobre as
 * competências esperadas, utiliza um mapeamento (`obterMapeamentoParametros`) para
 * ler corretamente as respostas de diferentes formulários e salva cada resposta
 * individual no banco de dados.
 *
 * 4.  **Gerenciamento de Modo de Edição:** O servlet distingue entre as ações "new" e
 * "edit", carregando previamente uma avaliação e suas respostas existentes quando
 * o usuário está editando um registro.
 * =================================================================================================
 */
package com.unifae.med.servlet;

import com.unifae.med.dao.*;
import com.unifae.med.entity.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/avaliacao/form")
public class AvaliacaoFormServlet extends HttpServlet {

    // Conjunto completo de DAOs para orquestrar a criação/edição de avaliações.
    private AvaliacaoPreenchidaDAO avaliacaoDAO;
    private QuestionarioDAO questionarioDAO;
    private UsuarioDAO usuarioDAO;
    private CompetenciaQuestionarioDAO competenciaDAO;
    private RespostaItemAvaliacaoDAO respostaDAO;
    private LocalEventoDAO localEventoDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.avaliacaoDAO = new AvaliacaoPreenchidaDAO();
        this.questionarioDAO = new QuestionarioDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.competenciaDAO = new CompetenciaQuestionarioDAO();
        this.respostaDAO = new RespostaItemAvaliacaoDAO();
        this.localEventoDAO = new LocalEventoDAO();
    }

    /**
     * Trata requisições GET para exibir o formulário de avaliação, seja para
     * uma nova avaliação ou para editar uma existente.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String questionarioIdStr = request.getParameter("questionarioId");
        String avaliacaoIdStr = request.getParameter("id");

        try {
            // 1. Carrega dados comuns para preencher selects (dropdowns) no formulário.
            prepareFormData(request);
            request.setAttribute("action", action);

            // 2. Se for uma edição, carrega os dados da avaliação e suas respostas.
            if ("edit".equals(action) && avaliacaoIdStr != null) {
                Integer avaliacaoId = Integer.parseInt(avaliacaoIdStr);
                AvaliacaoPreenchida avaliacao = avaliacaoDAO.findById(avaliacaoId)
                        .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

                // O ID do questionário é determinado pela avaliação que está sendo editada.
                questionarioIdStr = avaliacao.getQuestionario().getIdQuestionario().toString();

                List<RespostaItemAvaliacao> respostas = respostaDAO.findByAvaliacaoPreenchida(avaliacao);

                request.setAttribute("avaliacao", avaliacao);
                request.setAttribute("respostas", respostas);
            }

            // 3. Carrega o modelo de questionário selecionado.
            if (questionarioIdStr != null) {
                Integer questionarioId = Integer.parseInt(questionarioIdStr);
                request.setAttribute("questionarioIdSelecionado", questionarioId);
                Questionario questionario = questionarioDAO.findById(questionarioId)
                        .orElseThrow(() -> new RuntimeException("Questionário não encontrado"));
                request.setAttribute("questionario", questionario);
            }

            // 4. Determina qual arquivo JSP deve ser renderizado e encaminha a requisição.
            String jspPath = determinarJSP(questionarioIdStr);
            request.getRequestDispatcher(jspPath).forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao carregar formulário: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    /**
     * Trata requisições POST, processando a submissão de um formulário de
     * avaliação preenchido.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1. Coleta e processa os dados do cabeçalho da avaliação.
            String action = request.getParameter("action");
            String questionarioIdStr = request.getParameter("questionarioId");
            String avaliacaoIdStr = request.getParameter("avaliacaoId");
            String alunoAvaliadoIdStr = request.getParameter("alunoAvaliadoId");
            String avaliadorIdStr = request.getParameter("avaliadorId");
            String tipoAvaliadorNaoUsuario = request.getParameter("tipoAvaliadorNaoUsuario");
            String nomeAvaliadorNaoUsuario = request.getParameter("nomeAvaliadorNaoUsuario");
            String dataRealizacaoStr = request.getParameter("dataRealizacao");
            String horarioInicioStr = request.getParameter("horarioInicio");
            String horarioFimStr = request.getParameter("horarioFim");
            String localRealizacao = request.getParameter("localRealizacao");
            String feedbackPositivo = request.getParameter("feedbackPositivo");
            String feedbackMelhoria = request.getParameter("feedbackMelhoria");
            String contratoAprendizagem = request.getParameter("contratoAprendizagem");

            // 2. Busca as entidades "pai" necessárias.
            Questionario questionario = questionarioDAO.findById(Integer.parseInt(questionarioIdStr))
                    .orElseThrow(() -> new RuntimeException("Questionário não encontrado"));
            Usuario alunoAvaliado = usuarioDAO.findById(Integer.parseInt(alunoAvaliadoIdStr))
                    .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
            Usuario avaliador = null;
            if (avaliadorIdStr != null && !avaliadorIdStr.isEmpty() && !avaliadorIdStr.equals("0")) {
                avaliador = usuarioDAO.findById(Integer.parseInt(avaliadorIdStr)).orElse(null);
            }

            // 3. Cria uma nova avaliação ou busca a existente para edição.
            AvaliacaoPreenchida avaliacao = ("edit".equals(action))
                    ? avaliacaoDAO.findById(Integer.parseInt(avaliacaoIdStr)).orElseThrow(() -> new RuntimeException("Avaliação não encontrada"))
                    : new AvaliacaoPreenchida();

            // 4. Popula a entidade AvaliacaoPreenchida.
            avaliacao.setQuestionario(questionario);
            avaliacao.setAlunoAvaliado(alunoAvaliado);
            avaliacao.setAvaliador(avaliador);
            avaliacao.setTipoAvaliadorNaoUsuario(tipoAvaliadorNaoUsuario);
            avaliacao.setNomeAvaliadorNaoUsuario(nomeAvaliadorNaoUsuario);
            avaliacao.setDataRealizacao(LocalDate.parse(dataRealizacaoStr));
            if (horarioInicioStr != null && !horarioInicioStr.isEmpty()) {
                avaliacao.setHorarioInicio(LocalTime.parse(horarioInicioStr));
            }
            if (horarioFimStr != null && !horarioFimStr.isEmpty()) {
                avaliacao.setHorarioFim(LocalTime.parse(horarioFimStr));
            }
            avaliacao.setLocalRealizacao(localRealizacao);
            avaliacao.setFeedbackPositivo(feedbackPositivo);
            avaliacao.setFeedbackMelhoria(feedbackMelhoria);
            avaliacao.setContratoAprendizagem(contratoAprendizagem);

            // 5. Salva a avaliação (cabeçalho) para obter um ID.
            avaliacao = avaliacaoDAO.save(avaliacao);

            // 6. Se for uma edição, limpa as respostas antigas antes de inserir as novas.
            if ("edit".equals(action)) {
                respostaDAO.deleteByAvaliacaoPreenchida(avaliacao);
            }

            // 7. Processa e salva cada resposta individual do formulário.
            processarRespostasCompetencias(request, avaliacao, questionario);

            // 8. Redireciona para a página de listagem com mensagem de sucesso.
            response.sendRedirect(request.getContextPath() + "/avaliacoes?success=true");

        } catch (Exception e) {
            e.printStackTrace();
            // **CORREÇÃO APLICADA AQUI**
            // Em vez de chamar doGet, encaminha de volta ao JSP do formulário com a mensagem de erro.
            request.setAttribute("erro", "Erro ao salvar avaliação: " + e.getMessage());

            // Prepara novamente os dados para o formulário ser recarregado corretamente.
            prepareFormData(request);

            // Determina qual JSP usar e encaminha
            String questionarioIdStr = request.getParameter("questionarioId");
            String jspPath = determinarJSP(questionarioIdStr);
            request.getRequestDispatcher(jspPath).forward(request, response);
        }
    }

    /**
     * Método utilitário que carrega as listas de dados (locais, disciplinas,
     * etc.) necessárias para popular os campos <select> (dropdowns) nos
     * formulários.
     */
    private void prepareFormData(HttpServletRequest request) {
        request.setAttribute("alunos", usuarioDAO.findByTipoUsuario(TipoUsuario.ESTUDANTE));
        request.setAttribute("professores", usuarioDAO.findByTipoUsuario(TipoUsuario.PROFESSOR));
        request.setAttribute("locaisEventos", localEventoDAO.findAll());
        request.setAttribute("questionarios", questionarioDAO.findAll());
    }

    /**
     * Processa e salva as respostas para cada competência de um questionário.
     */
    private void processarRespostasCompetencias(HttpServletRequest request, AvaliacaoPreenchida avaliacao, Questionario questionario) {
        List<CompetenciaQuestionario> competencias = competenciaDAO.findByQuestionario(questionario.getIdQuestionario());
        Map<String, String> mapeamentoParametros = obterMapeamentoParametros(questionario);

        for (CompetenciaQuestionario competencia : competencias) {
            String nomeCompetencia = competencia.getNomeCompetencia().toLowerCase().trim();
            String nomeParametro = mapeamentoParametros.get(nomeCompetencia);

            if (nomeParametro != null) {
                String respostaValorStr = request.getParameter(nomeParametro);
                String naoAvaliadoStr = request.getParameter("nao_avaliado_" + nomeParametro.replace("resposta_", ""));

                RespostaItemAvaliacao resposta = new RespostaItemAvaliacao();
                resposta.setAvaliacaoPreenchida(avaliacao);
                resposta.setCompetenciaQuestionario(competencia);

                if ("true".equals(naoAvaliadoStr)) {
                    resposta.setNaoAvaliado(true);
                } else {
                    resposta.setNaoAvaliado(false);
                    if (respostaValorStr != null && !respostaValorStr.isEmpty()) {
                        resposta.setRespostaValorNumerico(new BigDecimal(respostaValorStr));
                    }
                }
                respostaDAO.save(resposta);
            }
        }
    }

    /**
     * Retorna um mapa que "traduz" o nome de uma competência (do banco) para o
     * nome do campo de input correspondente no HTML de cada formulário JSP.
     */
    private Map<String, String> obterMapeamentoParametros(Questionario questionario) {
        Map<String, String> mapeamento = new HashMap<>();

        String nomeQuestionario = questionario.getNomeModelo().toLowerCase();

        if (nomeQuestionario.contains("(mini-cex)") || (nomeQuestionario.contains("360") && nomeQuestionario.contains("professor"))) {
            // Mapeamento para Mini CEX e Avaliação 360° Professor
            mapeamento.put("entrevista médica", "resposta_entrevista_medica");
            mapeamento.put("exame físico", "resposta_exame_fisico");
            mapeamento.put("profissionalismo", "resposta_profissionalismo");
            mapeamento.put("julgamento clínico", "resposta_julgamento_clinico");
            mapeamento.put("habilidade de comunicação", "resposta_comunicacao");
            mapeamento.put("organização e eficiência", "resposta_organizacao");
            mapeamento.put("avaliação clínica geral", "resposta_avaliacao_geral");

        } else if (nomeQuestionario.contains("360") && nomeQuestionario.contains("pares")) {
            // Mapeamento específico para 360° Pares
            mapeamento.put("anamnese", "resposta_anamnese");
            mapeamento.put("exame físico", "resposta_exame_fisico");
            mapeamento.put("raciocínio clínico", "resposta_raciocinio_clinico");
            mapeamento.put("profissionalismo", "resposta_profissionalismo");
            mapeamento.put("comunicação", "resposta_comunicacao");
            mapeamento.put("organização e eficiência", "resposta_organizacao");
            mapeamento.put("competência profissional global", "resposta_competencia_global");
            mapeamento.put("atitude de compaixão e respeito", "resposta_compaixao");
            mapeamento.put("abordagem suave e sensível ao paciente", "resposta_abordagem_suave");
            mapeamento.put("comunicação e interação respeitosa com a equipe", "resposta_interacao_equipe");

        } else if (nomeQuestionario.contains("360") && nomeQuestionario.contains("equipe")) {
            // Mapeamento específico para 360° Equipe
            mapeamento.put("colaboração em equipe", "resposta_colaboracao_equipe");
            mapeamento.put("comunicação interprofissional", "resposta_comunicacao_interprofissional");
            mapeamento.put("respeito mútuo", "resposta_respeito_mutuo");
            mapeamento.put("responsabilidade compartilhada", "resposta_responsabilidade_compartilhada");
            mapeamento.put("liderança situacional", "resposta_lideranca_situacional");
            mapeamento.put("resolução de conflitos", "resposta_resolucao_conflitos");
            mapeamento.put("empatia profissional", "resposta_empatia_profissional");
            mapeamento.put("ética no trabalho em equipe", "resposta_etica_trabalho_equipe");
            mapeamento.put("flexibilidade e adaptação", "resposta_flexibilidade_adaptacao");
            mapeamento.put("contribuição para o ambiente de trabalho", "resposta_contribuicao_ambiente");

        } else if (nomeQuestionario.contains("360") && nomeQuestionario.contains("paciente")) {
            // Mapeamento para Avaliação 360° Paciente
            mapeamento.put("cortesia e educação", "resposta_cortesia_educacao");
            mapeamento.put("respeito à privacidade", "resposta_respeito_privacidade");
            mapeamento.put("demonstração de interesse", "resposta_demonstracao_interesse");
            mapeamento.put("demonstração de cuidado", "resposta_demonstracao_cuidado");
            mapeamento.put("clareza na comunicação", "resposta_clareza_comunicacao");
            mapeamento.put("explicação sobre procedimentos", "resposta_explicacao_procedimentos");
            mapeamento.put("envolvimento na tomada de decisão", "resposta_envolvimento_decisao");
            mapeamento.put("capacidade de tranquilizar", "resposta_capacidade_tranquilizar");
            mapeamento.put("tempo dedicado", "resposta_tempo_dedicado");
            mapeamento.put("satisfação geral", "resposta_satisfacao_geral");
        }

        return mapeamento;
    }

    /**
     * Determina qual arquivo JSP deve ser usado para renderizar o formulário,
     * com base no nome do questionário selecionado.
     */
    private String determinarJSP(String questionarioIdStr) {
        if (questionarioIdStr == null) {
            // Fallback para a lista se nenhum ID for fornecido
            return "/avaliacoes";
        }

        try {
            Questionario questionario = questionarioDAO.findById(Integer.parseInt(questionarioIdStr)).orElse(null);
            if (questionario != null) {
                String nomeQuestionario = questionario.getNomeModelo().toLowerCase();

                if (nomeQuestionario.contains("mini cex")) {
                    return "/WEB-INF/views/avaliacoes/minicex-form.jsp";
                }
                if (nomeQuestionario.contains("360") && nomeQuestionario.contains("professor")) {
                    return "/WEB-INF/views/avaliacoes/avaliacao360-professor-form.jsp";
                }
                if (nomeQuestionario.contains("360") && nomeQuestionario.contains("pares")) {
                    return "/WEB-INF/views/avaliacoes/avaliacao360-pares-form.jsp";
                }
                if (nomeQuestionario.contains("360") && nomeQuestionario.contains("equipe")) {
                    return "/WEB-INF/views/avaliacoes/avaliacao360-equipe-form.jsp";
                }
                if (nomeQuestionario.contains("360") && nomeQuestionario.contains("paciente")) {
                    return "/WEB-INF/views/avaliacoes/avaliacao360-paciente-form.jsp";
                }
            }
        } catch (NumberFormatException e) {
            /* Ignora */ }
        return "/WEB-INF/views/avaliacoes/minicex-form.jsp"; // Fallback
    }
}
