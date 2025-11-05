<%--
* =================================================================================
* NOME DO ARQUIVO: formularioUsuario.jsp
* ---------------------------------------------------------------------------------
* DESCRIÇÃO:
* Esta página JSP (JavaServer Pages) é o componente de VISÃO (View) para a criação
* e edição de Usuários do sistema. É um formulário de propósito duplo, que adapta
* seus títulos, textos e comportamento (especialmente para o campo de senha) com
* base em um parâmetro de ação, servindo tanto para cadastrar um novo usuário
* quanto para atualizar um existente.
*
* A página segue o padrão de arquitetura MVC (Model-View-Controller). 
* ---------------------------------------------------------------------------------
* LIGAÇÕES COM OUTROS ARQUIVOS:
*
* - CONTROLLER (Controlador):
* Esta página é controlada por uma Servlet Java (provavelmente "UsuarioServlet.java")
* mapeada para a URL "/admin/usuarios". A servlet é responsável por:
* 1. Exibir o formulário: Ao receber uma requisição GET (para action=new ou action=edit),
* a servlet prepara os dados necessários (como o objeto 'usuario' para edição e
* as listas de tipos de usuário e permissões) e encaminha para esta JSP.
* 2. Processar o formulário: Ao receber a submissão via POST deste formulário, a servlet
* valida os dados e interage com a camada de persistência (DAO) para salvar ou
* atualizar o usuário no banco de dados.
*
* - MODEL (Modelo de Dados):
* Recebe dados da servlet através de atributos na requisição (request attributes). 
* - "action": Uma String que define o modo do formulário ('new' para criar, 'edit' para editar). 
* - "usuario": Um objeto do tipo 'Usuario' que contém os dados de um usuário
* existente. Este atributo só é fornecido quando action='edit'.
* - "tiposUsuario": Uma coleção com os tipos de usuário disponíveis (ex: ALUNO, PROFESSOR)
* para popular o campo de seleção. 
* - "listPermissoes": Uma lista de objetos 'Permissao' para popular o campo de seleção de permissões. 
*
* - RECURSOS ESTÁTICOS:
* - Utiliza o arquivo de folha de estilos "/css/formularios.css" e contém um
* bloco de CSS específico para este formulário.
* =================================================================================
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
                <c:when test="${action == 'edit'}">Editar Usuário</c:when>
                <c:otherwise>Novo Usuário</c:otherwise>
            </c:choose>
            - Sistema UNIFAE
        </title>

        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <%-- A Expression Language (EL) ${pageContext.request.contextPath} gera a URL raiz da aplicação, garantindo que o caminho para o CSS seja sempre correto. --%>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">

        <style>
            /* Estilos específicos para formulário de usuários */
            .form-container {
                max-width: 900px;
                margin: 0 auto;
                background-color: white;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }

            .form-header {
                background-color: #28a745;
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
                border-color: #28a745;
                box-shadow: 0 0 0 2px rgba(40,167,69,0.25);
            }

            .form-row {
                display: flex;
                gap: 20px;
            }

            .form-col {
                flex: 1;
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
                background-color: #28a745;
                color: white;
            }

            .btn-primary:hover {
                background-color: #218838;
            }

            .btn-secondary {
                background-color: #6c757d;
                color: white;
            }

            .btn-secondary:hover {
                background-color: #545b62;
            }

            .container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 20px;
            }

            /* Mantém compatibilidade com elementos originais */
            label {
                display: block;
                margin-bottom: 5px;
                font-weight: 600;
                color: #333;
            }

            input, select {
                width: 100%;
                padding: 10px 12px;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 14px;
                transition: border-color 0.3s;
            }

            input:focus, select:focus {
                outline: none;
                border-color: #28a745;
                box-shadow: 0 0 0 2px rgba(40,167,69,0.25);
            }

            input[type="checkbox"] {
                width: auto;
                margin-right: 8px;
            }

            @media (max-width: 768px) {
                .form-container {
                    margin: 10px;
                    padding: 20px;
                }

                .form-row {
                    flex-direction: column;
                    gap: 0;
                }

                .form-actions {
                    flex-direction: column;
                }
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="form-container">
                <div class="form-header">
                    <h1>
                        <c:choose>
                            <c:when test="${action == 'edit'}">✏️ Editar Usuário</c:when>
                            <c:otherwise>➕ Novo Usuário</c:otherwise>
                        </c:choose> 
                    </h1> 
                    <p>
                        <c:choose>
                            <c:when test="${action == 'edit'}">Altere os dados do usuário conforme necessário</c:when>
                            <c:otherwise>Preencha os dados para cadastrar um novo usuário</c:otherwise> 
                        </c:choose>
                    </p>
                </div>

                <form action="${pageContext.request.contextPath}/admin/usuarios" method="post"> 

                    <%-- Bloco ADICIONADO para exibir mensagens de erro retornadas pelo Servlet --%>
                    <c:if test="${not empty error}">
                        <div class="error-message" style="background-color: #f8d7da; border: 1px solid #f5c6cb; color: #721c24; padding: 10px; border-radius: 4px; margin-bottom: 20px;">
                            <strong>Erro:</strong> ${error}
                        </div>
                    </c:if>

                    <%--
                        Este campo oculto é crucial para a funcionalidade de edição. Ele é renderizado apenas se um objeto 'usuario' existir (modo de edição). Sua função é enviar o ID do usuário de volta para a servlet, para que
                        ela saiba qual registro específico deve ser atualizado no banco de dados.
                    --%>
                    <c:if test="${usuario != null}">
                        <input type="hidden" name="idUsuario" value="${usuario.idUsuario}" />
                    </c:if>

                    <div class="form-section">
                        <h3>👤 Informações Pessoais</h3>

                        <div class="form-group">
                            <label for="nomeCompleto">Nome Completo:</label>
                            <%-- O atributo 'value' é preenchido com a EL ${usuario.nomeCompleto}, populando o campo no modo de edição. --%>
                            <input type="text" id="nomeCompleto" name="nomeCompleto" value="${usuario.nomeCompleto}" required>
                        </div>

                        <div class="form-row">
                            <div class="form-col">
                                <div class="form-group">
                                    <label for="email">Email:</label>
                                    <input type="email" id="email" name="email" value="${usuario.email}" required>
                                </div>
                            </div>
                            <div class="form-col">
                                <div class="form-group">
                                    <label for="telefone">Telefone:</label>
                                    <%-- CORREÇÃO: Adicionados maxlength e placeholder para guiar o usuário --%>
                                    <input type="text" id="telefone" name="telefone" value="${usuario.telefone}" 
                                           maxlength="11" 
                                           placeholder="Apenas números (Ex: 19912345678)">
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="senhaHash">Senha:</label>
                            <%--
                                    Este campo de senha possui lógica condicional complexa:
                                1. 'placeholder': Se for uma edição, exibe uma mensagem instruindo o usuário a
                                    deixar o campo em branco para não alterar a senha. 
 2. 'required': O campo é obrigatório ('required') apenas na criação de um novo
                                   usuário. Na edição, ele é opcional. 
                                Isso representa uma regra de negócio importante implementada na view.
                            --%>
                            <input type="password" id="senhaHash" name="senhaHash" placeholder="${action == 'edit' ? 'Deixe em branco para não alterar' : ''}" ${action == 'new' ? 'required' : ''}>
                        </div>
                    </div>

                    <div class="form-section">
                        <h3>🔐 Configurações de Acesso</h3>

                        <div class="form-row">
                            <div class="form-col">
                                <div class="form-group">
                                    <label for="tipoUsuario">Tipo de Usuário:</label>
                                    <select id="tipoUsuario" name="tipoUsuario" required>
                                        <%--
                                            O laço <c:forEach> itera sobre a coleção 'tiposUsuario' (enviada pelo servlet)
                                            para criar dinamicamente as opções do dropdown.
                                        --%>
                                        <c:forEach var="tipo" items="${tiposUsuario}">
                                            <%--
                                                A EL com operador ternário verifica se o tipo do usuário atual no laço
                                                corresponde ao tipo do usuário que está sendo editado. Se sim,
                                                adiciona o atributo 'selected' para pré-selecionar a opção correta.
                                            --%>
                                            <option value="${tipo}" ${usuario.tipoUsuario == tipo ? 'selected' : ''}>${tipo}</option> 
                                        </c:forEach>
                                    </select>
                                </div> 
                            </div>
                            <div class="form-col">
                                <div class="form-group"> 
                                    <label for="permissaoId">Permissão:</label>
                                    <select id="permissaoId" name="permissaoId" required>
                                        <option value="">Selecione uma permissão...</option>
                                        <c:forEach var="permissao" items="${listPermissoes}">
                                            <%-- Lógica similar ao campo anterior para pré-selecionar a permissão correta no modo de edição. --%>
                                            <option value="${permissao.idPermissao}" ${usuario.permissao.idPermissao == permissao.idPermissao ? 'selected' : ''}>${permissao.nomePermissao}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="form-section">
                        <h3>🎓 Informações Acadêmicas</h3>

                        <div class="form-row">
                            <div class="form-col">
                                <div class="form-group">
                                    <label for="matriculaRA">Matrícula/RA:</label>
                                    <%-- CORREÇÃO: Adicionado maxlength para limitar a entrada --%>
                                    <input type="text" id="matriculaRA" name="matriculaRA" value="${usuario.matriculaRA}" maxlength="6">
                                </div>
                            </div>
                            <div class="form-col">
                                <div class="form-group">
                                    <label for="periodoAtualAluno">Período (se aluno):</label>
                                    <input type="text" id="periodoAtualAluno" name="periodoAtualAluno" value="${usuario.periodoAtualAluno}">
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="ativo">
                                <%--
                                    Lógica para marcar o checkbox 'Ativo'. A EL adiciona o atributo 'checked' se a condição for verdadeira.  A condição "${usuario.ativo or action == 'new'}" significa:
                                    1. "usuario.ativo": Se estiver editando um usuário que já está ativo, o campo virá marcado. 
                                    2. "action == 'new'": Se for um novo cadastro, o campo também virá marcado por padrão. 
                                    Isso garante que novos usuários sejam ativos por padrão e que o status de usuários existentes seja preservado.
                                --%>
                                <input type="checkbox" id="ativo" name="ativo" ${usuario.ativo or action == 'new' ? 'checked' : ''}> 
                                Ativo
                            </label>
                        </div>
                    </div>

                    <div class="form-actions">
                        <a href="${pageContext.request.contextPath}/admin/usuarios" class="btn btn-secondary">❌ Cancelar</a>
                        <button type="submit" class="btn btn-primary">💾 Salvar</button>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>