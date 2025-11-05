<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Visualizar Avaliação - Sistema UNIFAE</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 20px;
                background-color: #f5f5f5;
            }
            .header {
                background-color: #2c3e50;
                color: white;
                padding: 20px;
                margin-bottom: 20px;
                text-align: center;
                border-radius: 8px;
            }
            .container {
                max-width: 800px;
                margin: 0 auto;
            }
            .card {
                background: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                margin: 20px 0;
            }
            .info-grid {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 20px;
                margin: 20px 0;
            }
            .info-item {
                padding: 10px;
                background: #f8f9fa;
                border-radius: 4px;
            }
            .info-label {
                font-weight: bold;
                color: #495057;
            }
            .info-value {
                margin-top: 5px;
            }
            .competencias {
                margin: 20px 0;
            }
            .competencia {
                background: #f8f9fa;
                padding: 15px;
                margin: 10px 0;
                border-radius: 4px;
                border-left: 4px solid #007bff;
            }
            .nota {
                font-size: 18px;
                font-weight: bold;
                color: #007bff;
            }
            .feedback {
                background: #e9ecef;
                padding: 15px;
                border-radius: 4px;
                margin: 10px 0;
            }
            .actions {
                text-align: center;
                margin: 20px 0;
            }
            .btn {
                background-color: #007bff;
                color: white;
                padding: 10px 20px;
                text-decoration: none;
                border-radius: 5px;
                margin: 5px;
                display: inline-block;
            }
            .btn:hover {
                background-color: #0056b3;
            }
            .btn-secondary {
                background-color: #6c757d;
            }
            .btn-secondary:hover {
                background-color: #545b62;
            }
            .breadcrumb {
                margin: 10px 0;
            }
            .breadcrumb a {
                color: #007bff;
                text-decoration: none;
            }
            .breadcrumb a:hover {
                text-decoration: underline;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h1>👁️ Visualizar Avaliação</h1>
                <p>Sistema de Avaliação UNIFAE</p>
            </div>

            <div class="breadcrumb">
                <a href="${pageContext.request.contextPath}/">🏠 Início</a> &gt; 
                <a href="${pageContext.request.contextPath}/avaliacoes">📋 Lista de Avaliações</a> &gt; 
                <span>Visualizar Avaliação</span>
            </div>

            <c:choose>
                <c:when test="${not empty avaliacao}">
                    <div class="card">
                        <h2>📋 ${avaliacao.questionario.nomeModelo}</h2>

                        <div class="info-grid">
                            <div class="info-item">
                                <div class="info-label">ID da Avaliação:</div>
                                <div class="info-value">${avaliacao.idAvaliacaoPreenchida}</div>
                            </div>
                            <div class="info-item">
                                <div class="info-label">Data de Realização:</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${not empty avaliacao.dataRealizacao}">
                                            ${avaliacao.dataRealizacao.dayOfMonth}/${avaliacao.dataRealizacao.monthValue}/${avaliacao.dataRealizacao.year}
                                        </c:when>
                                        <c:otherwise>
                                            -
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="info-item">
                                <div class="info-label">Aluno Avaliado:</div>
                                <div class="info-value">${avaliacao.alunoAvaliado.nomeCompleto}</div>
                            </div>
                            <div class="info-item">
                                <div class="info-label">Avaliador:</div>
                                <div class="info-value">${avaliacao.avaliador.nomeCompleto}</div>
                            </div>
                            <div class="info-item">
                                <div class="info-label">Horário:</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${not empty avaliacao.horarioInicio and not empty avaliacao.horarioFim}">
                                            ${avaliacao.horarioInicio.hour}:${avaliacao.horarioInicio.minute < 10 ? '0' : ''}${avaliacao.horarioInicio.minute} - 
                                            ${avaliacao.horarioFim.hour}:${avaliacao.horarioFim.minute < 10 ? '0' : ''}${avaliacao.horarioFim.minute}
                                        </c:when>
                                        <c:when test="${not empty avaliacao.horarioInicio}">
                                            ${avaliacao.horarioInicio.hour}:${avaliacao.horarioInicio.minute < 10 ? '0' : ''}${avaliacao.horarioInicio.minute}
                                        </c:when>
                                        <c:otherwise>
                                            -
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="info-item">
                                <div class="info-label">Local:</div>
                                <div class="info-value">${avaliacao.localRealizacao}</div>
                            </div>
                        </div>
                    </div>

                    <c:if test="${not empty respostas}">
                        <div class="card">
                            <h3>📊 Competências Avaliadas</h3>
                            <div class="competencias">
                                <c:forEach var="resposta" items="${respostas}">
                                    <div class="competencia">
                                        <div style="display: flex; justify-content: space-between; align-items: center;">
                                            <div>
                                                <strong>${resposta.competenciaQuestionario.nomeCompetencia}</strong>
                                                <p style="margin: 5px 0; color: #6c757d; font-size: 14px;">
                                                    ${resposta.competenciaQuestionario.descricaoPrompt}
                                                </p>
                                            </div>
                                            <div class="nota">
                                                <c:choose>
                                                    <c:when test="${resposta.naoAvaliado}">
                                                        N/A
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${resposta.respostaValorNumerico.intValue()}/9
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${not empty avaliacao.feedbackPositivo or not empty avaliacao.feedbackMelhoria or not empty avaliacao.contratoAprendizagem}">
                        <div class="card">
                            <h3>💬 Feedbacks e Contrato de Aprendizagem</h3>

                            <c:if test="${not empty avaliacao.feedbackPositivo}">
                                <div class="feedback">
                                    <h4 style="color: #28a745; margin-top: 0;">✅ Pontos Positivos:</h4>
                                    <p>${avaliacao.feedbackPositivo}</p>
                                </div>
                            </c:if>

                            <c:if test="${not empty avaliacao.feedbackMelhoria}">
                                <div class="feedback">
                                    <h4 style="color: #ffc107; margin-top: 0;">🔄 Pontos de Melhoria:</h4>
                                    <p>${avaliacao.feedbackMelhoria}</p>
                                </div>
                            </c:if>

                            <c:if test="${not empty avaliacao.contratoAprendizagem}">
                                <div class="feedback">
                                    <h4 style="color: #007bff; margin-top: 0;">📝 Contrato de Aprendizagem:</h4>
                                    <p>${avaliacao.contratoAprendizagem}</p>
                                </div>
                            </c:if>
                        </div>
                    </c:if>

                </c:when>
                <c:otherwise>
                    <div class="card" style="text-align: center;">
                        <h3>❌ Avaliação Não Encontrada</h3>
                        <p>A avaliação solicitada não foi encontrada no sistema.</p>
                        <p>Verifique se o ID está correto ou se a avaliação não foi removida.</p>
                    </div>
                </c:otherwise>
            </c:choose>

            <div class="actions">
                <a href="${pageContext.request.contextPath}/avaliacoes" class="btn btn-secondary">📋 Voltar à Lista</a>
                <c:if test="${not empty avaliacao}">
                    <a href="${pageContext.request.contextPath}/avaliacao/form?action=edit&id=${avaliacao.idAvaliacaoPreenchida}" class="btn">✏️ Editar Avaliação</a>
                </c:if>
            </div>

            <div style="margin-top: 30px; text-align: center; color: #6c757d;">
                <p>Sistema de Avaliação UNIFAE - NetBeans 21 + Tomcat 10.1.42 + JDK21</p>
            </div>
        </div>
    </body>
</html>

