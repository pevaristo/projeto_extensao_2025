<%--
* =================================================================================
* NOME DO ARQUIVO: formularioPermissao.jsp
* ---------------------------------------------------------------------------------
* DESCRIÇÃO:
* Esta página JSP (JavaServer Pages) funciona como a VISÃO (View) para a criação
* e edição de permissões do sistema. É um formulário de propósito duplo, que adapta
* seus títulos, textos e comportamento com base em uma variável de ação, servindo
* tanto para cadastrar uma nova permissão quanto para atualizar uma existente.
*
* A página segue o padrão de arquitetura MVC (Model-View-Controller).
* ---------------------------------------------------------------------------------
* LIGAÇÕES COM OUTROS ARQUIVOS:
*
* - CONTROLLER (Controlador):
* Esta página é controlada por uma Servlet Java (provavelmente "PermissaoServlet.java")
* mapeada para a URL "/admin/permissoes". A servlet é responsável por:
* 1. Exibir o formulário: Ao receber uma requisição GET (ex: para action=new ou action=edit),
* a servlet prepara os dados necessários e encaminha para esta JSP.
* 2. Processar o formulário: Ao receber a submissão via POST deste formulário, a servlet
* valida os dados e interage com a camada de persistência (DAO) para salvar ou
* atualizar a permissão no banco de dados.
*
* - MODEL (Modelo de Dados):
* Recebe dados da servlet através de atributos na requisição (request attributes).
* - "action": Uma String que define o modo do formulário ('new' para criar, 'edit' para editar).
* - "permissao": Um objeto do tipo 'Permissao' que contém os dados de uma permissão
* existente. Este atributo só é enviado quando action='edit'.
*
* - RECURSOS ESTÁTICOS:
* - Utiliza o arquivo de folha de estilos "/css/formularios.css" para
* estilização geral e possui um bloco <style> com CSS específico para este layout.
* =================================================================================
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Importa a biblioteca JSTL Core para permitir o uso de tags de controle de fluxo como <c:choose> e <c:if>. --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>
            <%--
                Título da página dinâmico. A tag <c:choose> funciona como um if/else.
                Se a variável "action" (recebida do servlet) for 'edit', o título será
                "Editar Permissão". Caso contrário, será "Nova Permissão".
                Isso melhora a experiência do usuário, deixando claro o contexto da operação.
            --%>
            <c:choose>
                <c:when test="${action == 'edit'}">Editar Permissão</c:when>
                <c:otherwise>Nova Permissão</c:otherwise>
            </c:choose>
            - Sistema UNIFAE
        </title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <%-- A Expression Language (EL) ${pageContext.request.contextPath} gera a URL raiz da aplicação, garantindo que o caminho para o CSS seja sempre correto. --%>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">

        <%-- Estilos CSS específicos para este formulário, garantindo um visual consistente e bem estruturado sem afetar outras páginas. --%>
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
                background-color: #6f42c1;
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
            .form-section, .help-section {
                margin-bottom: 30px;
                padding: 20px;
                border: 1px solid #e9ecef;
                border-radius: 6px;
                background-color: #f8f9fa;
            }
            .form-section h3, .help-section h3 {
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
                border-color: #6f42c1;
                box-shadow: 0 0 0 2px rgba(111,66,193,0.25);
            }
            .form-textarea {
                min-height: 80px;
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
                background-color: #6f42c1;
                color: white;
            }
            .btn-primary:hover {
                background-color: #5a2d91;
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
                    <%-- O cabeçalho principal também é dinâmico, mostrando um ícone e texto apropriados para a ação. --%>
                    <c:choose>
                        <c:when test="${action == 'edit'}">✏️ Editar Permissão</c:when>
                        <c:otherwise>➕ Nova Permissão</c:otherwise>
                    </c:choose>
                </h1>
                <p>
                    <%-- O texto de ajuda também muda de acordo com o contexto de edição ou criação. --%>
                    <c:choose>
                        <c:when test="${action == 'edit'}">Altere os dados da permissão conforme necessário</c:when>
                        <c:otherwise>Preencha os dados para cadastrar uma nova permissão</c:otherwise>
                    </c:choose>
                </p>
            </div>

            <%--
                O formulário envia seus dados usando o método POST para a servlet em "/admin/permissoes".
                O método POST é o correto para enviar dados que alteram o estado do sistema (criar/atualizar).
            --%>
            <form method="post" action="${pageContext.request.contextPath}/admin/permissoes" id="permissaoForm">
                <%--
                    Bloco CRÍTICO para a funcionalidade de EDIÇÃO.
                    Este campo oculto (<input type="hidden">) é renderizado APENAS no modo de edição.
                    Sua função é enviar o ID da permissão que está sendo editada de volta para a servlet.
                    Sem ele, a servlet não saberia qual registro atualizar no banco de dados.
                --%>
                <c:if test="${action == 'edit'}">
                    <input type="hidden" name="idPermissao" value="${permissao.idPermissao}">
                </c:if>

                <div class="form-section">
                    <h3>🔐 Informações da Permissão</h3>

                    <div class="form-group">
                        <label for="nomePermissao" class="form-label required">Nome da Permissão</label>
                        <%--
                            O atributo 'value' é preenchido com a EL ${permissao.nomePermissao}.
                            - Em modo 'edit', isso preenche o campo com o nome atual da permissão.
                            - Em modo 'new', o objeto 'permissao' é nulo, então a EL resulta em um valor vazio,
                              deixando o campo em branco como esperado.
                        --%>
                        <input type="text" id="nomePermissao" name="nomePermissao" class="form-input" value="${permissao.nomePermissao}" placeholder="Ex: GERENCIAR_USUARIOS" required maxlength="100">
                        <div class="form-help">Nome único (use MAIÚSCULAS e underscore). Ex: VISUALIZAR_RELATORIOS</div>
                    </div>

                    <div class="form-group">
                        <label for="descricaoPermissao" class="form-label">Descrição</label>
                        <%--
                            Para a tag <textarea>, o valor inicial é colocado entre as tags de abertura e fechamento.
                            A EL ${permissao.descricaoPermissao} funciona da mesma forma que no input de texto.
                        --%>
                        <textarea id="descricaoPermissao" name="descricaoPermissao" class="form-input form-textarea" placeholder="Descreva o que esta permissão permite fazer no sistema...">${permissao.descricaoPermissao}</textarea>
                        <div class="form-help">Descrição detalhada da funcionalidade que esta permissão concede (opcional)</div>
                    </div>
                </div>

                <div class="form-actions">
                    <%-- O botão "Cancelar" é um link que leva o usuário de volta para a página principal de gerenciamento de permissões. --%>
                    <a href="${pageContext.request.contextPath}/admin/permissoes" class="btn btn-secondary">
                        ❌ Cancelar
                    </a>
                    <button type="submit" class="btn btn-primary">
                        <%-- O texto do botão de submissão também é dinâmico, refletindo a ação a ser executada (salvar ou criar). --%>
                        <c:choose>
                            <c:when test="${action == 'edit'}">💾 Salvar Alterações</c:when>
                            <c:otherwise>➕ Criar Permissão</c:otherwise>
                        </c:choose>
                    </button>
                </div>
            </form>
        </div>

        <%--
            Bloco de JavaScript para melhorar a usabilidade do formulário (validação/formatação no lado do cliente).
            Este código é executado no navegador do usuário assim que a página é carregada.
        --%>
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const form = document.getElementById('permissaoForm');
                const nomeInput = document.getElementById('nomePermissao');

                // Adiciona um "ouvinte de evento" que é acionado a cada vez que o usuário digita no campo "Nome da Permissão".
                nomeInput.addEventListener('input', function () {
                    // Pega o valor atual do campo e o converte para maiúsculas.
                    let value = this.value.toUpperCase();
                    // Utiliza uma Expressão Regular (Regex) para remover qualquer caractere
                    // que NÃO SEJA uma letra de A-Z, um número de 0-9 ou um underscore (_).
                    value = value.replace(/[^A-Z0-9_]/g, '');
                    // Atualiza o valor do campo com a string já formatada.
                    this.value = value;
                });
            });
        </script>
    </body>
</html>