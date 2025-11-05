package com.unifae.med.servlet.admin;

import com.unifae.med.dao.UsuarioDAO;
import com.unifae.med.dao.TurmaDAO;
import com.unifae.med.entity.Usuario;
import com.unifae.med.entity.Turma;
import com.unifae.med.entity.TipoUsuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ALUNO SERVLET - CONTROLADOR PARA GERENCIAMENTO DE ALUNOS
 * ========================================================
 * 
 * Servlet responsável pelo CRUD específico de alunos (usuários do tipo ESTUDANTE).
 * Segue o padrão MVC e arquitetura já estabelecida no projeto.
 * 
 * FUNCIONALIDADES:
 * - Listar todos os alunos
 * - Criar novo aluno
 * - Editar aluno existente
 * - Excluir aluno
 * - Ativar/desativar aluno
 * - Filtros específicos para alunos
 * 
 * MAPEAMENTO:
 * - URL: /admin/alunos
 * - Métodos: GET (listagem/formulários) e POST (criação/edição)
 * 
 * RELACIONAMENTO COM OUTROS ARQUIVOS:
 * - UsuarioDAO.java: Acesso aos dados de usuários
 * - TurmaDAO.java: Acesso aos dados de turmas
 * - Usuario.java: Entidade JPA
 * - alunos/list.jsp: Listagem
 * - alunos/form.jsp: Formulário
 * 
 * @author Sistema de Avaliação UNIFAE
 * @version 1.0
 */
