<%-- /WEB-INF/views/admin/competencias/form.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>
            <c:choose>
                <c:when test="${action == 'edit'}">Editar Competência</c:when>
                <c:otherwise>Nova Competência</c:otherwise>
            </c:choose>
            - Sistema UNIFAE
        </title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">
        <style>
            .form-container {
                max-width: 900px;
                margin: 0 auto;
                background-color: white;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }
            .form-header {
                background-color: #007bff;
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
                margin-bottom: 20px;
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
            .form-label.required::after {
                content: " *";
                color: #dc3545;
            }
            .form-input, .form-select {
                width: 100%;
                padding: 10px 12px;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 14px;
                transition: border-color 0.3s;
            }
            .form-input:focus, .form-select:focus {
                outline: none;
                border-color: #007bff;
                box-shadow: 0 0 0 2px rgba(0,123,255,0.25);
            }
            .form-textarea {
                min-height: 100px;
                resize: vertical;
            }
            .form-help {
                font-size: 12px;
                color: #6c757d;
                margin-top: 5px;
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
                background-color: #007bff;
                color: white;
            }
            .btn-primary:hover {
                background-color: #0069d9;
            }
            .btn-secondary {
                background-color: #6c757d;
                color: white;
            }
            .btn-secondary:hover {
                background-color: #545b62;
            }
            .error-message {
                background-color: #f8d7da;
                border: 1px solid #f5c6cb;
                color: #721c24;
                padding: 10px;
                border-radius: 4px;
                margin-bottom: 20px;
            }
            .form-row {
                display: flex;
                gap: 20px;
            }
            .form-row .form-group {
                flex: 1;
            }
            .form-check {
                display: flex;
                align-items: center;
                gap: 10px;
            }
            .form-check input {
                width: auto;
            }
        </style>
    </head>
    <body>
        <div class="form-container">
            <div class="form-header">
                <h1>
                    <c:choose>
                        <c:when test="${action == 'edit'}">✏️ Editar Competência</c:when>
                        <c:otherwise>➕ Nova Competência</c:otherwise>
                    </c:choose>
                </h1>
                <p>Questionário: <strong>${questionario.nomeModelo}</strong></p>
            </div>

            <form method="post" action="${pageContext.request.contextPath}/admin/competencias">
                <c:if test="${action == 'edit'}">
                    <input type="hidden" name="idCompetenciaQuestionario" value="${competencia.idCompetenciaQuestionario}">
                </c:if>
                <input type="hidden" name="questionarioId" value="${questionario.idQuestionario}">

                <div class="form-section">
                    <div class="form-group">
                        <label for="nomeCompetencia" class="form-label required">Nome da Competência</label>
                        <input type="text" id="nomeCompetencia" name="nomeCompetencia" class="form-input" value="${competencia.nomeCompetencia}" required maxlength="255">
                    </div>

                    <div class="form-group">
                        <label for="descricaoPrompt" class="form-label">Descrição/Prompt</label>
                        <textarea id="descricaoPrompt" name="descricaoPrompt" class="form-input form-textarea">${competencia.descricaoPrompt}</textarea>
                        <div class="form-help">Texto que aparecerá como instrução para quem está avaliando esta competência.</div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="tipoItem" class="form-label required">Tipo do Item</label>
                            <select id="tipoItem" name="tipoItem" class="form-select" required>
                                <option value="escala_numerica" ${competencia.tipoItem == 'escala_numerica' ? 'selected' : ''}>Escala Numérica</option>
                                <option value="texto_livre" ${competencia.tipoItem == 'texto_livre' ? 'selected' : ''}>Texto Livre</option>
                                <option value="multipla_escolha" ${competencia.tipoItem == 'multipla_escolha' ? 'selected' : ''}>Múltipla Escolha</option>
                                <option value="checkbox" ${competencia.tipoItem == 'checkbox' ? 'selected' : ''}>Checkbox</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="ordemExibicao" class="form-label required">Ordem de Exibição</label>
                            <input type="number" id="ordemExibicao" name="ordemExibicao" class="form-input" value="${competencia.ordemExibicao != null ? competencia.ordemExibicao : 0}" required>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label class="form-label">Configurações</label>
                            <div class="form-check">
                                <input type="checkbox" id="obrigatorio" name="obrigatorio" ${competencia.obrigatorio ? 'checked' : ''}>
                                <label for="obrigatorio">Obrigatório</label>
                            </div>
                            <div class="form-check">
                                <input type="checkbox" id="ativo" name="ativo" ${competencia.ativo ? 'checked' : ''}>
                                <label for="ativo">Ativo</label>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/admin/competencias?questionarioId=${questionario.idQuestionario}" class="btn btn-secondary">
                        ❌ Cancelar
                    </a>
                    <button type="submit" class="btn btn-primary">
                        <c:choose>
                            <c:when test="${action == 'edit'}">💾 Salvar Alterações</c:when>
                            <c:otherwise>➕ Criar Competência</c:otherwise>
                        </c:choose>
                    </button>
                </div>
            </form>
        </div>
    </body>
</html>
