package com.unifae.med.servlet.admin;

import com.unifae.med.dao.LocalEventoDAO;
import com.unifae.med.entity.LocalEvento;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * SERVLET PARA GERENCIAMENTO DE LOCAIS DE EVENTOS
 * ===============================================
 * 
 * Controlador responsável por todas as operações CRUD relacionadas aos locais
 * onde os eventos podem ocorrer. Segue o mesmo padrão dos outros servlets
 * administrativos do sistema.
 * 
 * FUNCIONALIDADES:
 * - Listar todos os locais cadastrados
 * - Exibir formulário para novo local
 * - Exibir formulário para edição de local existente
 * - Salvar (criar/atualizar) local
 * - Excluir local
 * 
 * MAPEAMENTO: /admin/locais
 * 
 * @author Sistema UNIFAE
 */
@WebServlet("/admin/locais")
public class LocalEventoServlet extends HttpServlet {

    private LocalEventoDAO localEventoDAO;

    @Override
    public void init() {
        this.localEventoDAO = new LocalEventoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteLocal(request, response);
                    break;
                default:
                    listLocais(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Erro ao processar requisição: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Coleta dados do formulário
            String idStr = request.getParameter("idLocalEvento");
            String nomeLocal = request.getParameter("nomeLocal");
            String tipoLocal = request.getParameter("tipoLocal");
            String endereco = request.getParameter("endereco");
            String cidade = request.getParameter("cidade");
            String estado = request.getParameter("estado");

            // Validações básicas
            if (nomeLocal == null || nomeLocal.trim().isEmpty()) {
                throw new ServletException("Nome do local é obrigatório");
            }
            if (tipoLocal == null || tipoLocal.trim().isEmpty()) {
                throw new ServletException("Tipo do local é obrigatório");
            }
            if (endereco == null || endereco.trim().isEmpty()) {
                throw new ServletException("Endereço é obrigatório");
            }
            if (cidade == null || cidade.trim().isEmpty()) {
                throw new ServletException("Cidade é obrigatória");
            }
            if (estado == null || estado.trim().isEmpty()) {
                throw new ServletException("Estado é obrigatório");
            }

            // Determina se é edição ou criação
            LocalEvento local;
            if (idStr != null && !idStr.trim().isEmpty()) {
                // Edição - busca local existente
                Integer id = Integer.valueOf(idStr);
                local = localEventoDAO.findById(id)
                    .orElseThrow(() -> new ServletException("Local não encontrado"));
            } else {
                // Criação - novo local
                local = new LocalEvento();
            }

            // Preenche dados do local
            local.setNomeLocal(nomeLocal.trim());
            local.setTipoLocal(tipoLocal.trim());
            local.setEndereco(endereco.trim());
            local.setCidade(cidade.trim());
            local.setEstado(estado.trim().toUpperCase());

            // Salva no banco de dados
            localEventoDAO.save(local);

            // Redireciona para listagem com sucesso
            response.sendRedirect(request.getContextPath() + "/admin/locais?success=1");

        } catch (Exception e) {
            // Em caso de erro, volta para o formulário com mensagem
            request.setAttribute("erro", "Erro ao salvar local: " + e.getMessage());
            showNewForm(request, response);
        }
    }

    /**
     * Lista todos os locais cadastrados
     */
    private void listLocais(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<LocalEvento> listLocais = localEventoDAO.findAll();
        request.setAttribute("listLocais", listLocais);
        request.getRequestDispatcher("/WEB-INF/views/admin/locais/list.jsp").forward(request, response);
    }

    /**
     * Exibe formulário para novo local
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setAttribute("action", "new");
        request.getRequestDispatcher("/WEB-INF/views/admin/locais/form.jsp").forward(request, response);
    }

    /**
     * Exibe formulário para edição de local existente
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        Integer id = Integer.valueOf(request.getParameter("id"));
        LocalEvento local = localEventoDAO.findById(id)
            .orElseThrow(() -> new ServletException("Local não encontrado"));
        
        request.setAttribute("local", local);
        request.setAttribute("action", "edit");
        request.getRequestDispatcher("/WEB-INF/views/admin/locais/form.jsp").forward(request, response);
    }

    /**
     * Exclui um local
     */
    private void deleteLocal(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        Integer id = Integer.valueOf(request.getParameter("id"));
        localEventoDAO.deleteById(id);
        response.sendRedirect(request.getContextPath() + "/admin/locais?deleted=1");
    }
}
