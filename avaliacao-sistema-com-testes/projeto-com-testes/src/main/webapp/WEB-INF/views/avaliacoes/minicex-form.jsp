<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- Mapeamento mais robusto das respostas por competência --%>
<c:set var="respostaEntrevistaMedica" value="0" />
<c:set var="respostaExameFisico" value="0" />
<c:set var="respostaProfissionalismo" value="0" />
<c:set var="respostaJulgamentoClinico" value="0" />
<c:set var="respostaComunicacao" value="0" />
<c:set var="respostaOrganizacao" value="0" />
<c:set var="respostaAvaliacaoGeral" value="0" />

<c:if test="${respostas != null}">
    <c:forEach var="resposta" items="${respostas}">
        <c:set var="nomeComp" value="${fn:toLowerCase(fn:trim(resposta.competenciaQuestionario.nomeCompetencia))}" />
        <c:set var="valorInt" value="${resposta.respostaValorNumerico != null ? resposta.respostaValorNumerico.intValue() : 0}" />
        
        <c:choose>
            <c:when test="${nomeComp eq 'entrevista médica' || fn:contains(nomeComp, 'entrevista')}">
                <c:set var="respostaEntrevistaMedica" value="${valorInt}" />
            </c:when>
            <c:when test="${nomeComp eq 'exame físico' || fn:contains(nomeComp, 'exame físico')}">
                <c:set var="respostaExameFisico" value="${valorInt}" />
            </c:when>
            <c:when test="${nomeComp eq 'profissionalismo' || fn:contains(nomeComp, 'profissionalismo')}">
                <c:set var="respostaProfissionalismo" value="${valorInt}" />
            </c:when>
            <c:when test="${nomeComp eq 'julgamento clínico' || fn:contains(nomeComp, 'julgamento')}">
                <c:set var="respostaJulgamentoClinico" value="${valorInt}" />
            </c:when>
            <c:when test="${nomeComp eq 'habilidade de comunicação' || fn:contains(nomeComp, 'comunicação')}">
                <c:set var="respostaComunicacao" value="${valorInt}" />
            </c:when>
            <c:when test="${nomeComp eq 'organização e eficiência' || fn:contains(nomeComp, 'organização')}">
                <c:set var="respostaOrganizacao" value="${valorInt}" />
            </c:when>
            <c:when test="${nomeComp eq 'avaliação clínica geral' || fn:contains(nomeComp, 'geral')}">
                <c:set var="respostaAvaliacaoGeral" value="${valorInt}" />
            </c:when>
        </c:choose>
    </c:forEach>
</c:if>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulário Mini-CEX - MEDICINA UNIFAE</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">
    <style>
        .debug-info {
            background-color: #f0f0f0;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            font-size: 12px;
        }
    </style>
