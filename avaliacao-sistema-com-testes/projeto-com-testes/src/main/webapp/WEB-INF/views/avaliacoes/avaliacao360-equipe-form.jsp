<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- 1. Mapeamento CORRETO das respostas para as competências da Avaliação da Equipe --%>
<c:set var="respostaColaboracao" value="0" />
<c:set var="respostaComunicacaoInter" value="0" />
<c:set var="respostaRespeito" value="0" />
<c:set var="respostaResponsabilidade" value="0" />
<c:set var="respostaLideranca" value="0" />
<c:set var="respostaConflitos" value="0" />
<c:set var="respostaEmpatia" value="0" />
<c:set var="respostaEtica" value="0" />
<c:set var="respostaFlexibilidade" value="0" />
<c:set var="respostaContribuicao" value="0" />

<c:if test="${respostas != null}">
    <c:forEach var="resposta" items="${respostas}">
        <c:set var="nomeComp" value="${fn:toLowerCase(fn:trim(resposta.competenciaQuestionario.nomeCompetencia))}" />
        <c:set var="valorInt" value="${resposta.respostaValorNumerico != null ?
                                       resposta.respostaValorNumerico.intValue() : 0}" />

        <%-- 2. Bloco <c:choose> CORRIGIDO para as competências da Equipe --%>
        <c:choose>
            <c:when test="${fn:contains(nomeComp, 'colaboração em equipe')}">
                <c:set var="respostaColaboracao" value="${valorInt}" />
            </c:when>
            <c:when test="${fn:contains(nomeComp, 'comunicação interprofissional')}">
                <c:set var="respostaComunicacaoInter" value="${valorInt}" />
            </c:when>
            <c:when test="${fn:contains(nomeComp, 'respeito mútuo')}">
                <c:set var="respostaRespeito" value="${valorInt}" />
            </c:when>
            <c:when test="${fn:contains(nomeComp, 'responsabilidade compartilhada')}">
                <c:set var="respostaResponsabilidade" value="${valorInt}" />
            </c:when>
            <c:when test="${fn:contains(nomeComp, 'liderança situacional')}">
                <c:set var="respostaLideranca" value="${valorInt}" />
            </c:when>
            <c:when test="${fn:contains(nomeComp, 'resolução de conflitos')}">
                <c:set var="respostaConflitos" value="${valorInt}" />
            </c:when>
            <c:when test="${fn:contains(nomeComp, 'empatia profissional')}">
                <c:set var="respostaEmpatia" value="${valorInt}" />
            </c:when>
            <c:when test="${fn:contains(nomeComp, 'ética no trabalho em equipe')}">
                <c:set var="respostaEtica" value="${valorInt}" />
            </c:when>
            <c:when test="${fn:contains(nomeComp, 'flexibilidade e adaptação')}">
                <c:set var="respostaFlexibilidade" value="${valorInt}" />
            </c:when>
            <c:when test="${fn:contains(nomeComp, 'contribuição para o ambiente de trabalho')}">
                <c:set var="respostaContribuicao" value="${valorInt}" />
            </c:when>
        </c:choose>
    </c:forEach>
</c:if>

