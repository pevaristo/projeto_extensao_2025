<%--
* =================================================================================
* NOME DO ARQUIVO: formularioQuestionario.jsp
* ---------------------------------------------------------------------------------
* DESCRIÇÃO:
* Esta página JSP (JavaServer Pages) é o componente de VISÃO (View) para a criação
* e edição de modelos de questionários. É um formulário de propósito duplo, que se
* adapta dinamicamente para servir tanto para o cadastro de um novo questionário
* quanto para a atualização de um já existente, com base em um parâmetro de ação.
*
* A página segue o padrão de arquitetura MVC (Model-View-Controller).
* ---------------------------------------------------------------------------------
* LIGAÇÕES COM OUTROS ARQUIVOS:
*
* - CONTROLLER (Controlador):
* Esta página é controlada por uma Servlet Java (provavelmente "QuestionarioServlet.java")
* mapeada para a URL "/admin/questionarios". A servlet é responsável por:
* 1. Exibir o formulário: Ao receber uma requisição GET (para action=new ou action=edit),
* a servlet prepara os dados e encaminha a requisição para esta JSP.
* 2. Processar o formulário: Ao receber a submissão via POST deste formulário, a servlet
* valida os dados e interage com a camada de persistência (DAO) para salvar ou
* atualizar o questionário no banco de dados.
*
* - MODEL (Modelo de Dados):
* Recebe dados da servlet através de atributos na requisição (request attributes).
* - "action": Uma String que define o modo do formulário ('new' para criar, 'edit' para editar).
* - "questionario": Um objeto do tipo 'Questionario' que contém os dados de um
* questionário existente. Este atributo só é fornecido quando action='edit'.
*
* - OUTRAS VIEWS (Outras Visões):
* - O usuário chega a esta página a partir da tela de listagem de questionários e
* retorna a ela ao clicar em "Cancelar" ou após salvar os dados com sucesso.
*
* - RECURSOS ESTÁTICOS:
* - Utiliza o arquivo de folha de estilos "/css/formularios.css" para
* estilização geral, além de conter um bloco de CSS específico para este formulário.
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
            --%>
            <c:choose>
                <c:when test="${action == 'edit'}">Editar Questionário</c:when>
                <c:otherwise>Novo Questionário</c:otherwise>
            </c:choose>
            - Sistema UNIFAE
        </title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <%-- A Expression Language (EL) ${pageContext.request.contextPath} gera a URL base da aplicação, garantindo que o caminho para o CSS seja sempre correto. --%>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">

        <%-- Estilos CSS específicos para este formulário, garantindo um layout customizado e consistente sem afetar outras páginas do sistema. --%>
        <style>
            .form-container {
                max-width: 900px;
                margin: 0 auto;
                background-color: white;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }
            .form-header {
                background-color: #fd7e14;
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
                border-color: #fd7e14;
                box-shadow: 0 0 0 2px rgba(253,126,20,0.25);
            }
            .form-textarea {
                min-height: 100px;
                resize: vertical;
            }
            .form-help {
                font-size: 12px;
                color: #6c757d;
                margin-top: 5px;
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
                background-color: #fd7e14;
                color: white;
            }
            .btn-primary:hover {
                background-color: #e86a00;
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
        </style>
    </head>
    <body>
        <div class="form-container">
            <div class="form-header">
                <h1>
                    <%-- O cabeçalho principal também é dinâmico, mostrando um ícone e texto apropriados para a ação de editar ou criar. --%>
                    <c:choose>
                        <c:when test="${action == 'edit'}">✏️ Editar Questionário</c:when>
                        <c:otherwise>➕ Novo Questionário</c:otherwise>
                    </c:choose>
                </h1>
                <p>
                    <%-- O texto de ajuda segue a mesma lógica, adaptando-se ao contexto. --%>
                    <c:choose>
                        <c:when test="${action == 'edit'}">Altere os dados do modelo de avaliação</c:when>
                        <c:otherwise>Preencha os dados para criar um novo modelo de avaliação</c:otherwise>
                    </c:choose>
                </p>
            </div>

            <%--
                O formulário envia seus dados usando o método POST para a servlet em "/admin/questionarios".
                O método POST é o padrão para enviar dados que resultarão em uma criação ou atualização de um recurso no servidor.
            --%>
            <form method="post" action="${pageContext.request.contextPath}/admin/questionarios">
                <%--
                    Este bloco é FUNDAMENTAL para a funcionalidade de EDIÇÃO.
                    O campo oculto (<input type="hidden">) é renderizado somente se a ação for 'edit'.
                    Ele envia o ID do questionário de volta para a servlet, que o usará para saber
                    qual registro específico deve ser atualizado no banco de dados.
                --%>
                <c:if test="${action == 'edit'}">
                    <input type="hidden" name="idQuestionario" value="${questionario.idQuestionario}">
                </c:if>

                <div class="form-section">
                    <h3>📋 Informações do Modelo</h3>

                    <div class="form-group">
                        <label for="nomeModelo" class="form-label required">Nome do Modelo</label>
                        <%--
                            O atributo 'value' é preenchido com a EL ${questionario.nomeModelo}.
                            - No modo de edição, isso preenche o campo com o nome atual do questionário.
                            - No modo de criação, o objeto 'questionario' é nulo, então a EL resulta em um
                              valor vazio, deixando o campo em branco para o usuário preencher.
                        --%>
                        <input type="text" id="nomeModelo" name="nomeModelo" class="form-input" value="${questionario.nomeModelo}" placeholder="Ex: Mini Clinical Evaluation Exercise (Mini-CEX)" required maxlength="255">
                        <div class="form-help">Nome único e descritivo do modelo de avaliação.</div>
                    </div>

                    <div class="form-group">
                        <label for="descricao" class="form-label">Descrição</label>
                        <%--
                            Para a tag <textarea>, o valor inicial é colocado entre as tags de abertura e fechamento.
                            A EL ${questionario.descricao} funciona da mesma forma que no campo de texto anterior.
                        --%>
                        <textarea id="descricao" name="descricao" class="form-input form-textarea" placeholder="Descreva o objetivo, metodologia e aplicação deste questionário...">${questionario.descricao}</textarea>
                        <div class="form-help">Descrição detalhada sobre o propósito do questionário (opcional).</div>
                    </div>
                </div>

                <div class="form-actions">
                    <%-- O botão "Cancelar" é um link que leva o usuário de volta para a página principal de gerenciamento de questionários. --%>
                    <a href="${pageContext.request.contextPath}/admin/questionarios" class="btn btn-secondary">
                        ❌ Cancelar
                    </a>
                    <button type="submit" class="btn btn-primary">
                        <%-- O texto do botão de submissão também é dinâmico, melhorando a clareza para o usuário sobre a ação que será executada. --%>
                        <c:choose>
                            <c:when test="${action == 'edit'}">💾 Salvar Alterações</c:when>
                            <c:otherwise>➕ Criar Questionário</c:otherwise>
                        </c:choose>
                    </button>
                </div>
            </form>
        </div>
    </body>
</html>