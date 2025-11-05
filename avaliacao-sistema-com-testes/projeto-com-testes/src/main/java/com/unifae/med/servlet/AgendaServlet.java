/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `AgendaServlet`, é um Servlet Java que atua como o Controller central para
 * toda a funcionalidade de agenda/calendário da aplicação, seguindo o padrão MVC. Mapeado
 * para a URL `/agenda`, ele gerencia um conjunto complexo de interações do usuário.
 *
 * Diferente de um servlet de CRUD simples, suas responsabilidades incluem:
 * 1.  **Múltiplas Visualizações:** Ele não apenas lista eventos em uma tabela (`listEventos`),
 * mas também renderiza uma visualização de calendário mensal (`showCalendar`), que
 * envolve uma lógica mais elaborada de busca e agrupamento de dados por dia.
 *
 * 2.  **Processamento Avançado de Formulários:** O método `doPost` é o coração da classe,
 * realizando uma sequência de tarefas: coleta de dados, validação, conversão de tipos,
 * busca de múltiplas entidades relacionadas no banco de dados (Local, Disciplina, etc.)
 * e a montagem final do objeto `EventoAgenda`.
 *
 * 3.  **Validação de Regras de Negócio:** Contém lógica de negócio específica, como a
 * verificação de conflitos de horário (`hasConflito`) antes de salvar um evento,
 * garantindo a integridade dos agendamentos.
 *
 * 4.  **Filtragem de Dados:** A tela de listagem suporta múltiplos filtros (data, tipo,
 * status, etc.), cuja lógica é orquestrada por este servlet antes de consultar o DAO.
 *
 * 5.  **Reutilização de Código:** Utiliza métodos auxiliares como `prepareFormData` e
 * `parseDateTime` para evitar duplicação de código e centralizar lógicas comuns.
 * =================================================================================================
 */
package com.unifae.med.servlet;

// DAOs para interagir com o banco de dados.
import com.unifae.med.dao.EventoAgendaDAO;
import com.unifae.med.dao.LocalEventoDAO;
import com.unifae.med.dao.DisciplinaDAO;
import com.unifae.med.dao.TurmaDAO;
import com.unifae.med.dao.UsuarioDAO;
// Entidades que modelam os dados.
import com.unifae.med.entity.*;
// Classes do Jakarta Servlet para criar a servlet.
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Classes Java para manipulação de I/O, datas e coleções.
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/agenda")
public class AgendaServlet extends HttpServlet {

    // DAOs: Objetos responsáveis pela comunicação com o banco de dados.
    private EventoAgendaDAO eventoAgendaDAO;
    private LocalEventoDAO localEventoDAO;
    private DisciplinaDAO disciplinaDAO;
    private TurmaDAO turmaDAO;
    private UsuarioDAO usuarioDAO;

    /**
     * Método de inicialização da Servlet. Executado uma vez na carga da classe.
     * Instancia todos os DAOs necessários para as operações da agenda.
     */
    @Override
    public void init() {
        this.eventoAgendaDAO = new EventoAgendaDAO();
        this.localEventoDAO = new LocalEventoDAO();
        this.disciplinaDAO = new DisciplinaDAO();
        this.turmaDAO = new TurmaDAO();
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Trata requisições HTTP GET. Atua como um roteador, delegando para métodos
     * específicos com base no parâmetro 'action' da URL.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "list"; // Ação padrão é listar os eventos.
        }

