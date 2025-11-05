<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- Mapeamento das respostas por competência específicas do 360° Pares --%>
<c:set var="respostaAnamnese" value="0" />
<c:set var="respostaExameFisico" value="0" />
<c:set var="respostaRaciocinio" value="0" />
<c:set var="respostaProfissionalismo" value="0" />
<c:set var="respostaComunicacao" value="0" />
<c:set var="respostaOrganizacao" value="0" />
<c:set var="respostaCompetenciaGlobal" value="0" />
<c:set var="respostaCompaixao" value="0" />
<c:set var="respostaAbordagemSuave" value="0" />
<c:set var="respostaInteracaoEquipe" value="0" />

<c:if test="${respostas != null}">
    <c:forEach var="resposta" items="${respostas}">
        <c:set var="nomeComp" value="${fn:toLowerCase(fn:trim(resposta.competenciaQuestionario.nomeCompetencia))}" />
        <c:set var="valorInt" value="${resposta.respostaValorNumerico != null ? resposta.respostaValorNumerico.intValue() : 0}" />
        
        <c:choose>
            <c:when test="${nomeComp eq 'anamnese' || fn:contains(nomeComp, 'anamnese')}">
                <c:set var="respostaAnamnese" value="${valorInt}" />
            </c:when>
            <c:when test="${nomeComp eq 'exame físico' || fn:contains(nomeComp, 'exame físico')}">
                <c:set var="respostaExameFisico" value="${valorInt}" />
            </c:when>
            <c:when test="${nomeComp eq 'raciocínio clínico' || fn:contains(nomeComp, 'raciocínio')}">
                <c:set var="respostaRaciocinio" value="${valorInt}" />
            </c:when>
            <c:when test="${nomeComp eq 'profissionalismo' || fn:contains(nomeComp, 'profissionalismo')}">
                <c:set var="respostaProfissionalismo" value="${valorInt}" />
            </c:when>
            <c:when test="${nomeComp eq 'comunicação'}">
                <c:set var="respostaComunicacao" value="${valorInt}" />
            </c:when>
            <c:when test="${nomeComp eq 'organização e eficiência' || fn:contains(nomeComp, 'organização')}">
                <c:set var="respostaOrganizacao" value="${valorInt}" />
            </c:when>
            <c:when test="${nomeComp eq 'competência profissional global' || fn:contains(nomeComp, 'global')}">
                <c:set var="respostaCompetenciaGlobal" value="${valorInt}" />
            </c:when>
            <c:when test="${nomeComp eq 'atitude de compaixão e respeito' || fn:contains(nomeComp, 'compaixão')}">
                <c:set var="respostaCompaixao" value="${valorInt}" />
            </c:when>
            <c:when test="${nomeComp eq 'abordagem suave e sensível ao paciente' || fn:contains(nomeComp, 'suave')}">
                <c:set var="respostaAbordagemSuave" value="${valorInt}" />
            </c:when>
            <c:when test="${nomeComp eq 'comunicação e interação respeitosa com a equipe' || fn:contains(nomeComp, 'interação')}">
                <c:set var="respostaInteracaoEquipe" value="${valorInt}" />
            </c:when>
        </c:choose>
    </c:forEach>
