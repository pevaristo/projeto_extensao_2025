/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `UsuarioServlet`, é um Servlet Java que atua como o "Controller" central
 * em uma arquitetura MVC (Model-View-Controller) para a área administrativa de
 * gerenciamento de Usuários.
 *
 * Mapeado para a URL `/admin/usuarios`, o servlet gerencia todas as operações de CRUD
 * (Criar, Ler, Atualizar, Deletar) para os usuários. Sua complexidade é maior que a de
 * outros servlets devido à necessidade de lidar com relacionamentos e dados auxiliares:
 *
 * 1.  **Gerenciamento de Relacionamentos:** Para criar ou atualizar um usuário, o servlet
 * precisa lidar com sua `Permissao` associada. Ele utiliza o `PermissaoDAO` para
 * buscar a permissão selecionada no formulário e vinculá-la ao usuário.
 *
 * 2.  **Preparação de Dados para Formulários:** Os métodos `showNewForm` e `showEditForm`
 * não apenas preparam o objeto `Usuario`, mas também buscam e enviam para o JSP a
 * lista de todos os `Tipos de Usuário` (do Enum) e a lista de todas as `Permissões`
 * (do DAO). Isso é necessário para que o formulário possa renderizar as opções nos
 * campos de seleção (dropdowns).
 *
 * 3.  **Fluxo Padrão de Servlet:** A classe segue o padrão "Front Controller", onde o
 * método `doGet` roteia as requisições com base no parâmetro `action`, e o `doPost`
 * lida com a submissão de formulários, persistindo os dados e redirecionando o
 * usuário para a página de listagem.
 * =================================================================================================
 */
package com.unifae.med.servlet.admin;

