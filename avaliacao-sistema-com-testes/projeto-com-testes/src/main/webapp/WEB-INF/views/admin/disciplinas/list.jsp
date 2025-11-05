<%--
    DISCIPLINAS LIST.JSP - PÁGINA DE LISTAGEM DE DISCIPLINAS (CORRIGIDA)
    =====================================================================
    
    Página JSP para exibir lista de disciplinas cadastradas no sistema.
    Corrigida para usar apenas os campos da entidade Disciplina existente.
    
    CAMPOS DA ENTIDADE DISCIPLINA:
    - idDisciplina (Integer) - Chave primária
    - nomeDisciplina (String) - Nome da disciplina
    - siglaDisciplina (String) - Sigla da disciplina
    - ativa (Boolean) - Status ativo/inativo
    
    @version 1.1 - Corrigida para entidade existente
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Gerenciar Disciplinas - Sistema UNIFAE</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">
    
    <style>
        /* Estilos específicos para listagem de disciplinas */
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
        
        .disciplina-name {
            font-weight: 600;
            color: #333;
        }
        
        .disciplina-sigla {
            background-color: #e7f3ff;
            color: #0056b3;
            padding: 2px 6px;
            border-radius: 4px;
            font-size: 11px;
            font-weight: 600;
            margin-left: 8px;
        }
        
        @media (max-width: 768px) {
            .page-header {
                flex-direction: column;
                gap: 10px;
                text-align: center;
            }
            
            .search-form {
                flex-direction: column;
                align-items: stretch;
            }
            
            .search-input {
                min-width: auto;
            }
            
            .stats-section {
                flex-direction: column;
            }
            
            .actions {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
    <!-- Cabeçalho da página -->
    <div class="page-header">
        <div>
            <h1>📚 Gerenciar Disciplinas</h1>
            <p>Cadastro e controle das disciplinas do curso</p>
        </div>
        <div>
            <a href="${pageContext.request.contextPath}/admin/disciplinas?action=new" class="btn-new">
                ➕ Nova Disciplina
            </a>
        </div>
    </div>

    <!-- Seção de estatísticas -->
    <div class="stats-section">
        <div class="stat-card">
            <div class="stat-number">
                <c:set var="totalDisciplinas" value="0" />
                <c:forEach var="disciplina" items="${listDisciplinas}">
                    <c:set var="totalDisciplinas" value="${totalDisciplinas + 1}" />
                </c:forEach>
                ${totalDisciplinas}
            </div>
            <div class="stat-label">Total de Disciplinas</div>
        </div>
        
        <div class="stat-card">
            <div class="stat-number">
                <c:set var="disciplinasAtivas" value="0" />
                <c:forEach var="disciplina" items="${listDisciplinas}">
                    <c:if test="${disciplina.ativa}">
                        <c:set var="disciplinasAtivas" value="${disciplinasAtivas + 1}" />
                    </c:if>
                </c:forEach>
                ${disciplinasAtivas}
            </div>
            <div class="stat-label">Disciplinas Ativas</div>
        </div>
        
        <div class="stat-card">
            <div class="stat-number">
                <c:set var="disciplinasInativas" value="0" />
                <c:forEach var="disciplina" items="${listDisciplinas}">
                    <c:if test="${not disciplina.ativa}">
                        <c:set var="disciplinasInativas" value="${disciplinasInativas + 1}" />
                    </c:if>
                </c:forEach>
                ${disciplinasInativas}
            </div>
            <div class="stat-label">Disciplinas Inativas</div>
        </div>
        
        <div class="stat-card">
            <div class="stat-number">
                <c:set var="disciplinasComSigla" value="0" />
                <c:forEach var="disciplina" items="${listDisciplinas}">
                    <c:if test="${not empty disciplina.siglaDisciplina}">
                        <c:set var="disciplinasComSigla" value="${disciplinasComSigla + 1}" />
                    </c:if>
                </c:forEach>
                ${disciplinasComSigla}
            </div>
            <div class="stat-label">Com Sigla Definida</div>
        </div>
    </div>

    <!-- Seção de busca e filtros -->
    <div class="search-section">
        <form class="search-form" method="get">
            <input type="text" 
                   name="busca" 
                   class="search-input" 
                   placeholder="Buscar por nome ou sigla da disciplina..."
                   value="${param.busca}">
            
            <select name="status" class="search-input" style="min-width: 150px;">
                <option value="">Todos os Status</option>
                <option value="ativa" ${param.status == 'ativa' ? 'selected' : ''}>Apenas Ativas</option>
                <option value="inativa" ${param.status == 'inativa' ? 'selected' : ''}>Apenas Inativas</option>
            </select>
            
            <button type="submit" class="btn-search">🔍 Buscar</button>
            <a href="${pageContext.request.contextPath}/admin/disciplinas" class="btn-clear">🗑️ Limpar</a>
        </form>
    </div>

    <!-- Tabela de disciplinas -->
    <div class="table-container">
        <c:choose>
            <c:when test="${empty listDisciplinas}">
                <div class="empty-state">
                    <h3>📚 Nenhuma disciplina cadastrada</h3>
                    <p>Comece cadastrando a primeira disciplina clicando no botão "Nova Disciplina" acima.</p>
                </div>
            </c:when>
            <c:otherwise>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nome da Disciplina</th>
                            <th>Sigla</th>
                            <th>Status</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="disciplina" items="${listDisciplinas}">
                            <tr>
                                <td>${disciplina.idDisciplina}</td>
                                <td>
                                    <span class="disciplina-name">${disciplina.nomeDisciplina}</span>
                                    <c:if test="${not empty disciplina.siglaDisciplina}">
                                        <span class="disciplina-sigla">${disciplina.siglaDisciplina}</span>
                                    </c:if>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty disciplina.siglaDisciplina}">
                                            <strong>${disciplina.siglaDisciplina}</strong>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: #6c757d; font-style: italic;">Não definida</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <span class="status-badge ${disciplina.ativa ? 'status-ativo' : 'status-inativo'}">
                                        ${disciplina.ativa ? '✅ Ativa' : '❌ Inativa'}
                                    </span>
                                </td>
                                <td>
                                    <div class="actions">
                                        <a href="${pageContext.request.contextPath}/admin/disciplinas?action=edit&id=${disciplina.idDisciplina}" 
                                           class="btn-action btn-edit" title="Editar">
                                            ✏️ Editar
                                        </a>
                                        
                                        <a href="${pageContext.request.contextPath}/admin/disciplinas?action=toggle&id=${disciplina.idDisciplina}" 
                                           class="btn-action btn-toggle" 
                                           title="${disciplina.ativa ? 'Desativar' : 'Ativar'}"
                                           onclick="return confirm('Tem certeza que deseja ${disciplina.ativa ? 'desativar' : 'ativar'} esta disciplina?')">
                                            ${disciplina.ativa ? '🔒 Desativar' : '🔓 Ativar'}
                                        </a>
                                        
                                        <a href="${pageContext.request.contextPath}/admin/disciplinas?action=delete&id=${disciplina.idDisciplina}" 
                                           class="btn-action btn-delete" title="Excluir"
                                           onclick="return confirm('Tem certeza que deseja excluir esta disciplina? Esta ação não pode ser desfeita.')">
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
                    if (!confirm('Tem certeza que deseja excluir esta disciplina? Esta ação não pode ser desfeita.')) {
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
                    if (!confirm(`Tem certeza que deseja ${action} esta disciplina?`)) {
                        e.preventDefault();
                    }
                });
            });
        });
    </script>
</body>
</html>