</c:if>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Avaliação 360° Formativa - Avaliação por Pares</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>AVALIAÇÃO 360 FORMATIVA (MEDICINA UNIFAE)</h1>
            <h2>AVALIAÇÃO POR PARES</h2>
        </div>

        <form action="${pageContext.request.contextPath}/avaliacao/form" method="post">
            <input type="hidden" name="action" value="${action}">
            <input type="hidden" name="questionarioId" value="${questionarioIdSelecionado}">
            <c:if test="${action == 'edit'}">
                <input type="hidden" name="avaliacaoId" value="${avaliacao.idAvaliacaoPreenchida}">
            </c:if>

            <!-- Dados Básicos -->
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
                    
                    <div class="form-group">
                        <label for="avaliadorId">Par Avaliador:</label>
                        <select id="avaliadorId" name="avaliadorId" required>
                            <option value="">Selecione o par avaliador...</option>
                            <c:forEach var="aluno" items="${alunos}">
                                <option value="${aluno.idUsuario}"
                                        <c:if test="${avaliacao != null && avaliacao.avaliador.idUsuario == aluno.idUsuario}">selected</c:if>>
                                    ${aluno.nomeCompleto}
                                </option>
                            </c:forEach>
                        </select>
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

            <!-- Tabela de Competências -->
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
                        <!-- Anamnese -->
                        <tr>
                            <td class="competencia-nome">Anamnese</td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="1" end="3">
                                    <label><input type="radio" name="resposta_anamnese" value="${i}"
                                                  <c:if test="${respostaAnamnese == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="4" end="6">
                                    <label><input type="radio" name="resposta_anamnese" value="${i}"
                                                  <c:if test="${respostaAnamnese == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="7" end="9">
                                    <label><input type="radio" name="resposta_anamnese" value="${i}"
                                                  <c:if test="${respostaAnamnese == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="nao-avaliado">
                                <input type="checkbox" name="nao_avaliado_anamnese" value="true">
                            </td>
                        </tr>

                        <!-- Exame Físico -->
                        <tr>
                            <td class="competencia-nome">Exame Físico</td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="1" end="3">
                                    <label><input type="radio" name="resposta_exame_fisico" value="${i}"
                                                  <c:if test="${respostaExameFisico == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="4" end="6">
                                    <label><input type="radio" name="resposta_exame_fisico" value="${i}"
                                                  <c:if test="${respostaExameFisico == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="7" end="9">
                                    <label><input type="radio" name="resposta_exame_fisico" value="${i}"
                                                  <c:if test="${respostaExameFisico == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="nao-avaliado">
                                <input type="checkbox" name="nao_avaliado_exame_fisico" value="true">
                            </td>
                        </tr>

                        <!-- Raciocínio Clínico -->
                        <tr>
                            <td class="competencia-nome">Raciocínio Clínico</td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="1" end="3">
                                    <label><input type="radio" name="resposta_raciocinio_clinico" value="${i}"
                                                  <c:if test="${respostaRaciocinio == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="4" end="6">
                                    <label><input type="radio" name="resposta_raciocinio_clinico" value="${i}"
                                                  <c:if test="${respostaRaciocinio == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="7" end="9">
                                    <label><input type="radio" name="resposta_raciocinio_clinico" value="${i}"
                                                  <c:if test="${respostaRaciocinio == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="nao-avaliado">
                                <input type="checkbox" name="nao_avaliado_raciocinio_clinico" value="true">
                            </td>
                        </tr>

                        <!-- Profissionalismo -->
                        <tr>
                            <td class="competencia-nome">Profissionalismo</td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="1" end="3">
                                    <label><input type="radio" name="resposta_profissionalismo" value="${i}"
                                                  <c:if test="${respostaProfissionalismo == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="4" end="6">
                                    <label><input type="radio" name="resposta_profissionalismo" value="${i}"
                                                  <c:if test="${respostaProfissionalismo == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="7" end="9">
                                    <label><input type="radio" name="resposta_profissionalismo" value="${i}"
                                                  <c:if test="${respostaProfissionalismo == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="nao-avaliado">
                                <input type="checkbox" name="nao_avaliado_profissionalismo" value="true">
                            </td>
                        </tr>

                        <!-- Comunicação -->
                        <tr>
                            <td class="competencia-nome">Comunicação</td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="1" end="3">
                                    <label><input type="radio" name="resposta_comunicacao" value="${i}"
                                                  <c:if test="${respostaComunicacao == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="4" end="6">
                                    <label><input type="radio" name="resposta_comunicacao" value="${i}"
                                                  <c:if test="${respostaComunicacao == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="7" end="9">
                                    <label><input type="radio" name="resposta_comunicacao" value="${i}"
                                                  <c:if test="${respostaComunicacao == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="nao-avaliado">
                                <input type="checkbox" name="nao_avaliado_comunicacao" value="true">
                            </td>
                        </tr>

                        <!-- Organização e eficiência -->
                        <tr>
                            <td class="competencia-nome">Organização e eficiência</td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="1" end="3">
                                    <label><input type="radio" name="resposta_organizacao" value="${i}"
                                                  <c:if test="${respostaOrganizacao == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="4" end="6">
                                    <label><input type="radio" name="resposta_organizacao" value="${i}"
                                                  <c:if test="${respostaOrganizacao == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="7" end="9">
                                    <label><input type="radio" name="resposta_organizacao" value="${i}"
                                                  <c:if test="${respostaOrganizacao == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="nao-avaliado">
                                <input type="checkbox" name="nao_avaliado_organizacao" value="true">
                            </td>
                        </tr>

                        <!-- Competência Profissional Global -->
                        <tr>
                            <td class="competencia-nome">Competência Profissional Global</td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="1" end="3">
                                    <label><input type="radio" name="resposta_competencia_global" value="${i}"
                                                  <c:if test="${respostaCompetenciaGlobal == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="4" end="6">
                                    <label><input type="radio" name="resposta_competencia_global" value="${i}"
                                                  <c:if test="${respostaCompetenciaGlobal == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="7" end="9">
                                    <label><input type="radio" name="resposta_competencia_global" value="${i}"
                                                  <c:if test="${respostaCompetenciaGlobal == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="nao-avaliado">
                                <input type="checkbox" name="nao_avaliado_competencia_global" value="true">
                            </td>
                        </tr>

                        <!-- Seção Atitudes -->
                        <tr class="secao-header">
                            <td colspan="5"><strong>Atitudes</strong></td>
                        </tr>

                        <!-- Atitude de compaixão e respeito -->
                        <tr>
                            <td class="competencia-nome">Atitude de compaixão e respeito</td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="1" end="3">
                                    <label><input type="radio" name="resposta_compaixao" value="${i}"
                                                  <c:if test="${respostaCompaixao == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="4" end="6">
                                    <label><input type="radio" name="resposta_compaixao" value="${i}"
                                                  <c:if test="${respostaCompaixao == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="7" end="9">
                                    <label><input type="radio" name="resposta_compaixao" value="${i}"
                                                  <c:if test="${respostaCompaixao == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="nao-avaliado">
                                <input type="checkbox" name="nao_avaliado_compaixao" value="true">
                            </td>
                        </tr>

                        <!-- Abordagem suave e sensível ao paciente -->
                        <tr>
                            <td class="competencia-nome">Abordagem suave e sensível ao paciente</td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="1" end="3">
                                    <label><input type="radio" name="resposta_abordagem_suave" value="${i}"
                                                  <c:if test="${respostaAbordagemSuave == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="4" end="6">
                                    <label><input type="radio" name="resposta_abordagem_suave" value="${i}"
                                                  <c:if test="${respostaAbordagemSuave == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="7" end="9">
                                    <label><input type="radio" name="resposta_abordagem_suave" value="${i}"
                                                  <c:if test="${respostaAbordagemSuave == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="nao-avaliado">
                                <input type="checkbox" name="nao_avaliado_abordagem_suave" value="true">
                            </td>
                        </tr>

                        <!-- Comunicação e interação respeitosa com a equipe -->
                        <tr>
                            <td class="competencia-nome">Comunicação e interação respeitosa com a equipe</td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="1" end="3">
                                    <label><input type="radio" name="resposta_interacao_equipe" value="${i}"
                                                  <c:if test="${respostaInteracaoEquipe == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="4" end="6">
                                    <label><input type="radio" name="resposta_interacao_equipe" value="${i}"
                                                  <c:if test="${respostaInteracaoEquipe == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="escala-opcoes">
                                <c:forEach var="i" begin="7" end="9">
                                    <label><input type="radio" name="resposta_interacao_equipe" value="${i}"
                                                  <c:if test="${respostaInteracaoEquipe == i}">checked</c:if>> ${i}</label>
                                </c:forEach>
                            </td>
                            <td class="nao-avaliado">
                                <input type="checkbox" name="nao_avaliado_interacao_equipe" value="true">
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <!-- Feedback -->
            <div class="feedback-section">
                <h3>Feedback (preenchido pelo Par Avaliador)</h3>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="feedbackPositivo">O que foi bom? (Fortalezas no desempenho do estudante)</label>
                        <textarea id="feedbackPositivo" name="feedbackPositivo" rows="4">${avaliacao != null ? avaliacao.feedbackPositivo : ''}</textarea>
                    </div>
                    
                    <div class="form-group">
                        <label for="feedbackMelhoria">O que poderia ter sido melhor? (Fragilidades no desempenho do estudante)</label>
                        <textarea id="feedbackMelhoria" name="feedbackMelhoria" rows="4">${avaliacao != null ? avaliacao.feedbackMelhoria : ''}</textarea>
                    </div>
                </div>
            </div>

            <!-- Botões -->
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

    <script>
        // Desabilitar radio buttons quando "Não Avaliado" for marcado
        document.querySelectorAll('input[type="checkbox"][name^="nao_avaliado_"]').forEach(function(checkbox) {
            checkbox.addEventListener('change', function() {
                const competencia = this.name.replace('nao_avaliado_', '');
                const radios = document.querySelectorAll('input[name="resposta_' + competencia + '"]');
                
                radios.forEach(function(radio) {
                    radio.disabled = checkbox.checked;
                    if (checkbox.checked) {
                        radio.checked = false;
                    }
                });
            });
        });

        // Validação do formulário
        document.querySelector('form').addEventListener('submit', function(e) {
            const competencias = [
                'anamnese', 'exame_fisico', 'raciocinio_clinico', 'profissionalismo', 
                'comunicacao', 'organizacao', 'competencia_global', 'compaixao', 
                'abordagem_suave', 'interacao_equipe'
            ];
            
            let todasAvaliadas = true;
            
            competencias.forEach(function(comp) {
                const radios = document.querySelectorAll('input[name="resposta_' + comp + '"]:checked');
                const naoAvaliado = document.querySelector('input[name="nao_avaliado_' + comp + '"]:checked');
                
                if (radios.length === 0 && !naoAvaliado) {
                    todasAvaliadas = false;
                }
            });
            
            if (!todasAvaliadas) {
                alert('Por favor, avalie todas as competências ou marque "Não Avaliado" quando aplicável.');
                e.preventDefault();
            }
        });
    </script>
</body>
</html>

