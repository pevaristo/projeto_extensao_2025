/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `QuestionarioServlet`, é um Servlet Java que funciona como o "Controller"
 * em uma arquitetura MVC (Model-View-Controller) para a área administrativa de
 * gerenciamento de Questionários.
 *
 * Mapeado para a URL `/admin/questionarios`, este servlet centraliza o tratamento de
 * todas as requisições web para as operações de CRUD (Criar, Ler, Atualizar, Deletar)
 * sobre os modelos de questionário.
 *
 * O fluxo de operação é o seguinte:
 * 1.  **Roteamento de Ações:** O método `doGet` atua como um roteador, lendo o
 * parâmetro `action` da URL (`?action=...`) para determinar a operação desejada
 * (listar, novo, editar, deletar) e chamar o método privado correspondente.
 * 2.  **Interação com o Modelo:** Os métodos utilizam o `QuestionarioDAO` para
 * realizar as operações de busca, salvamento ou exclusão no banco de dados.
 * 3.  **Preparação da Visão:** Os dados obtidos do DAO são adicionados como atributos
 * ao objeto `request`.
 * 4.  **Renderização da Visão:** A requisição é encaminhada para o arquivo JSP
 * apropriado (a "View"), que utiliza os dados do `request` para gerar a página
 * HTML que será enviada de volta ao navegador do usuário.
 * 5.  **Tratamento de Submissões:** O método `doPost` lida com o envio de formulários
 * para criar ou atualizar questionários.
 * =================================================================================================
 */
package com.unifae.med.servlet.admin;

import com.unifae.med.dao.QuestionarioDAO;
import com.unifae.med.entity.Questionario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet("/admin/questionarios")
public class QuestionarioServlet extends HttpServlet {

    private QuestionarioDAO questionarioDAO;

    /**
     * Método do ciclo de vida do Servlet, chamado uma única vez na inicialização.
     * É usado aqui para criar a instância do DAO que será reutilizada em todas as requisições.
     */
    @Override
    public void init() {
        this.questionarioDAO = new QuestionarioDAO();
    }

    /**
     * Trata requisições HTTP GET. Atua como um roteador que delega a execução para
     * métodos privados com base no parâmetro 'action'.
     *
     * @param request  O objeto de requisição HTTP.
     * @param response O objeto de resposta HTTP.
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
                    deleteQuestionario(request, response);
                    break;
                default:
                    listQuestionarios(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Erro ao processar requisição: " + e.getMessage(), e);
        }
    }

    /**
     * Trata requisições HTTP POST, normalmente da submissão de um formulário para
     * criar ou atualizar um questionário.
     *
     * @param request  O objeto de requisição HTTP.
     * @param response O objeto de resposta HTTP.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("idQuestionario");
        String nomeModelo = request.getParameter("nomeModelo");
        String descricao = request.getParameter("descricao");

        Questionario questionario = null;
        try {
            // Verifica se é uma edição (ID presente) ou criação (ID ausente).
            if (idStr != null && !idStr.isEmpty()) {
                Integer id = Integer.parseInt(idStr);
                questionario = questionarioDAO.findById(id).orElseThrow(() -> new ServletException("Questionário não encontrado."));
            } else {
                questionario = new Questionario();
            }

            // Popula a entidade com os dados do formulário.
            questionario.setNomeModelo(nomeModelo);
            questionario.setDescricao(descricao);

            questionarioDAO.save(questionario);
            // Em caso de sucesso, redireciona o navegador para a página de listagem.
            response.sendRedirect(request.getContextPath() + "/admin/questionarios?success=1");

        } catch (Exception e) {
            // Em caso de erro, encaminha de volta para o formulário, mantendo os dados preenchidos.
            request.setAttribute("error", "Erro ao salvar questionário: " + e.getMessage());
            request.setAttribute("questionario", questionario);
            request.setAttribute("action", (idStr != null && !idStr.isEmpty()) ? "edit" : "new");
            request.getRequestDispatcher("/WEB-INF/views/admin/questionarios/form.jsp").forward(request, response);
        }
    }

    /**
     * Busca a lista de questionários (com filtro opcional), prepara os dados e encaminha para o JSP de listagem.
     */
    private void listQuestionarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String search = request.getParameter("search");

        Map<String, Object> result = questionarioDAO.findWithFiltersAndStats(search);
        // Adiciona os dados como atributos à requisição para serem usados pelo JSP.
        request.setAttribute("listQuestionarios", result.get("list"));
        request.setAttribute("stats", result.get("stats"));
        // Encaminha para o arquivo JSP que irá renderizar a tabela HTML.
        request.getRequestDispatcher("/WEB-INF/views/admin/questionarios/list.jsp").forward(request, response);
    }

    /**
     * Prepara os dados para o formulário de um novo questionário e encaminha para o JSP correspondente.
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("action", "new");
        request.setAttribute("questionario", new Questionario());
        request.getRequestDispatcher("/WEB-INF/views/admin/questionarios/form.jsp").forward(request, response);
    }

    /**
     * Busca um questionário existente pelo ID, prepara os dados para o formulário e encaminha para o JSP.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer id = Integer.valueOf(request.getParameter("id"));
        Questionario questionario = questionarioDAO.findById(id)
                .orElseThrow(() -> new ServletException("Questionário não encontrado"));

        request.setAttribute("questionario", questionario);
        request.setAttribute("action", "edit");
        request.getRequestDispatcher("/WEB-INF/views/admin/questionarios/form.jsp").forward(request, response);
    }

    /**
     * Deleta um questionário pelo ID e redireciona o navegador para a página de listagem.
     */
    private void deleteQuestionario(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        Integer id = Integer.valueOf(request.getParameter("id"));
        questionarioDAO.deleteById(id);
        // Redireciona para a lista, adicionando um parâmetro de sucesso na URL.
        response.sendRedirect(request.getContextPath() + "/admin/questionarios?deleted=1");
    }
}