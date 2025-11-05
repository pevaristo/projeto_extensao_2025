/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `TurmaServlet`, é um Servlet Java que atua como o "Controller" central
 * em uma arquitetura MVC (Model-View-Controller) para o gerenciamento de Turmas
 * acadêmicas na área administrativa da aplicação.
 *
 * Mapeado para a URL `/admin/turmas`, o servlet utiliza o padrão "Front Controller",
 * onde um único ponto de entrada (`doGet`) roteia as requisições para diferentes
 * lógicas de negócio com base no parâmetro `action` presente na URL.
 *
 * O fluxo de trabalho típico é:
 * 1.  **Recebimento e Roteamento:** O `doGet` recebe a requisição e, com base na `action`
 * (ex: "list", "new", "edit"), invoca o método privado correspondente.
 * 2.  **Interação com o Modelo:** Os métodos interagem com o `TurmaDAO` para realizar
 * operações de banco de dados (buscar, salvar, deletar).
 * 3.  **Preparação dos Dados:** Os dados retornados pelo DAO são colocados como
 * atributos no objeto `request` para serem acessados pela "View".
 * 4.  **Renderização da Visão:** A requisição é encaminhada para um arquivo JSP, que é
 * responsável por gerar a página HTML final para o usuário.
 * 5.  **Submissão de Formulários:** O `doPost` lida com a criação e atualização de
 * turmas, recebendo os dados do formulário, persistindo-os e redirecionando o
 * usuário.
 * =================================================================================================
 */
package com.unifae.med.servlet.admin;

import com.unifae.med.dao.TurmaDAO;
import com.unifae.med.entity.Turma;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/turmas")
public class TurmaServlet extends HttpServlet {

    private TurmaDAO turmaDAO;

    /**
     * Método do ciclo de vida do Servlet, executado uma vez na inicialização.
     * Utilizado para criar a instância do DAO que será usada em todas as
     * requisições.
     */
    @Override
    public void init() {
        this.turmaDAO = new TurmaDAO();
    }

    /**
     * Trata requisições HTTP GET, atuando como um roteador para diferentes
     * ações baseadas no parâmetro 'action' da URL.
     *
     * @param request O objeto de requisição.
     * @param response O objeto de resposta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                    deleteTurma(request, response);
                    break;
                default:
                    listTurmas(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    /**
     * Trata requisições HTTP POST, geralmente da submissão de um formulário
     * para criar ou atualizar uma turma.
     *
     * @param request O objeto de requisição.
     * @param response O objeto de resposta.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            // Extrai os dados do formulário da requisição.
            String idStr = request.getParameter("idTurma");
            String nomeTurma = request.getParameter("nomeTurma");
            String codigoTurma = request.getParameter("codigoTurma");
            String anoLetivoStr = request.getParameter("anoLetivo");
            String semestreStr = request.getParameter("semestre");
            // Checkboxes em HTML enviam o valor "on" quando marcados.
            boolean ativo = "on".equals(request.getParameter("ativo"));

            Turma turma;
            // Decide se é uma atualização (ID existe) ou uma criação (ID não existe).
            if (idStr != null && !idStr.isEmpty()) {
                Integer id = Integer.parseInt(idStr);
                turma = turmaDAO.findById(id).orElseThrow(() -> new ServletException("Turma não encontrada para atualização."));
            } else {
                turma = new Turma();
            }

            // Popula o objeto Turma com os dados do formulário.
            turma.setNomeTurma(nomeTurma);
            turma.setCodigoTurma(codigoTurma);

            if (anoLetivoStr != null && !anoLetivoStr.trim().isEmpty()) {
                turma.setAnoLetivo(Integer.parseInt(anoLetivoStr));
            }
            if (semestreStr != null && !semestreStr.trim().isEmpty()) {
                turma.setSemestre(Integer.parseInt(semestreStr));
            }
            turma.setAtivo(ativo);

            turmaDAO.save(turma);
            // Redireciona o navegador para a página de listagem para evitar reenvio do formulário.
            response.sendRedirect(request.getContextPath() + "/admin/turmas?success=1");
        } catch (Exception e) {
            // Em caso de erro, lança uma exceção para que o contêiner a trate.
            throw new ServletException("Erro ao salvar a turma.", e);
        }
    }

    /**
     * Busca a lista de turmas (com filtros opcionais), prepara os dados e
     * encaminha para o JSP de listagem.
     */
    private void listTurmas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("search");
        String status = request.getParameter("status");

        Map<String, Object> result = turmaDAO.findWithFiltersAndStats(search, status);
        // Coloca os dados como atributos na requisição para que o JSP possa acessá-los.
        request.setAttribute("listTurmas", result.get("list"));
        request.setAttribute("stats", result.get("stats"));
        request.getRequestDispatcher("/WEB-INF/views/admin/turmas/list.jsp").forward(request, response);
    }

    /**
     * Prepara um objeto Turma vazio e encaminha para o JSP do formulário para
     * criação.
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("action", "new");
        request.setAttribute("turma", new Turma());
        request.getRequestDispatcher("/WEB-INF/views/admin/turmas/form.jsp").forward(request, response);
    }

    /**
     * Busca uma turma existente pelo ID e encaminha para o JSP do formulário
     * para edição.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        Turma turmaExistente = turmaDAO.findById(id).orElseThrow(() -> new ServletException("Turma não encontrada"));
        request.setAttribute("turma", turmaExistente);
        request.setAttribute("action", "edit");
        request.getRequestDispatcher("/WEB-INF/views/admin/turmas/form.jsp").forward(request, response);
    }

    /**
     * Deleta uma turma pelo ID e redireciona o navegador para a página de
     * listagem.
     */
    private void deleteTurma(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        turmaDAO.deleteById(id);
        response.sendRedirect(request.getContextPath() + "/admin/turmas?deleted=1");
    }
}