        try {
            switch (action) {
                case "calendar":
                    showCalendar(request, response);
                    break;
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteEvento(request, response);
                    break;
                case "changeStatus":
                    changeStatus(request, response);
                    break;
                default:
                    listEventos(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Erro ao processar requisição: " + e.getMessage(), e);
        }
    }

    /**
     * Trata requisições HTTP POST, tipicamente da submissão do formulário de
     * criação ou edição de eventos.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 1. Coleta os dados brutos (String) do formulário.
            String idStr = request.getParameter("idEvento");
            // ... (coleta dos outros parâmetros) ...
            String titulo = request.getParameter("titulo");
            String descricao = request.getParameter("descricao");
            String dataInicioStr = request.getParameter("dataInicio");
            String dataFimStr = request.getParameter("dataFim");
            String localEventoIdStr = request.getParameter("localEventoId");
            String disciplinaIdStr = request.getParameter("disciplinaId");
            String turmaIdStr = request.getParameter("turmaId");
            String responsavelIdStr = request.getParameter("responsavelId");
            String tipoEventoStr = request.getParameter("tipoEvento");
            String statusEventoStr = request.getParameter("statusEvento");

            // 2. Valida campos obrigatórios.
            if (titulo == null || titulo.trim().isEmpty() || dataInicioStr == null || dataInicioStr.trim().isEmpty()) {
                throw new ServletException("Título e Data de Início são obrigatórios.");
            }

            // 3. Converte Strings para os tipos de dados corretos e valida a lógica de datas.
            LocalDateTime dataInicio = parseDateTime(dataInicioStr);
            LocalDateTime dataFim = (dataFimStr != null && !dataFimStr.trim().isEmpty()) ? parseDateTime(dataFimStr) : null;
            if (dataFim != null && dataFim.isBefore(dataInicio)) {
                throw new ServletException("Data de fim deve ser posterior à data de início.");
            }

            // 4. Busca as entidades relacionadas completas a partir de seus IDs.
            LocalEvento localEvento = (localEventoIdStr != null && !localEventoIdStr.isEmpty()) ? localEventoDAO.findById(Integer.valueOf(localEventoIdStr)).orElse(null) : null;
            Disciplina disciplina = (disciplinaIdStr != null && !disciplinaIdStr.isEmpty()) ? disciplinaDAO.findById(Integer.valueOf(disciplinaIdStr)).orElse(null) : null;
            Turma turma = (turmaIdStr != null && !turmaIdStr.isEmpty()) ? turmaDAO.findById(Integer.valueOf(turmaIdStr)).orElse(null) : null;
            Usuario responsavel = (responsavelIdStr != null && !responsavelIdStr.isEmpty()) ? usuarioDAO.findById(Integer.valueOf(responsavelIdStr)).orElse(null) : null;
            TipoEvento tipoEvento = TipoEvento.valueOf(tipoEventoStr);
            StatusEvento statusEvento = StatusEvento.valueOf(statusEventoStr);

            // 5. Determina se é uma operação de criação ou edição.
            EventoAgenda evento = (idStr != null && !idStr.trim().isEmpty())
                    ? eventoAgendaDAO.findById(Integer.valueOf(idStr)).orElseThrow(() -> new ServletException("Evento não encontrado"))
                    : new EventoAgenda();

            // 6. Aplica a regra de negócio de conflito de horário.
            if (localEvento != null && dataFim != null) {
                if (eventoAgendaDAO.hasConflito(localEvento, dataInicio, dataFim, evento.getIdEvento())) {
                    request.setAttribute("erro", "Conflito de horário: já existe evento agendado para este local no período especificado.");
                    showFormWithData(request, response, evento); // Retorna ao formulário com erro.
                    return;
                }
            }

            // 7. Popula a entidade com todos os dados processados.
            evento.setTitulo(titulo);
            evento.setDescricao(descricao);
            evento.setDataInicio(dataInicio);
            evento.setDataFim(dataFim);
            evento.setLocalEvento(localEvento);
            evento.setDisciplina(disciplina);
            evento.setTurma(turma);
            evento.setResponsavel(responsavel);
            evento.setTipoEvento(tipoEvento);
            evento.setStatusEvento(statusEvento);

            // 8. Salva a entidade no banco de dados.
            eventoAgendaDAO.save(evento);

            // 9. Redireciona o navegador para a página principal da agenda.
            response.sendRedirect(request.getContextPath() + "/agenda?success=1");

        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao salvar evento: " + e.getMessage());
            showNewForm(request, response); // Em caso de erro, volta para o formulário.
        }
    }

    /**
     * Busca e exibe a lista de eventos, com suporte a múltiplos filtros.
     */
    private void listEventos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String dataStr = request.getParameter("data");
        // ... (coleta dos outros parâmetros de filtro)
        String tipoEventoStr = request.getParameter("tipoEvento");
        String statusEventoStr = request.getParameter("statusEvento");
        String responsavelIdStr = request.getParameter("responsavelId");
        String disciplinaIdStr = request.getParameter("disciplinaId");
        String turmaIdStr = request.getParameter("turmaId");

        // Converte e prepara os filtros para a consulta no DAO.
        LocalDateTime dataInicio = null, dataFim = null;
        if (dataStr != null && !dataStr.isEmpty()) {
            LocalDate data = LocalDate.parse(dataStr);
            dataInicio = data.atStartOfDay();
            dataFim = data.atTime(23, 59, 59);
        }
        // ... (conversão dos outros filtros) ...
        TipoEvento tipoEvento = (tipoEventoStr != null && !tipoEventoStr.isEmpty()) ? TipoEvento.valueOf(tipoEventoStr) : null;
        StatusEvento statusEvento = (statusEventoStr != null && !statusEventoStr.isEmpty()) ? StatusEvento.valueOf(statusEventoStr) : null;
        Usuario responsavel = (responsavelIdStr != null && !responsavelIdStr.isEmpty()) ? usuarioDAO.findById(Integer.valueOf(responsavelIdStr)).orElse(null) : null;
        Disciplina disciplina = (disciplinaIdStr != null && !disciplinaIdStr.isEmpty()) ? disciplinaDAO.findById(Integer.valueOf(disciplinaIdStr)).orElse(null) : null;
        Turma turma = (turmaIdStr != null && !turmaIdStr.isEmpty()) ? turmaDAO.findById(Integer.valueOf(turmaIdStr)).orElse(null) : null;

        List<EventoAgenda> eventos = eventoAgendaDAO.findWithFilters(dataInicio, dataFim, tipoEvento, statusEvento, responsavel, disciplina, turma);

        request.setAttribute("eventos", eventos);
        prepareFormData(request); // Prepara dados para os selects de filtro.
        request.getRequestDispatcher("/WEB-INF/views/agenda/list.jsp").forward(request, response);
    }

