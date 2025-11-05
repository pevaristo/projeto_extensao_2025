package com.unifae.med.servlet.admin;

import com.unifae.med.dao.NotaDAO;
import com.unifae.med.dao.UsuarioDAO;
import com.unifae.med.dao.DisciplinaDAO;
import com.unifae.med.dao.TurmaDAO; // << CORREÇÃO: Importação adicionada
import com.unifae.med.entity.Nota;
import com.unifae.med.entity.Usuario;
import com.unifae.med.entity.Disciplina;
import com.unifae.med.entity.Turma; // << CORREÇÃO: Importação adicionada
import com.unifae.med.entity.TipoAvaliacao;
import com.unifae.med.entity.TipoUsuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * NOTA SERVLET - CONTROLADOR PARA GERENCIAMENTO DE NOTAS (CORRIGIDO FINAL)
 * ========================================================================
 *
 * CORREÇÃO 1: Substituído TipoUsuario.ALUNO por TipoUsuario.ESTUDANTE.
 * CORREÇÃO 2: Adicionado carregamento de Turmas e Professores para o formulário.
 * CORREÇÃO 3: Adicionado processamento dos campos opcionais no doPost.
 *
 * @version 2.0 - Corrigido erro de compilação e erros lógicos
 */
@WebServlet("/admin/notas")
public class NotaServlet extends HttpServlet {

    private NotaDAO notaDAO;
    private UsuarioDAO usuarioDAO;
    private DisciplinaDAO disciplinaDAO;
    private TurmaDAO turmaDAO; // << CORREÇÃO: DAO de Turma adicionado

    @Override
    public void init() {
        this.notaDAO = new NotaDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.disciplinaDAO = new DisciplinaDAO();
        this.turmaDAO = new TurmaDAO(); // << CORREÇÃO: DAO de Turma instanciado
    }

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
                    deleteNota(request, response);
                    break;
                case "toggle":
                    toggleAtivaNota(request, response);
                    break;
                default:
                    listNotas(request, response);
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
            String idStr = request.getParameter("idNota");
            String alunoIdStr = request.getParameter("alunoId");
            String disciplinaIdStr = request.getParameter("disciplinaId");
            String valorNotaStr = request.getParameter("valorNota");
            String pesoNotaStr = request.getParameter("pesoNota");
            String tipoAvaliacaoStr = request.getParameter("tipoAvaliacao");
            String dataAvaliacaoStr = request.getParameter("dataAvaliacao");
            String observacoes = request.getParameter("observacoes");
            boolean ativo = "on".equals(request.getParameter("ativo"));

            // << CORREÇÃO: Leitura dos campos opcionais que estavam faltando
            String turmaIdStr = request.getParameter("turmaId");
            String professorIdStr = request.getParameter("professorId");
            String descricaoAvaliacao = request.getParameter("descricaoAvaliacao");


            // Validações básicas
            if (alunoIdStr == null || alunoIdStr.trim().isEmpty()) {
                throw new ServletException("Aluno é obrigatório");
            }
            if (disciplinaIdStr == null || disciplinaIdStr.trim().isEmpty()) {
                throw new ServletException("Disciplina é obrigatória");
            }
            if (valorNotaStr == null || valorNotaStr.trim().isEmpty()) {
                throw new ServletException("Valor da nota é obrigatório");
            }

            // Conversões
            Integer alunoId = Integer.parseInt(alunoIdStr);
            Integer disciplinaId = Integer.parseInt(disciplinaIdStr);
            BigDecimal valorNota = new BigDecimal(valorNotaStr);
            BigDecimal pesoNota = pesoNotaStr != null && !pesoNotaStr.trim().isEmpty() ?
                                 new BigDecimal(pesoNotaStr) : BigDecimal.ONE;
            TipoAvaliacao tipoAvaliacao = TipoAvaliacao.valueOf(tipoAvaliacaoStr);
            LocalDate dataAvaliacao = LocalDate.parse(dataAvaliacaoStr);

