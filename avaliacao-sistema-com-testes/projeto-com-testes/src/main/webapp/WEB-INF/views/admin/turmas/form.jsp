<%--
* =================================================================================
* NOME DO ARQUIVO: formularioTurma.jsp
* ---------------------------------------------------------------------------------
* DESCRIÇÃO:
* Esta página JSP (JavaServer Pages) é o componente de VISÃO (View) para a criação
* e edição de Turmas. Trata-se de um formulário de propósito duplo, que adapta
* dinamicamente seus títulos, textos e comportamento com base em um parâmetro de
* ação, servindo tanto para cadastrar uma nova turma quanto para atualizar uma
* existente.
*
* A página segue o padrão de arquitetura MVC (Model-View-Controller).
* ---------------------------------------------------------------------------------
* LIGAÇÕES COM OUTROS ARQUIVOS:
*
* - CONTROLLER (Controlador):
* Esta página é controlada por uma Servlet Java (provavelmente "TurmaServlet.java")
* mapeada para a URL "/admin/turmas". A servlet é responsável por:
* 1. Exibir o formulário: Ao receber uma requisição GET (para action=new ou action=edit),
* a servlet prepara os dados necessários (como um objeto 'turma' para edição)
* e encaminha a requisição para esta JSP.
* 2. Processar o formulário: Ao receber a submissão via POST deste formulário, a servlet
* valida os dados e interage com a camada de persistência (DAO) para salvar ou
* atualizar a turma no banco de dados.
*
* - MODEL (Modelo de Dados):
* Recebe dados da servlet através de atributos na requisição (request attributes).
* - "action": Uma String que define o modo do formulário ('new' para criar, 'edit' para editar).
* - "turma": Um objeto do tipo 'Turma' que contém os dados de uma turma
* existente. Este atributo só é fornecido quando action='edit'.
*
* - RECURSOS ESTÁTICOS:
* - Utiliza o arquivo de folha de estilos "/css/formularios.css" para
* a maior parte da estilização.
* =================================================================================
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Importa a biblioteca JSTL Core para permitir o uso de tags de controle de fluxo e lógica na página, como <c:choose> e <c:if>. --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>
            <%--
                O título da página é gerado dinamicamente. A tag <c:choose> (equivalente a um if/else)
                verifica o valor da variável "action" enviada pelo servlet. Se a ação for 'edit',
                o título reflete a edição; caso contrário, reflete a criação de um novo item.
                Isso melhora a experiência do usuário, informando o contexto da operação.
            --%>
            <c:choose>
                <c:when test="${action == 'edit'}">Editar Turma</c:when>
                <c:otherwise>Nova Turma</c:otherwise>
            </c:choose>
            - Sistema UNIFAE
        </title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <%-- A Expression Language (EL) ${pageContext.request.contextPath} gera a URL raiz da aplicação, garantindo que o caminho para o CSS seja sempre correto. --%>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">
    </head>
    <body>
        <div class="form-container">
            <div class="form-header" style="background-color: #007bff;">
                <h1>
                    <%-- O cabeçalho principal também é dinâmico, mostrando um ícone e texto apropriados para a ação de editar ou criar. --%>
                    <c:choose>
                        <c:when test="${action == 'edit'}">✏️ Editar Turma</c:when>
                        <c:otherwise>➕ Nova Turma</c:otherwise>
                    </c:choose>
                </h1>
                <p>
                    <%-- O texto de ajuda segue a mesma lógica, adaptando-se ao contexto. --%>
                    <c:choose>
                        <c:when test="${action == 'edit'}">Altere os dados da turma conforme necessário</c:when>
                        <c:otherwise>Preencha os dados para cadastrar uma nova turma</c:otherwise>
                    </c:choose>
                </p>
            </div>

            <%--
                O formulário envia seus dados usando o método POST para a servlet em "/admin/turmas".
                O método POST é o padrão para enviar dados que resultarão em uma criação ou atualização de um recurso no servidor.
            --%>
            <form method="post" action="${pageContext.request.contextPath}/admin/turmas">
                <%--
                    Este bloco é FUNDAMENTAL para a funcionalidade de EDIÇÃO.
                    O campo oculto (<input type="hidden">) é renderizado somente se a ação for 'edit'.
                    Ele envia o ID da turma de volta para a servlet, que o usará para saber
                    qual registro específico deve ser atualizado no banco de dados.
                --%>
                <c:if test="${action == 'edit'}">
                    <input type="hidden" name="idTurma" value="${turma.idTurma}">
                </c:if>

                <div class="form-section">
                    <h3>📋 Dados da Turma</h3>

                    <div class="form-row">
                        <div class="form-col">
                            <div class="form-group">
                                <label for="nomeTurma" class="form-label required">Nome da Turma</label>
                                <%--
                                    O atributo 'value' é preenchido com a EL ${turma.nomeTurma}.
                                    - No modo de edição, isso preenche o campo com o nome atual da turma.
                                    - No modo de criação, o objeto 'turma' é nulo, então a EL resulta em um
                                      valor vazio, deixando o campo em branco para o usuário preencher.
                                --%>
                                <input type="text" id="nomeTurma" name="nomeTurma" class="form-input" value="${turma.nomeTurma}" placeholder="Ex: 3º Período - Turma A" required maxlength="255">
                            </div>
                        </div>
                        <div class="form-col">
                            <div class="form-group">
                                <label for="codigoTurma" class="form-label">Código da Turma</label>
                                <input type="text" id="codigoTurma" name="codigoTurma" class="form-input" value="${turma.codigoTurma}" placeholder="Ex: MED3A2025" maxlength="50">
                                <div class="form-help">Código único para identificação (opcional)</div>
                            </div>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-col">
                            <div class="form-group">
                                <label for="anoLetivo" class="form-label required">Ano Letivo</label>
                                <%-- O uso de type="number" e os atributos min/max fornecem validação no lado do cliente (navegador). --%>
                                <input type="number" id="anoLetivo" name="anoLetivo" class="form-input" value="${turma.anoLetivo}" required min="2000" max="2100" placeholder="Ex: 2025">
                            </div>
                        </div>
                        <div class="form-col-small">
                            <div class="form-group">
                                <label for="semestre" class="form-label required">Semestre</label>
                                <input type="number" id="semestre" name="semestre" class="form-input" value="${turma.semestre}" required min="1" max="2">
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-section">
                    <h3>⚙️ Status</h3>
                    <div class="form-group">
                        <div class="form-checkbox-group">
                            <%--
                                Lógica para marcar o checkbox 'Ativo'. A tag <c:if> adiciona o atributo 'checked' se a condição for verdadeira.
                                A condição "${turma.ativo or turma.idTurma == null}" significa:
                                1. "turma.ativo": Se estiver editando uma turma que já está ativa, o campo virá marcado.
                                2. "turma.idTurma == null": Se o ID da turma for nulo, significa que é um novo cadastro.
                                   Neste caso, o campo também virá marcado por padrão.
                                Essa lógica garante que novas turmas sejam ativas por padrão e que o status de turmas existentes seja preservado.
                            --%>
                            <input type="checkbox" id="ativo" name="ativo" class="form-checkbox" <c:if test="${turma.ativo or turma.idTurma == null}">checked</c:if>>
                                <label for="ativo" class="form-label">Turma Ativa</label>
                            </div>
                            <div class="form-help">Turmas inativas não aparecerão como opção em novos lançamentos.</div>
                        </div>
                    </div>

                    <div class="form-actions">
                    <%-- O botão "Cancelar" é um link que leva o usuário de volta para a página principal de gerenciamento de turmas. --%>
                    <a href="${pageContext.request.contextPath}/admin/turmas" class="btn btn-secondary">
                        ❌ Cancelar
                    </a>
                    <button type="submit" class="btn btn-primary" style="background-color: #007bff;">
                        <%-- O texto do botão de submissão também é dinâmico, melhorando a clareza para o usuário sobre a ação que será executada. --%>
                        <c:choose>
                            <c:when test="${action == 'edit'}">💾 Salvar Alterações</c:when>
                            <c:otherwise>➕ Criar Turma</c:otherwise>
                        </c:choose>
                    </button>
                </div>
            </form>
        </div>
    </body>
</html>