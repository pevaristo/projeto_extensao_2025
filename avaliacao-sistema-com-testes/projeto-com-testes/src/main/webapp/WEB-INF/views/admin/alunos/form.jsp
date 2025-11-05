<%--
    ALUNOS FORM.JSP - FORMULÁRIO DE CRIAÇÃO/EDIÇÃO DE ALUNOS
    ========================================================
    
    Página JSP para formulário de criação e edição de alunos (usuários do tipo ESTUDANTE).
    Segue o padrão visual e funcional já estabelecido no projeto.
    
    FUNCIONALIDADES:
    - Formulário unificado para criação e edição
    - Validações client-side e server-side específicas para alunos
    - Campos obrigatórios e opcionais para estudantes
    - Feedback visual de erros
    - Navegação de volta para listagem
    - Campos específicos: RA, período, observações
    
    RELACIONAMENTO COM OUTROS ARQUIVOS:
    - AlunoServlet.java: Processa o formulário
    - list.jsp: Listagem de alunos
    - css/formularios.css: Estilos compartilhados
    
    @version 1.0
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>
        <c:choose>
            <c:when test="${action == 'edit'}">Editar Aluno</c:when>
            <c:otherwise>Novo Aluno</c:otherwise>
        </c:choose>
        - Sistema UNIFAE
    </title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">
    
    <style>
        /* Estilos específicos para formulário de alunos */
        .form-container {
            max-width: 900px;
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
            margin-bottom: 30px;
            padding: 20px;
            border: 1px solid #e9ecef;
            border-radius: 6px;
            background-color: #f8f9fa;
        }
        
        .form-section h3 {
            margin: 0 0 15px 0;
            color: #495057;
            font-size: 16px;
            font-weight: 600;
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
        
        .form-textarea {
            min-height: 80px;
            resize: vertical;
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
        
        .form-row {
            display: flex;
            gap: 20px;
        }
        
        .form-col {
            flex: 1;
        }
        
        .form-col-small {
            flex: 0 0 150px;
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
        
        .password-strength {
            margin-top: 5px;
            font-size: 12px;
        }
        
        .strength-weak { color: #dc3545; }
        .strength-medium { color: #ffc107; }
        .strength-strong { color: #28a745; }
        
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
    <div class="form-container">
        <!-- Cabeçalho do formulário -->
        <div class="form-header">
            <h1>
                <c:choose>
                    <c:when test="${action == 'edit'}">✏️ Editar Aluno</c:when>
                    <c:otherwise>➕ Novo Aluno</c:otherwise>
                </c:choose>
            </h1>
            <p>
                <c:choose>
                    <c:when test="${action == 'edit'}">Altere os dados do aluno conforme necessário</c:when>
                    <c:otherwise>Preencha os dados para cadastrar um novo aluno</c:otherwise>
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
        <form method="post" action="${pageContext.request.contextPath}/admin/alunos" id="alunoForm">
            <!-- Campo oculto para ID (apenas em edição) -->
            <c:if test="${action == 'edit'}">
                <input type="hidden" name="idUsuario" value="${aluno.idUsuario}">
            </c:if>

            <!-- Seção: Dados Pessoais -->
            <div class="form-section">
                <h3>👤 Dados Pessoais</h3>
                
                <!-- Nome Completo -->
                <div class="form-group">
                    <label for="nomeCompleto" class="form-label required">Nome Completo</label>
                    <input type="text" 
                           id="nomeCompleto" 
                           name="nomeCompleto" 
                           class="form-input"
                           value="${aluno.nomeCompleto}"
                           placeholder="Ex: João Silva Santos"
                           required
                           maxlength="255">
                    <div class="form-help">Nome completo do aluno (máximo 255 caracteres)</div>
                </div>

                <!-- Email e Telefone -->
                <div class="form-row">
                    <div class="form-col">
                        <div class="form-group">
                            <label for="email" class="form-label required">Email</label>
                            <input type="email" 
                                   id="email" 
                                   name="email" 
                                   class="form-input"
                                   value="${aluno.email}"
                                   placeholder="Ex: joao.silva@unifae.br"
                                   required
                                   maxlength="254">
                            <div class="form-help">Email institucional ou pessoal do aluno</div>
                        </div>
                    </div>
                    
                    <div class="form-col">
                        <div class="form-group">
                            <label for="telefone" class="form-label">Telefone</label>
                            <input type="tel" 
                                   id="telefone" 
                                   name="telefone" 
                                   class="form-input"
                                   value="${aluno.telefone}"
                                   placeholder="Ex: 11999887766"
                                   maxlength="11">
                            <div class="form-help">Telefone de contato (apenas números)</div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Seção: Dados Acadêmicos -->
            <div class="form-section">
                <h3>🎓 Dados Acadêmicos</h3>
                
                <!-- RA e Período -->
                <div class="form-row">
                    <div class="form-col">
                        <div class="form-group">
                            <label for="matriculaRA" class="form-label required">Registro Acadêmico (RA)</label>
                            <input type="text" 
                                   id="matriculaRA" 
                                   name="matriculaRA" 
                                   class="form-input"
                                   value="${aluno.matriculaRA}"
                                   placeholder="Ex: 123456"
                                   required
                                   maxlength="6">
                            <div class="form-help">RA único do aluno (máximo 6 caracteres)</div>
                        </div>
                    </div>
                    
                    <div class="form-col-small">
                        <div class="form-group">
                            <label for="periodoAtualAluno" class="form-label">Período Atual</label>
                            <select id="periodoAtualAluno" 
                                    name="periodoAtualAluno" 
                                    class="form-input">
                                <option value="">Selecione...</option>
                                <c:forEach var="i" begin="1" end="12">
                                    <option value="${i}" ${aluno.periodoAtualAluno == i ? 'selected' : ''}>
                                        ${i}º Período
                                    </option>
                                </c:forEach>
                            </select>
                            <div class="form-help">Período/semestre atual</div>
                        </div>
                    </div>
                </div>

                <!-- Observações Acadêmicas -->
                <div class="form-group">
                    <label for="observacoesGeraisAluno" class="form-label">Observações Acadêmicas</label>
                    <textarea id="observacoesGeraisAluno" 
                              name="observacoesGeraisAluno" 
                              class="form-input form-textarea"
                              placeholder="Observações sobre o desempenho, comportamento, necessidades especiais, etc.">${aluno.observacoesGeraisAluno}</textarea>
                    <div class="form-help">Observações gerais sobre o aluno (opcional)</div>
                </div>
            </div>

            <!-- Seção: Acesso ao Sistema -->
            <div class="form-section">
                <h3>🔐 Acesso ao Sistema</h3>
                
                <!-- Senha -->
                <div class="form-group">
                    <label for="senhaHash" class="form-label ${action == 'new' ? 'required' : ''}">
                        <c:choose>
                            <c:when test="${action == 'edit'}">Nova Senha (deixe em branco para manter atual)</c:when>
                            <c:otherwise>Senha</c:otherwise>
                        </c:choose>
                    </label>
                    <input type="password" 
                           id="senhaHash" 
                           name="senhaHash" 
                           class="form-input"
                           placeholder="Digite a senha"
                           ${action == 'new' ? 'required' : ''}
                           minlength="6">
                    <div class="form-help">
                        <c:choose>
                            <c:when test="${action == 'edit'}">Deixe em branco para manter a senha atual</c:when>
                            <c:otherwise>Senha para acesso ao sistema (mínimo 6 caracteres)</c:otherwise>
                        </c:choose>
                    </div>
                    <div id="passwordStrength" class="password-strength"></div>
                </div>

                <!-- Status Ativo -->
                <div class="form-group">
                    <div class="form-checkbox-group">
                        <input type="checkbox" 
                               id="ativo" 
                               name="ativo" 
                               class="form-checkbox"
                               ${aluno.ativo || action == 'new' ? 'checked' : ''}>
                        <label for="ativo" class="form-label">Aluno Ativo</label>
                    </div>
                    <div class="form-help">Alunos inativos não podem acessar o sistema</div>
                </div>
            </div>

            <!-- Ações do formulário -->
            <div class="form-actions">
                <a href="${pageContext.request.contextPath}/admin/alunos" class="btn btn-secondary">
                    ❌ Cancelar
                </a>
                <button type="submit" class="btn btn-primary">
                    <c:choose>
                        <c:when test="${action == 'edit'}">💾 Salvar Alterações</c:when>
                        <c:otherwise>➕ Criar Aluno</c:otherwise>
                    </c:choose>
                </button>
            </div>
        </form>
    </div>

    <script>
        // Validações e melhorias de UX
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('alunoForm');
            const nomeInput = document.getElementById('nomeCompleto');
            const emailInput = document.getElementById('email');
            const raInput = document.getElementById('matriculaRA');
            const telefoneInput = document.getElementById('telefone');
            const senhaInput = document.getElementById('senhaHash');

            // Formatação automática do telefone
            telefoneInput.addEventListener('input', function() {
                // Remove tudo que não é número
                this.value = this.value.replace(/\D/g, '');
            });

            // Formatação automática do RA
            raInput.addEventListener('input', function() {
                // Remove tudo que não é número
                this.value = this.value.replace(/\D/g, '');
            });

            // Formatação automática do email (minúsculo)
            emailInput.addEventListener('input', function() {
                this.value = this.value.toLowerCase();
            });

            // Verificador de força da senha
            senhaInput.addEventListener('input', function() {
                const password = this.value;
                const strengthDiv = document.getElementById('passwordStrength');
                
                if (password.length === 0) {
                    strengthDiv.textContent = '';
                    return;
                }
                
                let strength = 0;
                let feedback = [];
                
                // Critérios de força
                if (password.length >= 8) strength++;
                else feedback.push('pelo menos 8 caracteres');
                
                if (/[a-z]/.test(password)) strength++;
                else feedback.push('letras minúsculas');
                
                if (/[A-Z]/.test(password)) strength++;
                else feedback.push('letras maiúsculas');
                
                if (/[0-9]/.test(password)) strength++;
                else feedback.push('números');
                
                if (/[^A-Za-z0-9]/.test(password)) strength++;
                else feedback.push('símbolos especiais');
                
                // Exibe resultado
                if (strength < 2) {
                    strengthDiv.className = 'password-strength strength-weak';
                    strengthDiv.textContent = '❌ Senha fraca - adicione: ' + feedback.slice(0, 2).join(', ');
                } else if (strength < 4) {
                    strengthDiv.className = 'password-strength strength-medium';
                    strengthDiv.textContent = '⚠️ Senha média - considere adicionar: ' + feedback.slice(0, 1).join(', ');
                } else {
                    strengthDiv.className = 'password-strength strength-strong';
                    strengthDiv.textContent = '✅ Senha forte';
                }
            });

            // Validação antes do envio
            form.addEventListener('submit', function(e) {
                let isValid = true;
                let errorMessage = '';

                // Validar nome
                if (!nomeInput.value.trim()) {
                    isValid = false;
                    errorMessage += 'Nome completo é obrigatório.\n';
                }

                // Validar email
                if (!emailInput.value.trim()) {
                    isValid = false;
                    errorMessage += 'Email é obrigatório.\n';
                } else if (!isValidEmail(emailInput.value)) {
                    isValid = false;
                    errorMessage += 'Email deve ter formato válido.\n';
                }

                // Validar RA
                if (!raInput.value.trim()) {
                    isValid = false;
                    errorMessage += 'RA é obrigatório.\n';
                } else if (raInput.value.length > 6) {
                    isValid = false;
                    errorMessage += 'RA deve ter no máximo 6 caracteres.\n';
                }

                // Validar senha (apenas para novos alunos)
                const isNewStudent = !document.querySelector('input[name="idUsuario"]');
                if (isNewStudent && !senhaInput.value.trim()) {
                    isValid = false;
                    errorMessage += 'Senha é obrigatória para novos alunos.\n';
                } else if (senhaInput.value && senhaInput.value.length < 6) {
                    isValid = false;
                    errorMessage += 'Senha deve ter pelo menos 6 caracteres.\n';
                }

                // Validar telefone (se preenchido)
                if (telefoneInput.value && telefoneInput.value.length < 10) {
                    isValid = false;
                    errorMessage += 'Telefone deve ter pelo menos 10 dígitos.\n';
                }

                if (!isValid) {
                    e.preventDefault();
                    alert('Por favor, corrija os seguintes erros:\n\n' + errorMessage);
                }
            });

            // Função auxiliar para validar email
            function isValidEmail(email) {
                const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                return emailRegex.test(email);
            }

            // Auto-completar email institucional
            nomeInput.addEventListener('blur', function() {
                if (!emailInput.value && this.value) {
                    const nomes = this.value.toLowerCase().split(' ');
                    if (nomes.length >= 2) {
                        const primeiroNome = nomes[0];
                        const ultimoNome = nomes[nomes.length - 1];
                        const emailSugerido = `${primeiroNome}.${ultimoNome}@unifae.br`;
                        emailInput.placeholder = `Sugestão: ${emailSugerido}`;
                    }
                }
            });
        });
    </script>
</body>
</html>
