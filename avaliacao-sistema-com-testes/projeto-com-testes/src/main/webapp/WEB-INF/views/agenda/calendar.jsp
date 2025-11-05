<%--
* =================================================================================
* NOME DO ARQUIVO: agendaCalendar.jsp
* ---------------------------------------------------------------------------------
* DESCRIÇÃO:
* Esta página JSP (JavaServer Pages) é o componente de VISÃO (View) para a
* funcionalidade de Agenda, renderizando os eventos em um formato de calendário
* mensal interativo. A página é responsável por construir a grade do calendário,
* popular os dias com os respectivos eventos e permitir a navegação entre meses e anos.
*
* A página segue o padrão de arquitetura MVC (Model-View-Controller).
* ---------------------------------------------------------------------------------
* LIGAÇÕES COM OUTROS ARQUIVOS:
*
* - CONTROLLER (Controlador):
* Esta página é controlada por uma Servlet Java (provavelmente "AgendaServlet.java")
* mapeada para a URL "/agenda". A servlet processa as requisições, busca os eventos
* do mês solicitado no banco de dados, calcula as informações de data necessárias
* (primeiro e último dia do mês) e organiza os eventos em uma estrutura de dados
* otimizada (como um Map) antes de encaminhar para esta JSP.
*
* - MODEL (Modelo de Dados):
* Recebe dados da servlet através de atributos na requisição (request attributes):
* - "mes", "ano": O mês e ano atuais sendo exibidos.
* - "primeiroDia", "ultimoDia": Objetos 'LocalDate' representando o primeiro e último dia do mês.
* - "eventosPorDia": Uma estrutura de dados (provavelmente um Map<LocalDate, List<Evento>>) que
* mapeia cada dia a uma lista de seus eventos.
* - "totalEventos": Um contador com o total de eventos no mês.
*
* - OUTRAS VIEWS (Outras Visões):
* - Contém links para a visualização em lista ("/agenda") e para o formulário de
* criação/edição de eventos ("formularioAgenda.jsp").
*
* - RECURSOS ESTÁTICOS:
* - Utiliza "/css/formularios.css" e possui um extenso bloco de CSS interno para
* construir e estilizar a grade do calendário e os itens de evento.
* =================================================================================
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Importa bibliotecas JSTL: Core (c) para lógica, Format (fmt) para formatação e Functions (fn) para funções utilitárias como fn:length. --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%-- Importa classes do Java 8 (java.time) para permitir a manipulação de datas modernas diretamente na JSP, uma prática poderosa. --%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDate" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Calendário de Eventos - Sistema UNIFAE</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">

        <%-- Bloco de estilos CSS internos que define toda a estrutura e aparência do calendário, desde a grade até as cores dos eventos. --%>
        <style>
            /* Estilos específicos para calendário de eventos */
            .page-header {
                background-color: #28a745;
                color: white;
                padding: 20px;
                margin-bottom: 20px;
                border-radius: 8px;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .btn-new {
                background-color: #007bff;
                color: white;
                padding: 10px 20px;
                text-decoration: none;
                border-radius: 5px;
                font-weight: 500;
            }

            .btn-new:hover {
                background-color: #0056b3;
            }

            .view-toggle {
                display: flex;
                gap: 10px;
                margin-bottom: 20px;
            }

            .month-selector {
                background: #f8f9fa;
                padding: 15px;
                border-radius: 8px;
                margin-bottom: 20px;
                display: flex;
                gap: 15px;
                align-items: center;
                flex-wrap: wrap;
            }

            .legend {
                background: #f8f9fa;
                padding: 15px;
                border-radius: 8px;
                margin-bottom: 20px;
                display: flex;
                gap: 20px;
                flex-wrap: wrap;
            }

            .legend-item {
                display: flex;
                align-items: center;
                gap: 8px;
            }

            .legend-color {
                width: 16px;
                height: 16px;
                border-radius: 3px;
            }

            .calendar-container {
                background: white;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                overflow: hidden;
            }

            .calendar-header {
                background: #007bff;
                color: white;
                padding: 20px;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .calendar-nav a {
                color: white;
                text-decoration: none;
                padding: 8px 12px;
                border-radius: 4px;
                background: rgba(255,255,255,0.2);
                transition: background 0.2s;
            }

            .calendar-nav a:hover {
                background: rgba(255,255,255,0.3);
            }

            .calendar-title {
                font-size: 24px;
                font-weight: bold;
            }

            .calendar-grid {
                display: grid;
                grid-template-columns: repeat(7, 1fr);
            }

            .calendar-day-header {
                background: #f8f9fa;
                padding: 15px 10px;
                text-align: center;
                font-weight: bold;
            }

            .calendar-day {
                min-height: 120px;
                border-right: 1px solid #dee2e6;
                border-bottom: 1px solid #dee2e6;
                padding: 8px;
                position: relative;
                transition: background 0.2s ease;
            }

            .calendar-day:nth-child(7n) {
                border-right: none;
            }

            .calendar-day.other-month {
                background: #f8f9fa;
                color: #adb5bd;
            }

            .calendar-day.today {
                background: #fff3cd;
            }

            .day-number {
                font-weight: bold;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .event-count {
                background: #007bff;
                color: white;
                border-radius: 50%;
                width: 20px;
                height: 20px;
                font-size: 11px;
                display: flex;
                align-items: center;
                justify-content: center;
            }

            .day-events {
                margin-top: 5px;
                max-height: 80px;
                overflow-y: auto;
            }

            .event-item-compact {
                padding: 3px 5px;
                margin: 2px 0;
                border-radius: 3px;
                font-size: 11px;
                cursor: pointer;
                display: flex;
                color: white;
            }

            .event-item-compact:hover {
                filter: brightness(110%);
            }

            .event-item-compact.tipo-aula {
                background: #007bff;
            }
            .event-item-compact.tipo-prova {
                background: #dc3545;
            }
            .event-item-compact.tipo-avaliacao {
                background: #fd7e14;
            }
            .event-item-compact.tipo-seminario {
                background: #6f42c1;
            }
            .event-item-compact.tipo-reuniao {
                background: #20c997;
            }
            .event-item-compact.tipo-evento {
                background: #ffc107;
                color: #212529;
            }
            .event-item-compact.status-cancelado {
                opacity: 0.6;
                text-decoration: line-through;
            }
            .event-item-compact.status-concluido {
                opacity: 0.8;
            }

            .event-time {
                margin-right: 5px;
                font-weight: bold;
            }
            .event-title {
                flex: 1;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
            }
            .actions.menu {
                display: flex;
                gap: 10px;
                margin-bottom: 20px;
                justify-content: flex-start;
            }
            .btn {
                padding: 10px 20px;
                text-decoration: none;
                border-radius: 4px;
                font-size: 14px;
                font-weight: 500;
                transition: background-color 0.3s;
                border: none;
                cursor: pointer;
            }
            .btn-primary {
                background-color: #28a745;
                color: white;
            }
            .btn-primary:hover {
                background-color: #218838;
            }
            .btn-secondary {
                background-color: #6c757d;
                color: white;
            }
            .btn-secondary:hover {
                background-color: #545b62;
            }
            .container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 20px;
            }
            label {
                display: block;
                margin-bottom: 5px;
                font-weight: 600;
                color: #333;
            }
            select {
                padding: 8px 12px;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 14px;
            }
            select:focus {
                outline: none;
                border-color: #28a745;
                box-shadow: 0 0 0 2px rgba(40,167,69,0.25);
            }

            @media (max-width: 768px) {
                .page-header {
                    flex-direction: column;
                    gap: 15px;
                    text-align: center;
                }
                .month-selector {
                    flex-direction: column;
                    align-items: flex-start;
                    gap: 10px;
                }
                .legend {
                    flex-direction: column;
                    gap: 10px;
                }
                .calendar-day {
                    min-height: 80px;
                }
                .actions.menu {
                    flex-direction: column;
                }
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="page-header">
                <div>
                    <h1>📅 Calendário de Eventos</h1>
                    <p>Visualização mensal dos eventos e atividades acadêmicas</p>
                </div>
                <a href="${pageContext.request.contextPath}/agenda?action=new" class="btn-new">
                    ➕ Novo Evento
                </a>
            </div>

            <div class="view-toggle">
                <a href="${pageContext.request.contextPath}/agenda" class="btn btn-secondary">📋 Lista</a>
                <a href="${pageContext.request.contextPath}/agenda?action=calendar" class="btn btn-primary">📅 Calendário</a>
            </div>

            <div class="actions menu">
                <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">🏠 Voltar ao Início</a>
            </div>

            <div class="month-selector">
                <label for="mesSelect">Mês:</label>
                <%-- O evento 'onchange' chama a função JavaScript 'changeMonth()' sempre que um novo mês é selecionado. --%>
                <select id="mesSelect" onchange="changeMonth()">
                    <%-- As opções são marcadas como 'selected' com base na variável 'mes' vinda do servlet, para manter o estado. --%>
                    <option value="1" ${mes == 1 ? 'selected' : ''}>Janeiro</option>
                    <option value="2" ${mes == 2 ? 'selected' : ''}>Fevereiro</option>
                    <option value="3" ${mes == 3 ? 'selected' : ''}>Março</option>
                    <option value="4" ${mes == 4 ? 'selected' : ''}>Abril</option>
                    <option value="5" ${mes == 5 ? 'selected' : ''}>Maio</option>
                    <option value="6" ${mes == 6 ? 'selected' : ''}>Junho</option>
                    <option value="7" ${mes == 7 ? 'selected' : ''}>Julho</option>
                    <option value="8" ${mes == 8 ? 'selected' : ''}>Agosto</option>
                    <option value="9" ${mes == 9 ? 'selected' : ''}>Setembro</option>
                    <option value="10" ${mes == 10 ? 'selected' : ''}>Outubro</option>
                    <option value="11" ${mes == 11 ? 'selected' : ''}>Novembro</option>
                    <option value="12" ${mes == 12 ? 'selected' : ''}>Dezembro</option>
                </select>

                <label for="anoSelect">Ano:</label>
                <select id="anoSelect" onchange="changeMonth()">
                    <%-- Gera dinamicamente opções para 2 anos antes e 2 anos depois do ano atual. --%>
                    <c:forEach var="i" begin="${ano - 2}" end="${ano + 2}">
                        <option value="${i}" ${ano == i ? 'selected' : ''}>${i}</option>
                    </c:forEach>
                </select>

                <button onclick="goToToday()" class="btn btn-primary">📍 Hoje</button>
            </div>

            <div class="legend">
                <div class="legend-item"><div class="legend-color" style="background: #007bff;"></div><span>Aula</span></div>
                <div class="legend-item"><div class="legend-color" style="background: #dc3545;"></div><span>Prova</span></div>
                <div class="legend-item"><div class="legend-color" style="background: #fd7e14;"></div><span>Avaliação</span></div>
                <div class="legend-item"><div class="legend-color" style="background: #6f42c1;"></div><span>Seminário</span></div>
                <div class="legend-item"><div class="legend-color" style="background: #20c997;"></div><span>Reunião</span></div>
                <div class="legend-item"><div class="legend-color" style="background: #ffc107;"></div><span>Evento</span></div>
            </div>

            <div class="calendar-container">
                <div class="calendar-header">
                    <div class="calendar-nav">
                        <%-- Link para o mês anterior. A EL calcula o mês e o ano corretos, tratando a virada de ano (Janeiro -> Dezembro do ano anterior). --%>
                        <a href="?action=calendar&mes=${mes == 1 ? 12 : mes - 1}&ano=${mes == 1 ? ano - 1 : ano}">‹ Anterior</a>
                    </div>
                    <div class="calendar-title">
                        <%-- Converte o número do mês para o nome por extenso. --%>
                        <c:choose>
                            <c:when test="${mes == 1}">Janeiro</c:when> <c:when test="${mes == 2}">Fevereiro</c:when> <c:when test="${mes == 3}">Março</c:when>
                            <c:when test="${mes == 4}">Abril</c:when> <c:when test="${mes == 5}">Maio</c:when> <c:when test="${mes == 6}">Junho</c:when>
                            <c:when test="${mes == 7}">Julho</c:when> <c:when test="${mes == 8}">Agosto</c:when> <c:when test="${mes == 9}">Setembro</c:when>
                            <c:when test="${mes == 10}">Outubro</c:when> <c:when test="${mes == 11}">Novembro</c:when> <c:when test="${mes == 12}">Dezembro</c:when>
                        </c:choose>
                        ${ano}
                    </div>
                    <div class="calendar-nav">
                        <%-- Link para o próximo mês. A EL trata a virada de ano (Dezembro -> Janeiro do próximo ano). --%>
                        <a href="?action=calendar&mes=${mes == 12 ? 1 : mes + 1}&ano=${mes == 12 ? ano + 1 : ano}">Próximo ›</a>
                    </div>
                </div>

                <div class="calendar-grid">
                    <div class="calendar-day-header">Dom</div> <div class="calendar-day-header">Seg</div> <div class="calendar-day-header">Ter</div>
                    <div class="calendar-day-header">Qua</div> <div class="calendar-day-header">Qui</div> <div class="calendar-day-header">Sex</div>
                    <div class="calendar-day-header">Sáb</div>

                    <%--
                        Lógica JSTL para calcular e exibir os dias do calendário.
                        1. Calcula em qual dia da semana o mês começa para saber quantos dias "fantasmas" do mês anterior são necessários.
                    --%>
                    <c:set var="primeiroDiaSemana" value="${primeiroDia.dayOfWeek.value % 7}" />
                    <c:set var="diasNoMes" value="${ultimoDia.dayOfMonth}" />

                    <c:if test="${primeiroDiaSemana > 0}">
                        <c:set var="ultimoDiaMesAnterior" value="${primeiroDia.minusDays(1)}" />
                        <c:forEach var="i" begin="1" end="${primeiroDiaSemana}">
                            <c:set var="dia" value="${ultimoDiaMesAnterior.dayOfMonth - primeiroDiaSemana + i}" />
                            <div class="calendar-day other-month"><div class="day-number">${dia}</div></div>
                            </c:forEach>
                        </c:if>

                    <c:forEach var="dia" begin="1" end="${diasNoMes}">
                        <%--
                            Para cada dia do mês, o código:
                            1. Cria um objeto 'LocalDate' do Java para representar a data exata.
                            2. Usa esse objeto como chave para buscar a lista de eventos no Map 'eventosPorDia'.
                            3. Verifica se o dia atual é hoje para aplicar um estilo de destaque.
                        --%>
                        <c:set var="dataAtualObj" value="${LocalDate.of(ano, mes, dia)}" />
                        <c:set var="eventosDoDia" value="${eventosPorDia[dataAtualObj]}" />
                        <c:set var="temEventos" value="${not empty eventosDoDia}" />
                        <c:set var="isToday" value="${dataAtualObj.isEqual(LocalDate.now())}" />

                        <div class="calendar-day ${isToday ? 'today' : ''}">
                            <div class="day-number">
                                ${dia}
                                <c:if test="${temEventos}">
                                    <%-- Usa a função JSTL 'length' para contar e exibir o número de eventos do dia. --%>
                                    <span class="event-count">${fn:length(eventosDoDia)}</span>
                                </c:if>
                            </div>

                            <div class="day-events">
                                <%-- Itera sobre a lista de eventos do dia e exibe cada um. --%>
                                <c:forEach var="evento" items="${eventosDoDia}">
                                    <%--
                                        Cria classes CSS dinâmicas baseadas no tipo e status do evento. Ex: tipo-aula, status-cancelado.
                                        Isso permite a colorização e estilização automática dos eventos.
                                        O evento é clicável e redireciona para a página de edição.
                                    --%>
                                    <div class="event-item-compact tipo-${evento.tipoEvento.name().toLowerCase()} status-${evento.statusEvento.name().toLowerCase()}"
                                         title="Clique para editar: ${fn:escapeXml(evento.titulo)}"
                                         onclick="window.location.href = '${pageContext.request.contextPath}/agenda?action=edit&idEvento=${evento.idEvento}'">
                                        <%--
                                            Chama diretamente o método 'format' de um objeto Java (dataInicio) a partir da EL.
                                            Isso formata a hora do evento para o padrão HH:mm.
                                        --%>
                                        <div class="event-time">${evento.dataInicio.format(DateTimeFormatter.ofPattern('HH:mm'))}</div>
                                        <div class="event-title">${evento.titulo}</div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </c:forEach>

                    <c:set var="totalCells" value="${primeiroDiaSemana + diasNoMes}" />
                    <c:if test="${totalCells % 7 != 0}">
                        <c:forEach var="i" begin="1" end="${7 - (totalCells % 7)}">
                            <div class="calendar-day other-month"><div class="day-number">${i}</div></div>
                            </c:forEach>
                        </c:if>
                </div>
            </div>

            <div style="margin-top: 20px; padding: 15px; background: #f8f9fa; border-radius: 8px; color: #666;">
                📊 Total de eventos no mês: <strong>${totalEventos}</strong>
            </div>
        </div>

        <script>
            // Função chamada quando o usuário muda o mês ou o ano no dropdown.
            function changeMonth() {
                const mes = document.getElementById('mesSelect').value;
                const ano = document.getElementById('anoSelect').value;
                // Recarrega a página com os novos parâmetros de mês e ano.
                window.location.href = `?action=calendar&mes=${mes}&ano=${ano}`;
            }

            // Função chamada ao clicar no botão "Hoje".
            function goToToday() {
                const hoje = new Date();
                const mes = hoje.getMonth() + 1; // getMonth() é 0-indexado, então adicionamos 1.
                const ano = hoje.getFullYear();
                // Recarrega a página com os parâmetros do mês e ano atuais.
                window.location.href = `?action=calendar&mes=${mes}&ano=${ano}`;
            }
        </script>
    </body>
</html>