</head>
<body>
    <div class="formulario-container">
        <div class="formulario-header">
            <div class="formulario-titulo">Formulário Mini-CEX (MEDICINA UNIFAE)</div>
        </div>

        <!-- DEBUG INFO (remover em produção) -->
        <div class="debug-info">
            <strong>DEBUG - Valores Mapeados:</strong><br>
            Entrevista: ${respostaEntrevistaMedica} | 
            Exame: ${respostaExameFisico} | 
            Profissionalismo: ${respostaProfissionalismo} | 
            Julgamento: ${respostaJulgamentoClinico} | 
            Comunicação: ${respostaComunicacao} | 
            Organização: ${respostaOrganizacao} | 
            Geral: ${respostaAvaliacaoGeral}
            <br><strong>Total Respostas:</strong> ${respostas != null ? respostas.size() : 0}
        </div>

        <form method="post" action="${pageContext.request.contextPath}/avaliacao/form">
            <input type="hidden" name="action" value="${action}">
            <input type="hidden" name="questionarioId" value="${questionarioIdSelecionado}">
            <c:if test="${action == 'edit'}">
                <input type="hidden" name="avaliacaoId" value="${avaliacao.idAvaliacaoPreenchida}">
            </c:if>

            <!-- Dados Básicos -->
            <div class="dados-basicos">
                <table>
                    <tr>
                        <td>
                            <label>Aluno:</label>
                            <select name="alunoAvaliadoId" required style="width: 200px;">
                                <option value="0">Selecione o aluno</option>
                                <c:forEach var="aluno" items="${alunos}">
                                    <option value="${aluno.idUsuario}" 
                                            ${avaliacao != null && avaliacao.alunoAvaliado.idUsuario == aluno.idUsuario ? 'selected' : ''}>
                                        ${aluno.nomeCompleto}
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>Professor/preceptor:</label>
                            <select name="avaliadorId" style="width: 200px;">
                                <option value="0">Selecione o professor</option>
                                <c:forEach var="professor" items="${professores}">
                                    <option value="${professor.idUsuario}"
                                            ${avaliacao != null && avaliacao.avaliador != null && avaliacao.avaliador.idUsuario == professor.idUsuario ? 'selected' : ''}>
                                        ${professor.nomeCompleto}
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>Local de realização:</label>
                            <select name="localRealizacao" style="width: 300px;">
                                <option value="0">Selecione o local</option>
                                <c:forEach var="local" items="${locaisEventos}">
                                    <option value="${local.nomeLocal}" 
                                            ${avaliacao != null && avaliacao.localRealizacao == local.nomeLocal ? 'selected' : ''}>
                                        ${local.nomeLocal} - ${local.tipoLocal}
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>Horário de início:</label>
                            <input type="time" name="horarioInicio" 
                                   value="${avaliacao != null && avaliacao.horarioInicio != null ? avaliacao.horarioInicio : ''}">
                            
                            <label style="margin-left: 20px;">Horário de término:</label>
                            <input type="time" name="horarioFim" 
                                   value="${avaliacao != null && avaliacao.horarioFim != null ? avaliacao.horarioFim : ''}">
                            
                            <label style="margin-left: 20px;">Data da realização:</label>
                            <input type="date" name="dataRealizacao" required
                                   value="${avaliacao != null ? avaliacao.dataRealizacao : ''}">
                        </td>
                    </tr>
                </table>
            </div>

            <!-- Competências -->
            <div class="competencias-container">
                <!-- Entrevista médica -->
                <div class="competencia-item">
                    <div class="competencia-header">Entrevista médica</div>
                    <div class="competencia-content">
                        <div class="comportamento-pre">
                            Inibe o relato adequado da história clínica pelo paciente;<br><br>
                            Não utiliza perguntas adequadas para obter informações relevantes;<br><br>
                            A qualidade da história não permite ajuizamento apropriado do caso.
                        </div>
                        <div class="escala-container">
                            <div class="escala-labels">
                                <span>Insatisfatório</span>
                                <span>Satisfatório</span>
                                <span>Excelente</span>
                            </div>
                            <div class="escala-numeros">
                                <c:forEach var="i" begin="1" end="9">
                                    <label>
                                        <input type="radio" name="resposta_entrevista_medica" value="${i}"
                                               <c:if test="${respostaEntrevistaMedica == i}">checked</c:if>>
                                        ${i}
                                    </label>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="comportamento-pos">
                            Aproxima-se, apresenta-se e acolhe o paciente respeitosamente;<br><br>
                            Usa perguntas apropriadas para obter informações precisas;<br><br>
                            Responde adequadamente à comunicação verbal e não verbal;<br><br>
                            Processa a história do paciente de forma compreensível, eficaz e suficiente.
                        </div>
                    </div>
                </div>

                <!-- Exame Físico -->
                <div class="competencia-item">
                    <div class="competencia-header">Exame Físico</div>
                    <div class="competencia-content">
                        <div class="comportamento-pre">
                            Não lava as mãos e nem utiliza medidas de proteção universais;<br><br>
                            Não explica o exame ao paciente;<br><br>
                            Realiza o exame de forma desorganizada;<br><br>
                            Apresenta dificuldade de interpretação;<br><br>
                            Não registra corretamente os dados obtidos.
                        </div>
                        <div class="escala-container">
                            <div class="escala-labels">
                                <span>Insatisfatório</span>
                                <span>Satisfatório</span>
                                <span>Excelente</span>
                            </div>
                            <div class="escala-numeros">
                                <c:forEach var="i" begin="1" end="9">
                                    <label>
                                        <input type="radio" name="resposta_exame_fisico" value="${i}"
                                               <c:if test="${respostaExameFisico == i}">checked</c:if>>
                                        ${i}
                                    </label>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="comportamento-pos">
                            Segue uma sequência lógica e coerente;<br><br>
                            Aplica exame físico pleno e compatível com a queixa do paciente;<br><br>
                            Explica ao paciente de forma compreensível os achados clínicos;<br><br>
                            Mostra-se sensível às queixas e oferece conforto ao paciente.
                        </div>
                    </div>
                </div>

                <!-- Profissionalismo -->
                <div class="competencia-item">
                    <div class="competencia-header">Profissionalismo</div>
                    <div class="competencia-content">
                        <div class="comportamento-pre">
                            Não estabelece contato satisfatório ou acolhe as queixas;<br><br>
                            Demonstra desatenção e desinteresse pelo paciente;<br><br>
                            Desconsidera as necessidades de conforto físico;<br><br>
                            Não respeita à privacidade ou comporta-se de forma ética;<br><br>
                            Utiliza linguagem inadequada à compreensão.
                        </div>
                        <div class="escala-container">
                            <div class="escala-labels">
                                <span>Insatisfatório</span>
                                <span>Satisfatório</span>
                                <span>Excelente</span>
                            </div>
                            <div class="escala-numeros">
                                <c:forEach var="i" begin="1" end="9">
                                    <label>
                                        <input type="radio" name="resposta_profissionalismo" value="${i}"
                                               <c:if test="${respostaProfissionalismo == i}">checked</c:if>>
                                        ${i}
                                    </label>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="comportamento-pos">
                            Demonstra respeito, compaixão, empatia e estabelece confiança;<br><br>
                            Atende às necessidades de conforto do paciente;<br><br>
                            Comporta-se de forma ética e demonstra conhecimento de pontos legalmente relevantes;<br><br>
                            Explica o diagnóstico e exames complementares e apresenta o plano terapêutico.
                        </div>
                    </div>
                </div>

                <!-- Julgamento Clínico -->
                <div class="competencia-item">
                    <div class="competencia-header">Julgamento Clínico</div>
                    <div class="competencia-content">
                        <div class="comportamento-pre">
                            Não obtém consentimento do paciente;<br><br>
                            Apresenta dificuldades para o(s) diagnóstico(s) e hipóteses diagnósticas apropriadas;<br><br>
                            Utiliza instrumentos diagnósticos de forma inadequada;<br><br>
                            Formula plano diagnóstico e terapêutico inconsistente;<br><br>
                            Interpreta de forma insatisfatória os exames complementares.
                        </div>
                        <div class="escala-container">
                            <div class="escala-labels">
                                <span>Insatisfatório</span>
                                <span>Satisfatório</span>
                                <span>Excelente</span>
                            </div>
                            <div class="escala-numeros">
                                <c:forEach var="i" begin="1" end="9">
                                    <label>
                                        <input type="radio" name="resposta_julgamento_clinico" value="${i}"
                                               <c:if test="${respostaJulgamentoClinico == i}">checked</c:if>>
                                        ${i}
                                    </label>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="comportamento-pos">
                            Faz o diagnóstico apropriado e formula um plano de conduta adequado.<br><br>
                            Solicita ou realiza adequadamente exames diagnósticos.<br><br>
                            Realiza diagnósticos diferenciais apropriados;<br><br>
                            Considera riscos, benefícios e orienta adequadamente o paciente.
                        </div>
                    </div>
                </div>

                <!-- Habilidade de comunicação -->
                <div class="competencia-item">
                    <div class="competencia-header">Habilidade de comunicação</div>
                    <div class="competencia-content">
                        <div class="comportamento-pre">
                            Não utiliza efetivamente as habilidades de comunicação (tipo de perguntas, escuta ativa, questões para esclarecer a informação, silêncio, comunicação verbal e não verbal);<br><br>
                            Cria barreiras e dificuldades para comunicação.
                        </div>
                        <div class="escala-container">
                            <div class="escala-labels">
                                <span>Insatisfatório</span>
                                <span>Satisfatório</span>
                                <span>Excelente</span>
                            </div>
                            <div class="escala-numeros">
                                <c:forEach var="i" begin="1" end="9">
                                    <label>
                                        <input type="radio" name="resposta_comunicacao" value="${i}"
                                               <c:if test="${respostaComunicacao == i}">checked</c:if>>
                                        ${i}
                                    </label>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="comportamento-pos">
                            Utiliza efetivamente as habilidades de comunicação;<br><br>
                            Facilita a comunicação e demonstra interesse genuíno;<br><br>
                            Responde adequadamente às necessidades do paciente;<br><br>
                            Estabelece rapport e confiança.
                        </div>
                    </div>
                </div>

                <!-- Organização e Eficiência -->
                <div class="competencia-item">
                    <div class="competencia-header">Organização e Eficiência</div>
                    <div class="competencia-content">
                        <div class="comportamento-pre">
                            Não prioriza adequadamente;<br><br>
                            Não utiliza o tempo de forma eficiente;<br><br>
                            Desorganizado na abordagem.
                        </div>
                        <div class="escala-container">
                            <div class="escala-labels">
                                <span>Insatisfatório</span>
                                <span>Satisfatório</span>
                                <span>Excelente</span>
                            </div>
                            <div class="escala-numeros">
                                <c:forEach var="i" begin="1" end="9">
                                    <label>
                                        <input type="radio" name="resposta_organizacao" value="${i}"
                                               <c:if test="${respostaOrganizacao == i}">checked</c:if>>
                                        ${i}
                                    </label>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="comportamento-pos">
                            Prioriza adequadamente;<br><br>
                            Utiliza o tempo de forma eficiente;<br><br>
                            Organizado na abordagem.
                        </div>
                    </div>
                </div>

                <!-- Avaliação Clínica Geral -->
                <div class="competencia-item">
                    <div class="competencia-header">Avaliação Clínica Geral</div>
                    <div class="competencia-content">
                        <div class="comportamento-pre">
                            Desempenho geral insatisfatório;<br><br>
                            Necessita supervisão constante;<br><br>
                            Não demonstra competência clínica adequada.
                        </div>
                        <div class="escala-container">
                            <div class="escala-labels">
                                <span>Insatisfatório</span>
                                <span>Satisfatório</span>
                                <span>Excelente</span>
                            </div>
                            <div class="escala-numeros">
                                <c:forEach var="i" begin="1" end="9">
                                    <label>
                                        <input type="radio" name="resposta_avaliacao_geral" value="${i}"
                                               <c:if test="${respostaAvaliacaoGeral == i}">checked</c:if>>
                                        ${i}
                                    </label>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="comportamento-pos">
                            Desempenho geral excelente;<br><br>
                            Demonstra autonomia adequada;<br><br>
                            Competência clínica bem desenvolvida.
                        </div>
                    </div>
                </div>
            </div>

            <!-- Campos de Feedback -->
            <div class="feedback-container">
                <div class="feedback-item">
                    <label>O que foi feito bem?</label>
                    <textarea name="feedbackPositivo" rows="3" style="width: 100%;">${avaliacao != null ? avaliacao.feedbackPositivo : ''}</textarea>
                </div>
                
                <div class="feedback-item">
                    <label>Sugestões para melhoria:</label>
                    <textarea name="feedbackMelhoria" rows="3" style="width: 100%;">${avaliacao != null ? avaliacao.feedbackMelhoria : ''}</textarea>
                </div>
                
                <div class="feedback-item">
                    <label>Plano de ação/contrato de aprendizagem:</label>
                    <textarea name="contratoAprendizagem" rows="3" style="width: 100%;">${avaliacao != null ? avaliacao.contratoAprendizagem : ''}</textarea>
                </div>
            </div>

            <!-- Botões -->
            <div class="botoes-container">
                <button type="submit" class="btn-salvar">Salvar Avaliação</button>
                <a href="${pageContext.request.contextPath}/avaliacoes" class="btn-cancelar">Cancelar</a>
            </div>
        </form>
    </div>
</body>
</html>

