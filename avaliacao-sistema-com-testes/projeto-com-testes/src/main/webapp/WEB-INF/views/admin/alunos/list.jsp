<%--
    ALUNOS LIST.JSP - PÁGINA DE LISTAGEM DE ALUNOS
    ==============================================
    
    Página JSP para exibir lista de alunos (usuários do tipo ESTUDANTE) cadastrados no sistema.
    Segue o padrão visual e funcional já estabelecido no projeto.
    
    FUNCIONALIDADES:
    - Listagem de todos os alunos
    - Filtros e busca específicos para alunos
    - Ações de editar, excluir e ativar/desativar
    - Link para criar novo aluno
    - Indicadores visuais de status e período
    - Estatísticas de alunos por período
    
    RELACIONAMENTO COM OUTROS ARQUIVOS:
    - AlunoServlet.java: Controlador que fornece dados
    - form.jsp: Formulário de criação/edição
    - css/formularios.css: Estilos compartilhados
    
    @version 1.0
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Gerenciar Alunos - Sistema UNIFAE</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">
    
    <style>
        /* Estilos específicos para listagem de alunos */
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
        
        .search-section {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
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
            background-color: #28a745;
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
            cursor: pointer;
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
        
        .status-badge {
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: bold;
        }
        
        .status-ativo {
            background-color: #d4edda;
            color: #155724;
        }
        
        .status-inativo {
            background-color: #f8d7da;
            color: #721c24;
        }
        
        .periodo-badge {
            background-color: #e7f3ff;
            color: #0056b3;
            padding: 3px 8px;
            border-radius: 12px;
            font-size: 11px;
            font-weight: 600;
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
        }
        
        .btn-edit {
            background-color: #ffc107;
            color: #212529;
        }
        
        .btn-delete {
            background-color: #dc3545;
            color: white;
        }
        
        .btn-toggle {
            background-color: #6c757d;
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
        
        .stats-section {
            display: flex;
            gap: 20px;
            margin-bottom: 20px;
        }
        
        .stat-card {
            background-color: white;
            padding: 15px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            flex: 1;
            text-align: center;
        }
        
        .stat-number {
            font-size: 24px;
            font-weight: bold;
            color: #28a745;
        }
        
        .stat-label {
            color: #6c757d;
            font-size: 14px;
        }
        
        .student-info {
            display: flex;
            flex-direction: column;
            gap: 2px;
        }
        
        .student-name {
            font-weight: 600;
            color: #333;
        }
        
        .student-email {
            font-size: 12px;
            color: #6c757d;
        }
        
        .student-ra {
            font-size: 11px;
            color: #007bff;
            font-weight: 500;
        }
    </style>
</head>
<body>
    <!-- Cabeçalho da página -->
    <div class="page-header">
        <div>
            <h1>👨‍🎓 Gerenciar Alunos</h1>
            <p>Cadastro e manutenção dos estudantes do curso</p>
        </div>
        <a href="${pageContext.request.contextPath}/admin/alunos?action=new" class="btn-new">
            ➕ Novo Aluno
        </a>
    </div>

    <!-- Seção de estatísticas -->
    <div class="stats-section">
        <div class="stat-card">
            <div class="stat-number">
                <c:set var="totalAlunos" value="0" />
                <c:forEach var="aluno" items="${listAlunos}">
                    <c:set var="totalAlunos" value="${totalAlunos + 1}" />
                </c:forEach>
                ${totalAlunos}
            </div>
            <div class="stat-label">Total de Alunos</div>
        </div>
        
        <div class="stat-card">
            <div class="stat-number">
                <c:set var="alunosAtivos" value="0" />
                <c:forEach var="aluno" items="${listAlunos}">
                    <c:if test="${aluno.ativo}">
                        <c:set var="alunosAtivos" value="${alunosAtivos + 1}" />
                    </c:if>
                </c:forEach>
                ${alunosAtivos}
            </div>
            <div class="stat-label">Alunos Ativos</div>
        </div>
        
        <div class="stat-card">
            <div class="stat-number">
                <c:set var="alunosComPeriodo" value="0" />
                <c:forEach var="aluno" items="${listAlunos}">
                    <c:if test="${not empty aluno.periodoAtualAluno}">
                        <c:set var="alunosComPeriodo" value="${alunosComPeriodo + 1}" />
                    </c:if>
                </c:forEach>
                ${alunosComPeriodo}
            </div>
            <div class="stat-label">Com Período Definido</div>
        </div>
        
        <div class="stat-card">
            <div class="stat-number">
                <c:set var="totalTurmas" value="0" />
                <c:forEach var="turma" items="${listTurmas}">
                    <c:set var="totalTurmas" value="${totalTurmas + 1}" />
                </c:forEach>
                ${totalTurmas}
            </div>
            <div class="stat-label">Turmas Disponíveis</div>
        </div>
    </div>

    <!-- Seção de busca e filtros -->
    <div class="search-section">
        <form class="search-form" method="get">
            <input type="text" name="search" class="search-input" 
                   placeholder="Buscar por nome, email ou RA..." 
                   value="${param.search}">
            
            <select name="status" class="search-input">
                <option value="">Todos os Status</option>
                <option value="ativo" ${param.status == 'ativo' ? 'selected' : ''}>Apenas Ativos</option>
                <option value="inativo" ${param.status == 'inativo' ? 'selected' : ''}>Apenas Inativos</option>
            </select>
            
            <select name="periodo" class="search-input">
                <option value="">Todos os Períodos</option>
                <option value="1" ${param.periodo == '1' ? 'selected' : ''}>1º Período</option>
                <option value="2" ${param.periodo == '2' ? 'selected' : ''}>2º Período</option>
                <option value="3" ${param.periodo == '3' ? 'selected' : ''}>3º Período</option>
                <option value="4" ${param.periodo == '4' ? 'selected' : ''}>4º Período</option>
                <option value="5" ${param.periodo == '5' ? 'selected' : ''}>5º Período</option>
                <option value="6" ${param.periodo == '6' ? 'selected' : ''}>6º Período</option>
                <option value="7" ${param.periodo == '7' ? 'selected' : ''}>7º Período</option>
                <option value="8" ${param.periodo == '8' ? 'selected' : ''}>8º Período</option>
                <option value="9" ${param.periodo == '9' ? 'selected' : ''}>9º Período</option>
                <option value="10" ${param.periodo == '10' ? 'selected' : ''}>10º Período</option>
                <option value="11" ${param.periodo == '11' ? 'selected' : ''}>11º Período</option>
                <option value="12" ${param.periodo == '12' ? 'selected' : ''}>12º Período</option>
            </select>
            
            <button type="submit" class="btn-search">🔍 Buscar</button>
            <a href="${pageContext.request.contextPath}/admin/alunos" class="btn-clear">🗑️ Limpar</a>
        </form>
    </div>

    <!-- Tabela de alunos -->
    <div class="table-container">
        <c:choose>
            <c:when test="${empty listAlunos}">
                <div class="empty-state">
                    <h3>👨‍🎓 Nenhum aluno cadastrado</h3>
                    <p>Comece criando seu primeiro aluno clicando no botão "Novo Aluno" acima.</p>
                </div>
            </c:when>
            <c:otherwise>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Dados do Aluno</th>
                            <th>Período</th>
                            <th>Telefone</th>
                            <th>Status</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="aluno" items="${listAlunos}">
                            <tr>
                                <td>${aluno.idUsuario}</td>
                                <td>
                                    <div class="student-info">
                                        <div class="student-name">${aluno.nomeCompleto}</div>
                                        <div class="student-email">${aluno.email}</div>
                                        <div class="student-ra">RA: ${aluno.matriculaRA}</div>
                                    </div>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty aluno.periodoAtualAluno}">
                                            <span class="periodo-badge">${aluno.periodoAtualAluno}º Período</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: #6c757d;">Não definido</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty aluno.telefone}">
                                            ${aluno.telefone}
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: #6c757d;">-</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <span class="status-badge ${aluno.ativo ? 'status-ativo' : 'status-inativo'}">
                                        ${aluno.ativo ? '✅ Ativo' : '❌ Inativo'}
                                    </span>
                                </td>
                                <td>
                                    <div class="actions">
                                        <a href="${pageContext.request.contextPath}/admin/alunos?action=edit&id=${aluno.idUsuario}" 
                                           class="btn-action btn-edit" title="Editar">
                                            ✏️ Editar
                                        </a>
                                        
                                        <a href="${pageContext.request.contextPath}/admin/alunos?action=toggle&id=${aluno.idUsuario}" 
                                           class="btn-action btn-toggle" 
                                           title="${aluno.ativo ? 'Desativar' : 'Ativar'}"
                                           onclick="return confirm('Tem certeza que deseja ${aluno.ativo ? 'desativar' : 'ativar'} este aluno?')">
                                            ${aluno.ativo ? '🔒 Desativar' : '🔓 Ativar'}
                                        </a>
                                        
                                        <a href="${pageContext.request.contextPath}/admin/alunos?action=delete&id=${aluno.idUsuario}" 
                                           class="btn-action btn-delete" title="Excluir"
                                           onclick="return confirm('Tem certeza que deseja excluir este aluno? Esta ação não pode ser desfeita.')">
                                            🗑️ Excluir
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

    <!-- Link de volta -->
    <div style="margin-top: 20px; text-align: center;">
        <a href="${pageContext.request.contextPath}/" class="btn-action btn-toggle">
            🏠 Voltar ao Início
        </a>
    </div>

    <script>
        // Confirmação para ações destrutivas
        document.addEventListener('DOMContentLoaded', function() {
            // Adiciona confirmação para links de exclusão
            const deleteLinks = document.querySelectorAll('a[href*="action=delete"]');
            deleteLinks.forEach(link => {
                link.addEventListener('click', function(e) {
                    if (!confirm('Tem certeza que deseja excluir este aluno? Esta ação não pode ser desfeita.')) {
                        e.preventDefault();
                    }
                });
            });
            
            // Adiciona confirmação para links de toggle
            const toggleLinks = document.querySelectorAll('a[href*="action=toggle"]');
            toggleLinks.forEach(link => {
                link.addEventListener('click', function(e) {
                    const isActive = link.textContent.includes('Desativar');
                    const action = isActive ? 'desativar' : 'ativar';
                    if (!confirm(`Tem certeza que deseja ${action} este aluno?`)) {
                        e.preventDefault();
                    }
                });
            });
        });
    </script>
</body>
</html>
