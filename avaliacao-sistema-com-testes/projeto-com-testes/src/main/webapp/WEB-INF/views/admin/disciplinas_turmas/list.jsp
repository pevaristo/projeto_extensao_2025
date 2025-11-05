<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Gerenciar Disciplinas da Turma - Sistema UNIFAE</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">
        <style>
            body {
                font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
                background-color: #f4f6f9;
                color: #333;
                margin: 20px;
            }
            .page-header {
                background-color: #17a2b8;
                color: white;
                padding: 20px;
                margin-bottom: 20px;
                border-radius: 8px;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            .page-header h1 {
                margin: 0;
                font-size: 28px;
            }
            .page-header p {
                margin: 5px 0 0;
                opacity: 0.9;
            }
            .btn-new {
                background-color: #28a745;
                color: white;
                padding: 10px 20px;
                text-decoration: none;
                border-radius: 5px;
                font-weight: 500;
            }
            .btn-back {
                background-color: #6c757d;
                color: white;
                padding: 10px 20px;
                text-decoration: none;
                border-radius: 5px;
                font-weight: 500;
            }
            .table-container {
                background-color: white;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }
            .data-table {
                width: 100%;
                border-collapse: collapse;
            }
            .data-table th {
                background-color: #f8f9fa;
                padding: 12px 15px;
                text-align: left;
                border-bottom: 2px solid #dee2e6;
                font-weight: 600;
            }
            .data-table td {
                padding: 12px 15px;
                border-bottom: 1px solid #dee2e6;
            }
            .data-table tr:hover {
                background-color: #f1f1f1;
            }
            .actions {
                display: flex;
                gap: 5px;
            }
            .btn-action {
                padding: 5px 10px;
                text-decoration: none;
                border-radius: 3px;
                font-size: 12px;
            }
            .btn-delete {
                background-color: #dc3545;
                color: white;
            }
            .empty-state {
                text-align: center;
                padding: 40px;
                color: #6c757d;
            }
        </style>
    </head>
    <body>
        <div class="page-header">
            <div>
                <h1>📚 Gerenciar Disciplinas da Turma</h1>
                <p>Vincule disciplinas e professores à turma: <strong>${turma.nomeTurma}</strong></p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/admin/turmas" class="btn-back">
                    ⬅️ Voltar para Turmas
                </a>
                <a href="${pageContext.request.contextPath}/admin/gerenciar-disciplinas-turma?action=new&idTurma=${turma.idTurma}" class="btn-new">
                    ➕ Vincular Nova Disciplina
                </a>
            </div>
        </div>

        <c:if test="${param.success == '1'}">
            <div class="success-message"><strong>Sucesso:</strong> Disciplina vinculada com sucesso!</div>
        </c:if>
        <c:if test="${param.deleted == '1'}">
            <div class="success-message"><strong>Sucesso:</strong> Vínculo com a disciplina foi desfeito!</div>
        </c:if>

        <div class="table-container">
            <c:choose>
                <c:when test="${empty vinculos}">
                    <div class="empty-state">
                        <h3>📚 Nenhuma disciplina vinculada</h3>
                        <p>Clique em "Vincular Nova Disciplina" para começar a adicionar.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Disciplina</th>
                                <th>Código</th>
                                <th>Professor Responsável</th>
                                <th style="width: 120px;">Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="vinculo" items="${vinculos}">
                                <tr>
                                    <td>${vinculo.disciplina.nomeDisciplina}</td>
                                    <td>${vinculo.disciplina.codigoDisciplina}</td>
                                    <td>${vinculo.professor.nomeCompleto}</td>
                                    <td>
                                        <div class="actions">
                                            <a href="${pageContext.request.contextPath}/admin/gerenciar-disciplinas-turma?action=delete&idVinculo=${vinculo.idDisciplinaTurma}&idTurma=${turma.idTurma}" class="btn-action btn-delete" title="Desvincular"
                                               onclick="return confirm('Tem certeza que deseja desvincular esta disciplina da turma?');">
                                                🗑️ Desvincular
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </body>
</html>