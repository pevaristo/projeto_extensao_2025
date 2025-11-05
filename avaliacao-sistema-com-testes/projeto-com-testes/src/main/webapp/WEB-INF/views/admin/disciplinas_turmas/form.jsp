<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Vincular Disciplina à Turma - Sistema UNIFAE</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">
    </head>
    <body>
        <div class="form-container">
            <div class="form-header" style="background-color: #17a2b8;">
                <h1>➕ Vincular Disciplina à Turma</h1>
                <p>Selecione a disciplina e o professor responsável para a turma <strong>${turma.nomeTurma}</strong></p>
            </div>

            <form method="post" action="${pageContext.request.contextPath}/admin/gerenciar-disciplinas-turma">
                <input type="hidden" name="idTurma" value="${turma.idTurma}">

                <div class="form-section">
                    <h3>🔗 Dados do Vínculo</h3>
                    <div class="form-row">
                        <div class="form-col">
                            <div class="form-group">
                                <label for="idDisciplina" class="form-label required">Disciplina</label>
                                <select id="idDisciplina" name="idDisciplina" class="form-input" required>
                                    <option value="">-- Selecione uma disciplina --</option>
                                    <c:forEach var="disciplina" items="${disciplinasDisponiveis}">
                                        <option value="${disciplina.idDisciplina}">${disciplina.nomeDisciplina}</option>
                                    </c:forEach>
                                </select>
                                <c:if test="${empty disciplinasDisponiveis}">
                                    <div class="form-help" style="color: #dc3545;">Não há mais disciplinas disponíveis para vincular a esta turma.</div>
                                </c:if>
                            </div>
                        </div>
                        <div class="form-col">
                            <div class="form-group">
                                <label for="idProfessor" class="form-label required">Professor Responsável</label>
                                <select id="idProfessor" name="idProfessor" class="form-input" required>
                                    <option value="">-- Selecione um professor --</option>
                                    <c:forEach var="prof" items="${professores}">
                                        <option value="${prof.idUsuario}">${prof.nomeCompleto}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/admin/gerenciar-disciplinas-turma?idTurma=${turma.idTurma}" class="btn btn-secondary">
                        ❌ Cancelar
                    </a>
                    <button type="submit" class="btn btn-primary" style="background-color: #17a2b8;" <c:if test="${empty disciplinasDisponiveis}">disabled</c:if>>
                        💾 Salvar Vínculo
                    </button>
                </div>
            </form>
        </div>
    </body>
</html>
