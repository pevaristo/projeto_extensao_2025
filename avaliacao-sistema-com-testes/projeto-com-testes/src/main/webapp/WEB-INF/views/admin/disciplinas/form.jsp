<%--
    DISCIPLINAS FORM.JSP - FORMULÁRIO DE CRIAÇÃO/EDIÇÃO DE DISCIPLINAS (CORRIGIDO)
    ==============================================================================
    
    Página JSP para formulário de criação e edição de disciplinas.
    Corrigido para usar apenas os campos da entidade Disciplina existente.
    
    CAMPOS DA ENTIDADE DISCIPLINA:
    - idDisciplina (Integer) - Chave primária
    - nomeDisciplina (String) - Nome da disciplina (obrigatório, max 255)
    - siglaDisciplina (String) - Sigla da disciplina (opcional, max 10)
    - ativa (Boolean) - Status ativo/inativo
    
    @version 1.1 - Corrigido para entidade existente
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>
        <c:choose>
            <c:when test="${action == 'edit'}">Editar Disciplina</c:when>
            <c:otherwise>Nova Disciplina</c:otherwise>
        </c:choose>
        - Sistema UNIFAE
    </title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">
    
    <style>
        /* Estilos específicos para formulário de disciplinas */
        .form-container {
            max-width: 600px;
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
        
        .form-checkbox-group {
            display: flex;
            align-items: center;
            gap: 8px;
        }
        
        .form-checkbox {
            width: auto;
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
        
        .error-message {
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            color: #721c24;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        
        .success-message {
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
            color: #155724;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        
        @media (max-width: 768px) {
            .form-container {
                margin: 10px;
                padding: 20px;
            }
            
            .form-actions {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
    <div class="form-container">
        <!-- Cabeçalho do formulário -->
        <div class="form-header">
            <h1>
                <c:choose>
                    <c:when test="${action == 'edit'}">✏️ Editar Disciplina</c:when>
                    <c:otherwise>➕ Nova Disciplina</c:otherwise>
                </c:choose>
            </h1>
            <p>
                <c:choose>
                    <c:when test="${action == 'edit'}">Altere os dados da disciplina conforme necessário</c:when>
                    <c:otherwise>Preencha os dados para cadastrar uma nova disciplina</c:otherwise>
                </c:choose>
            </p>
        </div>

        <!-- Mensagens de erro ou sucesso -->
        <c:if test="${not empty error}">
            <div class="error-message">
                <strong>Erro:</strong> ${error}
            </div>
        </c:if>

        <c:if test="${not empty success}">
            <div class="success-message">
                <strong>Sucesso:</strong> ${success}
            </div>
        </c:if>

        <!-- Formulário -->
        <form method="post" action="${pageContext.request.contextPath}/admin/disciplinas" id="disciplinaForm">
            <!-- Campo oculto para ID (apenas em edição) -->
            <c:if test="${action == 'edit'}">
                <input type="hidden" name="idDisciplina" value="${disciplina.idDisciplina}">
            </c:if>

            <!-- Nome da Disciplina -->
            <div class="form-group">
                <label for="nomeDisciplina" class="form-label required">Nome da Disciplina</label>
                <input type="text" 
                       id="nomeDisciplina" 
                       name="nomeDisciplina" 
                       class="form-input"
                       value="${disciplina.nomeDisciplina}"
                       placeholder="Ex: Anatomia Humana I"
                       maxlength="255"
                       required>
                <div class="form-help">Nome completo da disciplina (máximo 255 caracteres)</div>
            </div>

            <!-- Sigla da Disciplina -->
            <div class="form-group">
                <label for="siglaDisciplina" class="form-label">Sigla da Disciplina</label>
                <input type="text" 
                       id="siglaDisciplina" 
                       name="siglaDisciplina" 
                       class="form-input"
                       value="${disciplina.siglaDisciplina}"
                       placeholder="Ex: ANA001"
                       maxlength="10"
                       style="text-transform: uppercase;">
                <div class="form-help">Sigla ou código da disciplina (opcional, máximo 10 caracteres)</div>
            </div>

            <!-- Status Ativo -->
            <div class="form-group">
                <div class="form-checkbox-group">
                    <input type="checkbox" 
                           id="ativa" 
                           name="ativa" 
                           class="form-checkbox"
                           ${disciplina.ativa || action == 'new' ? 'checked' : ''}>
                    <label for="ativa" class="form-label">Disciplina Ativa</label>
                </div>
                <div class="form-help">Disciplinas inativas não aparecem nas listagens principais</div>
            </div>

            <!-- Ações do formulário -->
            <div class="form-actions">
                <a href="${pageContext.request.contextPath}/admin/disciplinas" class="btn btn-secondary">
                    ❌ Cancelar
                </a>
                <button type="submit" class="btn btn-primary">
                    <c:choose>
                        <c:when test="${action == 'edit'}">💾 Salvar Alterações</c:when>
                        <c:otherwise>➕ Cadastrar Disciplina</c:otherwise>
                    </c:choose>
                </button>
            </div>
        </form>
    </div>

    <script>
        // Validações e melhorias de UX
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('disciplinaForm');
            const nomeDisciplinaInput = document.getElementById('nomeDisciplina');
            const siglaDisciplinaInput = document.getElementById('siglaDisciplina');

            // Converte sigla para maiúsculo automaticamente
            siglaDisciplinaInput.addEventListener('input', function() {
                this.value = this.value.toUpperCase();
            });

            // Validação do nome da disciplina
            nomeDisciplinaInput.addEventListener('input', function() {
                if (this.value.length > 255) {
                    this.setCustomValidity('Nome da disciplina deve ter no máximo 255 caracteres');
                } else if (this.value.trim().length === 0) {
                    this.setCustomValidity('Nome da disciplina é obrigatório');
                } else {
                    this.setCustomValidity('');
                }
            });

            // Validação da sigla
            siglaDisciplinaInput.addEventListener('input', function() {
                if (this.value.length > 10) {
                    this.setCustomValidity('Sigla deve ter no máximo 10 caracteres');
                } else {
                    this.setCustomValidity('');
                }
            });

            // Validação antes do envio
            form.addEventListener('submit', function(e) {
                let isValid = true;
                let errorMessage = '';

                // Validar nome da disciplina
                const nomeDisciplina = nomeDisciplinaInput.value.trim();
                if (!nomeDisciplina) {
                    isValid = false;
                    errorMessage += 'Nome da disciplina é obrigatório.\n';
                } else if (nomeDisciplina.length > 255) {
                    isValid = false;
                    errorMessage += 'Nome da disciplina deve ter no máximo 255 caracteres.\n';
                }

                // Validar sigla (se preenchida)
                const siglaDisciplina = siglaDisciplinaInput.value.trim();
                if (siglaDisciplina && siglaDisciplina.length > 10) {
                    isValid = false;
                    errorMessage += 'Sigla da disciplina deve ter no máximo 10 caracteres.\n';
                }

                if (!isValid) {
                    e.preventDefault();
                    alert('Por favor, corrija os seguintes erros:\n\n' + errorMessage);
                }
            });

            // Foco inicial no campo nome
            nomeDisciplinaInput.focus();
        });
    </script>
</body>
</html>
