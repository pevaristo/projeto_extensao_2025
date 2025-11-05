package com.unifae.med.servlet.admin;

import com.unifae.med.dao.CompetenciaQuestionarioDAO;
import com.unifae.med.dao.QuestionarioDAO;
import com.unifae.med.entity.CompetenciaQuestionario;
import com.unifae.med.entity.Questionario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `CompetenciaQuestionarioServlet`, é o Controller (Controlador)
 * na arquitetura MVC para o gerenciamento de Competências de um Questionário.
 *
 * Mapeado para a URL `/admin/competencias`, este servlet centraliza o
 * tratamento de todas as requisições web para as operações de CRUD (Criar, Ler,
 * Atualizar, Deletar) sobre as competências, que são sempre filhas de um
 * Questionário.
 *
 * O fluxo de operação é o seguinte: 1. **Roteamento de Ações:** O método
 * `doGet` atua como um roteador, lendo o parâmetro `action` da URL
 * (`?action=...`) para determinar a operação desejada (listar, novo, editar,
 * deletar) e chamar o método privado correspondente. 2. **Contexto do
 * Questionário:** Todas as operações exigem um `questionarioId` como parâmetro
 * para saber a qual questionário as competências pertencem. 3. **Interação com
 * o Modelo:** Os métodos utilizam o `CompetenciaQuestionarioDAO` e
 * `QuestionarioDAO` para realizar as operações no banco de dados. 4.
 * **Preparação da Visão:** Os dados obtidos do DAO (listas, objetos) são
 * adicionados como atributos ao objeto `request`. 5. **Renderização da Visão:**
 * A requisição é encaminhada para o arquivo JSP apropriado (a "View"), que
 * utiliza os dados para gerar a página HTML. 6. **Tratamento de Submissões:** O
 * método `doPost` lida com o envio do formulário para criar ou atualizar uma
 * competência.
 * =================================================================================================
 */
@WebServlet("/admin/competencias")
public class CompetenciaQuestionarioServlet extends HttpServlet {

    private CompetenciaQuestionarioDAO competenciaDAO;
    private QuestionarioDAO questionarioDAO;

    @Override
    public void init() {
        this.competenciaDAO = new CompetenciaQuestionarioDAO();
        this.questionarioDAO = new QuestionarioDAO();
    }

