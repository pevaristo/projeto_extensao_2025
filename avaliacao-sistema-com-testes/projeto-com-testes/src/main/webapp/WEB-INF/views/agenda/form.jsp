<%--
* =================================================================================
* NOME DO ARQUIVO: formularioAgenda.jsp
* ---------------------------------------------------------------------------------
* DESCRIÇÃO:
* Esta página JSP (JavaServer Pages) é o componente de VISÃO (View) para a criação
* e edição de eventos da Agenda. É um formulário de propósito duplo, que se adapta
* dinamicamente para servir tanto para o cadastro de um novo evento quanto para a
* atualização de um já existente. É uma página complexa que lida com múltiplos
* relacionamentos de dados (locais, turmas, usuários, etc.).
*
* A página segue o padrão de arquitetura MVC (Model-View-Controller).
* ---------------------------------------------------------------------------------
* LIGAÇÕES COM OUTROS ARQUIVOS:
*
* - CONTROLLER (Controlador):
* Esta página é controlada por uma Servlet Java (provavelmente "AgendaServlet.java")
* mapeada para a URL "/agenda". A servlet é responsável por:
* 1. Exibir o formulário: Ao receber uma requisição GET (para action=new ou action=edit),
* a servlet prepara um conjunto rico de dados (o objeto 'evento' para edição e
* várias listas para preencher os seletores) e encaminha para esta JSP.
* 2. Processar o formulário: Ao receber a submissão via POST, a servlet valida os
* dados e interage com a camada de persistência (DAO) para salvar ou atualizar o evento.
*
* - MODEL (Modelo de Dados):
* Recebe um conjunto extenso de dados da servlet através de atributos na requisição:
* - "evento": Um objeto 'Evento' para preencher o formulário no modo de edição.
* - "erro": Uma String com uma mensagem de erro, caso a validação no servidor falhe.
* - "tiposEvento", "statusEvento": Coleções dos Enums correspondentes para os seletores.
* - "locaisEvento", "disciplinas", "turmas", "usuarios": Listas de entidades relacionadas
* para popular os respectivos campos de seleção no formulário.
*
* - RECURSOS ESTÁTICOS:
* - Utiliza o arquivo de folha de estilos "/css/formularios.css" e contém um
* bloco de CSS específico para este formulário.
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
        <%-- O título da página é dinâmico, usando EL para verificar se um objeto 'evento' existe e exibir "Editar" ou "Novo". --%>
        <title>${not empty evento ? 'Editar' : 'Novo'} Evento - Sistema UNIFAE</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">

        <style>
            /* Estilos específicos para formulário de eventos */
            .form-container {
                max-width: 1000px;
                margin: 0 auto;
                background-color: white;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }
            .form-header {
                background-color: #28a745;
                color: white;
                padding: 20px;
                margin: -30px -30px 30px -30px;
                border-radius: 8px 8px 0 0;
            }
            .form-header h1 {
                margin: 0;
                font-size: 24px;
            }
            .form-header p {
                margin: 5px 0 0 0;
                opacity: 0.9;
            }
            .form-section {
                background: #f8f9fa;
                padding: 20px;
                border-radius: 8px;
                margin-bottom: 20px;
            }
            .form-section h3 {
                margin-top: 0;
                color: #495057;
                border-bottom: 2px solid #dee2e6;
                padding-bottom: 10px;
            }
            .form-row {
                display: flex;
                gap: 20px;
                margin-bottom: 15px;
            }
            .form-row .form-group {
                flex: 1;
            }
            .form-group {
                margin-bottom: 20px;
            }
            .form-label {
                display: block;
                margin-bottom: 5px;
                font-weight: 600;
                color: #333;
            }
            .form-input {
                width: 100%;
                padding: 10px 12px;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 14px;
                transition: border-color 0.3s;
            }
            .form-input:focus {
                outline: none;
                border-color: #28a745;
                box-shadow: 0 0 0 2px rgba(40,167,69,0.25);
            }
            .datetime-input {
                font-family: monospace;
            }
            .error-message {
                background: #f8d7da;
                color: #721c24;
                padding: 10px;
                border-radius: 4px;
                margin-bottom: 20px;
                border: 1px solid #f5c6cb;
            }
            .status-info {
                background: #d1ecf1;
                color: #0c5460;
                padding: 10px;
                border-radius: 4px;
                margin-bottom: 20px;
                border: 1px solid #bee5eb;
            }
            .help-text {
                font-size: 12px;
                color: #6c757d;
                margin-top: 4px;
            }
            .required {
                color: #dc3545;
            }
            .form-actions {
                display: flex;
                gap: 10px;
                justify-content: flex-end;
                margin-top: 30px;
                padding-top: 20px;
                border-top: 1px solid #dee2e6;
            }
            .btn {
                padding: 10px 20px;
                border: none;
                border-radius: 4px;
                font-size: 14px;
                font-weight: 500;
                text-decoration: none;
                cursor: pointer;
                transition: background-color 0.3s;
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
                margin-left: auto;
            }
            .btn-danger:hover {
                background-color: #c82333;
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
            input, select, textarea {
                width: 100%;
                padding: 10px 12px;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 14px;
                transition: border-color 0.3s;
            }
            input:focus, select:focus, textarea:focus {
                outline: none;
                border-color: #28a745;
                box-shadow: 0 0 0 2px rgba(40,167,69,0.25);
            }
            textarea {
                min-height: 80px;
                resize: vertical;
            }
            @media (max-width: 768px) {
                .form-container {
                    margin: 10px;
                    padding: 20px;
                }
                .form-row {
                    flex-direction: column;
                    gap: 0;
                }
                .form-actions {
                    flex-direction: column;
                }
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="form-container">
                <div class="form-header">
                    <h1>📅 ${not empty evento ? 'Editar Evento' : 'Novo Evento'}</h1>
                    <p>
                        <c:choose>
                            <c:when test="${not empty evento}">Altere os dados do evento conforme necessário</c:when>
                            <c:otherwise>Preencha os dados para cadastrar um novo evento</c:otherwise>
                        </c:choose>
                    </p>
                </div>

                <c:if test="${not empty erro}">
                    <div class="error-message">❌ ${erro}</div>
                </c:if>

                <c:if test="${not empty evento}">
                    <div class="status-info">
                        ℹ️ <strong>Status atual:</strong> ${evento.statusEvento.descricao}
                        <c:if test="${evento.statusEvento == 'CONCLUIDO' || evento.statusEvento == 'CANCELADO'}">
                            - Este evento está finalizado e algumas alterações podem ser limitadas.
                        </c:if>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/agenda" method="post">
                    <%-- Campo oculto para enviar o ID do evento no modo de edição. Crucial para a lógica de atualização. --%>
                    <c:if test="${not empty evento}">
                        <input type="hidden" name="idEvento" value="${evento.idEvento}" />
                    </c:if>

                    <div class="form-section">
                        <h3>📋 Informações Básicas</h3>
                        <div class="form-group">
                            <label for="titulo">Título <span class="required">*</span>:</label>
                            <input type="text" id="titulo" name="titulo" value="${evento.titulo}" required placeholder="Ex: Aula de Anatomia - Sistema Cardiovascular">
                        </div>
                        <div class="form-group">
                            <label for="descricao">Descrição:</label>
                            <textarea id="descricao" name="descricao" placeholder="Descrição detalhada do evento (opcional)">${evento.descricao}</textarea>
                        </div>
                        <div class="form-row">
                            <div class="form-group">
                                <label for="tipoEvento">Tipo de Evento <span class="required">*</span>:</label>
                                <select id="tipoEvento" name="tipoEvento" required>
                                    <option value="">Selecione o tipo...</option>
                                    <c:forEach var="tipo" items="${tiposEvento}">
                                        <option value="${tipo}" ${evento.tipoEvento == tipo ? 'selected' : ''}>${tipo.descricao}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="statusEvento">Status <span class="required">*</span>:</label>
                                <select id="statusEvento" name="statusEvento" required>
                                    <c:forEach var="status" items="${statusEvento}">
                                        <option value="${status}" ${evento.statusEvento == status ? 'selected' : ''}>${status.descricao}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="form-section">
                        <h3>🕐 Data e Horário</h3>
                        <div class="form-row">
                            <div class="form-group">
                                <label for="dataInicio">Data/Hora de Início <span class="required">*</span>:</label>
                                <%--
                                    O campo 'datetime-local' requer um formato específico (yyyy-MM-dd'T'HH:mm).
                                    A EL formata o objeto LocalDateTime do Java (evento.dataInicio) usando o
                                    DateTimeFormatter importado, garantindo a compatibilidade para preencher o campo.
                                --%>
                                <input type="datetime-local" id="dataInicio" name="dataInicio" class="datetime-input" required
                                       value="${evento.dataInicio != null ? evento.dataInicio.format(DateTimeFormatter.ofPattern('yyyy-MM-dd\'T\'HH:mm')) : ''}">
                            </div>
                            <div class="form-group">
                                <label for="dataFim">Data/Hora de Fim:</label>
                                <input type="datetime-local" id="dataFim" name="dataFim" class="datetime-input"
                                       value="${evento.dataFim != null ? evento.dataFim.format(DateTimeFormatter.ofPattern('yyyy-MM-dd\'T\'HH:mm')) : ''}">
                            </div>
                        </div>
                    </div>

                    <%-- Cada um destes seletores é populado por uma lista enviada pelo servlet e usa EL para pré-selecionar o valor correto no modo de edição. --%>
                    <div class="form-section">
                        <h3>📍 Local</h3>
                        <div class="form-group">
                            <label for="localEventoId">Local do Evento:</label>
                            <select id="localEventoId" name="localEventoId">
                                <option value="">Selecione o local...</option>
                                <c:forEach var="local" items="${locaisEvento}">
                                    <option value="${local.idLocalEvento}" ${not empty evento.localEvento && evento.localEvento.idLocalEvento == local.idLocalEvento ? 'selected' : ''}>
                                        ${local.nomeLocal} <c:if test="${not empty local.tipoLocal}"> - ${local.tipoLocal}</c:if>
                                        </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-section">
                        <h3>🎓 Relacionamentos Acadêmicos</h3>
                        <div class="form-row">
                            <div class="form-group">
                                <label for="disciplinaId">Disciplina:</label>
                                <select id="disciplinaId" name="disciplinaId">
                                    <option value="">Selecione a disciplina...</option>
                                    <c:forEach var="disciplina" items="${disciplinas}">
                                        <option value="${disciplina.idDisciplina}" ${not empty evento.disciplina && evento.disciplina.idDisciplina == disciplina.idDisciplina ? 'selected' : ''}>
                                            ${disciplina.nomeDisciplina} <c:if test="${not empty disciplina.siglaDisciplina}"> (${disciplina.siglaDisciplina})</c:if>
                                            </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="turmaId">Turma:</label>
                                <select id="turmaId" name="turmaId">
                                    <option value="">Selecione a turma...</option>
                                    <c:forEach var="turma" items="${turmas}">
                                        <option value="${turma.idTurma}" ${not empty evento.turma && evento.turma.idTurma == turma.idTurma ? 'selected' : ''}>
                                            ${turma.nomeTurma} <c:if test="${not empty turma.codigoTurma}"> (${turma.codigoTurma})</c:if>
                                            </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="form-section">
                        <h3>👤 Responsável</h3>
                        <div class="form-group">
                            <label for="responsavelId">Responsável pelo Evento:</label>
                            <select id="responsavelId" name="responsavelId">
                                <option value="">Selecione o responsável...</option>
                                <c:forEach var="usuario" items="${usuarios}">
                                    <option value="${usuario.idUsuario}" ${not empty evento.responsavel && evento.responsavel.idUsuario == usuario.idUsuario ? 'selected' : ''}>
                                        ${usuario.nomeCompleto} (${usuario.tipoUsuario})
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-actions">
                        <a href="${pageContext.request.contextPath}/agenda" class="btn btn-secondary">❌ Cancelar</a>
                        <button type="submit" class="btn btn-primary">💾 Salvar Evento</button>
                        <%-- O botão de Excluir só aparece no modo de edição e possui uma confirmação via JavaScript. --%>
                        <c:if test="${not empty evento}">
                            <a href="${pageContext.request.contextPath}/agenda?action=delete&id=${evento.idEvento}" class="btn btn-danger"
                               onclick="return confirm('Tem certeza que deseja excluir este evento?\\n\\nEsta ação não pode ser desfeita.')">🗑️ Excluir</a>
                        </c:if>
                    </div>
                </form>
            </div>
        </div>

        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const dataInicioInput = document.getElementById('dataInicio');
                const dataFimInput = document.getElementById('dataFim');
                const form = document.querySelector('form');

                // Função que verifica se a data de fim é posterior à data de início.
                function validarDatas() {
                    if (dataInicioInput.value && dataFimInput.value) {
                        const dataInicio = new Date(dataInicioInput.value);
                        const dataFim = new Date(dataFimInput.value);

                        if (dataFim <= dataInicio) {
                            alert('A data de fim deve ser posterior à data de início.');
                            return false; // Retorna false se a validação falhar.
                        }
                    }
                    return true; // Retorna true se a validação passar ou se os campos não estiverem preenchidos.
                }

                // Adiciona um "ouvinte" ao evento de submissão do formulário.
                form.addEventListener('submit', function (e) {
                    // Antes de enviar, executa a validação.
                    if (!validarDatas()) {
                        e.preventDefault(); // Se a validação falhar, impede o envio do formulário.
                    }
                });

                // Adiciona validação em tempo real sempre que o valor de um dos campos de data é alterado.
                dataFimInput.addEventListener('change', validarDatas);
                dataInicioInput.addEventListener('change', validarDatas);
            });
        </script>
    </body>
</html>