<!DOCTYPE html>
<html lang="pt-BR">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Avaliação 360° Formativa - Avaliação da Equipe de Saúde</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">
    </head>
    <body>
        <div class="container">
            <div class="header">

                <h1>AVALIAÇÃO 360 FORMATIVA (MEDICINA UNIFAE)</h1>
                <h2>AVALIAÇÃO DA EQUIPE DE SAÚDE (PROFISSIONAIS NÃO MÉDICOS)</h2>
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
                                <label><input type="radio" name="tipoAvaliadorNaoUsuario" value="Enfermagem" <c:if test="${avaliacao.tipoAvaliadorNaoUsuario == 'Enfermagem'}">checked</c:if>> Enfermagem</label> 

                                    <label><input type="radio" name="tipoAvaliadorNaoUsuario" value="Fisioterapia" <c:if test="${avaliacao.tipoAvaliadorNaoUsuario == 'Fisioterapia'}">checked</c:if>> Fisioterapia</label> 
                                <label><input type="radio" name="tipoAvaliadorNaoUsuario" value="Psicologia" <c:if test="${avaliacao.tipoAvaliadorNaoUsuario == 'Psicologia'}">checked</c:if>> Psicologia</label> 
                                <label><input type="radio" name="tipoAvaliadorNaoUsuario" value="Tecnico de Enfermagem" <c:if test="${avaliacao.tipoAvaliadorNaoUsuario == 'Tecnico de Enfermagem'}">checked</c:if>> Técnico de Enfermagem</label> 

                                    <label><input type="radio" name="tipoAvaliadorNaoUsuario" value="Outros" <c:if test="${avaliacao.tipoAvaliadorNaoUsuario == 'Outros'}">checked</c:if>> Outros</label> 
                                </div>
                            </div>
                            <div class="form-group">

                                <label for="nomeAvaliadorNaoUsuario">Nome do Avaliador (Opcional):</label>
                                <input type="text" id="nomeAvaliadorNaoUsuario" name="nomeAvaliadorNaoUsuario" value="${avaliacao != null ?
                                                                                                                    avaliacao.nomeAvaliadorNaoUsuario : ''}">
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
                                   value="${avaliacao != null ?
                                            avaliacao.horarioInicio : ''}" required>
                        </div>

                        <div class="form-group">
                            <label for="horarioFim">Horário de término:</label> 

                            <input type="time" id="horarioFim" name="horarioFim" 
                                   value="${avaliacao != null ?
                                            avaliacao.horarioFim : ''}" required>
                        </div>

                        <div class="form-group">
                            <label for="dataRealizacao">Data da realização:</label>

                            <input type="date" id="dataRealizacao" name="dataRealizacao" 
                                   value="${avaliacao != null ?
                                            avaliacao.dataRealizacao : ''}" required>
                        </div>
                    </div>
                </div>

                <div class="evaluation-table">
                    <table>

                        <thead>
                            <tr>
                                <th rowspan="2">Competências</th>
                                <th colspan="3">Classificação</th>

                                <th rowspan="2">Não<br>avaliado</th>
                            </tr>
                            <tr>
                                <th>Insatisfatório</th> 

                                <th>Satisfatório</th> 
                                <th>Superior</th> 
                            </tr>
                        </thead>

                        <tbody>
                            <tr>
                                <td class="competencia-nome">Colaboração em equipe</td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="1" end="3">
                                        <label><input type="radio" name="resposta_colaboracao_equipe" value="${i}" <c:if test="${respostaColaboracao == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="4" end="6">
                                        <label><input type="radio" name="resposta_colaboracao_equipe" value="${i}" <c:if test="${respostaColaboracao == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="7" end="9">
                                        <label><input type="radio" name="resposta_colaboracao_equipe" value="${i}" <c:if test="${respostaColaboracao == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="nao-avaliado"><input type="checkbox" name="nao_avaliado_colaboracao_equipe" value="true"></td>
                            </tr>
                            <tr>
                                <td class="competencia-nome">Comunicação interprofissional</td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="1" end="3">
                                        <label><input type="radio" name="resposta_comunicacao_interprofissional" value="${i}" <c:if test="${respostaComunicacaoInter == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="4" end="6">
                                        <label><input type="radio" name="resposta_comunicacao_interprofissional" value="${i}" <c:if test="${respostaComunicacaoInter == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="7" end="9">
                                        <label><input type="radio" name="resposta_comunicacao_interprofissional" value="${i}" <c:if test="${respostaComunicacaoInter == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="nao-avaliado"><input type="checkbox" name="nao_avaliado_comunicacao_interprofissional" value="true"></td>
                            </tr>
                            <tr>
                                <td class="competencia-nome">Respeito mútuo</td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="1" end="3">
                                        <label><input type="radio" name="resposta_respeito_mutuo" value="${i}" <c:if test="${respostaRespeito == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="4" end="6">
                                        <label><input type="radio" name="resposta_respeito_mutuo" value="${i}" <c:if test="${respostaRespeito == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="7" end="9">
                                        <label><input type="radio" name="resposta_respeito_mutuo" value="${i}" <c:if test="${respostaRespeito == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="nao-avaliado"><input type="checkbox" name="nao_avaliado_respeito_mutuo" value="true"></td>
                            </tr>
                            <tr>
                                <td class="competencia-nome">Responsabilidade compartilhada</td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="1" end="3">
                                        <label><input type="radio" name="resposta_responsabilidade_compartilhada" value="${i}" <c:if test="${respostaResponsabilidade == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="4" end="6">
                                        <label><input type="radio" name="resposta_responsabilidade_compartilhada" value="${i}" <c:if test="${respostaResponsabilidade == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="7" end="9">
                                        <label><input type="radio" name="resposta_responsabilidade_compartilhada" value="${i}" <c:if test="${respostaResponsabilidade == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="nao-avaliado"><input type="checkbox" name="nao_avaliado_responsabilidade_compartilhada" value="true"></td>
                            </tr>
                            <tr>
                                <td class="competencia-nome">Liderança situacional</td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="1" end="3">
                                        <label><input type="radio" name="resposta_lideranca_situacional" value="${i}" <c:if test="${respostaLideranca == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="4" end="6">
                                        <label><input type="radio" name="resposta_lideranca_situacional" value="${i}" <c:if test="${respostaLideranca == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="7" end="9">
                                        <label><input type="radio" name="resposta_lideranca_situacional" value="${i}" <c:if test="${respostaLideranca == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="nao-avaliado"><input type="checkbox" name="nao_avaliado_lideranca_situacional" value="true"></td>
                            </tr>
                            <tr>
                                <td class="competencia-nome">Resolução de conflitos</td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="1" end="3">
                                        <label><input type="radio" name="resposta_resolucao_conflitos" value="${i}" <c:if test="${respostaConflitos == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="4" end="6">
                                        <label><input type="radio" name="resposta_resolucao_conflitos" value="${i}" <c:if test="${respostaConflitos == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="7" end="9">
                                        <label><input type="radio" name="resposta_resolucao_conflitos" value="${i}" <c:if test="${respostaConflitos == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="nao-avaliado"><input type="checkbox" name="nao_avaliado_resolucao_conflitos" value="true"></td>
                            </tr>
                            <tr>
                                <td class="competencia-nome">Empatia profissional</td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="1" end="3">
                                        <label><input type="radio" name="resposta_empatia_profissional" value="${i}" <c:if test="${respostaEmpatia == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="4" end="6">
                                        <label><input type="radio" name="resposta_empatia_profissional" value="${i}" <c:if test="${respostaEmpatia == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="7" end="9">
                                        <label><input type="radio" name="resposta_empatia_profissional" value="${i}" <c:if test="${respostaEmpatia == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="nao-avaliado"><input type="checkbox" name="nao_avaliado_empatia_profissional" value="true"></td>
                            </tr>
                            <tr>
                                <td class="competencia-nome">Ética no trabalho em equipe</td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="1" end="3">
                                        <label><input type="radio" name="resposta_etica_trabalho_equipe" value="${i}" <c:if test="${respostaEtica == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="4" end="6">
                                        <label><input type="radio" name="resposta_etica_trabalho_equipe" value="${i}" <c:if test="${respostaEtica == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="7" end="9">
                                        <label><input type="radio" name="resposta_etica_trabalho_equipe" value="${i}" <c:if test="${respostaEtica == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="nao-avaliado"><input type="checkbox" name="nao_avaliado_etica_trabalho_equipe" value="true"></td>
                            </tr>
                            <tr>
                                <td class="competencia-nome">Flexibilidade e adaptação</td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="1" end="3">
                                        <label><input type="radio" name="resposta_flexibilidade_adaptacao" value="${i}" <c:if test="${respostaFlexibilidade == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="4" end="6">
                                        <label><input type="radio" name="resposta_flexibilidade_adaptacao" value="${i}" <c:if test="${respostaFlexibilidade == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="7" end="9">
                                        <label><input type="radio" name="resposta_flexibilidade_adaptacao" value="${i}" <c:if test="${respostaFlexibilidade == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="nao-avaliado"><input type="checkbox" name="nao_avaliado_flexibilidade_adaptacao" value="true"></td>
                            </tr>
                            <tr>
                                <td class="competencia-nome">Contribuição para o ambiente de trabalho</td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="1" end="3">
                                        <label><input type="radio" name="resposta_contribuicao_ambiente" value="${i}" <c:if test="${respostaContribuicao == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="4" end="6">
                                        <label><input type="radio" name="resposta_contribuicao_ambiente" value="${i}" <c:if test="${respostaContribuicao == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="escala-opcoes">
                                    <c:forEach var="i" begin="7" end="9">
                                        <label><input type="radio" name="resposta_contribuicao_ambiente" value="${i}" <c:if test="${respostaContribuicao == i}">checked</c:if>> ${i}</label>
                                        </c:forEach>
                                </td>
                                <td class="nao-avaliado"><input type="checkbox" name="nao_avaliado_contribuicao_ambiente" value="true"></td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="feedback-section">

                    <h3>Feedback (preenchido pelo Avaliador)</h3> 

                    <div class="form-row">
                        <div class="form-group">
                            <label for="feedbackPositivo">O que foi bom?
                                (Fortalezas no desempenho do estudante)</label> 
                            <textarea id="feedbackPositivo" name="feedbackPositivo" rows="4">${avaliacao != null ?
                                                                                               avaliacao.feedbackPositivo : ''}</textarea>
                        </div>

                        <div class="form-group">
                            <label for="feedbackMelhoria">O que poderia ter sido melhor?
                                (Fragilidades no desempenho do estudante)</label> 
                            <textarea id="feedbackMelhoria" name="feedbackMelhoria" rows="4">${avaliacao != null ?
                                                                                               avaliacao.feedbackMelhoria : ''}</textarea>
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

                        <div class="assinatura-label">Assinatura do Avaliador</div> 
                    </div>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <c:choose>

                            <c:when test="${action == 'edit'}">Atualizar Avaliação</c:when>
                            <c:otherwise>Salvar Avaliação</c:otherwise>
                        </c:choose>
                    </button>
                    <a href="${pageContext.request.contextPath}/avaliacoes" 
                       class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>

        <script>
            document.querySelectorAll('input[type="checkbox"][name^="nao_avaliado_"]').forEach(function (checkbox) {
                checkbox.addEventListener('change', function () {
                    const competencia = this.name.replace('nao_avaliado_', '');
                    const radios = document.querySelectorAll('input[name="resposta_' + competencia + '"]');
                    radios.forEach(function (radio) {
                        radio.disabled = checkbox.checked;

                        if (checkbox.checked) {
                            radio.checked = false;
                        }
                    });
                });

            });
        </script>
    </body>
</html>