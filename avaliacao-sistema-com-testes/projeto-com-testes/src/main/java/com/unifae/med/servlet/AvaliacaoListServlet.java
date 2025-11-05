package com.unifae.med.servlet;

import com.unifae.med.dao.AvaliacaoPreenchidaDAO;
import com.unifae.med.dao.UsuarioDAO;
import com.unifae.med.dao.QuestionarioDAO;
import com.unifae.med.entity.AvaliacaoPreenchida;
import com.unifae.med.entity.Usuario;
import com.unifae.med.entity.Questionario;
import com.unifae.med.entity.TipoUsuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * AVALIACAOLISTSERVLET - CONTROLADOR PARA LISTAGEM DE AVALIAÇÕES
 * ===============================================================
 * 
 * Servlet responsável por exibir lista de avaliações preenchidas no sistema.
 * Implementa funcionalidades de listagem com filtros dinâmicos para facilitar
 * a busca e visualização de avaliações específicas.
 * 
 * RESPONSABILIDADES:
 * - Listar todas as avaliações ou aplicar filtros
 * - Fornecer dados para filtros (alunos, professores, questionários)
 * - Tratar parâmetros de busca da URL
 * - Preparar dados para exibição na JSP
 * - Gerenciar erros e exceções
 * 
 * PADRÃO MVC:
 * - Model: Entidades (AvaliacaoPreenchida, Usuario, Questionario)
 * - View: JSP (/WEB-INF/views/avaliacoes/list.jsp)
 * - Controller: Esta classe (AvaliacaoListServlet)
 * 
 * FILTROS SUPORTADOS:
 * - Por aluno avaliado (alunoId)
 * - Por questionário (questionarioId)
 * - Por avaliador (avaliadorId)
 * - Combinação aluno + questionário
 * - Sem filtros (todas as avaliações)
 * 
 * RELACIONAMENTO COM OUTROS ARQUIVOS:
 * - DAOs: AvaliacaoPreenchidaDAO, UsuarioDAO, QuestionarioDAO
 * - Entidades: AvaliacaoPreenchida, Usuario, Questionario, TipoUsuario
 * - JSP: /WEB-INF/views/avaliacoes/list.jsp (view principal)
 * - JSP: /WEB-INF/views/error.jsp (view de erro)
 * - web.xml: Configuração de mapeamento de servlets
 * 
 * URL DE ACESSO:
 * - GET /avaliacoes - Lista todas as avaliações
 * - GET /avaliacoes?alunoId=1 - Filtra por aluno
 * - GET /avaliacoes?questionarioId=2 - Filtra por questionário
 * - GET /avaliacoes?avaliadorId=3 - Filtra por avaliador
 * - GET /avaliacoes?alunoId=1&questionarioId=2 - Filtros combinados
 * 
 * @author Sistema de Avaliação UNIFAE
 * @version 1.0
 */
@WebServlet("/avaliacoes")  // Mapeia URL /avaliacoes para este servlet
public class AvaliacaoListServlet extends HttpServlet {

    /**
     * DAOS PARA ACESSO A DADOS
     * ========================
     * Instâncias dos DAOs necessários para buscar dados do banco.
     * Inicializados no método init() para reutilização.
     */
    private AvaliacaoPreenchidaDAO avaliacaoDAO;    // Acesso a avaliações
    private UsuarioDAO usuarioDAO;                  // Acesso a usuários
    private QuestionarioDAO questionarioDAO;       // Acesso a questionários

    /**
     * INICIALIZAÇÃO DO SERVLET
     * ========================
     * Método chamado uma vez quando servlet é carregado pelo container.
     * Inicializa DAOs que serão reutilizados em todas as requisições.
     * 
     * CICLO DE VIDA:
     * 1. Container carrega servlet
     * 2. Chama init() uma vez
     * 3. Servlet fica pronto para atender requisições
     * 
     * @throws ServletException se erro na inicialização
     */
    @Override
    public void init() throws ServletException {
        super.init();
        
        // Inicializa DAOs para acesso ao banco de dados
        this.avaliacaoDAO = new AvaliacaoPreenchidaDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.questionarioDAO = new QuestionarioDAO();
    }

