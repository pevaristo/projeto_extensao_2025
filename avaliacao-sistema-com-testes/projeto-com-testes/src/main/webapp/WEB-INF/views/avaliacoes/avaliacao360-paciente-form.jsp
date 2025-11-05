<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- Mapeamento das respostas para as competências da Avaliação do Paciente --%>
<c:set var="respostaCortesia" value="0" />
<c:set var="respostaClareza" value="0" />
<c:set var="respostaInteresse" value="0" />
<c:set var="respostaRespeito" value="0" />
<c:set var="respostaTempo" value="0" />
<c:set var="respostaTranquilizar" value="0" />
<c:set var="respostaExplicacao" value="0" />
<c:set var="respostaEnvolvimento" value="0" />
<c:set var="respostaCuidado" value="0" />
<c:set var="respostaSatisfacao" value="0" />

<%-- Mapeamento de competências adicionais do PDF que podem ser associadas --%>
<c:set var="respostaTratamentoNivel" value="0" />
<c:set var="respostaEscuta" value="0" />

<c:if test="${respostas != null}">
    <c:forEach var="resposta" items="${respostas}">
        <c:set var="nomeComp" value="${fn:toLowerCase(fn:trim(resposta.competenciaQuestionario.nomeCompetencia))}" />
        <c:set var="valorInt" value="${resposta.respostaValorNumerico != null ?
                                       resposta.respostaValorNumerico.intValue() : 0}" />

        <c:choose>
            <c:when test="${fn:contains(nomeComp, 'cortesia')}">
                <c:set var="respostaCortesia" value="${valorInt}" />
            </c:when>
            <c:when test="${fn:contains(nomeComp, 'clareza')}">
                <c:set var="respostaClareza" value="${valorInt}" />
            </c:when>
            <c:when test="${fn:contains(nomeComp, 'interesse')}">
                <c:set var="respostaInteresse" value="${valorInt}" />
            </c:when>
            <c:when test="${fn:contains(nomeComp, 'respeito')}">
                <c:set var="respostaRespeito" value="${valorInt}" />
            </c:when>
            <c:when test="${fn:contains(nomeComp, 'tempo')}">
                <c:set var="respostaTempo" value="${valorInt}" />
            </c:when>
            <c:when test="${fn:contains(nomeComp, 'tranquilizar')}">
                <c:set var="respostaTranquilizar" value="${valorInt}" />
            </c:when>
            <c:when test="${fn:contains(nomeComp, 'explicação')}">
                <c:set var="respostaExplicacao" value="${valorInt}" />
            </c:when>
            <c:when test="${fn:contains(nomeComp, 'envolvimento')}">
                <c:set var="respostaEnvolvimento" value="${valorInt}" />
            </c:when>
            <c:when test="${fn:contains(nomeComp, 'cuidado')}">
                <c:set var="respostaCuidado" value="${valorInt}" />
            </c:when>
            <c:when test="${fn:contains(nomeComp, 'satisfação')}">
                <c:set var="respostaSatisfacao" value="${valorInt}" />
            </c:when>
        </c:choose>
    </c:forEach>
</c:if>

