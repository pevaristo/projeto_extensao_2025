<%--
* = "================================================================================
* NOME DO ARQUIVO: agendaList.jsp
* ---------------------------------------------------------------------------------
* DESCRIÇÃO:
* Esta página JSP (JavaServer Pages) é o componente de VISÃO (View) para a
* funcionalidade de Agenda, renderizando os eventos em um formato de lista detalhada.
* A página é responsável por exibir os eventos em uma tabela, fornecer opções
* de filtragem avançada e apresentar ações de gerenciamento para cada evento.
*
* A página segue o padrão de arquitetura MVC (Model-View-Controller).
* ---------------------------------------------------------------------------------
* LIGAÇÕES COM OUTROS ARQUIVOS:
*
* - CONTROLLER (Controlador):
* Esta página é controlada por uma Servlet Java (provavelmente "AgendaServlet.java")
* mapeada para a URL "/agenda". A servlet processa as requisições, busca os eventos
* no banco de dados aplicando os filtros solicitados (data, tipo, status), e encaminha
* os dados necessários para esta JSP renderizar a página.
*
* - MODEL (Modelo de Dados):
* Recebe um conjunto rico de dados da servlet através de atributos na requisição:
* - "eventos": Uma lista de objetos 'Evento' filtrada para popular a tabela.
* - "tiposEvento", "statusEvento": Coleções dos Enums correspondentes para preencher os seletores de filtro.
* A página também lê parâmetros da URL (usando o objeto implícito 'param') para
* manter o estado dos filtros e para exibir mensagens de sucesso após uma ação.
*
* - OUTRAS VIEWS (Outras Visões):
* - Contém links para a visualização em calendário ("/agenda?action=calendar") e para
* o formulário de criação/edição de eventos ("formularioAgenda.jsp").
*
* - RECURSOS ESTÁTICOS:
* - Utiliza "/css/formularios.css" e possui um extenso bloco de CSS interno para
* estilizar a tabela, os filtros e os badges de status/tipo de evento.
* =================================================================================
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%-- Importa a classe do Java 8 (java.time) para permitir a formatação de datas modernas diretamente na JSP. --%>
<%@ page import="java.time.format.DateTimeFormatter" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Agenda de Eventos - Sistema UNIFAE</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">

        <style>
            /* Estilos específicos para listagem de eventos */
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
            .filters {
                background: #f8f9fa;
                padding: 20px;
                border-radius: 8px;
                margin-bottom: 20px;
            }
            .filter-row {
                display: flex;
                gap: 15px;
                align-items: end;
                flex-wrap: wrap;
            }
            .filter-group {
                display: flex;
                flex-direction: column;
                min-width: 150px;
            }
            .table-container {
                background-color: white;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }
            .data-table {
                width: 100%;
                border-collapse: collapse;
            }
            .data-table th {
                background-color: #f8f9fa;
                padding: 12px;
                text-align: left;
                border-bottom: 2px solid #dee2e6;
                font-weight: 600;
            }
            .data-table td {
                padding: 12px;
                border-bottom: 1px solid #dee2e6;
            }
            .data-table tr:hover {
                background-color: #f8f9fa;
            }

            /* Estilos para os badges de status do evento */
            .status-badge {
                padding: 4px 8px;
                border-radius: 12px;
                font-size: 12px;
                font-weight: bold;
                text-transform: uppercase;
            }
            .status-agendado {
                background: #007bff;
                color: white;
            }
            .status-em_andamento {
                background: #ffc107;
                color: #212529;
            }
            .status-concluido {
                background: #28a745;
                color: white;
            }
            .status-cancelado {
                background: #dc3545;
                color: white;
            }
            .tipo-badge {
                padding: 2px 6px;
                border-radius: 4px;
                font-size: 11px;
                background: #e9ecef;
                color: #495057;
            }

            /* Estilos para a borda colorida da linha, indicando o tipo de evento */
            .event-row {
                border-left: 4px solid #007bff;
            } /* Cor padrão para 'Aula' */
            .event-row.tipo-prova {
                border-left-color: #dc3545;
            }
            .event-row.tipo-avaliacao {
                border-left-color: #fd7e14;
            }
            .event-row.tipo-seminario {
                border-left-color: #6f42c1;
            }
            .event-row.tipo-reuniao {
                border-left-color: #20c997;
            }
            .event-row.tipo-evento {
                border-left-color: #ffc107;
            }

            .success-message {
                background: #d4edda;
                color: #155724;
                padding: 10px;
                border-radius: 4px;
                margin-bottom: 20px;
                border: 1px solid #c3e6cb;
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
            .btn-danger {
                background-color: #dc3545;
                color: white;
            }
            .btn-danger:hover {
                background-color: #c82333;
            }
            .container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 20px;
            }
            .table {
                width: 100%;
                border-collapse: collapse;
                background-color: white;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }
            .table th {
                background-color: #f8f9fa;
                padding: 12px;
                text-align: left;
                border-bottom: 2px solid #dee2e6;
                font-weight: 600;
            }
            .table td {
                padding: 12px;
                border-bottom: 1px solid #dee2e6;
            }
            .table tr:hover {
                background-color: #f8f9fa;
            }
            label {
                display: block;
                margin-bottom: 5px;
                font-weight: 600;
                color: #333;
            }
            input, select {
                width: 100%;
                padding: 8px 12px;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 14px;
            }
            input:focus, select:focus {
                outline: none;
                border-color: #28a745;
                box-shadow: 0 0 0 2px rgba(40,167,69,0.25);
            }
            @media (max-width: 768px) {
                .filter-row {
                    flex-direction: column;
                    gap: 10px;
                }
                .filter-group {
                    min-width: 100%;
                }
                .page-header {
                    flex-direction: column;
                    gap: 15px;
                    text-align: center;
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
                    <h1>📅 Agenda de Eventos</h1>
                    <p>Gerenciamento de eventos e atividades acadêmicas</p>
                </div>
                <a href="${pageContext.request.contextPath}/agenda?action=new" class="btn-new">
                    ➕ Novo Evento
                </a>
            </div>

            <%--
                Estes blocos usam <c:if> para verificar parâmetros na URL. É uma implementação do padrão
                Post-Redirect-Get (PRG) para exibir mensagens de sucesso após uma ação (salvar, excluir,
                mudar status) de forma segura, sem risco de reenvio de formulário.
            --%>
            <c:if test="${param.success == '1'}"><div class="success-message">✅ Evento salvo com sucesso!</div></c:if>
            <c:if test="${param.deleted == '1'}"><div class="success-message">🗑️ Evento excluído com sucesso!</div></c:if>
            <c:if test="${param.statusChanged == '1'}"><div class="success-message">🔄 Status do evento alterado com sucesso!</div></c:if>

                <div class="view-toggle">
                    <a href="${pageContext.request.contextPath}/agenda" class="btn btn-primary">📋 Lista</a>
                <a href="${pageContext.request.contextPath}/agenda?action=calendar" class="btn btn-secondary">📅 Calendário</a>
            </div>

            <div class="actions menu">
                <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">🏠 Voltar ao Início</a>
            </div>

            <div class="filters">
                <form method="get" action="${pageContext.request.contextPath}/agenda">
                    <div class="filter-row">
                        <div class="filter-group">
                            <label for="data">Data:</label>
                            <%-- O valor do campo é preenchido com o parâmetro 'data' da URL para manter o estado do filtro. --%>
                            <input type="date" id="data" name="data" value="${param.data}">
                        </div>
                        <div class="filter-group">
                            <label for="tipoEvento">Tipo:</label>
                            <select id="tipoEvento" name="tipoEvento">
                                <option value="">Todos os tipos</option>
                                <c:forEach var="tipo" items="${tiposEvento}">
                                    <%-- A opção é pré-selecionada se seu valor corresponder ao parâmetro 'tipoEvento' na URL. --%>
                                    <option value="${tipo}" ${param.tipoEvento == tipo ? 'selected' : ''}>${tipo.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="filter-group">
                            <label for="statusEvento">Status:</label>
                            <select id="statusEvento" name="statusEvento">
                                <option value="">Todos os status</option>
                                <c:forEach var="status" items="${statusEvento}">
                                    <option value="${status}" ${param.statusEvento == status ? 'selected' : ''}>${status.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="filter-group"><label>&nbsp;</label><button type="submit" class="btn btn-primary">🔍 Filtrar</button></div>
                        <div class="filter-group"><label>&nbsp;</label><a href="${pageContext.request.contextPath}/agenda" class="btn btn-secondary">🗑️ Limpar</a></div>
                    </div>
                </form>
            </div>

            <table class="table">
                <thead>
                    <tr>
                        <th>Data/Hora</th> <th>Título</th> <th>Tipo</th> <th>Status</th>
                        <th>Local</th> <th>Responsável</th> <th>Disciplina</th> <th>Turma</th> <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <%-- Se a lista de eventos estiver vazia, exibe uma mensagem amigável. --%>
                        <c:when test="${empty eventos}">
                            <tr><td colspan="9" style="text-align: center; padding: 40px; color: #666;">📭 Nenhum evento encontrado com os filtros aplicados.</td></tr>
                        </c:when>
                        <c:otherwise>
                            <%-- Itera sobre a lista de eventos e cria uma linha para cada um. --%>
                            <c:forEach var="evento" items="${eventos}">
                                <%--
                                    A classe da linha <tr> é definida dinamicamente. Ela pega o nome do Enum 'tipoEvento'
                                    (ex: PROVA), converte para minúsculas ('prova') e cria a classe 'tipo-prova'.
                                    O CSS usa essa classe para aplicar uma cor específica à borda esquerda da linha.
                                --%>
                                <tr class="event-row tipo-${evento.tipoEvento.name().toLowerCase()}">
                                    <td>
                                        <%-- Formata a data e hora do evento usando o DateTimeFormatter do Java 8, chamado diretamente via EL. --%>
                                        <div>${evento.dataInicio.format(DateTimeFormatter.ofPattern('dd/MM/yyyy'))}</div>
                                        <div>
                                            ${evento.dataInicio.format(DateTimeFormatter.ofPattern('HH:mm'))}
                                            <c:if test="${evento.dataFim != null}"> - ${evento.dataFim.format(DateTimeFormatter.ofPattern('HH:mm'))}</c:if>
                                            </div>
                                        </td>
                                        <td>
                                            <strong>${evento.titulo}</strong>
                                        <%-- Trunca a descrição se ela for muito longa para não quebrar o layout da tabela. --%>
                                        <c:if test="${not empty evento.descricao}">
                                            <div style="font-size: 12px; color: #666; margin-top: 4px;">
                                                ${evento.descricao.length() > 50 ? evento.descricao.substring(0, 50).concat('...') : evento.descricao}
                                            </div>
                                        </c:if>
                                    </td>
                                    <td><span class="tipo-badge">${evento.tipoEvento.descricao}</span></td>
                                        <%-- A classe do badge de status também é definida dinamicamente, permitindo a colorização via CSS. --%>
                                    <td><span class="status-badge status-${evento.statusEvento.name().toLowerCase()}">${evento.statusEvento.descricao}</span></td>
                                        <%--
                                            Para campos de relacionamento (Local, Responsável etc.), o <c:choose> verifica
                                            se o objeto relacionado não é nulo antes de tentar exibir seu nome, evitando erros.
                                        --%>
                                    <td><c:choose><c:when test="${evento.localEvento != null}">${evento.localEvento.nomeLocal}</c:when><c:otherwise><span style="color: #999;">-</span></c:otherwise></c:choose></td>
                                    <td><c:choose><c:when test="${evento.responsavel != null}">${evento.responsavel.nomeCompleto}</c:when><c:otherwise><span style="color: #999;">-</span></c:otherwise></c:choose></td>
                                    <td><c:choose><c:when test="${evento.disciplina != null}">${evento.disciplina.nomeDisciplina}</c:when><c:otherwise><span style="color: #999;">-</span></c:otherwise></c:choose></td>
                                    <td><c:choose><c:when test="${evento.turma != null}">${evento.turma.nomeTurma}</c:when><c:otherwise><span style="color: #999;">-</span></c:otherwise></c:choose></td>
                                            <td>
                                        <%--
                                            Ações contextuais: os botões para mudar o status do evento são exibidos
                                            condicionalmente, baseados no status atual do evento. Isso implementa
                                            uma máquina de estados simples na interface do usuário.
                                        --%>
                                        <div style="display: flex; gap: 5px; flex-wrap: wrap;">
                                            <a href="agenda?action=edit&idEvento=${evento.idEvento}" class="btn" title="Editar">✏️</a>
                                            <c:if test="${evento.statusEvento == 'AGENDADO'}">
                                                <a href="agenda?action=changeStatus&idEvento=${evento.idEvento}&status=EM_ANDAMENTO" class="btn" title="Iniciar Evento" onclick="return confirm('Iniciar este evento?')">▶️</a>
                                            </c:if>
                                            <c:if test="${evento.statusEvento == 'EM_ANDAMENTO'}">
                                                <a href="agenda?action=changeStatus&idEvento=${evento.idEvento}&status=CONCLUIDO" class="btn" title="Concluir Evento" onclick="return confirm('Concluir este evento?')">✅</a>
                                            </c:if>
                                            <c:if test="${evento.statusEvento == 'AGENDADO' || evento.statusEvento == 'EM_ANDAMENTO'}">
                                                <a href="agenda?action=changeStatus&idEvento=${evento.idEvento}&status=CANCELADO" class="btn" title="Cancelar Evento" onclick="return confirm('Cancelar este evento?')">❌</a>
                                            </c:if>
                                            <a href="agenda?action=delete&idEvento=${evento.idEvento}" class="btn btn-danger" title="Excluir" onclick="return confirm('Tem certeza que deseja excluir este evento?\n\nEsta ação não pode ser desfeita.')">🗑️</a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>

            <c:if test="${not empty eventos}">
                <div style="margin-top: 20px; padding: 15px; background: #f8f9fa; border-radius: 8px; color: #666;">
                    📊 Total de eventos: <strong>${eventos.size()}</strong>
                </div>
            </c:if>
        </div>
    </body>
</html>