<%--
    NOTAS FORM.JSP - FORMULÁRIO DE CRIAÇÃO/EDIÇÃO DE NOTAS
    ======================================================
    
    Página JSP para formulário de criação e edição de notas.
    Segue o padrão visual e funcional já estabelecido no projeto.
    
    FUNCIONALIDADES:
    - Formulário unificado para criação e edição
    - Validações client-side e server-side
    - Campos obrigatórios e opcionais
    - Feedback visual de erros
    - Navegação de volta para listagem
    - Cálculo automático de peso sugerido
    
    RELACIONAMENTO COM OUTROS ARQUIVOS:
    - NotaServlet.java: Processa o formulário
    - list.jsp: Listagem de notas
    - css/formularios.css: Estilos compartilhados
    
    @version 1.0
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>
        <c:choose>
            <c:when test="${action == 'edit'}">Editar Nota</c:when>
            <c:otherwise>Nova Nota</c:otherwise>
        </c:choose>
        - Sistema UNIFAE
    </title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">
    
    <style>
        /* Estilos específicos para formulário de notas */
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
            border-color: #6f42c1;
            box-shadow: 0 0 0 2px rgba(111,66,193,0.25);
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
            background-color: #6f42c1;
            color: white;
        }
        
        .btn-primary:hover {
            background-color: #5a2d91;
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
        
        .nota-preview {
            background-color: #e7f3ff;
            border: 1px solid #b8daff;
            padding: 15px;
            border-radius: 4px;
            margin-top: 10px;
        }
        
        .nota-preview h4 {
            margin: 0 0 10px 0;
            color: #0056b3;
        }
        
        .preview-item {
            display: flex;
            justify-content: space-between;
            margin-bottom: 5px;
        }
        
        .peso-sugerido {
            background-color: #fff3cd;
            border: 1px solid #ffeaa7;
            color: #856404;
            padding: 8px;
            border-radius: 4px;
            font-size: 12px;
            margin-top: 5px;
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
                    <c:when test="${action == 'edit'}">✏️ Editar Nota</c:when>
                    <c:otherwise>➕ Nova Nota</c:otherwise>
                </c:choose>
            </h1>
            <p>
                <c:choose>
                    <c:when test="${action == 'edit'}">Altere os dados da nota conforme necessário</c:when>
                    <c:otherwise>Preencha os dados para lançar uma nova nota</c:otherwise>
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
        <form method="post" action="${pageContext.request.contextPath}/admin/notas" id="notaForm">
            <!-- Campo oculto para ID (apenas em edição) -->
            <c:if test="${action == 'edit'}">
                <input type="hidden" name="idNota" value="${nota.idNota}">
            </c:if>

            <!-- Seção: Dados da Avaliação -->
            <div class="form-section">
                <h3>📊 Dados da Avaliação</h3>
                
                <!-- Aluno e Disciplina -->
                <div class="form-row">
                    <div class="form-col">
                        <div class="form-group">
                            <label for="alunoId" class="form-label required">Aluno</label>
                            <select id="alunoId" 
                                    name="alunoId" 
                                    class="form-input"
                                    required>
                                <option value="">Selecione o aluno...</option>
                                <c:forEach var="aluno" items="${listAlunos}">
                                    <option value="${aluno.idUsuario}" 
                                            ${nota.aluno.idUsuario == aluno.idUsuario ? 'selected' : ''}>
                                        ${aluno.nomeCompleto} (RA: ${aluno.matriculaRA})
                                    </option>
                                </c:forEach>
                            </select>
                            <div class="form-help">Selecione o aluno que receberá a nota</div>
                        </div>
                    </div>
                    
                    <div class="form-col">
                        <div class="form-group">
                            <label for="disciplinaId" class="form-label required">Disciplina</label>
                            <select id="disciplinaId" 
                                    name="disciplinaId" 
                                    class="form-input"
                                    required>
                                <option value="">Selecione a disciplina...</option>
                                <c:forEach var="disciplina" items="${listDisciplinas}">
                                    <option value="${disciplina.idDisciplina}" 
                                            ${nota.disciplina.idDisciplina == disciplina.idDisciplina ? 'selected' : ''}>
                                        ${disciplina.nomeDisciplina} (${disciplina.codigoDisciplina})
                                    </option>
                                </c:forEach>
                            </select>
                            <div class="form-help">Selecione a disciplina da avaliação</div>
                        </div>
                    </div>
                </div>

                <!-- Turma e Professor -->
                <div class="form-row">
                    <div class="form-col">
                        <div class="form-group">
                            <label for="turmaId" class="form-label">Turma</label>
                            <select id="turmaId" 
                                    name="turmaId" 
                                    class="form-input">
                                <option value="">Selecione a turma (opcional)...</option>
                                <c:forEach var="turma" items="${listTurmas}">
                                    <option value="${turma.idTurma}" 
                                            ${nota.turma.idTurma == turma.idTurma ? 'selected' : ''}>
                                        ${turma.nomeTurma} (${turma.anoLetivo}/${turma.semestre})
                                    </option>
                                </c:forEach>
                            </select>
                            <div class="form-help">Turma onde a avaliação foi aplicada (opcional)</div>
                        </div>
                    </div>
                    
                    <div class="form-col">
                        <div class="form-group">
                            <label for="professorId" class="form-label">Professor</label>
                            <select id="professorId" 
                                    name="professorId" 
                                    class="form-input">
                                <option value="">Selecione o professor (opcional)...</option>
                                <c:forEach var="professor" items="${listProfessores}">
                                    <option value="${professor.idUsuario}" 
                                            ${nota.professor.idUsuario == professor.idUsuario ? 'selected' : ''}>
                                        ${professor.nomeCompleto}
                                    </option>
                                </c:forEach>
                            </select>
                            <div class="form-help">Professor que aplicou a avaliação (opcional)</div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Seção: Nota e Avaliação -->
            <div class="form-section">
                <h3>📝 Nota e Avaliação</h3>
                
                <!-- Valor da Nota e Peso -->
                <div class="form-row">
                    <div class="form-col-small">
                        <div class="form-group">
                            <label for="valorNota" class="form-label required">Nota</label>
                            <input type="number" 
                                   id="valorNota" 
                                   name="valorNota" 
                                   class="form-input"
                                   value="${nota.valorNota}"
                                   placeholder="Ex: 8.5"
                                   min="0"
                                   max="10"
                                   step="0.1"
                                   required>
                            <div class="form-help">Nota de 0.0 a 10.0</div>
                        </div>
                    </div>
                    
                    <div class="form-col-small">
                        <div class="form-group">
                            <label for="pesoNota" class="form-label">Peso</label>
                            <input type="number" 
                                   id="pesoNota" 
                                   name="pesoNota" 
                                   class="form-input"
                                   value="${nota.pesoNota != null ? nota.pesoNota : '1.0'}"
                                   placeholder="Ex: 2.0"
                                   min="0.1"
                                   max="10"
                                   step="0.1">
                            <div class="form-help">Peso para cálculo de média</div>
                            <div id="pesoSugerido" class="peso-sugerido" style="display: none;"></div>
                        </div>
                    </div>
                    
                    <div class="form-col">
                        <div class="form-group">
                            <label for="tipoAvaliacao" class="form-label required">Tipo de Avaliação</label>
                            <select id="tipoAvaliacao" 
                                    name="tipoAvaliacao" 
                                    class="form-input"
                                    required>
                                <option value="">Selecione o tipo...</option>
                                <c:forEach var="tipo" items="${tiposAvaliacao}">
                                    <option value="${tipo}" 
                                            ${nota.tipoAvaliacao == tipo ? 'selected' : ''}
                                            data-peso="${tipo.pesoSugerido}">
                                        ${tipo.nomeAmigavel}
                                    </option>
                                </c:forEach>
                            </select>
                            <div class="form-help">Tipo da avaliação realizada</div>
                        </div>
                    </div>
                </div>

                <!-- Data da Avaliação -->
                <div class="form-group">
                    <label for="dataAvaliacao" class="form-label required">Data da Avaliação</label>
                    <input type="date" 
                           id="dataAvaliacao" 
                           name="dataAvaliacao" 
                           class="form-input"
                           value="${nota.dataAvaliacao}"
                           required>
                    <div class="form-help">Data em que a avaliação foi realizada</div>
                </div>

                <!-- Descrição da Avaliação -->
                <div class="form-group">
                    <label for="descricaoAvaliacao" class="form-label">Descrição da Avaliação</label>
                    <input type="text" 
                           id="descricaoAvaliacao" 
                           name="descricaoAvaliacao" 
                           class="form-input"
                           value="${nota.descricaoAvaliacao}"
                           placeholder="Ex: Prova P1 - Anatomia do Sistema Cardiovascular"
                           maxlength="500">
                    <div class="form-help">Descrição detalhada da avaliação (opcional, máximo 500 caracteres)</div>
                </div>

                <!-- Observações -->
                <div class="form-group">
                    <label for="observacoes" class="form-label">Observações</label>
                    <textarea id="observacoes" 
                              name="observacoes" 
                              class="form-input form-textarea"
                              placeholder="Observações sobre o desempenho do aluno, feedback, etc.">${nota.observacoes}</textarea>
                    <div class="form-help">Observações e feedback sobre a avaliação (opcional)</div>
                </div>

                <!-- Status Ativo -->
                <div class="form-group">
                    <div class="form-checkbox-group">
                        <input type="checkbox" 
                               id="ativo" 
                               name="ativo" 
                               class="form-checkbox"
                               ${nota.ativo || action == 'new' ? 'checked' : ''}>
                        <label for="ativo" class="form-label">Nota Ativa</label>
                    </div>
                    <div class="form-help">Notas inativas não são consideradas no cálculo de médias</div>
                </div>
            </div>

            <!-- Preview da Nota -->
            <div id="notaPreview" class="nota-preview" style="display: none;">
                <h4>📊 Preview da Nota</h4>
                <div class="preview-item">
                    <span>Aluno:</span>
                    <span id="previewAluno">-</span>
                </div>
                <div class="preview-item">
                    <span>Disciplina:</span>
                    <span id="previewDisciplina">-</span>
                </div>
                <div class="preview-item">
                    <span>Nota:</span>
                    <span id="previewNota">-</span>
                </div>
                <div class="preview-item">
                    <span>Peso:</span>
                    <span id="previewPeso">-</span>
                </div>
                <div class="preview-item">
                    <span>Nota Ponderada:</span>
                    <span id="previewNotaPonderada">-</span>
                </div>
            </div>

            <!-- Ações do formulário -->
            <div class="form-actions">
                <a href="${pageContext.request.contextPath}/admin/notas" class="btn btn-secondary">
                    ❌ Cancelar
                </a>
                <button type="submit" class="btn btn-primary">
                    <c:choose>
                        <c:when test="${action == 'edit'}">💾 Salvar Alterações</c:when>
                        <c:otherwise>➕ Lançar Nota</c:otherwise>
                    </c:choose>
                </button>
            </div>
        </form>
    </div>

    <script>
        // Validações e melhorias de UX
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('notaForm');
            const alunoSelect = document.getElementById('alunoId');
            const disciplinaSelect = document.getElementById('disciplinaId');
            const valorNotaInput = document.getElementById('valorNota');
            const pesoNotaInput = document.getElementById('pesoNota');
            const tipoAvaliacaoSelect = document.getElementById('tipoAvaliacao');
            const dataAvaliacaoInput = document.getElementById('dataAvaliacao');
            const notaPreview = document.getElementById('notaPreview');

            // Define data padrão como hoje
            if (!dataAvaliacaoInput.value) {
                const hoje = new Date().toISOString().split('T')[0];
                dataAvaliacaoInput.value = hoje;
            }

            // Sugestão de peso baseado no tipo de avaliação
            tipoAvaliacaoSelect.addEventListener('change', function() {
                const selectedOption = this.options[this.selectedIndex];
                const pesoSugerido = selectedOption.getAttribute('data-peso');
                const pesoSugeridoDiv = document.getElementById('pesoSugerido');
                
                if (pesoSugerido && !pesoNotaInput.value) {
                    pesoNotaInput.value = pesoSugerido;
                    pesoSugeridoDiv.textContent = `💡 Peso sugerido para ${selectedOption.text}: ${pesoSugerido}`;
                    pesoSugeridoDiv.style.display = 'block';
                } else {
                    pesoSugeridoDiv.style.display = 'none';
                }
                
                updatePreview();
            });

            // Atualiza preview em tempo real
            function updatePreview() {
                const aluno = alunoSelect.options[alunoSelect.selectedIndex]?.text || '-';
                const disciplina = disciplinaSelect.options[disciplinaSelect.selectedIndex]?.text || '-';
                const nota = valorNotaInput.value || '0';
                const peso = pesoNotaInput.value || '1';
                const notaPonderada = (parseFloat(nota) * parseFloat(peso)).toFixed(2);

                document.getElementById('previewAluno').textContent = aluno;
                document.getElementById('previewDisciplina').textContent = disciplina;
                document.getElementById('previewNota').textContent = nota;
                document.getElementById('previewPeso').textContent = peso;
                document.getElementById('previewNotaPonderada').textContent = notaPonderada;

                // Mostra preview se há dados suficientes
                if (alunoSelect.value && disciplinaSelect.value && valorNotaInput.value) {
                    notaPreview.style.display = 'block';
                } else {
                    notaPreview.style.display = 'none';
                }
            }

            // Eventos para atualizar preview
            alunoSelect.addEventListener('change', updatePreview);
            disciplinaSelect.addEventListener('change', updatePreview);
            valorNotaInput.addEventListener('input', updatePreview);
            pesoNotaInput.addEventListener('input', updatePreview);

            // Validação da nota
            valorNotaInput.addEventListener('input', function() {
                const valor = parseFloat(this.value);
                if (valor < 0 || valor > 10) {
                    this.setCustomValidity('A nota deve estar entre 0.0 e 10.0');
                } else {
                    this.setCustomValidity('');
                }
            });

            // Validação do peso
            pesoNotaInput.addEventListener('input', function() {
                const peso = parseFloat(this.value);
                if (peso < 0.1 || peso > 10) {
                    this.setCustomValidity('O peso deve estar entre 0.1 e 10.0');
                } else {
                    this.setCustomValidity('');
                }
            });

            // Validação antes do envio
            form.addEventListener('submit', function(e) {
                let isValid = true;
                let errorMessage = '';

                // Validar aluno
                if (!alunoSelect.value) {
                    isValid = false;
                    errorMessage += 'Aluno é obrigatório.\n';
                }

                // Validar disciplina
                if (!disciplinaSelect.value) {
                    isValid = false;
                    errorMessage += 'Disciplina é obrigatória.\n';
                }

                // Validar nota
                const nota = parseFloat(valorNotaInput.value);
                if (!valorNotaInput.value || isNaN(nota) || nota < 0 || nota > 10) {
                    isValid = false;
                    errorMessage += 'Nota deve ser um número entre 0.0 e 10.0.\n';
                }

                // Validar peso
                const peso = parseFloat(pesoNotaInput.value);
                if (!pesoNotaInput.value || isNaN(peso) || peso < 0.1 || peso > 10) {
                    isValid = false;
                    errorMessage += 'Peso deve ser um número entre 0.1 e 10.0.\n';
                }

                // Validar tipo de avaliação
                if (!tipoAvaliacaoSelect.value) {
                    isValid = false;
                    errorMessage += 'Tipo de avaliação é obrigatório.\n';
                }

                // Validar data
                if (!dataAvaliacaoInput.value) {
                    isValid = false;
                    errorMessage += 'Data da avaliação é obrigatória.\n';
                }

                if (!isValid) {
                    e.preventDefault();
                    alert('Por favor, corrija os seguintes erros:\n\n' + errorMessage);
                }
            });

            // Inicializa preview
            updatePreview();
        });
    </script>
</body>
</html>
