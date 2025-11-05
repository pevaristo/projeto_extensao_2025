<%--
* =================================================================================
* NOME DO ARQUIVO: gerenciarUsuarios.jsp
* ---------------------------------------------------------------------------------
* DESCRIÇÃO:
* Esta página JSP (JavaServer Pages) é o componente de VISÃO (View) principal para
* a funcionalidade de gerenciamento de Usuários. Ela exibe uma lista de todos os
* usuários cadastrados no sistema em formato de tabela e apresenta as opções de
* CRUD (Criar, Ler, Atualizar, Excluir) para cada registro.
*
* A página segue o padrão de arquitetura MVC (Model-View-Controller).
* ---------------------------------------------------------------------------------
* LIGAÇÕES COM OUTROS ARQUIVOS:
*
* - CONTROLLER (Controlador):
* Esta página é controlada por uma Servlet Java (provavelmente "UsuarioServlet.java")
* mapeada para a URL "/admin/usuarios". A servlet processa as requisições (listar,
* excluir), interage com a camada de dados (DAO) e encaminha a lista de usuários
* para que esta JSP possa renderizar a página.
*
* - MODEL (Modelo de Dados):
* Recebe dados da servlet através de atributos na requisição (request attributes).
* O principal atributo esperado é:
* - "listUsuarios": Uma lista de objetos 'Usuario' para popular a tabela.
* A página também lê parâmetros da URL (usando o objeto implícito 'param') para
* fornecer feedback ao usuário após uma ação (ex: param.success, param.deleted).
*
* - OUTRAS VIEWS (Outras Visões):
* - A partir desta página, o usuário é redirecionado para o formulário
* "formularioUsuario.jsp" ao clicar em "Novo Usuário" ou "Editar".
*
* - RECURSOS ESTÁTICOS:
* - Utiliza o arquivo de folha de estilos "/css/formularios.css" e contém um
* bloco de CSS específico para padronizar o layout.
* =================================================================================
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Gerenciar Usuários - Sistema UNIFAE</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <%-- A Expression Language (EL) ${pageContext.request.contextPath} gera a URL raiz da aplicação, garantindo que o caminho para o CSS seja sempre correto. --%>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">

        <style>
            /* Estilos específicos para listagem de usuários */
            .page-header {
                background-color: #28a745;
                color: white;
                padding: 20px;
                margin-bottom: 20px;
                border-radius: 8px;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .btn-new {
                background-color: #007bff;
                color: white;
                padding: 10px 20px;
                text-decoration: none;
                border-radius: 5px;
                font-weight: 500;
            }

            .btn-new:hover {
                background-color: #0056b3;
            }

            .table-container {
                background-color: white;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }

            .data-table {
                width: 100%;
                border-collapse: collapse;
            }

            .data-table th {
                background-color: #f8f9fa;
                padding: 12px;
                text-align: left;
                border-bottom: 2px solid #dee2e6;
                font-weight: 600;
            }

            .data-table td {
                padding: 12px;
                border-bottom: 1px solid #dee2e6;
            }

            .data-table tr:hover {
                background-color: #f8f9fa;
            }

            .actions {
                display: flex;
                gap: 5px;
            }

            .btn-action {
                padding: 5px 10px;
                text-decoration: none;
                border-radius: 3px;
                font-size: 12px;
                font-weight: 500;
            }

            .btn-edit {
                background-color: #ffc107;
                color: #212529;
            }

            .btn-delete {
                background-color: #dc3545;
                color: white;
            }

            .btn-action:hover {
                opacity: 0.8;
            }

            .actions.menu {
                display: flex;
                gap: 10px;
                margin-bottom: 20px;
                justify-content: flex-start;
            }

            .btn {
                padding: 10px 20px;
                text-decoration: none;
                border-radius: 4px;
                font-size: 14px;
                font-weight: 500;
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

            .btn-danger {
                background-color: #dc3545;
                color: white;
            }

            .btn-danger:hover {
                background-color: #c82333;
            }

            /* Estilos para os badges de status Ativo/Inativo */
            .status {
                padding: 3px 8px;
                border-radius: 12px;
                font-size: 11px;
                font-weight: bold;
                text-transform: uppercase;
            }

            .status.realizada { /* Classe usada para status "Ativo" */
                background-color: #d4edda;
                color: #155724;
            }

            .status.pendente { /* Classe usada para status "Inativo" */
                background-color: #f8d7da;
                color: #721c24;
            }

            .container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 20px;
            }

            .table {
                width: 100%;
                border-collapse: collapse;
                background-color: white;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }

            .table th {
                background-color: #f8f9fa;
                padding: 12px;
                text-align: left;
                border-bottom: 2px solid #dee2e6;
                font-weight: 600;
            }

            .table td {
                padding: 12px;
                border-bottom: 1px solid #dee2e6;
            }

            .table tr:hover {
                background-color: #f8f9fa;
            }

            .success-message {
                background-color: #d4edda;
                border: 1px solid #c3e6cb;
                color: #155724;
                padding: 10px;
                border-radius: 4px;
                margin-bottom: 20px;
            }

            @media (max-width: 768px) {
                .page-header {
                    flex-direction: column;
                    gap: 15px;
                    text-align: center;
                }

                .actions.menu {
                    flex-direction: column;
                }

                .table-container {
                    overflow-x: auto;
                }
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="page-header">
                <div>
                    <h1>👤 Gerenciar Usuários</h1>
                    <p>Cadastro e manutenção dos usuários do sistema</p>
                </div>
                <%-- Este link direciona o usuário para a ação de criar um novo usuário, que exibirá o formulário em branco. --%>
                <a href="${pageContext.request.contextPath}/admin/usuarios?action=new" class="btn-new">
                    ➕ Novo Usuário
                </a>
            </div>

            <%--
                Blocos para exibir mensagens de feedback ao usuário. Eles usam a tag <c:if> para verificar
                a presença de parâmetros na URL (ex: ?success=1). Isso é parte do padrão Post-Redirect-Get (PRG),
                onde após uma ação (salvar, excluir), a servlet redireciona para esta página com um parâmetro
                de status, permitindo exibir uma mensagem de confirmação de forma segura.
            --%>
            <c:if test="${param.success == '1'}">
                <div class="success-message">
                    <strong>Sucesso:</strong> Usuário salvo com sucesso!
                </div>
            </c:if>
            <c:if test="${param.deleted == '1'}">
                <div class="success-message">
                    <strong>Sucesso:</strong> Usuário excluído com sucesso!
                </div>
            </c:if>

            <div class="actions menu">
                <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">🏠 Voltar ao Início</a>
            </div>

            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome Completo</th>
                        <th>Email</th>
                        <th>Tipo</th>
                        <th>Status</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <%--
                        A tag <c:forEach> itera sobre a coleção 'listUsuarios' (enviada pelo servlet).
                        Para cada objeto 'usuario' na lista, um novo bloco <tr> (linha da tabela) é criado.
                    --%>
                    <c:forEach var="usuario" items="${listUsuarios}">
                        <tr>
                            <td>${usuario.idUsuario}</td>
                            <td>${usuario.nomeCompleto}</td>
                            <td>${usuario.email}</td>
                            <td>${usuario.tipoUsuario}</td>
                            <td>
                                <%--
                                    Renderização condicional do status. A EL usa um operador ternário para
                                    gerar uma string contendo o HTML de um <span> com a classe e o texto
                                    apropriados. Se 'usuario.ativo' for true, ele gera o HTML para "Ativo";
                                    caso contrário, gera o HTML para "Inativo". O navegador então renderiza essa string.
                                --%>
                                ${usuario.ativo ? '<span class="status realizada">Ativo</span>' : '<span class="status pendente">Inativo</span>'}
                            </td>
                            <td>
                                <div class="actions">
                                    <%-- Link para editar: passa a ação 'edit' e o ID do usuário para o servlet. --%>
                                    <a href="usuarios?action=edit&id=${usuario.idUsuario}" class="btn-action btn-edit">✏️ Editar</a>
                                    <%--
                                        Link para excluir: passa a ação 'delete' e o ID. O atributo 'onclick'
                                        executa um JavaScript que exibe uma caixa de confirmação antes de prosseguir.
                                    --%>
                                    <a href="usuarios?action=delete&id=${usuario.idUsuario}" class="btn-action btn-delete" onclick="return confirm('Tem certeza que deseja excluir este usuário?')">🗑️ Excluir</a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <%--
            Este script JavaScript é executado no navegador do usuário para adicionar uma camada de segurança.
            Ele previne a exclusão acidental de um usuário ao exibir uma caixa de diálogo de confirmação.
            Embora já exista um 'onclick' no link, esta é uma abordagem mais robusta e desacoplada.
        --%>
        <script>
            // Garante que o script rode apenas após o carregamento completo da página.
            document.addEventListener('DOMContentLoaded', function () {
                // Seleciona todos os links que contêm "action=delete" em seu href.
                const deleteLinks = document.querySelectorAll('a[href*="action=delete"]');
                // Para cada link de exclusão encontrado...
                deleteLinks.forEach(link => {
                    // ...adiciona um "ouvinte" que espera por um evento de clique.
                    link.addEventListener('click', function (e) {
                        // Se o usuário clicar em "Cancelar" na caixa de diálogo, a função confirm() retorna 'false'.
                        if (!confirm('Tem certeza que deseja excluir este usuário?')) {
                            // Impede a ação padrão do link (que seria navegar para a URL de exclusão), cancelando a operação.
                            e.preventDefault();
                        }
                    });
                });
            });
        </script>
    </body>
</html>