            // Buscar entidades relacionadas
            Usuario aluno = usuarioDAO.findById(alunoId)
                .orElseThrow(() -> new ServletException("Aluno não encontrado"));
            Disciplina disciplina = disciplinaDAO.findById(disciplinaId)
                .orElseThrow(() -> new ServletException("Disciplina não encontrada"));

            // Determina se é criação ou edição
            Nota nota;
            if (idStr != null && !idStr.isEmpty()) {
                Integer id = Integer.parseInt(idStr);
                nota = notaDAO.findById(id)
                    .orElseThrow(() -> new ServletException("Nota não encontrada"));
            } else {
                nota = new Nota();
            }

            // Define propriedades
            nota.setAluno(aluno);
            nota.setDisciplina(disciplina);
            nota.setValorNota(valorNota);
            nota.setPesoNota(pesoNota);
            nota.setTipoAvaliacao(tipoAvaliacao);
            nota.setDataAvaliacao(dataAvaliacao);
            nota.setObservacoes(observacoes != null ? observacoes.trim() : null);
            nota.setAtivo(ativo);
            nota.setDescricaoAvaliacao(descricaoAvaliacao != null ? descricaoAvaliacao.trim() : null); // << CORREÇÃO

            // << CORREÇÃO: Lógica para associar Turma e Professor (opcionais)
            if (turmaIdStr != null && !turmaIdStr.trim().isEmpty()) {
                Integer turmaId = Integer.parseInt(turmaIdStr);
                Turma turma = turmaDAO.findById(turmaId).orElse(null);
                nota.setTurma(turma);
            } else {
                nota.setTurma(null);
            }

            if (professorIdStr != null && !professorIdStr.trim().isEmpty()) {
                Integer professorId = Integer.parseInt(professorIdStr);
                Usuario professor = usuarioDAO.findById(professorId).orElse(null);
                nota.setProfessor(professor);
            } else {
                nota.setProfessor(null);
            }

            if (idStr == null || idStr.isEmpty()) {
                nota.setDataLancamento(LocalDate.now());
            }

            // Salva no banco
            notaDAO.save(nota);