<!DOCTYPE html>
<html lang="pt-BR">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Avaliação 360° Formativa - Avaliação do Paciente ou da Família</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h1>AVALIAÇÃO 360 FORMATIVA (MEDICINA UNIFAE)</h1>
                <h2>AVALIAÇÃO DO PACIENTE OU DA FAMÍLIA</h2>
            </div>

            <form action="${pageContext.request.contextPath}/avaliacao/form" method="post">
                <input type="hidden" name="action" value="${action}">
                <input type="hidden" name="questionarioId" value="${questionarioIdSelecionado}">
                <c:if test="${action == 'edit'}">
                    <input type="hidden" name="avaliacaoId" value="${avaliacao.idAvaliacaoPreenchida}">
                </c:if>

                <div class="form-section">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="alunoAvaliadoId">Aluno:</label>
                            <select id="alunoAvaliadoId" name="alunoAvaliadoId" required>
                                <option value="">Selecione o aluno...</option>
                                <c:forEach var="aluno" items="${alunos}">
                                    <option value="${aluno.idUsuario}" 
                                            <c:if test="${avaliacao != null && avaliacao.alunoAvaliado.idUsuario == aluno.idUsuario}">selected</c:if>>
                                        ${aluno.nomeCompleto}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label>Avaliador:</label>
                            <div class="escala-simples" style="padding-top: 5px;">
                                <label><input type="radio" name="tipoAvaliadorNaoUsuario" value="Paciente" <c:if test="${avaliacao.tipoAvaliadorNaoUsuario == 'Paciente'}">checked</c:if>> Paciente</label>
                                <label><input type="radio" name="tipoAvaliadorNaoUsuario" value="Familiar" <c:if test="${avaliacao.tipoAvaliadorNaoUsuario == 'Familiar'}">checked</c:if>> Familiar</label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="nomeAvaliadorNaoUsuario">Nome do Avaliador (Opcional):</label>
                                <input type="text" id="nomeAvaliadorNaoUsuario" name="nomeAvaliadorNaoUsuario" value="${avaliacao != null ? avaliacao.nomeAvaliadorNaoUsuario : ''}">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="localRealizacao">Local de realização:</label>
                            <select id="localRealizacao" name="localRealizacao" required>
                                <option value="">Selecione o local...</option>
                                <c:forEach var="local" items="${locaisEventos}">
                                    <option value="${local.nomeLocal}"
                                            <c:if test="${avaliacao != null && avaliacao.localRealizacao == local.nomeLocal}">selected</c:if>>
                                        ${local.nomeLocal}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="horarioInicio">Horário de início:</label>
                            <input type="time" id="horarioInicio" name="horarioInicio" 
                                   value="${avaliacao != null ? avaliacao.horarioInicio : ''}" required>
                        </div>

                        <div class="form-group">
                            <label for="horarioFim">Horário de término:</label>
                            <input type="time" id="horarioFim" name="horarioFim" 
                                   value="${avaliacao != null ? avaliacao.horarioFim : ''}" required>
                        </div>

                        <div class="form-group">
                            <label for="dataRealizacao">Data da realização:</label>
                            <input type="date" id="dataRealizacao" name="dataRealizacao" 
                                   value="${avaliacao != null ? avaliacao.dataRealizacao : ''}" required>
                        </div>
                    </div>
                </div>

                <div class="competencias-container">
                    <div class="competencia-header" style="text-align: left; padding-left: 15px;">Avalie o atendimento oferecido por este estudante</div>
                    <div style="padding: 10px; text-align: center; border: 1px solid #000; border-top: none; margin-bottom: 15px;">
                        Classifique de 1 a 9, em que o número 1 indica a pior avaliação e o 9 a melhor avaliação possível
                    </div>

                    <div class="competencia-simples">
                        <div class="competencia-nome">O chamou pelo seu nome? O cumprimentou carinhosamente? Foi gentil?</div>
                        <div class="escala-simples">
                            <c:forEach var="i" begin="1" end="9">
                                <label><input type="radio" name="resposta_cortesia_educacao" value="${i}" <c:if test="${respostaCortesia == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                        </div>
                    </div>
                    <div class="competencia-simples">
                        <div class="competencia-nome">Tratou você no mesmo nível? O tratou como criança?</div>
                        <div class="escala-simples">
                            <c:forEach var="i" begin="1" end="9">
                                <label><input type="radio" name="resposta_respeito_privacidade" value="${i}" <c:if test="${respostaRespeito == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                        </div>
                    </div>
                    <div class="competencia-simples">
                        <div class="competencia-nome">Deixou contar sua história? Escutou o que você falou? O interrompeu quando você falava?</div>
                        <div class="escala-simples">
                            <c:forEach var="i" begin="1" end="9">
                                <label><input type="radio" name="resposta_demonstracao_interesse" value="${i}" <c:if test="${respostaInteresse == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                        </div>
                    </div>
                    <div class="competencia-simples">
                        <div class="competencia-nome">Mostrou interesse em você como pessoa? Não se mostrou aborrecido enquanto você falava?</div>
                        <div class="escala-simples">
                            <c:forEach var="i" begin="1" end="9">
                                <label><input type="radio" name="resposta_demonstracao_cuidado" value="${i}" <c:if test="${respostaCuidado == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                        </div>
                    </div>
                    <div class="competencia-simples">
                        <div class="competencia-nome">Buscou explicar a doença? Confirmou que você compreendeu? Foi verdadeiro e franco?</div>
                        <div class="escala-simples">
                            <c:forEach var="i" begin="1" end="9">
                                <label><input type="radio" name="resposta_clareza_comunicacao" value="${i}" <c:if test="${respostaClareza == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                        </div>
                    </div>
                    <div class="competencia-simples">
                        <div class="competencia-nome">Teve o cuidado de falar o que iria fazer quando estava examinando e o que encontrou após examinar?</div>
                        <div class="escala-simples">
                            <c:forEach var="i" begin="1" end="9">
                                <label><input type="radio" name="resposta_explicacao_procedimentos" value="${i}" <c:if test="${respostaExplicacao == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                        </div>
                    </div>
                    <div class="competencia-simples">
                        <div class="competencia-nome">Conversou sobre possibilidades de exames e tratamentos? Permitiu que opinasse sobre eles?</div>
                        <div class="escala-simples">
                            <c:forEach var="i" begin="1" end="9">
                                <label><input type="radio" name="resposta_envolvimento_decisao" value="${i}" <c:if test="${respostaEnvolvimento == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                        </div>
                    </div>
                    <div class="competencia-simples">
                        <div class="competencia-nome">Estimulou a fazer perguntas?</div>
                        <div class="escala-simples">
                            <c:forEach var="i" begin="1" end="9">
                                <label><input type="radio" name="resposta_capacidade_tranquilizar" value="${i}" <c:if test="${respostaTranquilizar == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                        </div>
                    </div>
                    <div class="competencia-simples">
                        <div class="competencia-nome">Respondeu suas dúvidas de modo com que entendesse? Usou palavras que pôde compreender? Explicou os termos médicos na sua linguagem?</div>
                        <div class="escala-simples">
                            <c:forEach var="i" begin="1" end="9">
                                <label><input type="radio" name="resposta_tempo_dedicado" value="${i}" <c:if test="${respostaTempo == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                        </div>
                    </div>
                    <div class="competencia-simples">
                        <div class="competencia-nome">Mostrou-se disponível para novas explicações ou ajudar em outros momentos?</div>
                        <div class="escala-simples">
                            <c:forEach var="i" begin="1" end="9">
                                <label><input type="radio" name="resposta_satisfacao_geral" value="${i}" <c:if test="${respostaSatisfacao == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                        </div>
                    </div>
                </div>

                <div class="feedback-section">
                    <h3>Feedback do paciente ou da família</h3>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="feedbackPositivo">O que foi bom no atendimento do aluno?</label>
                            <textarea id="feedbackPositivo" name="feedbackPositivo" rows="4">${avaliacao != null ? avaliacao.feedbackPositivo : ''}</textarea>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="feedbackMelhoria">O que poderia ter sido melhor no atendimento do aluno?</label>
                            <textarea id="feedbackMelhoria" name="feedbackMelhoria" rows="4">${avaliacao != null ? avaliacao.feedbackMelhoria : ''}</textarea>
                        </div>
                    </div>
                </div>

                <div class="assinaturas" style="margin-top: 25px; border-top: 2px solid #000; padding-top: 20px;">
                    <div class="assinatura-campo">
                        <div class="assinatura-linha"></div>
                        <div class="assinatura-label">Assinatura e carimbo do Aluno</div>
                    </div>
                    <div class="assinatura-campo">
                        <div class="assinatura-linha"></div>
                        <div class="assinatura-label">Assinatura do Paciente ou Familiar</div>
                    </div>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <c:choose>
                            <c:when test="${action == 'edit'}">Atualizar Avaliação</c:when>
                            <c:otherwise>Salvar Avaliação</c:otherwise>
                        </c:choose>
                    </button>
                    <a href="${pageContext.request.contextPath}/avaliacoes" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </body>
</html>
