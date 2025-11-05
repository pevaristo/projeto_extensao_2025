<%--
    LOCAIS FORM.JSP - FORMULÁRIO DE CRIAÇÃO/EDIÇÃO DE LOCAIS DE EVENTO
    ==================================================================
    
    Página JSP para formulário de criação e edição de locais de evento.
    Padronizada seguindo o modelo visual dos arquivos de alunos.
    
    FUNCIONALIDADES:
    - Formulário unificado para criação e edição
    - Validações client-side e server-side
    - Campos obrigatórios e opcionais
    - Feedback visual de erros
    - Navegação de volta para listagem
    
    @version 2.0 - Padronizada
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>
        <c:choose>
            <c:when test="${action == 'edit'}">Editar Local</c:when>
            <c:otherwise>Novo Local</c:otherwise>
        </c:choose>
        - Sistema UNIFAE
    </title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">
    
    <style>
        /* Estilos específicos para formulário de locais */
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
                    <c:when test="${action == 'edit'}">✏️ Editar Local</c:when>
                    <c:otherwise>➕ Novo Local</c:otherwise>
                </c:choose>
            </h1>
            <p>
                <c:choose>
                    <c:when test="${action == 'edit'}">Altere os dados do local conforme necessário</c:when>
                    <c:otherwise>Preencha os dados para cadastrar um novo local</c:otherwise>
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
        <form method="post" action="${pageContext.request.contextPath}/admin/locais" id="localForm">
            <!-- Campo oculto para ID (apenas em edição) -->
            <c:if test="${action == 'edit'}">
                <input type="hidden" name="idLocalEvento" value="${local.idLocalEvento}">
            </c:if>

            <!-- Seção: Informações Básicas -->
            <div class="form-section">
                <h3>📍 Informações Básicas</h3>
                
                <!-- Nome do Local -->
                <div class="form-group">
                    <label for="nomeLocal" class="form-label required">Nome do Local</label>
                    <input type="text" 
                           id="nomeLocal" 
                           name="nomeLocal" 
                           class="form-input"
                           value="${local.nomeLocal}"
                           placeholder="Ex: Hospital Universitário - Enfermaria A"
                           required
                           maxlength="255">
                    <div class="form-help">Nome completo e descritivo do local</div>
                </div>

                <!-- Tipo do Local -->
                <div class="form-group">
                    <label for="tipoLocal" class="form-label required">Tipo do Local</label>
                    <select id="tipoLocal" 
                            name="tipoLocal" 
                            class="form-input"
                            required>
                        <option value="">Selecione o tipo...</option>
                        <option value="Hospital" ${local.tipoLocal == 'Hospital' ? 'selected' : ''}>Hospital</option>
                        <option value="Ambulatório" ${local.tipoLocal == 'Ambulatório' ? 'selected' : ''}>Ambulatório</option>
                        <option value="Laboratório" ${local.tipoLocal == 'Laboratório' ? 'selected' : ''}>Laboratório</option>
                        <option value="Sala de Aula" ${local.tipoLocal == 'Sala de Aula' ? 'selected' : ''}>Sala de Aula</option>
                        <option value="Auditório" ${local.tipoLocal == 'Auditório' ? 'selected' : ''}>Auditório</option>
                        <option value="UBS" ${local.tipoLocal == 'UBS' ? 'selected' : ''}>UBS</option>
                        <option value="Clínica" ${local.tipoLocal == 'Clínica' ? 'selected' : ''}>Clínica</option>
                        <option value="Centro Cirúrgico" ${local.tipoLocal == 'Centro Cirúrgico' ? 'selected' : ''}>Centro Cirúrgico</option>
                        <option value="UTI" ${local.tipoLocal == 'UTI' ? 'selected' : ''}>UTI</option>
                        <option value="Pronto Socorro" ${local.tipoLocal == 'Pronto Socorro' ? 'selected' : ''}>Pronto Socorro</option>
                        <option value="Outro" ${local.tipoLocal == 'Outro' ? 'selected' : ''}>Outro</option>
                    </select>
                    <div class="form-help">Categoria do local para organização</div>
                </div>
            </div>

            <!-- Seção: Localização -->
            <div class="form-section">
                <h3>🗺️ Localização</h3>
                
                <!-- Endereço Completo -->
                <div class="form-group">
                    <label for="endereco" class="form-label required">Endereço Completo</label>
                    <input type="text" 
                           id="endereco" 
                           name="endereco" 
                           class="form-input"
                           value="${local.endereco}"
                           placeholder="Ex: Rua das Flores, 123 - Bloco A, 2º andar"
                           required
                           maxlength="200">
                    <div class="form-help">Endereço completo incluindo número, bloco, andar, etc.</div>
                </div>

                <!-- Cidade e Estado -->
                <div class="form-row">
                    <div class="form-col">
                        <div class="form-group">
                            <label for="cidade" class="form-label required">Cidade</label>
                            <input type="text" 
                                   id="cidade" 
                                   name="cidade" 
                                   class="form-input"
                                   value="${local.cidade}"
                                   placeholder="Ex: São Paulo"
                                   required
                                   maxlength="100">
                            <div class="form-help">Nome da cidade</div>
                        </div>
                    </div>
                    
                    <div class="form-col">
                        <div class="form-group">
                            <label for="estado" class="form-label required">Estado</label>
                            <select id="estado" 
                                    name="estado" 
                                    class="form-input"
                                    required>
                                <option value="">Selecione...</option>
                                <option value="AC" ${local.estado == 'AC' ? 'selected' : ''}>Acre</option>
                                <option value="AL" ${local.estado == 'AL' ? 'selected' : ''}>Alagoas</option>
                                <option value="AP" ${local.estado == 'AP' ? 'selected' : ''}>Amapá</option>
                                <option value="AM" ${local.estado == 'AM' ? 'selected' : ''}>Amazonas</option>
                                <option value="BA" ${local.estado == 'BA' ? 'selected' : ''}>Bahia</option>
                                <option value="CE" ${local.estado == 'CE' ? 'selected' : ''}>Ceará</option>
                                <option value="DF" ${local.estado == 'DF' ? 'selected' : ''}>Distrito Federal</option>
                                <option value="ES" ${local.estado == 'ES' ? 'selected' : ''}>Espírito Santo</option>
                                <option value="GO" ${local.estado == 'GO' ? 'selected' : ''}>Goiás</option>
                                <option value="MA" ${local.estado == 'MA' ? 'selected' : ''}>Maranhão</option>
                                <option value="MT" ${local.estado == 'MT' ? 'selected' : ''}>Mato Grosso</option>
                                <option value="MS" ${local.estado == 'MS' ? 'selected' : ''}>Mato Grosso do Sul</option>
                                <option value="MG" ${local.estado == 'MG' ? 'selected' : ''}>Minas Gerais</option>
                                <option value="PA" ${local.estado == 'PA' ? 'selected' : ''}>Pará</option>
                                <option value="PB" ${local.estado == 'PB' ? 'selected' : ''}>Paraíba</option>
                                <option value="PR" ${local.estado == 'PR' ? 'selected' : ''}>Paraná</option>
                                <option value="PE" ${local.estado == 'PE' ? 'selected' : ''}>Pernambuco</option>
                                <option value="PI" ${local.estado == 'PI' ? 'selected' : ''}>Piauí</option>
                                <option value="RJ" ${local.estado == 'RJ' ? 'selected' : ''}>Rio de Janeiro</option>
                                <option value="RN" ${local.estado == 'RN' ? 'selected' : ''}>Rio Grande do Norte</option>
                                <option value="RS" ${local.estado == 'RS' ? 'selected' : ''}>Rio Grande do Sul</option>
                                <option value="RO" ${local.estado == 'RO' ? 'selected' : ''}>Rondônia</option>
                                <option value="RR" ${local.estado == 'RR' ? 'selected' : ''}>Roraima</option>
                                <option value="SC" ${local.estado == 'SC' ? 'selected' : ''}>Santa Catarina</option>
                                <option value="SP" ${local.estado == 'SP' ? 'selected' : ''}>São Paulo</option>
                                <option value="SE" ${local.estado == 'SE' ? 'selected' : ''}>Sergipe</option>
                                <option value="TO" ${local.estado == 'TO' ? 'selected' : ''}>Tocantins</option>
                            </select>
                            <div class="form-help">Estado onde o local está localizado</div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Ações do formulário -->
            <div class="form-actions">
                <a href="${pageContext.request.contextPath}/admin/locais" class="btn btn-secondary">
                    ❌ Cancelar
                </a>
                <button type="submit" class="btn btn-primary">
                    <c:choose>
                        <c:when test="${action == 'edit'}">💾 Salvar Alterações</c:when>
                        <c:otherwise>➕ Criar Local</c:otherwise>
                    </c:choose>
                </button>
            </div>
        </form>
    </div>

    <script>
        // Validações e melhorias de UX
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('localForm');
            const nomeInput = document.getElementById('nomeLocal');
            const tipoInput = document.getElementById('tipoLocal');
            const enderecoInput = document.getElementById('endereco');
            const cidadeInput = document.getElementById('cidade');
            const estadoInput = document.getElementById('estado');

            // Validação do formulário
            form.addEventListener('submit', function(e) {
                let isValid = true;
                let errorMessage = '';

                // Validar nome
                if (!nomeInput.value.trim()) {
                    isValid = false;
                    errorMessage += 'Nome do local é obrigatório.\n';
                } else if (nomeInput.value.trim().length < 3) {
                    isValid = false;
                    errorMessage += 'Nome do local deve ter pelo menos 3 caracteres.\n';
                }

                // Validar tipo
                if (!tipoInput.value) {
                    isValid = false;
                    errorMessage += 'Tipo do local é obrigatório.\n';
                }

                // Validar endereço
                if (!enderecoInput.value.trim()) {
                    isValid = false;
                    errorMessage += 'Endereço é obrigatório.\n';
                }

                // Validar cidade
                if (!cidadeInput.value.trim()) {
                    isValid = false;
                    errorMessage += 'Cidade é obrigatória.\n';
                }

                // Validar estado
                if (!estadoInput.value) {
                    isValid = false;
                    errorMessage += 'Estado é obrigatório.\n';
                }

                if (!isValid) {
                    e.preventDefault();
                    alert('Por favor, corrija os seguintes erros:\n\n' + errorMessage);
                }
            });

            // Formatação automática do nome (primeira letra maiúscula)
            nomeInput.addEventListener('blur', function() {
                if (this.value) {
                    this.value = this.value.charAt(0).toUpperCase() + this.value.slice(1);
                }
            });

            // Formatação automática da cidade (primeira letra maiúscula)
            cidadeInput.addEventListener('blur', function() {
                if (this.value) {
                    this.value = this.value.charAt(0).toUpperCase() + this.value.slice(1);
                }
            });
        });
    </script>
</body>
</html>
