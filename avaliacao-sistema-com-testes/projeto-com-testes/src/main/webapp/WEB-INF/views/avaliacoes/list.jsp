<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Lista de Avaliações - Sistema UNIFAE</title>
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
                max-width: 1200px;
                margin: 0 auto;
            }
            .actions {
                margin: 20px 0;
                text-align: center;
                display: flex;
                justify-content: center;
                align-items: center;
                gap: 20px;
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
            .btn-success {
                background-color: #28a745;
            }
            .btn-success:hover {
                background-color: #1e7e34;
            }
            .btn-warning {
                background-color: #ffc107;
                color: #212529;
            }
            .btn-warning:hover {
                background-color: #e0a800;
            }
            .btn-danger {
                background-color: #dc3545;
            }
            .btn-danger:hover {
                background-color: #c82333;
            }
            .table {
                width: 100%;
                border-collapse: collapse;
                background: white;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }
            .table th, .table td {
                padding: 12px;
                text-align: left;
                border-bottom: 1px solid #ddd;
            }
            .table th {
                background-color: #f8f9fa;
                font-weight: bold;
            }
            .table tr:hover {
                background-color: #f5f5f5;
            }
            .status {
                padding: 4px 8px;
                border-radius: 4px;
                font-size: 12px;
                font-weight: bold;
            }
            .status.realizada {
                background-color: #d4edda;
                color: #155724;
            }
            .status.pendente {
                background-color: #fff3cd;
                color: #856404;
            }
            .empty-state {
                text-align: center;
                padding: 40px;
                background: white;
                border-radius: 8px;
                margin: 20px 0;
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

            /* === INÍCIO DO CÓDIGO CSS ADICIONADO PARA O MENU DROPDOWN === */
            .dropdown {
                position: relative;
                display: inline-block;
            }
            .dropdown .btn-menu {
                background-color: #28a745;
                color: white;
                padding: 12px 24px;
                text-decoration: none;
                border-radius: 5px;
                border: none;
                cursor: pointer;
                font-family: Arial, sans-serif;
                font-size: 14px;
                transition: background-color 0.3s;
            }
            .dropdown:hover .btn-menu {
                background-color: #218838;
            }
            .dropdown-content {
                display: none;
                position: absolute;
                background-color: #f1f1f1;
                min-width: 260px;
                box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
                z-index: 1;
                border-radius: 5px;
                overflow: hidden;
            }
            .dropdown-content a {
                color: black;
                padding: 12px 16px;
                text-decoration: none;
                display: block;
                background-color: #f1f1f1;
                text-align: left;
                border-radius: 0;
            }
            .dropdown-content a:hover {
                background-color: #ddd;
            }
            .dropdown:hover .dropdown-content {
                display: block;
            }
            /* === FIM DO CÓDIGO CSS ADICIONADO PARA O MENU DROPDOWN === */
        </style>
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h1>📋 Lista de Avaliações</h1>
                <p>Sistema de Avaliação UNIFAE</p>
            </div>

            <div class="breadcrumb">
                <a href="${pageContext.request.contextPath}/">🏠 Início</a> &gt;
                <span>Lista de Avaliações</span>
            </div>

            <c:if test="${param.deleted == 'true'}">
                <div style="background-color: #d4edda; color: #155724; padding: 10px; border-radius: 5px; margin: 10px 0; border: 1px solid #c3e6cb;">
                    ✅ <strong>Sucesso!</strong> Avaliação excluída com sucesso.
                </div>
            </c:if>

            <c:if test="${param.error != null}">
                <div style="background-color: #f8d7da; color: #721c24; padding: 10px; border-radius: 5px; margin: 10px 0; border: 1px solid #f5c6cb;">
                    ❌ <strong>Erro!</strong> ${param.error}
                </div>
            </c:if>


            <div class="actions">
                <a href="${pageContext.request.contextPath}/" class="btn">🏠 Voltar ao Início</a>

                <div class="dropdown">
                    <button class="btn-menu">📝 Nova Avaliação ▼</button>
                    <div class="dropdown-content">

                        <a href="avaliacao/form?action=new&questionarioId=1">📝 Nova Mini CEX</a>
                        <a href="avaliacao/form?action=new&questionarioId=2">🎯 Avaliação 360 - Professor</a>
                        <a href="avaliacao/form?action=new&questionarioId=3">👥 Avaliação 360 - Pares</a>
                        <a href="avaliacao/form?action=new&questionarioId=4">⚕️ Avaliação 360 - Equipe</a>

                        <a href="avaliacao/form?action=new&questionarioId=5">🩺 Avaliação 360 - Paciente</a>
                    </div>
                </div>
            </div>
            <c:choose>
                <c:when test="${not empty avaliacoes}">
                    <table class="table">

                        <thead>
                            <tr>

                                <th>ID</th>
                                <th>Tipo de Avaliação</th>

                                <th>Aluno Avaliado</th>
                                <th>Avaliador</th>

                                <th>Data</th>

                                <th>Status</th>
                                <th>Ações</th>

                            </tr>
                        </thead>
                        <tbody>

                            <c:forEach var="avaliacao" items="${avaliacoes}">

                                <tr>
                                    <td>${avaliacao.idAvaliacaoPreenchida}</td>

                                    <td>

                                        <c:choose>
                                            <c:when test="${avaliacao.questionario.nomeModelo == 'Mini CEX'}">

                                                📝 ${avaliacao.questionario.nomeModelo}

                                            </c:when>

                                            <c:when test="${avaliacao.questionario.nomeModelo.contains('Professor')}">

                                                🎯 ${avaliacao.questionario.nomeModelo}

                                            </c:when>
                                            <c:when test="${avaliacao.questionario.nomeModelo.contains('Pares')}">

                                                👥 ${avaliacao.questionario.nomeModelo}

                                            </c:when>
                                            <c:when test="${avaliacao.questionario.nomeModelo.contains('Equipe')}">

                                                ⚕️ ${avaliacao.questionario.nomeModelo}
                                            </c:when>
                                            <c:when test="${avaliacao.questionario.nomeModelo.contains('Paciente')}">

                                                🩺 ${avaliacao.questionario.nomeModelo}
                                            </c:when>


                                            <c:otherwise>
                                                📋 ${avaliacao.questionario.nomeModelo}


                                            </c:otherwise>
                                        </c:choose>
                                    </td>


                                    <td>${avaliacao.alunoAvaliado.nomeCompleto}</td>
                                    <td>
                                        <c:choose>


                                            <c:when test="${not empty avaliacao.avaliador}">
                                                ${avaliacao.avaliador.nomeCompleto}


                                            </c:when>
                                            <c:when test="${not empty avaliacao.nomeAvaliadorNaoUsuario}">
                                                ${avaliacao.nomeAvaliadorNaoUsuario} (${avaliacao.tipoAvaliadorNaoUsuario})


                                            </c:when>
                                            <c:otherwise>


                                                ${avaliacao.tipoAvaliadorNaoUsuario}
                                            </c:otherwise>
                                        </c:choose>


                                    </td>
                                    <td>
                                        <c:choose>


                                            <c:when test="${not empty avaliaco.dataRealizacao}">
                                                ${avaliacao.dataRealizacao.dayOfMonth}/${avaliacao.dataRealizacao.monthValue}/${avaliacao.dataRealizacao.year}


                                            </c:when>
                                            <c:otherwise>


                                                -
                                            </c:otherwise>
                                        </c:choose>


                                    </td>
                                    <td>
                                        <c:choose>


                                            <c:when test="${not empty avaliacao.respostasItens}">
                                                <span class="status realizada">✅ Realizada</span>


                                            </c:when>
                                            <c:otherwise>
                                                <span class="status pendente">⏳ Pendente</span>


                                            </c:otherwise>
                                        </c:choose>


                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/avaliacao/view?id=${avaliacao.idAvaliacaoPreenchida}" 


                                           class="btn btn-warning" style="padding: 5px 10px;
                                           font-size: 12px;">👁️ Ver</a>
                                        <a href="${pageContext.request.contextPath}/avaliacao/form?action=edit&id=${avaliacao.idAvaliacaoPreenchida}" 
                                           class="btn" style="padding: 5px 10px;
                                           font-size: 12px;">✏️ Editar</a>
                                        <a href="${pageContext.request.contextPath}/avaliacao/delete?id=${avaliacao.idAvaliacaoPreenchida}" 
                                           class="btn btn-danger" style="padding: 5px 10px;
                                           font-size: 12px;"
                                           onclick="return confirm('Tem certeza que deseja excluir esta avaliação?')">🗑️ Excluir</a>
                                    </td>


                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>


                    <div class="empty-state">
                        <h3>📋 Nenhuma Avaliação Encontrada</h3>
                        <p>Ainda não há avaliações cadastradas no sistema.</p>
                        <p>Clique no botão "Nova Avaliação" para criar a primeira.</p>

                    </div>
                </c:otherwise>
            </c:choose>

            <div style="margin-top: 30px;
                 text-align: center; color: #6c757d;">
                <p>Sistema de Avaliação UNIFAE - NetBeans 21 + Tomcat 10.1.42 + JDK21</p>
                <p>Total de avaliações: <strong>${not empty avaliacoes ? avaliacoes.size() : 0}</strong></p>
            </div>
        </div>
    </body>
</html>