import com.unifae.med.dao.PermissaoDAO;
import com.unifae.med.dao.UsuarioDAO;
import com.unifae.med.entity.Permissao;
import com.unifae.med.entity.TipoUsuario;
import com.unifae.med.entity.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// --- ADIÇÕES: Imports necessários para o tratamento de erro ---
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
// --- FIM ADIÇÕES ---

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/usuarios")
public class UsuarioServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    private PermissaoDAO permissaoDAO;

    /**
     * Método de inicialização do Servlet. Cria as instâncias dos DAOs
     * necessários.
     */
    @Override
    public void init() {
        this.usuarioDAO = new UsuarioDAO();
        this.permissaoDAO = new PermissaoDAO();
    }

    /**
     * Trata requisições HTTP GET, roteando para a ação apropriada (listar,
     * novo, editar, deletar).
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
                    deleteUsuario(request, response);
                    break;
                default:
                    listUsuarios(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    /**
     * Trata requisições HTTP POST do formulário de criação/edição de usuário.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Extrai todos os dados do formulário da requisição.
        String idStr = request.getParameter("idUsuario");
        String nomeCompleto = request.getParameter("nomeCompleto");
        String email = request.getParameter("email");
        String senha = request.getParameter("senhaHash"); // Campo de senha
        TipoUsuario tipoUsuario = TipoUsuario.valueOf(request.getParameter("tipoUsuario"));
        Integer permissaoId = Integer.parseInt(request.getParameter("permissaoId"));
        boolean ativo = "on".equals(request.getParameter("ativo")); // Checkbox
        String matriculaRA = request.getParameter("matriculaRA");
        String telefone = request.getParameter("telefone");
        String periodo = request.getParameter("periodoAtualAluno");

        // --- CORREÇÃO: Lógica envolvida em um bloco try-catch ---
        Usuario usuario = null; // Declarado fora para ser acessível no catch
        try {
            // Busca a entidade de Permissão completa a partir do ID recebido.
            Permissao permissao = permissaoDAO.findById(permissaoId).orElse(null);

            // Verifica se é uma edição (ID presente) ou criação (ID ausente).
            if (idStr != null && !idStr.isEmpty()) {
                Integer id = Integer.parseInt(idStr);
                usuario = usuarioDAO.findById(id).orElseThrow(() -> new ServletException("Usuário não encontrado"));
            } else {
                usuario = new Usuario();
            }

            // Popula o objeto Usuario com os dados do formulário.
            usuario.setNomeCompleto(nomeCompleto);
            usuario.setEmail(email);

            // A senha só deve ser atualizada se um novo valor for fornecido.
            if (senha != null && !senha.isEmpty()) {
                // NOTA DE SEGURANÇA: Em um sistema de produção, a senha NUNCA deve ser salva como texto puro.
                // É essencial usar um algoritmo de hash forte, como BCrypt.
                // Exemplo: usuario.setSenhaHash(BCrypt.hashpw(senha, BCrypt.gensalt()));
                usuario.setSenhaHash(senha); // Esta linha é insegura e serve apenas para desenvolvimento.
            }

            usuario.setTipoUsuario(tipoUsuario);
            usuario.setPermissao(permissao); // Associa o objeto Permissão completo.
            usuario.setAtivo(ativo);
            usuario.setMatriculaRA(matriculaRA);
            usuario.setTelefone(telefone);
            usuario.setPeriodoAtualAluno(periodo);

            usuarioDAO.save(usuario);
            // Redireciona para a lista de usuários após o sucesso.
            response.sendRedirect(request.getContextPath() + "/admin/usuarios?success=1");

        } catch (ConstraintViolationException e) {
            // Em caso de erro de validação, monta uma mensagem amigável.
            String errorMessage = "Erro de validação: " + e.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(". "));

            // Define os atributos necessários para reenviar o usuário ao formulário.
            request.setAttribute("error", errorMessage); // Mensagem de erro para o JSP
            request.setAttribute("usuario", usuario); // Devolve o objeto com os dados preenchidos para não perdê-los
            request.setAttribute("action", (idStr != null && !idStr.isEmpty()) ? "edit" : "new");
            request.setAttribute("tiposUsuario", TipoUsuario.values());
            request.setAttribute("listPermissoes", permissaoDAO.findAll());

            // Encaminha de volta para o formulário, em vez de quebrar a aplicação.
            request.getRequestDispatcher("/WEB-INF/views/admin/usuarios/form.jsp").forward(request, response);

        } catch (Exception e) {
            // Captura outras exceções genéricas
            throw new ServletException("Ocorreu um erro ao salvar o usuário.", e);
        }
    }

    /**
     * Busca a lista de usuários e encaminha para o JSP de listagem.
     */
    private void listUsuarios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Usuario> listUsuarios = usuarioDAO.findAll();
        request.setAttribute("listUsuarios", listUsuarios);
        request.getRequestDispatcher("/WEB-INF/views/admin/usuarios/list.jsp").forward(request, response);
    }

    /**
     * Prepara os dados necessários para o formulário de criação (listas de
     * permissões e tipos) e encaminha para o JSP do formulário.
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("action", "new");
        // Envia a lista de valores do Enum 'TipoUsuario' para o JSP popular um select.
        request.setAttribute("tiposUsuario", TipoUsuario.values());
        // Envia a lista de todas as permissões para o JSP popular outro select.
        request.setAttribute("listPermissoes", permissaoDAO.findAll());
        request.getRequestDispatcher("/WEB-INF/views/admin/usuarios/form.jsp").forward(request, response);
    }

    /**
     * Busca um usuário existente e os dados para os selects do formulário, e
     * encaminha para o JSP de edição.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        Usuario usuarioExistente = usuarioDAO.findById(id).orElseThrow(() -> new ServletException("Usuário não encontrado"));
        // Envia o usuário existente para o formulário ser preenchido.
        request.setAttribute("usuario", usuarioExistente);
        request.setAttribute("action", "edit");
        // Também envia as listas para os selects, como no formulário de criação.
        request.setAttribute("tiposUsuario", TipoUsuario.values());
        request.setAttribute("listPermissoes", permissaoDAO.findAll());
        request.getRequestDispatcher("/WEB-INF/views/admin/usuarios/form.jsp").forward(request, response);
    }

    /**
     * Deleta um usuário pelo ID e redireciona para a página de listagem.
     */
    private void deleteUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        usuarioDAO.deleteById(id);
        response.sendRedirect(request.getContextPath() + "/admin/usuarios?deleted=1");
    }
}