            // Redireciona para listagem
            response.sendRedirect(request.getContextPath() + "/admin/notas");

        } catch (Exception e) {
            throw new ServletException("Erro ao salvar nota: " + e.getMessage(), e);
        }
    }

    private void listNotas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ... (lógica de filtro inalterada) ...
        String alunoIdStr = request.getParameter("alunoId");
        String disciplinaIdStr = request.getParameter("disciplinaId");
        String tipoAvaliacaoStr = request.getParameter("tipoAvaliacao");

        List<Nota> listNotas;
        
        if (alunoIdStr != null && !alunoIdStr.isEmpty()) {
            Integer alunoId = Integer.parseInt(alunoIdStr);
            Usuario aluno = usuarioDAO.findById(alunoId).orElse(null);
            if (aluno != null) {
                if (disciplinaIdStr != null && !disciplinaIdStr.isEmpty()) {
                    Integer disciplinaId = Integer.parseInt(disciplinaIdStr);
                    Disciplina disciplina = disciplinaDAO.findById(disciplinaId).orElse(null);
                    if (disciplina != null) {
                        listNotas = notaDAO.findByAlunoAndDisciplina(aluno, disciplina);
                    } else {
                        listNotas = notaDAO.findByAluno(aluno);
                    }
                } else {
                    listNotas = notaDAO.findByAluno(aluno);
                }
            } else {
                listNotas = notaDAO.findAtivas();
            }
        } else if (disciplinaIdStr != null && !disciplinaIdStr.isEmpty()) {
            Integer disciplinaId = Integer.parseInt(disciplinaIdStr);
            Disciplina disciplina = disciplinaDAO.findById(disciplinaId).orElse(null);
            if (disciplina != null) {
                listNotas = notaDAO.findByDisciplina(disciplina);
            } else {
                listNotas = notaDAO.findAtivas();
            }
        } else if (tipoAvaliacaoStr != null && !tipoAvaliacaoStr.isEmpty()) {
            TipoAvaliacao tipoAvaliacao = TipoAvaliacao.valueOf(tipoAvaliacaoStr);
            listNotas = notaDAO.findByTipoAvaliacao(tipoAvaliacao);
        } else {
            listNotas = notaDAO.findAtivas();
        }

        // << CORREÇÃO DE COMPILAÇÃO: Trocado ALUNO por ESTUDANTE
        List<Usuario> listAlunos = usuarioDAO.findByTipoUsuario(TipoUsuario.ESTUDANTE);
        List<Disciplina> listDisciplinas = disciplinaDAO.findAtivas();
        TipoAvaliacao[] tiposAvaliacao = TipoAvaliacao.values();

        request.setAttribute("listNotas", listNotas);
        request.setAttribute("listAlunos", listAlunos);
        request.setAttribute("listDisciplinas", listDisciplinas);
        request.setAttribute("tiposAvaliacao", tiposAvaliacao);

        request.getRequestDispatcher("/WEB-INF/views/admin/notas/list.jsp")
               .forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // << CORREÇÃO DE COMPILAÇÃO: Trocado ALUNO por ESTUDANTE
        List<Usuario> listAlunos = usuarioDAO.findByTipoUsuario(TipoUsuario.ESTUDANTE);
        List<Disciplina> listDisciplinas = disciplinaDAO.findAtivas();
        TipoAvaliacao[] tiposAvaliacao = TipoAvaliacao.values();
        
        // << CORREÇÃO: Adicionado carregamento de dados para os selects opcionais
        List<Usuario> listProfessores = usuarioDAO.findByTipoUsuario(TipoUsuario.PROFESSOR);
        List<Turma> listTurmas = turmaDAO.findAtivas(); // Assumindo que turmaDAO tem o método findAtivas()

        request.setAttribute("listAlunos", listAlunos);
        request.setAttribute("listDisciplinas", listDisciplinas);
        request.setAttribute("tiposAvaliacao", tiposAvaliacao);
        request.setAttribute("listProfessores", listProfessores); // << CORREÇÃO
        request.setAttribute("listTurmas", listTurmas);           // << CORREÇÃO
        request.setAttribute("action", "new");

        request.getRequestDispatcher("/WEB-INF/views/admin/notas/form.jsp")
               .forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer id = Integer.parseInt(request.getParameter("id"));
        Nota notaExistente = notaDAO.findById(id)
            .orElseThrow(() -> new ServletException("Nota não encontrada"));

        // << CORREÇÃO DE COMPILAÇÃO: Trocado ALUNO por ESTUDANTE
        List<Usuario> listAlunos = usuarioDAO.findByTipoUsuario(TipoUsuario.ESTUDANTE);
        List<Disciplina> listDisciplinas = disciplinaDAO.findAtivas();
        TipoAvaliacao[] tiposAvaliacao = TipoAvaliacao.values();
        
        // << CORREÇÃO: Adicionado carregamento de dados para os selects opcionais
        List<Usuario> listProfessores = usuarioDAO.findByTipoUsuario(TipoUsuario.PROFESSOR);
        List<Turma> listTurmas = turmaDAO.findAtivas(); // Assumindo que turmaDAO tem o método findAtivas()

        request.setAttribute("nota", notaExistente);
        request.setAttribute("listAlunos", listAlunos);
        request.setAttribute("listDisciplinas", listDisciplinas);
        request.setAttribute("tiposAvaliacao", tiposAvaliacao);
        request.setAttribute("listProfessores", listProfessores); // << CORREÇÃO
        request.setAttribute("listTurmas", listTurmas);           // << CORREÇÃO
        request.setAttribute("action", "edit");

        request.getRequestDispatcher("/WEB-INF/views/admin/notas/form.jsp")
               .forward(request, response);
    }

    private void deleteNota(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        Integer id = Integer.parseInt(request.getParameter("id"));
        notaDAO.deleteById(id);
        response.sendRedirect(request.getContextPath() + "/admin/notas");
    }

    private void toggleAtivaNota(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        Integer id = Integer.parseInt(request.getParameter("id"));
        Nota nota = notaDAO.findById(id)
            .orElseThrow(() -> new ServletException("Nota não encontrada"));

        nota.setAtivo(!nota.getAtivo());
        notaDAO.save(nota);

        response.sendRedirect(request.getContextPath() + "/admin/notas");
    }
}