    /**
     * PROCESSAR REQUISIÇÕES GET
     * =========================
     * Método principal que processa requisições HTTP GET.
     * Implementa lógica de listagem com filtros dinâmicos.
     * 
     * FLUXO DE EXECUÇÃO:
     * 1. Extrai parâmetros de filtro da URL
     * 2. Aplica filtros conforme parâmetros recebidos
     * 3. Busca dados auxiliares para interface
     * 4. Prepara atributos para JSP
     * 5. Encaminha para view apropriada
     * 6. Trata erros se necessário
     * 
     * PARÂMETROS SUPORTADOS:
     * - alunoId: ID do aluno para filtrar avaliações
     * - questionarioId: ID do questionário para filtrar
     * - avaliadorId: ID do avaliador para filtrar
     * 
     * @param request Requisição HTTP com parâmetros
     * @param response Resposta HTTP para o cliente
     * @throws ServletException se erro no servlet
     * @throws IOException se erro de I/O
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // =====================================
            // EXTRAÇÃO DE PARÂMETROS DE FILTRO
            // =====================================
            // Obtém parâmetros da URL para aplicar filtros
            
            String alunoId = request.getParameter("alunoId");           // Filtro por aluno
            String questionarioId = request.getParameter("questionarioId"); // Filtro por questionário
            String avaliadorId = request.getParameter("avaliadorId");   // Filtro por avaliador

            List<AvaliacaoPreenchida> avaliacoes;

            // =====================================
            // APLICAÇÃO DE FILTROS DINÂMICOS
            // =====================================
            // Lógica condicional para aplicar filtros conforme parâmetros
            
            if (alunoId != null && !alunoId.isEmpty()) {
                // FILTRO POR ALUNO (com possível combinação com questionário)
                
                // Busca aluno pelo ID
                Usuario aluno = usuarioDAO.findById(Integer.parseInt(alunoId))
                        .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

                if (questionarioId != null && !questionarioId.isEmpty()) {
                    // FILTRO COMBINADO: Aluno + Questionário
                    Questionario questionario = questionarioDAO.findById(Integer.parseInt(questionarioId))
                            .orElseThrow(() -> new RuntimeException("Questionário não encontrado"));
                    
                    // Busca avaliações específicas do aluno e questionário
                    avaliacoes = avaliacaoDAO.findByAlunoAvaliadoAndQuestionario(aluno, questionario);
                } else {
                    // FILTRO SIMPLES: Apenas por aluno
                    avaliacoes = avaliacaoDAO.findByAlunoAvaliado(aluno);
                }
                
            } else if (avaliadorId != null && !avaliadorId.isEmpty()) {
                // FILTRO POR AVALIADOR
                
                Usuario avaliador = usuarioDAO.findById(Integer.parseInt(avaliadorId))
                        .orElseThrow(() -> new RuntimeException("Avaliador não encontrado"));
                
                // Busca avaliações feitas por este avaliador
                avaliacoes = avaliacaoDAO.findByAvaliador(avaliador);
                
            } else if (questionarioId != null && !questionarioId.isEmpty()) {
                // FILTRO POR QUESTIONÁRIO
                
                Questionario questionario = questionarioDAO.findById(Integer.parseInt(questionarioId))
                        .orElseThrow(() -> new RuntimeException("Questionário não encontrado"));
                
                // Busca avaliações deste questionário
                avaliacoes = avaliacaoDAO.findByQuestionario(questionario);
                
            } else {
                // SEM FILTROS: Lista todas as avaliações
                avaliacoes = avaliacaoDAO.findAll();
            }

            // =====================================
            // BUSCA DE DADOS AUXILIARES
            // =====================================
            // Dados necessários para montar filtros na interface
            
            // Lista de alunos para filtro dropdown
            List<Usuario> alunos = usuarioDAO.findByTipoUsuario(TipoUsuario.ESTUDANTE);
            
            // Lista de professores para filtro dropdown
            List<Usuario> professores = usuarioDAO.findByTipoUsuario(TipoUsuario.PROFESSOR);
            
            // Lista de questionários para filtro dropdown
            List<Questionario> questionarios = questionarioDAO.findAll();

            // =====================================
            // PREPARAÇÃO DE ATRIBUTOS PARA JSP
            // =====================================
            // Define atributos que serão acessíveis na JSP
            
            // Dados principais
            request.setAttribute("avaliacoes", avaliacoes);         // Lista de avaliações
            request.setAttribute("alunos", alunos);                 // Lista para filtro
            request.setAttribute("professores", professores);       // Lista para filtro
            request.setAttribute("questionarios", questionarios);   // Lista para filtro
            
            // Valores selecionados nos filtros (para manter estado)
            request.setAttribute("alunoIdSelecionado", alunoId);
            request.setAttribute("questionarioIdSelecionado", questionarioId);
            request.setAttribute("avaliadorIdSelecionado", avaliadorId);

            // =====================================
            // ENCAMINHAMENTO PARA VIEW
            // =====================================
            // Encaminha requisição para JSP que renderizará a interface
            request.getRequestDispatcher("/WEB-INF/views/avaliacoes/list.jsp").forward(request, response);

        } catch (Exception e) {
            // =====================================
            // TRATAMENTO DE ERROS
            // =====================================
            // Captura qualquer exceção e encaminha para página de erro
            
            // Log do erro para debug
            e.printStackTrace();
            
            // Define mensagem de erro para exibição
            request.setAttribute("erro", "Erro ao carregar avaliações: " + e.getMessage());
            
            // Encaminha para página de erro
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}

