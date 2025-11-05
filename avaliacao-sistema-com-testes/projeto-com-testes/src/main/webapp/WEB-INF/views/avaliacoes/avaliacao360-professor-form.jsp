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
    <title>Avaliação 360 Formativa - Avaliação Professor/Preceptor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">
</head>
<body>
    <div class="formulario-container">
        <div class="formulario-header">
            <div class="formulario-titulo">AVALIAÇÃO 360 FORMATIVA (MEDICINA UNIFAE)</div>
            <div class="avaliacao360-tipo">AVALIAÇÃO PROFESSOR/PRECEPTOR</div>
        </div>

        <form method="post" action="${pageContext.request.contextPath}/avaliacao/form">
            <input type="hidden" name="action" value="${action}">
            <input type="hidden" name="questionarioId" value="${questionarioIdSelecionado}">
            <input type="hidden" name="tipoAvaliadorNaoUsuario" value="Professor/Preceptor">
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

            <!-- Competências (mesmo layout do Mini CEX) -->
            <div class="competencias-container">
                <!-- Entrevista médica -->
                <div class="competencia-item">
                    <div class="competencia-header">Entrevista médica</div>
                    <div class="competencia-content">
                        <div class="comportamento-pre">
                            Inibe o relato adequado da história clínica pelo paciente.<br><br>
                            Não utiliza perguntas adequadas para obter informações relevantes.<br><br>
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
                            Aproxima-se, apresenta-se e acolhe o paciente respeitosamente.<br><br>
                            Usa perguntas apropriadas para obter informações precisas.<br><br>
                            Responde adequadamente à comunicação verbal e não verbal.<br><br>
                            Processa a história do paciente de forma compreensível, eficaz e suficiente.
                        </div>
                    </div>
                </div>

                <!-- Exame Físico -->
                <div class="competencia-item">
                    <div class="competencia-header">Exame Físico</div>
                    <div class="competencia-content">
                        <div class="comportamento-pre">
                            Não lava as mãos e nem utiliza medidas de proteção universais.<br><br>
                            Não explica o exame ao paciente.<br><br>
                            Realiza o exame de forma desorganizada.<br><br>
                            Apresenta dificuldade de interpretação dos achados.<br><br>
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
                            O exame físico segue uma sequência lógica e coerente.<br><br>
                            Aplica exame físico pleno e compatível com a queixa do paciente.<br><br>
                            Explica ao paciente de forma compreensível os achados clínicos.<br><br>
                            Mostra-se sensível às queixas e oferece conforto ao paciente.
                        </div>
                    </div>
                </div>

                <!-- Profissionalismo -->
                <div class="competencia-item">
                    <div class="competencia-header">Profissionalismo</div>
                    <div class="competencia-content">
                        <div class="comportamento-pre">
                            Não estabelece contato satisfatório ou acolhe as queixas do paciente.<br><br>
                            Demonstra desatenção e desinteresse pelo paciente.<br><br>
                            Desconsidera as necessidades de conforto físico.<br><br>
                            Não respeita à privacidade ou comporta-se de forma ética.<br><br>
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
                            Demonstra respeito, compaixão, empatia e estabelece confiança.<br><br>
                            Atende às necessidades de conforto do paciente.<br><br>
                            Comporta-se de forma ética e demonstra conhecimento de pontos legalmente relevantes.<br><br>
                            Explica o diagnóstico e exames complementares e apresenta o plano terapêutico.
                        </div>
                    </div>
                </div>

                <!-- Julgamento Clínico -->
                <div class="competencia-item">
                    <div class="competencia-header">Julgamento Clínico</div>
                    <div class="competencia-content">
                        <div class="comportamento-pre">
                            Não obtém consentimento do paciente.<br><br>
                            Apresenta dificuldades para o(s) diagnóstico(s) e hipóteses diagnósticas apropriadas.<br><br>
                            Utiliza instrumentos diagnósticos de forma inadequada.<br><br>
                            Formula plano diagnóstico e terapêutico inconsistente.<br><br>
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
                            Realiza diagnósticos diferenciais apropriados.<br><br>
                            Considera riscos, benefícios e orienta adequadamente o paciente.
                        </div>
                    </div>
                </div>

                <!-- Habilidade de comunicação -->
                <div class="competencia-item">
                    <div class="competencia-header">Habilidade de comunicação</div>
                    <div class="competencia-content">
                        <div class="comportamento-pre">
                            Não utiliza efetivamente as habilidades de comunicação (tipo de perguntas, escuta ativa, questões para esclarecer a informação, silêncio, comunicação verbal e não verbal).<br><br>
                            Cria barreiras e dificuldades para comunicação.<br><br>
                            Oferece hipótese diagnóstica de forma descuidada ou insensível.
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
                            Aborda o paciente de forma honesta e empática.<br><br>
                            Comunica-se sem jargões ou preconceito.<br><br>
                            Incorpora a perspectiva e repertório do paciente de modo sensível.<br><br>
                            Oferece a hipótese diagnóstica de forma sensível.<br><br>
                            Pactua o plano terapêutico de forma compreensível.
                        </div>
                    </div>
                </div>

                <!-- Organização e Eficiência -->
                <div class="competencia-item">
                    <div class="competencia-header">Organização e Eficiência</div>
                    <div class="competencia-content">
                        <div class="comportamento-pre">
                            Não define prioridades ou distribui adequadamente o tempo.<br><br>
                            Parece não ter construído um raciocínio clínico.<br><br>
                            Toma decisão desconsiderando o raciocínio clínico.<br><br>
                            Registra informações de forma desordenada em prontuário.<br><br>
                            Desconsidera custo/efetividade das intervenções.
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
                            Utiliza o tempo de forma precisa;<br><br>
                            Demonstra habilidades em priorizar tarefas.<br><br>
                            Desempenha as tarefas em uma sequência lógica.<br><br>
                            Reconhece limitações, observando riscos e benefícios.<br><br>
                            Demonstra habilidade em utilizar recursos disponíveis, como exames complementares, e ferramentas de apoio ao diagnóstico.<br><br>
                            Demonstra habilidade em documentar as informações relevantes de forma clara e concisa.
                        </div>
                    </div>
                </div>

                <!-- Avaliação Clínica Geral -->
                <div class="competencia-item">
                    <div class="competencia-header">Avaliação Clínica Geral</div>
                    <div class="competencia-content">
                        <div class="comportamento-pre">
                            Aborda o paciente de forma não sistematizada, mostra-se culturalmente insensível, desconsidera a segurança e a perspectiva do paciente e toma decisão subestimando o raciocínio clinico e a utilização racional de recursos diagnósticos.
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
                            <div style="font-size: 10px; text-align: center; margin-top: 5px;">
                                Avaliador, considere o atendimento observado como um todo e faça sua avaliação enfatizando o conceito global do desempenho do interno.
                            </div>
                        </div>
                        <div class="comportamento-pos">
                            Demonstra julgamento clínico satisfatório, síntese, cuidado, eficácia, eficiência, uso apropriado de recursos, equilibra riscos e benefícios, considera a segurança do paciente e apresenta consciência das próprias limitações.
                        </div>
                    </div>
                </div>
            </div>

            <!-- Feedback -->
            <div class="feedback-section">
                <div class="feedback-title">Feedback (preenchido pelo professor/preceptor)</div>
                
                <div class="feedback-item">
                    <label>O que foi bom? (Fortalezas no desempenho do estudante)</label>
                    <textarea name="feedbackPositivo">${avaliacao != null ? avaliacao.feedbackPositivo : ''}</textarea>
                </div>
                
                <div class="feedback-item">
                    <label>O que poderia ter sido melhor? (Fragilidades no desempenho do estudante)</label>
                    <textarea name="feedbackMelhoria">${avaliacao != null ? avaliacao.feedbackMelhoria : ''}</textarea>
                </div>
                
                <div class="feedback-item">
                    <label>Contrato de aprendizagem (preenchido pelo estudante)</label>
                    <label style="font-size: 11px; font-weight: normal;">Propostas efetivas de aprimoramento de habilidades. (Compromisso com plano de melhoria)</label>
                    <textarea name="contratoAprendizagem">${avaliacao != null ? avaliacao.contratoAprendizagem : ''}</textarea>
                </div>
            </div>

            <!-- Assinaturas -->
            <div class="assinaturas">
                <div class="assinatura-campo">
                    <div class="assinatura-linha"></div>
                    <div class="assinatura-label">Assinatura e carimbo do Aluno</div>
                </div>
                <div class="assinatura-campo">
                    <div class="assinatura-linha"></div>
                    <div class="assinatura-label">Assinatura e carimbo do Professor/preceptor</div>
                </div>
            </div>

            <!-- Botões de Ação -->
            <div class="botoes-acao">
                <button type="submit" class="btn btn-primary">Salvar Avaliação</button>
                <button type="button" class="btn" onclick="window.print()">Imprimir</button>
                <a href="${pageContext.request.contextPath}/avaliacoes" class="btn">Cancelar</a>
            </div>
        </form>
    </div>
</body>
</html>

