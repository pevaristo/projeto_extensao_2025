package com.unifae.med.servlet.admin;

import com.unifae.med.dao.DisciplinaDAO;
import com.unifae.med.entity.Disciplina;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * DISCIPLINA SERVLET - CONTROLADOR PARA GERENCIAMENTO DE DISCIPLINAS
 * ==================================================================
 * 
 * Servlet responsável pelo CRUD completo de disciplinas no sistema.
 * Segue o padrão MVC e arquitetura já estabelecida no projeto.
 * 
 * FUNCIONALIDADES:
 * - Listar todas as disciplinas
 * - Criar nova disciplina
 * - Editar disciplina existente
 * - Excluir disciplina
 * - Ativar/desativar disciplina
 * 
 * MAPEAMENTO:
 * - URL: /admin/disciplinas
 * - Métodos: GET (listagem/formulários) e POST (criação/edição)
 * 
 * RELACIONAMENTO COM OUTROS ARQUIVOS:
 * - DisciplinaDAO.java: Acesso aos dados
 * - Disciplina.java: Entidade JPA existente
 * - disciplinas/list.jsp: Listagem
 * - disciplinas/form.jsp: Formulário
 * 
 * @author Sistema de Avaliação UNIFAE
 * @version 1.0 - Corrigido para entidade existente
 */
@WebServlet("/admin/disciplinas")
public class DisciplinaServlet extends HttpServlet {

    private DisciplinaDAO disciplinaDAO;

    /**
     * INICIALIZAÇÃO DO SERVLET
     * ========================
     * Inicializa o DAO necessário para operações de banco.
     */
    @Override
    public void init() {
        this.disciplinaDAO = new DisciplinaDAO();
    }

    /**
     * MÉTODO GET - NAVEGAÇÃO E EXIBIÇÃO
     * =================================
     * Processa requisições GET para:
     * - Listar disciplinas (padrão)
     * - Exibir formulário de nova disciplina
     * - Exibir formulário de edição
     * - Excluir disciplina
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action") != null ? 
                       request.getParameter("action") : "list";

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteDisciplina(request, response);
                    break;
                case "toggle":
                    toggleAtivaDisciplina(request, response);
                    break;
                default:
                    listDisciplinas(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Erro ao processar requisição: " + e.getMessage(), e);
        }
    }

    /**
     * MÉTODO POST - CRIAÇÃO E EDIÇÃO
     * ==============================
     * Processa formulários de criação e edição de disciplinas.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Recupera parâmetros do formulário
            String idStr = request.getParameter("idDisciplina");
            String nomeDisciplina = request.getParameter("nomeDisciplina");
            String siglaDisciplina = request.getParameter("siglaDisciplina");
            boolean ativa = "on".equals(request.getParameter("ativa"));

            // Validações básicas
            if (nomeDisciplina == null || nomeDisciplina.trim().isEmpty()) {
                throw new ServletException("Nome da disciplina é obrigatório");
            }

            if (nomeDisciplina.trim().length() > 255) {
                throw new ServletException("Nome da disciplina deve ter no máximo 255 caracteres");
            }

            if (siglaDisciplina != null && siglaDisciplina.trim().length() > 10) {
                throw new ServletException("Sigla da disciplina deve ter no máximo 10 caracteres");
            }

            // Determina se é criação ou edição
            Disciplina disciplina;
            if (idStr != null && !idStr.isEmpty()) {
                // Edição - busca disciplina existente
                Integer id = Integer.parseInt(idStr);
                disciplina = disciplinaDAO.findById(id)
                    .orElseThrow(() -> new ServletException("Disciplina não encontrada"));
            } else {
                // Criação - nova disciplina
                disciplina = new Disciplina();
            }

            // Define propriedades usando os métodos corretos da entidade existente
            disciplina.setNomeDisciplina(nomeDisciplina.trim());
            disciplina.setSiglaDisciplina(siglaDisciplina != null && !siglaDisciplina.trim().isEmpty() ? 
                                         siglaDisciplina.trim() : null);
            disciplina.setAtiva(ativa);

            // Salva no banco
            disciplinaDAO.save(disciplina);

            // Redireciona para listagem
            response.sendRedirect(request.getContextPath() + "/admin/disciplinas");

        } catch (Exception e) {
            throw new ServletException("Erro ao salvar disciplina: " + e.getMessage(), e);
        }
    }

    /**
     * LISTAR DISCIPLINAS
     * ==================
     * Busca todas as disciplinas e encaminha para página de listagem.
     */
    private void listDisciplinas(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<Disciplina> listDisciplinas = disciplinaDAO.findAll();
        request.setAttribute("listDisciplinas", listDisciplinas);
        
        request.getRequestDispatcher("/WEB-INF/views/admin/disciplinas/list.jsp")
               .forward(request, response);
    }

    /**
     * EXIBIR FORMULÁRIO DE NOVA DISCIPLINA
     * ====================================
     * Prepara dados e encaminha para formulário de criação.
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setAttribute("action", "new");
        request.getRequestDispatcher("/WEB-INF/views/admin/disciplinas/form.jsp")
               .forward(request, response);
    }

    /**
     * EXIBIR FORMULÁRIO DE EDIÇÃO
     * ===========================
     * Busca disciplina existente e encaminha para formulário de edição.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        Integer id = Integer.parseInt(request.getParameter("id"));
        Disciplina disciplinaExistente = disciplinaDAO.findById(id)
            .orElseThrow(() -> new ServletException("Disciplina não encontrada"));
        
        request.setAttribute("disciplina", disciplinaExistente);
        request.setAttribute("action", "edit");
        request.getRequestDispatcher("/WEB-INF/views/admin/disciplinas/form.jsp")
               .forward(request, response);
    }

    /**
     * EXCLUIR DISCIPLINA
     * ==================
     * Remove disciplina do banco de dados.
     */
    private void deleteDisciplina(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        Integer id = Integer.parseInt(request.getParameter("id"));
        disciplinaDAO.deleteById(id);
        response.sendRedirect(request.getContextPath() + "/admin/disciplinas");
    }

    /**
     * ATIVAR/DESATIVAR DISCIPLINA
     * ===========================
     * Alterna status ativo/inativo da disciplina.
     */
    private void toggleAtivaDisciplina(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        
        Integer id = Integer.parseInt(request.getParameter("id"));
        Disciplina disciplina = disciplinaDAO.findById(id)
            .orElseThrow(() -> new ServletException("Disciplina não encontrada"));
        
        // Alterna status
        disciplina.setAtiva(!disciplina.getAtiva());
        disciplinaDAO.save(disciplina);
        
        response.sendRedirect(request.getContextPath() + "/admin/disciplinas");
    }
}
