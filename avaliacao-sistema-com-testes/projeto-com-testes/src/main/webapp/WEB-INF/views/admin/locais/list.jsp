<%--
    LOCAIS LIST.JSP - PÁGINA DE LISTAGEM DE LOCAIS DE EVENTO
    ========================================================
    
    Página JSP para exibir lista de locais de evento cadastrados no sistema.
    Padronizada seguindo o modelo visual dos arquivos de alunos.
    
    FUNCIONALIDADES:
    - Listagem de todos os locais
    - Filtros e busca específicos para locais
    - Ações de editar e excluir
    - Link para criar novo local
    - Indicadores visuais de tipo e localização
    - Estatísticas de locais por tipo
    
    @version 2.0 - Padronizada
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Gerenciar Locais - Sistema UNIFAE</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">
    
    <style>
        /* Estilos específicos para listagem de locais */
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
        
        .tipo-badge {
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
        
        .local-info {
            display: flex;
            flex-direction: column;
            gap: 2px;
        }
        
        .local-name {
            font-weight: 600;
            color: #333;
        }
        
        .local-endereco {
            font-size: 12px;
            color: #6c757d;
            max-width: 200px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
        
        .success-message {
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
            color: #155724;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <!-- Cabeçalho da página -->
    <div class="page-header">
        <div>
            <h1>📍 Gerenciar Locais</h1>
            <p>Cadastro e manutenção dos locais de evento</p>
        </div>
        <a href="${pageContext.request.contextPath}/admin/locais?action=new" class="btn-new">
            ➕ Novo Local
        </a>
    </div>

    <!-- Mensagens de sucesso -->
    <c:if test="${param.success == '1'}">
        <div class="success-message">
            <strong>Sucesso:</strong> Local salvo com sucesso!
        </div>
    </c:if>
    <c:if test="${param.deleted == '1'}">
        <div class="success-message">
            <strong>Sucesso:</strong> Local excluído com sucesso!
        </div>
    </c:if>

    <!-- Seção de estatísticas -->
    <div class="stats-section">
        <div class="stat-card">
            <div class="stat-number">
                <c:set var="totalLocais" value="0" />
                <c:forEach var="local" items="${listLocais}">
                    <c:set var="totalLocais" value="${totalLocais + 1}" />
                </c:forEach>
                ${totalLocais}
            </div>
            <div class="stat-label">Total de Locais</div>
        </div>
        
        <div class="stat-card">
            <div class="stat-number">
                <c:set var="hospitais" value="0" />
                <c:forEach var="local" items="${listLocais}">
                    <c:if test="${local.tipoLocal == 'Hospital'}">
                        <c:set var="hospitais" value="${hospitais + 1}" />
                    </c:if>
                </c:forEach>
                ${hospitais}
            </div>
            <div class="stat-label">Hospitais</div>
        </div>
        
        <div class="stat-card">
            <div class="stat-number">
                <c:set var="ambulatorios" value="0" />
                <c:forEach var="local" items="${listLocais}">
                    <c:if test="${local.tipoLocal == 'Ambulatório'}">
                        <c:set var="ambulatorios" value="${ambulatorios + 1}" />
                    </c:if>
                </c:forEach>
                ${ambulatorios}
            </div>
            <div class="stat-label">Ambulatórios</div>
        </div>
        
        <div class="stat-card">
            <div class="stat-number">
                <c:set var="outros" value="0" />
                <c:forEach var="local" items="${listLocais}">
                    <c:if test="${local.tipoLocal != 'Hospital' && local.tipoLocal != 'Ambulatório'}">
                        <c:set var="outros" value="${outros + 1}" />
                    </c:if>
                </c:forEach>
                ${outros}
            </div>
            <div class="stat-label">Outros Tipos</div>
        </div>
    </div>

    <!-- Seção de busca e filtros -->
    <div class="search-section">
        <form class="search-form" method="get">
            <input type="text" name="filtroNome" class="search-input" 
                   placeholder="Buscar por nome do local..." 
                   value="${param.filtroNome}">
            
            <input type="text" name="filtroTipo" class="search-input" 
                   placeholder="Buscar por tipo..." 
                   value="${param.filtroTipo}">
            
            <input type="text" name="filtroId" class="search-input" 
                   placeholder="Buscar por ID..." 
                   value="${param.filtroId}">
            
            <button type="submit" class="btn-search">🔍 Buscar</button>
            <a href="${pageContext.request.contextPath}/admin/locais" class="btn-clear">🗑️ Limpar</a>
        </form>
    </div>

    <!-- Tabela de locais -->
    <div class="table-container">
        <c:choose>
            <c:when test="${empty listLocais}">
                <div class="empty-state">
                    <h3>📍 Nenhum local cadastrado</h3>
                    <p>Comece criando seu primeiro local clicando no botão "Novo Local" acima.</p>
                </div>
            </c:when>
            <c:otherwise>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Dados do Local</th>
                            <th>Tipo</th>
                            <th>Localização</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="local" items="${listLocais}">
                            <tr>
                                <td>${local.idLocalEvento}</td>
                                <td>
                                    <div class="local-info">
                                        <div class="local-name">${local.nomeLocal}</div>
                                        <div class="local-endereco">${local.endereco}</div>
                                    </div>
                                </td>
                                <td>
                                    <span class="tipo-badge">${local.tipoLocal}</span>
                                </td>
                                <td>
                                    <div>
                                        <strong>${local.cidade}</strong><br>
                                        <small style="color: #6c757d;">${local.estado}</small>
                                    </div>
                                </td>
                                <td>
                                    <div class="actions">
                                        <a href="${pageContext.request.contextPath}/admin/locais?action=edit&id=${local.idLocalEvento}" 
                                           class="btn-action btn-edit" title="Editar">
                                            ✏️ Editar
                                        </a>
                                        
                                        <a href="${pageContext.request.contextPath}/admin/locais?action=delete&id=${local.idLocalEvento}" 
                                           class="btn-action btn-delete" title="Excluir"
                                           onclick="return confirm('Tem certeza que deseja excluir este local?\n\nAtenção: Esta ação não pode ser desfeita e pode afetar eventos já agendados.')">
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
        <a href="${pageContext.request.contextPath}/" class="btn-action btn-clear">
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
                    if (!confirm('Tem certeza que deseja excluir este local?\n\nAtenção: Esta ação não pode ser desfeita e pode afetar eventos já agendados.')) {
                        e.preventDefault();
                    }
                });
            });
        });
    </script>
</body>
</html>
