<%--
* =================================================================================
* NOME DO ARQUIVO: gerenciarQuestionarios.jsp
* ---------------------------------------------------------------------------------
* DESCRIÇÃO:
* Esta página JSP (JavaServer Pages) é o componente de VISÃO (View) principal para
* a funcionalidade de gerenciamento de modelos de questionários. Ela exibe uma lista
* de todos os modelos cadastrados, oferece uma barra de busca, exibe estatísticas
* e apresenta as opções de CRUD (Criar, Ler, Atualizar, Excluir).
*
* A página segue o padrão de arquitetura MVC (Model-View-Controller).
* ---------------------------------------------------------------------------------
* LIGAÇÕES COM OUTROS ARQUIVOS:
*
* - CONTROLLER (Controlador):
* Esta página é controlada por uma Servlet Java (provavelmente "QuestionarioServlet.java")
* mapeada para a URL "/admin/questionarios". A servlet processa as requisições (listar,
* buscar, excluir), interage com a camada de dados (DAO) e encaminha os dados
* necessários para que esta JSP possa renderizar a página.
*
* - MODEL (Modelo de Dados):
* Recebe dados da servlet através de atributos na requisição (request attributes).
* Os principais atributos esperados são:
* - "listQuestionarios": Uma lista de objetos 'Questionario' para popular a tabela.
* - "stats": Um objeto ou Map contendo estatísticas, como o total de questionários.
* A página também lê parâmetros da URL (usando o objeto implícito 'param') para
* fornecer feedback ao usuário (ex: param.success) e para manter o estado da busca (param.search).
*
* - OUTRAS VIEWS (Outras Visões):
* - A partir desta página, o usuário é redirecionado para o formulário
* "formularioQuestionario.jsp" ao clicar em "Novo Questionário" ou "Editar".
*
* - RECURSOS ESTÁTICOS:
* - Utiliza o arquivo de folha de estilos "/css/formularios.css" e contém
* um bloco de CSS específico para padronizar o layout.
* =================================================================================
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Importa a biblioteca JSTL Core, essencial para usar tags lógicas como <c:if>, <c:choose> e <c:forEach>. --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Gerenciar Questionários - Sistema UNIFAE</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <%-- A Expression Language (EL) ${pageContext.request.contextPath} gera a URL raiz da aplicação, garantindo que o caminho para os recursos seja sempre correto. --%>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">

        <%-- Bloco de estilos CSS internos para garantir a padronização visual desta página de listagem, incluindo o tema de cor específico (laranja). --%>
        <style>
            body {
                font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
                background-color: #f4f6f9;
                color: #333;
                margin: 20px;
            }
            .page-header {
                background-color: #fd7e14;
                color: white;
                padding: 20px;
                margin-bottom: 20px;
                border-radius: 8px;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            .page-header h1 {
                margin: 0;
                font-size: 28px;
            }
            .page-header p {
                margin: 5px 0 0;
                opacity: 0.9;
            }
            .btn-new {
                background-color: #28a745;
                color: white;
                padding: 10px 20px;
                text-decoration: none;
                border-radius: 5px;
                font-weight: 500;
                transition: background-color 0.3s;
            }
            .btn-new:hover {
                background-color: #218838;
            }
            .search-section {
                background-color: #f8f9fa;
                padding: 15px;
                border-radius: 5px;
                margin-bottom: 20px;
                border: 1px solid #dee2e6;
            }
            .search-form {
                display: flex;
                gap: 10px;
                align-items: center;
                flex-wrap: wrap;
            }
            .search-input {
                padding: 8px 12px;
                border: 1px solid #ddd;
                border-radius: 4px;
                min-width: 200px;
            }
            .btn-search {
                background-color: #fd7e14;
                color: white;
                padding: 8px 16px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
            }
            .btn-clear {
                background-color: #6c757d;
                color: white;
                padding: 8px 16px;
                border: none;
                border-radius: 4px;
                text-decoration: none;
                display: inline-block;
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
                padding: 12px 15px;
                text-align: left;
                border-bottom: 2px solid #dee2e6;
                font-weight: 600;
            }
            .data-table td {
                padding: 12px 15px;
                border-bottom: 1px solid #dee2e6;
            }
            .data-table tr:hover {
                background-color: #f1f1f1;
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
                transition: opacity 0.2s;
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
            .empty-state {
                text-align: center;
                padding: 40px;
                color: #6c757d;
            }
            .stats-section {
                display: flex;
                gap: 20px;
                margin-bottom: 20px;
            }
            .stat-card {
                background-color: white;
                padding: 15px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                flex: 1;
                text-align: center;
            }
            .stat-number {
                font-size: 24px;
                font-weight: bold;
                color: #fd7e14;
            }
            .stat-label {
                color: #6c757d;
                font-size: 14px;
            }
            .main-info {
                font-weight: 600;
                color: #333;
            }
            .back-link-container {
                margin-top: 20px;
                text-align: center;
            }
            .success-message {
                background-color: #d4edda;
                border: 1px solid #c3e6cb;
                color: #155724;
                padding: 10px;
                border-radius: 4px;
                margin-bottom: 20px;
            }
        </style>
    </head>
    <body>
        <div class="page-header">
            <div>
                <h1>📋 Gerenciar Questionários</h1>
                <p>Modelos de avaliação utilizados no sistema</p>
            </div>
            <%-- Este link direciona o usuário para a ação de criar um novo questionário, que exibirá o formulário em branco. --%>
            <a href="${pageContext.request.contextPath}/admin/questionarios?action=new" class="btn-new">
                ➕ Novo Questionário
            </a>
        </div>

        <%--
            Blocos para exibir mensagens de feedback ao usuário. Eles usam a tag <c:if> para verificar
            a presença de parâmetros na URL (ex: ?success=1). Isso é parte do padrão Post-Redirect-Get (PRG),
            onde após uma ação (salvar, excluir), a servlet redireciona para esta página com um parâmetro
            de status, permitindo exibir uma mensagem de confirmação de forma segura.
        --%>
        <c:if test="${param.success == '1'}">
            <div class="success-message"><strong>Sucesso:</strong> Questionário salvo com sucesso!</div>
        </c:if>
        <c:if test="${param.deleted == '1'}">
            <div class="success-message"><strong>Sucesso:</strong> Questionário excluído com sucesso!</div>
        </c:if>

        <%-- Seção de estatísticas rápidas. O valor é obtido de um objeto 'stats' enviado pelo servlet. --%>
        <div class="stats-section">
            <div class="stat-card">
                <div class="stat-number">${stats.totalQuestionarios}</div>
                <div class="stat-label">Total de Modelos</div>
            </div>
        </div>

        <%--
            Formulário de busca. Ele envia os dados via método GET para a mesma URL, adicionando o termo
            de busca como um parâmetro (ex: ?search=TERMO_PESQUISADO).
        --%>
        <div class="search-section">
            <form class="search-form" action="${pageContext.request.contextPath}/admin/questionarios" method="get">
                <%--
                    O valor do campo de busca é preenchido com ${param.search}. Isso garante que, após a
                    busca ser realizada, o termo pesquisado permaneça visível no campo, melhorando a usabilidade.
                --%>
                <input type="text" name="search" class="search-input" placeholder="Buscar por nome ou descrição..." value="${param.search}">
                <button type="submit" class="btn-search">🔍 Buscar</button>
                <a href="${pageContext.request.contextPath}/admin/questionarios" class="btn-clear">🗑️ Limpar</a>
            </form>
        </div>

        <div class="table-container">
            <%--
                A tag <c:choose> verifica se a lista de questionários ('listQuestionarios') está vazia.
                - Se sim (<c:when>), exibe uma mensagem amigável de "estado vazio".
                - Se não (<c:otherwise>), renderiza a tabela com os dados.
                Isso previne a exibição de uma tabela vazia e informa o usuário sobre o resultado da busca ou listagem.
            --%>
            <c:choose>
                <c:when test="${empty listQuestionarios}">
                    <div class="empty-state">
                        <h3>📋 Nenhum questionário encontrado</h3>
                        <p>Crie um novo modelo ou ajuste os filtros da sua busca.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nome do Modelo</th>
                                <th>Descrição</th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%--
                                A tag <c:forEach> itera sobre a coleção 'listQuestionarios'. Para cada objeto 'questionario'
                                na lista, um novo bloco <tr> (linha da tabela) é criado, exibindo seus dados.
                            --%>
                            <c:forEach var="questionario" items="${listQuestionarios}">
                                <tr>
                                    <td>${questionario.idQuestionario}</td>
                                    <td class="main-info">${questionario.nomeModelo}</td>
                                    <td>${questionario.descricao}</td>
                                    <td>
                                        <div class="actions">
                                            <%-- Link para editar: passa a ação 'edit' e o ID do questionário para o servlet. --%>
                                            <a href="questionarios?action=edit&id=${questionario.idQuestionario}" class="btn-action btn-edit" title="Editar">✏️ Editar</a>
                                            
                                            <%-- Link para cadastrar competências --%>
                                            <a href="${pageContext.request.contextPath}/admin/competencias?questionarioId=${questionario.idQuestionario}" class="btn-action" style="background-color: #17a2b8; color: white;" title="Gerenciar Competências">🛠️ Competências</a>

                                            
                                            <%-- Link para excluir: passa a ação 'delete' e o ID. Um script JS adiciona uma confirmação de segurança. --%>
                                            <a href="questionarios?action=delete&id=${questionario.idQuestionario}" class="btn-action btn-delete" title="Excluir">🗑️ Excluir</a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="back-link-container">
            <a href="${pageContext.request.contextPath}/" class="btn-action" style="background-color: #6c757d; color: white;">
                🏠 Voltar ao Início
            </a>
        </div>

        <%--
            Este script JavaScript é executado no navegador do usuário para adicionar uma camada de segurança.
            Ele previne a exclusão acidental de um questionário ao exibir uma caixa de diálogo de confirmação.
        --%>
        <script>
            // Garante que o script rode apenas após o carregamento completo do DOM (estrutura da página).
            document.addEventListener('DOMContentLoaded', function () {
                // Seleciona todos os links que têm a classe 'btn-delete'.
                const deleteLinks = document.querySelectorAll('a.btn-delete');
                // Para cada link de exclusão encontrado...
                deleteLinks.forEach(link => {
                    // ...adiciona um "ouvinte" que espera por um evento de clique.
                    link.addEventListener('click', function (e) {
                        // Quando o link é clicado, exibe uma caixa de confirmação do navegador.
                        // Se o usuário clicar em "Cancelar", a função confirm() retorna 'false'.
                        if (!confirm('Tem certeza que deseja excluir este questionário? Esta ação não pode ser desfeita e pode afetar avaliações existentes.')) {
                            // Impede a ação padrão do link (que seria navegar para a URL de exclusão), cancelando a operação.
                            e.preventDefault();
                        }
                    });
                });
            });
        </script>
    </body>
</html>