@WebServlet("/admin/alunos")
public class AlunoServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    private TurmaDAO turmaDAO;

    /**
     * INICIALIZAÇÃO DO SERVLET
     * ========================
     * Inicializa os DAOs necessários para operações de banco.
     */
    @Override
    public void init() {
        this.usuarioDAO = new UsuarioDAO();
        this.turmaDAO = new TurmaDAO();
    }

    /**
     * MÉTODO GET - NAVEGAÇÃO E EXIBIÇÃO
     * =================================
     * Processa requisições GET para:
     * - Listar alunos (padrão)
     * - Exibir formulário de novo aluno
     * - Exibir formulário de edição
     * - Excluir aluno
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
                    deleteAluno(request, response);
                    break;
                case "toggle":
                    toggleAtivoAluno(request, response);
                    break;
                default:
                    listAlunos(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Erro ao processar requisição: " + e.getMessage(), e);
        }
    }

    /**
     * MÉTODO POST - CRIAÇÃO E EDIÇÃO
     * ==============================
     * Processa formulários de criação e edição de alunos.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Recupera parâmetros do formulário
            String idStr = request.getParameter("idUsuario");
            String nomeCompleto = request.getParameter("nomeCompleto");
            String email = request.getParameter("email");
            String senha = request.getParameter("senhaHash");
            String matriculaRA = request.getParameter("matriculaRA");
            String telefone = request.getParameter("telefone");
            String periodoAtual = request.getParameter("periodoAtualAluno");
            String observacoes = request.getParameter("observacoesGeraisAluno");
            boolean ativo = "on".equals(request.getParameter("ativo"));

            // Validações básicas
            if (nomeCompleto == null || nomeCompleto.trim().isEmpty()) {
                throw new ServletException("Nome completo é obrigatório");
            }
            if (email == null || email.trim().isEmpty()) {
                throw new ServletException("Email é obrigatório");
            }
            if (matriculaRA == null || matriculaRA.trim().isEmpty()) {
                throw new ServletException("Matrícula/RA é obrigatório para alunos");
            }

            // Determina se é criação ou edição
            Usuario aluno;
            if (idStr != null && !idStr.isEmpty()) {
                // Edição - busca aluno existente
                Integer id = Integer.parseInt(idStr);
                aluno = usuarioDAO.findById(id)
                    .orElseThrow(() -> new ServletException("Aluno não encontrado"));
                
                // Verifica se é realmente um aluno
                if (aluno.getTipoUsuario() != TipoUsuario.ESTUDANTE) {
                    throw new ServletException("Usuário não é um estudante");
                }
            } else {
                // Criação - novo aluno
                aluno = new Usuario();
                aluno.setTipoUsuario(TipoUsuario.ESTUDANTE); // Define como estudante
            }

            // Validações específicas para alunos
            if (usuarioDAO.existsByEmail(email) && 
                (aluno.getIdUsuario() == null || !email.equals(aluno.getEmail()))) {
                throw new ServletException("Email já está em uso por outro usuário");
            }
            
            if (usuarioDAO.existsByMatriculaRA(matriculaRA) && 
                (aluno.getIdUsuario() == null || !matriculaRA.equals(aluno.getMatriculaRA()))) {
                throw new ServletException("Matrícula/RA já está em uso por outro usuário");
            }

            // Define propriedades
            aluno.setNomeCompleto(nomeCompleto.trim());
            aluno.setEmail(email.trim().toLowerCase());
            aluno.setMatriculaRA(matriculaRA.trim());
            aluno.setTelefone(telefone != null ? telefone.trim() : null);
            aluno.setPeriodoAtualAluno(periodoAtual != null ? periodoAtual.trim() : null);
            aluno.setObservacoesGeraisAluno(observacoes != null ? observacoes.trim() : null);
            aluno.setAtivo(ativo);

            // Define senha apenas se fornecida (criação ou alteração)
            if (senha != null && !senha.trim().isEmpty()) {
                // IMPORTANTE: Em produção, usar hash da senha (BCrypt, etc.)
                aluno.setSenhaHash(senha.trim());
            } else if (aluno.getIdUsuario() == null) {
                // Para novos alunos, senha é obrigatória
                throw new ServletException("Senha é obrigatória para novos alunos");
            }

            // Salva no banco
            usuarioDAO.save(aluno);

            // Redireciona para listagem
            response.sendRedirect(request.getContextPath() + "/admin/alunos");

        } catch (Exception e) {
            throw new ServletException("Erro ao salvar aluno: " + e.getMessage(), e);
        }
    }

    /**
     * LISTAR ALUNOS
     * =============
     * Busca todos os usuários do tipo ESTUDANTE e encaminha para página de listagem.
     */
    private void listAlunos(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Busca apenas usuários do tipo ESTUDANTE
        List<Usuario> todosUsuarios = usuarioDAO.findAll();
        List<Usuario> listAlunos = todosUsuarios.stream()
            .filter(usuario -> usuario.getTipoUsuario() == TipoUsuario.ESTUDANTE)
            .collect(Collectors.toList());
        
        // Busca turmas para exibição de estatísticas
        List<Turma> listTurmas = turmaDAO.findAll();
        
        request.setAttribute("listAlunos", listAlunos);
        request.setAttribute("listTurmas", listTurmas);
        request.getRequestDispatcher("/WEB-INF/views/admin/alunos/list.jsp")
               .forward(request, response);
    }

    /**
     * EXIBIR FORMULÁRIO DE NOVO ALUNO
     * ===============================
     * Prepara dados e encaminha para formulário de criação.
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Busca turmas para seleção
        List<Turma> listTurmas = turmaDAO.findAll();
        
        request.setAttribute("action", "new");
        request.setAttribute("listTurmas", listTurmas);
        request.getRequestDispatcher("/WEB-INF/views/admin/alunos/form.jsp")
               .forward(request, response);
    }

    /**
     * EXIBIR FORMULÁRIO DE EDIÇÃO
     * ===========================
     * Busca aluno existente e encaminha para formulário de edição.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        Integer id = Integer.parseInt(request.getParameter("id"));
        Usuario alunoExistente = usuarioDAO.findById(id)
            .orElseThrow(() -> new ServletException("Aluno não encontrado"));
        
        // Verifica se é realmente um aluno
        if (alunoExistente.getTipoUsuario() != TipoUsuario.ESTUDANTE) {
            throw new ServletException("Usuário não é um estudante");
        }
        
        // Busca turmas para seleção
        List<Turma> listTurmas = turmaDAO.findAll();
        
        request.setAttribute("aluno", alunoExistente);
        request.setAttribute("action", "edit");
        request.setAttribute("listTurmas", listTurmas);
        request.getRequestDispatcher("/WEB-INF/views/admin/alunos/form.jsp")
               .forward(request, response);
    }

    /**
     * EXCLUIR ALUNO
     * =============
     * Remove aluno do banco de dados.
     */
    private void deleteAluno(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        
        Integer id = Integer.parseInt(request.getParameter("id"));
        Usuario aluno = usuarioDAO.findById(id)
            .orElseThrow(() -> new ServletException("Aluno não encontrado"));
        
        // Verifica se é realmente um aluno
        if (aluno.getTipoUsuario() != TipoUsuario.ESTUDANTE) {
            throw new ServletException("Usuário não é um estudante");
        }
        
        usuarioDAO.deleteById(id);
        response.sendRedirect(request.getContextPath() + "/admin/alunos");
    }

    /**
     * ATIVAR/DESATIVAR ALUNO
     * ======================
     * Alterna status ativo/inativo do aluno (soft delete).
     */
    private void toggleAtivoAluno(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        
        Integer id = Integer.parseInt(request.getParameter("id"));
        Usuario aluno = usuarioDAO.findById(id)
            .orElseThrow(() -> new ServletException("Aluno não encontrado"));
        
        // Verifica se é realmente um aluno
        if (aluno.getTipoUsuario() != TipoUsuario.ESTUDANTE) {
            throw new ServletException("Usuário não é um estudante");
        }
        
        // Alterna status
        aluno.setAtivo(!aluno.getAtivo());
        usuarioDAO.save(aluno);
        
        response.sendRedirect(request.getContextPath() + "/admin/alunos");
    }
}