    /**
     * Trata requisições HTTP GET. Atua como um roteador que delega a execução
     * para métodos privados com base no parâmetro 'action'.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action") != null ? request.getParameter("action") : "list";

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteCompetencia(request, response);
                    break;
                default:
                    listCompetencias(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Erro ao processar requisição de competências: " + e.getMessage(), e);
        }
    }

    /**
     * Trata requisições HTTP POST do formulário de criação/edição de
     * competência.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // O ID do questionário pai é sempre necessário.
        Integer questionarioId = Integer.parseInt(request.getParameter("questionarioId"));
        String idStr = request.getParameter("idCompetenciaQuestionario");

        CompetenciaQuestionario competencia = null;
        try {
            // Garante que o questionário pai existe.
            Questionario questionario = questionarioDAO.findById(questionarioId)
                    .orElseThrow(() -> new ServletException("Questionário pai não encontrado."));

            // Se um ID de competência existe, é uma edição; caso contrário, é uma criação.
            if (idStr != null && !idStr.isEmpty()) {
                Integer id = Integer.parseInt(idStr);
                competencia = competenciaDAO.findById(id).orElseThrow(() -> new ServletException("Competência não encontrada."));
            } else {
                competencia = new CompetenciaQuestionario();
            }

            // Popula a entidade com os dados do formulário.
            competencia.setQuestionario(questionario);
            competencia.setNomeCompetencia(request.getParameter("nomeCompetencia"));
            competencia.setTipoItem(CompetenciaQuestionario.TipoItem.valueOf(request.getParameter("tipoItem")));
            competencia.setDescricaoPrompt(request.getParameter("descricaoPrompt"));
            competencia.setOrdemExibicao(Integer.parseInt(request.getParameter("ordemExibicao")));

            // Para checkboxes, o parâmetro só vem na requisição se estiver marcado.
            competencia.setObrigatorio(request.getParameter("obrigatorio") != null);
            competencia.setAtivo(request.getParameter("ativo") != null);

            competenciaDAO.save(competencia);

            // Redireciona para a lista de competências do mesmo questionário com mensagem de sucesso.
            response.sendRedirect(request.getContextPath() + "/admin/competencias?questionarioId=" + questionarioId + "&success=1");

        } catch (Exception e) {
            // Em caso de erro, reexibe o formulário com os dados preenchidos e uma mensagem de erro.
            request.setAttribute("error", "Erro ao salvar competência: " + e.getMessage());
            request.setAttribute("competencia", competencia);
            request.setAttribute("questionarioId", questionarioId);
            request.setAttribute("questionario", competencia != null ? competencia.getQuestionario() : questionarioDAO.findById(questionarioId).orElse(null));
            request.setAttribute("action", (idStr != null && !idStr.isEmpty()) ? "edit" : "new");
            request.getRequestDispatcher("/WEB-INF/views/admin/competencias/form.jsp").forward(request, response);
        }
    }

    /**
     * Busca a lista de competências de um questionário e encaminha para o JSP
     * de listagem.
     */
    private void listCompetencias(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer questionarioId = Integer.parseInt(request.getParameter("questionarioId"));
        Questionario questionario = questionarioDAO.findById(questionarioId)
                .orElseThrow(() -> new ServletException("Questionário não encontrado."));

        String search = request.getParameter("search");
        List<CompetenciaQuestionario> listCompetencias = competenciaDAO.findByQuestionarioAndFilters(questionarioId, search);

        request.setAttribute("listCompetencias", listCompetencias);
        request.setAttribute("questionario", questionario);
        request.getRequestDispatcher("/WEB-INF/views/admin/competencias/list.jsp").forward(request, response);
    }

    /**
     * Prepara os dados para o formulário de uma nova competência.
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer questionarioId = Integer.parseInt(request.getParameter("questionarioId"));
        Questionario questionario = questionarioDAO.findById(questionarioId)
                .orElseThrow(() -> new ServletException("Questionário não encontrado."));

        CompetenciaQuestionario novaCompetencia = new CompetenciaQuestionario();
        novaCompetencia.setAtivo(true); // Default para true
        novaCompetencia.setObrigatorio(true); // Default para true

        request.setAttribute("action", "new");
        request.setAttribute("competencia", novaCompetencia);
        request.setAttribute("questionario", questionario);
        request.getRequestDispatcher("/WEB-INF/views/admin/competencias/form.jsp").forward(request, response);
    }

    /**
     * Busca uma competência pelo ID e encaminha para o formulário de edição.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer id = Integer.valueOf(request.getParameter("id"));

        CompetenciaQuestionario competencia = competenciaDAO.findByIdWithQuestionario(id)
                .orElseThrow(() -> new ServletException("Competência não encontrada."));

        request.setAttribute("competencia", competencia);
        request.setAttribute("questionario", competencia.getQuestionario());
        request.setAttribute("action", "edit");
        request.getRequestDispatcher("/WEB-INF/views/admin/competencias/form.jsp").forward(request, response);
    }

    /**
     * Deleta uma competência pelo ID e redireciona para a página de listagem.
     */
    private void deleteCompetencia(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        Integer id = Integer.valueOf(request.getParameter("id"));
        Integer questionarioId = Integer.valueOf(request.getParameter("questionarioId"));

        competenciaDAO.deleteById(id);

        // Redireciona para a lista, passando o ID do questionário pai e um parâmetro de sucesso.
        response.sendRedirect(request.getContextPath() + "/admin/competencias?questionarioId=" + questionarioId + "&deleted=1");
    }

}