    /**
     * Busca os dados e exibe a visualização em formato de calendário.
     */
    private void showCalendar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LocalDate hoje = LocalDate.now();
        int mes = (request.getParameter("mes") != null) ? Integer.parseInt(request.getParameter("mes")) : hoje.getMonthValue();
        int ano = (request.getParameter("ano") != null) ? Integer.parseInt(request.getParameter("ano")) : hoje.getYear();

        LocalDate primeiroDia = LocalDate.of(ano, mes, 1);
        LocalDate ultimoDia = primeiroDia.withDayOfMonth(primeiroDia.lengthOfMonth());

        List<EventoAgenda> eventosDoMes = eventoAgendaDAO.findByPeriodo(primeiroDia.atStartOfDay(), ultimoDia.atTime(23, 59, 59));

        // Agrupa os eventos por dia para facilitar a renderização no calendário do JSP.
        Map<LocalDate, List<EventoAgenda>> eventosPorDia = eventosDoMes.stream()
                .collect(Collectors.groupingBy(evento -> evento.getDataInicio().toLocalDate()));

        request.setAttribute("eventosPorDia", eventosPorDia);
        // ... (outros atributos para controle do calendário) ...
        request.setAttribute("totalEventos", eventosDoMes.size());
        request.setAttribute("mes", mes);
        request.setAttribute("ano", ano);
        request.setAttribute("primeiroDia", primeiroDia);
        request.setAttribute("ultimoDia", ultimoDia);
        request.getRequestDispatcher("/WEB-INF/views/agenda/calendar.jsp").forward(request, response);
    }

    /**
     * Prepara e exibe o formulário para um novo evento.
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        prepareFormData(request);
        request.getRequestDispatcher("/WEB-INF/views/agenda/form.jsp").forward(request, response);
    }

    /**
     * Busca um evento existente e exibe o formulário para edição.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.valueOf(request.getParameter("idEvento"));
        EventoAgenda evento = eventoAgendaDAO.findById(id).orElseThrow(() -> new ServletException("Evento não encontrado"));
        request.setAttribute("evento", evento);
        prepareFormData(request);
        request.getRequestDispatcher("/WEB-INF/views/agenda/form.jsp").forward(request, response);
    }

    /**
     * Exclui um evento pelo ID e redireciona.
     */
    private void deleteEvento(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer id = Integer.valueOf(request.getParameter("idEvento"));
        eventoAgendaDAO.deleteById(id);
        response.sendRedirect(request.getContextPath() + "/agenda?deleted=1");
    }

    /**
     * Altera o status de um evento e redireciona.
     */
    private void changeStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.valueOf(request.getParameter("idEvento"));
        StatusEvento novoStatus = StatusEvento.valueOf(request.getParameter("status"));
        EventoAgenda evento = eventoAgendaDAO.findById(id).orElseThrow(() -> new ServletException("Evento não encontrado"));
        evento.setStatusEvento(novoStatus);
        eventoAgendaDAO.save(evento);
        response.sendRedirect(request.getContextPath() + "/agenda?statusChanged=1");
    }

    /**
     * Método utilitário que carrega as listas de dados (locais, disciplinas,
     * etc.) necessárias para popular os campos <select> (dropdowns) nos
     * formulários.
     */
    private void prepareFormData(HttpServletRequest request) {
        request.setAttribute("locaisEvento", localEventoDAO.findAll());
        request.setAttribute("disciplinas", disciplinaDAO.findAll());
        request.setAttribute("turmas", turmaDAO.findAll());
        request.setAttribute("usuarios", usuarioDAO.findAtivos()); // Busca apenas usuários ativos
        request.setAttribute("tiposEvento", TipoEvento.values());
        request.setAttribute("statusEvento", StatusEvento.values());
    }

    /**
     * Método utilitário para reenviar o usuário ao formulário mantendo os dados
     * já preenchidos, em caso de erro de validação.
     */
    private void showFormWithData(HttpServletRequest request, HttpServletResponse response, EventoAgenda evento) throws ServletException, IOException {
        request.setAttribute("evento", evento);
        prepareFormData(request);
        request.getRequestDispatcher("/WEB-INF/views/agenda/form.jsp").forward(request, response);
    }

    /**
     * Método utilitário para converter uma String de data/hora no formato ISO
     * para um objeto LocalDateTime, com tratamento de erro.
     */
    private LocalDateTime parseDateTime(String dateTimeStr) throws ServletException {
        try {
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw new ServletException("Formato de data/hora inválido. Use yyyy-MM-ddTHH:mm. Valor: " + dateTimeStr, e);
        }
    }
}
