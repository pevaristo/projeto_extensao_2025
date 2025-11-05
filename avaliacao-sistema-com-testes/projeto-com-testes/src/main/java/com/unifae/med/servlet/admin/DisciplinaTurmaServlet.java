package com.unifae.med.servlet.admin;

import com.unifae.med.dao.*;
import com.unifae.med.entity.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/admin/gerenciar-disciplinas-turma")
public class DisciplinaTurmaServlet extends HttpServlet {

    private TurmaDAO turmaDAO;
    private DisciplinaDAO disciplinaDAO;
    private UsuarioDAO usuarioDAO;
    private DisciplinaTurmaDAO disciplinaTurmaDAO;

    @Override
    public void init() {
        this.turmaDAO = new TurmaDAO();
        this.disciplinaDAO = new DisciplinaDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.disciplinaTurmaDAO = new DisciplinaTurmaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") != null ? request.getParameter("action") : "list";
        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "delete":
                    deleteVinculo(request, response);
                    break;
                default:
                    listDisciplinas(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Erro ao processar a requisição de disciplinas da turma.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Integer idTurma = Integer.parseInt(request.getParameter("idTurma"));
            Integer idDisciplina = Integer.parseInt(request.getParameter("idDisciplina"));
            Integer idProfessor = Integer.parseInt(request.getParameter("idProfessor"));

            Turma turma = turmaDAO.findById(idTurma).orElseThrow(() -> new ServletException("Turma não encontrada."));
            Disciplina disciplina = disciplinaDAO.findById(idDisciplina).orElseThrow(() -> new ServletException("Disciplina não encontrada."));
            Usuario professor = usuarioDAO.findById(idProfessor).orElseThrow(() -> new ServletException("Professor não encontrado."));

            DisciplinaTurma novoVinculo = new DisciplinaTurma();
            novoVinculo.setTurma(turma);
            novoVinculo.setDisciplina(disciplina);
            novoVinculo.setProfessor(professor);

            disciplinaTurmaDAO.save(novoVinculo);
            response.sendRedirect(request.getContextPath() + "/admin/gerenciar-disciplinas-turma?idTurma=" + idTurma + "&success=1");
        } catch (Exception e) {
            throw new ServletException("Erro ao salvar vínculo da disciplina.", e);
        }
    }

    private void listDisciplinas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idTurmaParam = request.getParameter("idTurma");

        System.out.println("--- EXECUTANDO A VERSÃO CORRIGIDA DO SERVLET ---");
        
        // Verifica se o parâmetro idTurma existe e não está vazio
        if (idTurmaParam == null || idTurmaParam.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "O ID da turma é obrigatório para visualizar as disciplinas.");
            return;
        }

        try {
            Integer idTurma = Integer.parseInt(idTurmaParam);
            Turma turma = turmaDAO.findById(idTurma).orElseThrow(() -> new ServletException("Turma não encontrada"));
            List<DisciplinaTurma> vinculos = disciplinaTurmaDAO.findByTurmaId(idTurma);

            request.setAttribute("turma", turma);
            request.setAttribute("vinculos", vinculos);
            request.getRequestDispatcher("/WEB-INF/views/admin/disciplinas_turmas/list.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            // Trata o caso onde o idTurma não é um número válido
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "O ID da turma fornecido é inválido.");
        }
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idTurmaParam = request.getParameter("idTurma");

        // Verifica se o parâmetro idTurma existe e não está vazio
        if (idTurmaParam == null || idTurmaParam.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "O ID da turma é obrigatório para adicionar uma nova disciplina.");
            return;
        }

        try {
            Integer idTurma = Integer.parseInt(idTurmaParam);
            Turma turma = turmaDAO.findById(idTurma).orElseThrow(() -> new ServletException("Turma não encontrada"));

            List<DisciplinaTurma> vinculosAtuais = disciplinaTurmaDAO.findByTurmaId(idTurma);
            List<Integer> idsDisciplinasVinculadas = vinculosAtuais.stream()
                    .map(vinculo -> vinculo.getDisciplina().getIdDisciplina())
                    .collect(Collectors.toList());

            List<Disciplina> disciplinasDisponiveis = disciplinaDAO.findAtivas().stream()
                    .filter(d -> !idsDisciplinasVinculadas.contains(d.getIdDisciplina()))
                    .collect(Collectors.toList());

            List<Usuario> professores = usuarioDAO.findProfessoresAtivos();

            request.setAttribute("turma", turma);
            request.setAttribute("disciplinasDisponiveis", disciplinasDisponiveis);
            request.setAttribute("professores", professores);
            request.getRequestDispatcher("/WEB-INF/views/admin/disciplinas_turmas/form.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            // Trata o caso onde o idTurma não é um número válido
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "O ID da turma fornecido é inválido.");
        }
    }

    private void deleteVinculo(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Integer idVinculo = Integer.parseInt(request.getParameter("idVinculo"));
        // Para o redirect, precisamos saber de qual turma viemos.
        DisciplinaTurma vinculo = disciplinaTurmaDAO.findById(idVinculo).orElseThrow(() -> new ServletException("Vínculo não encontrado"));
        Integer idTurma = vinculo.getTurma().getIdTurma();

        disciplinaTurmaDAO.deleteById(idVinculo);
        response.sendRedirect(request.getContextPath() + "/admin/gerenciar-disciplinas-turma?idTurma=" + idTurma + "&deleted=1");
    }
}
