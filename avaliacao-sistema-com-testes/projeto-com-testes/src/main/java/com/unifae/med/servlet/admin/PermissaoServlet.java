/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `PermissaoServlet`, é um Servlet Java que atua como um "Controller" no
 * padrão de arquitetura MVC (Model-View-Controller). Sua principal responsabilidade é
 * gerenciar todas as requisições web para a manutenção de Permissões em uma área
 * administrativa do sistema.
 *
 * Diferente das classes JAX-RS (que retornam JSON para uma API), este Servlet trabalha
 * com um fluxo de renderização no lado do servidor:
 * 1.  **Recebe a Requisição:** É mapeado para a URL `/admin/permissoes` e recebe
 * requisições HTTP (GET para visualização, POST para submissão de formulários).
 * 2.  **Determina a Ação:** Ele utiliza um parâmetro na URL (`?action=...`) para decidir
 * qual operação deve ser executada (listar, mostrar formulário de edição, deletar, etc.).
 * Este é um padrão comum conhecido como "Front Controller".
 * 3.  **Interage com o Modelo (DAO):** Utiliza o `PermissaoDAO` para buscar ou salvar
 * dados no banco de dados.
 * 4.  **Prepara os Dados para a Visão:** Coloca os dados necessários (como a lista de
 * permissões ou uma permissão específica) como atributos no objeto `HttpServletRequest`.
 * 5.  **Encaminha para a Visão (JSP):** Transfere o controle para um arquivo JSP
 * (`.jsp`), que é responsável por ler os atributos da requisição e renderizar a
 * página HTML final que será enviada ao navegador do usuário.
 * =================================================================================================
 */
package com.unifae.med.servlet.admin;

import com.unifae.med.dao.PermissaoDAO;
import com.unifae.med.entity.Permissao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * SERVLET PARA GERENCIAMENTO DE PERMISSÕES
 * ========================================
 *
 * @version 2.1 - Corrigida chamada a método inexistente setAtivo()
 * @author Sistema UNIFAE
 */
@WebServlet("/admin/permissoes")
public class PermissaoServlet extends HttpServlet {

    private PermissaoDAO permissaoDAO;

    /**
     * Método do ciclo de vida do Servlet. É chamado pelo contêiner uma única
     * vez, quando o servlet é carregado pela primeira vez. Ideal para
     * inicializar recursos que serão usados durante toda a vida do servlet,
     * como o DAO.
     */
    @Override
    public void init() {
        this.permissaoDAO = new PermissaoDAO();
    }

    /**
     * Trata todas as requisições HTTP do tipo GET. Atua como um roteador que,
     * baseado no parâmetro 'action', direciona para o método apropriado.
     *
     * @param request O objeto de requisição.
     * @param response O objeto de resposta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Se o parâmetro 'action' não for fornecido, a ação padrão é 'list'.
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
                    deletePermissao(request, response);
                    break;
                default:
                    listPermissoes(request, response);
                    break;
            }
        } catch (Exception e) {
            // Lança uma ServletException para que o contêiner possa tratar o erro (ex: exibir uma página de erro padrão).
            throw new ServletException("Erro ao processar requisição: " + e.getMessage(), e);
        }
    }

    /**
     * Trata todas as requisições HTTP do tipo POST, geralmente originadas da
     * submissão de um formulário HTML para criar ou atualizar uma permissão.
     *
     * @param request O objeto de requisição.
     * @param response O objeto de resposta.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("idPermissao");
        String nomePermissao = request.getParameter("nomePermissao");
        String descricaoPermissao = request.getParameter("descricaoPermissao");

        Permissao permissao = null;
        try {
            // Se um ID for fornecido, trata-se de uma edição; busca a entidade existente.
            if (idStr != null && !idStr.isEmpty()) {
                Integer id = Integer.parseInt(idStr);
                permissao = permissaoDAO.findById(id).orElseThrow(() -> new ServletException("Permissão não encontrada."));
            } else {
                // Se não houver ID, trata-se da criação de uma nova entidade.
                permissao = new Permissao();
            }

            // Popula o objeto com os dados vindos do formulário.
            permissao.setNomePermissao(nomePermissao);
            permissao.setDescricaoPermissao(descricaoPermissao);

            permissaoDAO.save(permissao);
            // Em caso de sucesso, redireciona o navegador para a página de listagem.
            response.sendRedirect(request.getContextPath() + "/admin/permissoes?success=1");

        } catch (Exception e) {
            // Se ocorrer um erro, não redireciona. Em vez disso, encaminha de volta para o formulário.
            request.setAttribute("error", "Erro ao salvar permissão: " + e.getMessage());
            request.setAttribute("permissao", permissao); // Envia o objeto com os dados preenchidos de volta.
            request.setAttribute("action", (idStr != null && !idStr.isEmpty()) ? "edit" : "new");
            // Encaminha para o JSP do formulário para que o usuário possa corrigir os dados.
            request.getRequestDispatcher("/WEB-INF/views/admin/permissoes/form-permissoes.jsp").forward(request, response);
        }
    }

    /**
     * Lógica para listar as permissões. Busca os dados no DAO, coloca na
     * requisição e encaminha para o JSP de listagem.
     */
    private void listPermissoes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String search = request.getParameter("search");

        Map<String, Object> result = permissaoDAO.findWithFiltersAndStats(search);
        // Coloca a lista e as estatísticas como atributos na requisição para serem acessados pelo JSP.
        request.setAttribute("listPermissoes", result.get("list"));
        request.setAttribute("stats", result.get("stats"));
        // Encaminha para o arquivo JSP que renderizará a tabela de permissões.
        request.getRequestDispatcher("/WEB-INF/views/admin/permissoes/list.jsp").forward(request, response);
    }

    /**
     * Lógica para exibir o formulário de criação. Prepara um objeto vazio e
     * encaminha para o JSP do formulário.
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("action", "new");
        request.setAttribute("permissao", new Permissao());
        request.getRequestDispatcher("/WEB-INF/views/admin/permissoes/form.jsp").forward(request, response);
    }

    /**
     * Lógica para exibir o formulário de edição. Busca a permissão pelo ID,
     * coloca na requisição e encaminha para o JSP do formulário.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer id = Integer.valueOf(request.getParameter("id"));
        Permissao permissaoExistente = permissaoDAO.findById(id)
                .orElseThrow(() -> new ServletException("Permissão não encontrada"));

        request.setAttribute("permissao", permissaoExistente);
        request.setAttribute("action", "edit");
        request.getRequestDispatcher("/WEB-INF/views/admin/permissoes/form.jsp").forward(request, response);
    }

    /**
     * Lógica para deletar uma permissão. Executa a deleção e redireciona para a
     * página de listagem.
     */
    private void deletePermissao(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        Integer id = Integer.valueOf(request.getParameter("id"));
        permissaoDAO.deleteById(id);
        // Redireciona o navegador para a lista, com um parâmetro para indicar o sucesso da operação.
        response.sendRedirect(request.getContextPath() + "/admin/permissoes?deleted=1");
    }
}
