<%-- /WEB-INF/views/admin/competencias/list.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Gerenciar Competências - ${questionario.nomeModelo}</title>
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
                background-color: #007bff;
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
                transition: background-color 0.3s;
            }
            .btn-new:hover {
                background-color: #218838;
            }
            .search-section {
                background-color: #f8f9fa;
                padding: 15px;
                border-radius: 5px;
                margin-bottom: 20px;
                border: 1px solid #dee2e6;
            }
            .search-form {
                display: flex;
                gap: 10px;
                align-items: center;
                flex-wrap: wrap;
            }
            .search-input {
                padding: 8px 12px;
                border: 1px solid #ddd;
                border-radius: 4px;
                min-width: 200px;
            }
            .btn-search {
                background-color: #007bff;
                color: white;
                padding: 8px 16px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
            }
            .btn-clear {
                background-color: #6c757d;
                color: white;
                padding: 8px 16px;
                border: none;
                border-radius: 4px;
                text-decoration: none;
                display: inline-block;
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
                font-weight: 500;
                transition: opacity 0.2s;
            }
            .btn-edit {
                background-color: #ffc107;
                color: #212529;
            }
            .btn-delete {
                background-color: #dc3545;
                color: white;
            }
            .btn-action:hover {
                opacity: 0.8;
            }
            .empty-state {
                text-align: center;
                padding: 40px;
                color: #6c757d;
            }
            .back-link-container {
                margin-top: 20px;
            }
            .success-message {
                background-color: #d4edda;
                border: 1px solid #c3e6cb;
                color: #155724;
                padding: 10px;
                border-radius: 4px;
                margin-bottom: 20px;
            }
            .status-badge {
                padding: 3px 8px;
                border-radius: 10px;
                font-size: 12px;
                color: white;
            }
            .status-true {
                background-color: #28a745;
            }
            .status-false {
                background-color: #dc3545;
            }
        </style>
    </head>
    <body>
        <div class="page-header">
            <div>
                <h1>🛠️ Gerenciar Competências</h1>
                <p>Questionário: <strong>${questionario.nomeModelo}</strong></p>
            </div>
            <a href="${pageContext.request.contextPath}/admin/competencias?action=new&questionarioId=${questionario.idQuestionario}" class="btn-new">
                ➕ Nova Competência
            </a>
        </div>

        <c:if test="${param.success == '1'}">
            <div class="success-message"><strong>Sucesso:</strong> Competência salva com sucesso!</div>
        </c:if>
        <c:if test="${param.deleted == '1'}">
            <div class="success-message"><strong>Sucesso:</strong> Competência excluída com sucesso!</div>
        </c:if>

        <div class="search-section">
            <form class="search-form" action="${pageContext.request.contextPath}/admin/competencias" method="get">
                <input type="hidden" name="questionarioId" value="${questionario.idQuestionario}">
                <input type="text" name="search" class="search-input" placeholder="Buscar por nome da competência..." value="${param.search}">
                <button type="submit" class="btn-search">🔍 Buscar</button>
                <a href="${pageContext.request.contextPath}/admin/competencias?questionarioId=${questionario.idQuestionario}" class="btn-clear">🗑️ Limpar</a>
            </form>
        </div>

        <div class="table-container">
            <c:choose>
                <c:when test="${empty listCompetencias}">
                    <div class="empty-state">
                        <h3>📋 Nenhuma competência encontrada</h3>
                        <p>Crie uma nova competência para este questionário.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Ordem</th>
                                <th>Nome da Competência</th>
                                <th>Tipo</th>
                                <th>Obrigatório</th>
                                <th>Ativo</th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="competencia" items="${listCompetencias}">
                                <tr>
                                    <td>${competencia.ordemExibicao}</td>
                                    <td>${competencia.nomeCompetencia}</td>
                                    <td>${competencia.tipoItem.toString().replace("_", " ")}</td>
                                    <td>
                                        <span class="status-badge status-${competencia.obrigatorio}">${competencia.obrigatorio ? 'Sim' : 'Não'}</span>
                                    </td>
                                    <td>
                                        <span class="status-badge status-${competencia.ativo}">${competencia.ativo ? 'Sim' : 'Não'}</span>
                                    </td>
                                    <td>
                                        <div class="actions">
                                            <a href="competencias?action=edit&id=${competencia.idCompetenciaQuestionario}" class="btn-action btn-edit" title="Editar">✏️ Editar</a>
                                            <a href="competencias?action=delete&id=${competencia.idCompetenciaQuestionario}&questionarioId=${questionario.idQuestionario}" class="btn-action btn-delete" title="Excluir">🗑️ Excluir</a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="back-link-container">
            <a href="${pageContext.request.contextPath}/admin/questionarios" class="btn-action" style="background-color: #6c757d; color: white;">
                ⬅️ Voltar para Questionários
            </a>
        </div>

        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const deleteLinks = document.querySelectorAll('a.btn-delete');
                deleteLinks.forEach(link => {
                    link.addEventListener('click', function (e) {
                        if (!confirm('Tem certeza que deseja excluir esta competência?')) {
                            e.preventDefault();
                        }
                    });
                });
            });
        </script>
    </body>
</html>
