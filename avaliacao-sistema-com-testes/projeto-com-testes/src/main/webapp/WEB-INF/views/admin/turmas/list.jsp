<%--
* =================================================================================
* NOME DO ARQUIVO: gerenciarTurmas.jsp
* ---------------------------------------------------------------------------------
* DESCRIÇÃO:
* Esta página JSP (JavaServer Pages) é o componente de VISÃO (View) principal para
* a funcionalidade de gerenciamento de Turmas. Ela exibe uma lista de todas as
* turmas cadastradas, oferece uma barra de busca e filtros, exibe estatísticas
* e apresenta as opções de CRUD (Criar, Ler, Atualizar, Excluir).
*
* A página segue o padrão de arquitetura MVC (Model-View-Controller).
* ---------------------------------------------------------------------------------
* LIGAÇÕES COM OUTROS ARQUIVOS:
*
* - CONTROLLER (Controlador):
* Esta página é controlada por uma Servlet Java (provavelmente "TurmaServlet.java")
* mapeada para a URL "/admin/turmas". A servlet processa as requisições (listar,
* buscar, filtrar, excluir), interage com a camada de dados (DAO) e encaminha os
* dados necessários para que esta JSP possa renderizar a página.
*
* - MODEL (Modelo de Dados):
* Recebe dados da servlet através de atributos na requisição (request attributes).
* Os principais atributos esperados são:
* - "listTurmas": Uma lista de objetos 'Turma' para popular a tabela.
* - "stats": Um objeto ou Map contendo estatísticas, como o total de turmas e o
* número de turmas ativas.
* A página também lê parâmetros da URL (usando o objeto implícito 'param') para
* fornecer feedback ao usuário (ex: param.success) e para manter o estado dos
* filtros (param.search, param.status).
*
* - OUTRAS VIEWS (Outras Visões):
* - A partir desta página, o usuário é redirecionado para o formulário
* "formularioTurma.jsp" ao clicar em "Nova Turma" ou "Editar".
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
        <title>Gerenciar Turmas - Sistema UNIFAE</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <%-- A Expression Language (EL) ${pageContext.request.contextPath} gera a URL raiz da aplicação, garantindo que o caminho para os recursos seja sempre correto. --%>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">

        <%-- Bloco de estilos CSS internos para garantir a padronização visual desta página de listagem, incluindo o tema de cor específico (azul). --%>
        <style>
            body {
                font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
                background-color: #f4f6f9;
                color: #333;
                margin: 20px;
            }
            .page-header {
                background-color: #007bff;
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
                background-color: #007bff;
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
            .status-badge {
                padding: 4px 8px;
                border-radius: 4px;
                font-size: 12px;
                font-weight: bold;
            }
            .status-ativo {
                background-color: #d4edda;
                color: #155724;
            }
            .status-inativo {
                background-color: #f8d7da;
                color: #721c24;
            }
            .periodo-badge {
                background-color: #e7f3ff;
                color: #0056b3;
                padding: 3px 8px;
                border-radius: 12px;
                font-size: 11px;
                font-weight: 600;
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
                color: #007bff;
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
        </style>
    </head>
    <body>
        <div class="page-header">
            <div>
                <h1>👥 Gerenciar Turmas</h1>
                <p>Cadastro e manutenção das turmas do curso</p>
            </div>
            <%-- Este link direciona o usuário para a ação de criar uma nova turma, que exibirá o formulário em branco. --%>
            <a href="${pageContext.request.contextPath}/admin/turmas?action=new" class="btn-new">
                ➕ Nova Turma
            </a>
        </div>

        <%--
            Blocos para exibir mensagens de feedback ao usuário. Eles usam a tag <c:if> para verificar
            a presença de parâmetros na URL (ex: ?success=1). Isso é parte do padrão Post-Redirect-Get (PRG),
            onde após uma ação (salvar, excluir), a servlet redireciona para esta página com um parâmetro
            de status, permitindo exibir uma mensagem de confirmação de forma segura e evitando o reenvio de formulários.
        --%>
        <c:if test="${param.success == '1'}">
            <div class="success-message"><strong>Sucesso:</strong> Turma salva com sucesso!</div>
        </c:if>
        <c:if test="${param.deleted == '1'}">
            <div class="success-message"><strong>Sucesso:</strong> Turma excluída com sucesso!</div>
        </c:if>

        <%-- Seção de estatísticas rápidas. Os valores são obtidos de um objeto 'stats' enviado pelo servlet. --%>
        <div class="stats-section">
            <div class="stat-card">
                <div class="stat-number">${stats.totalTurmas}</div>
                <div class="stat-label">Total de Turmas</div>
            </div>
            <div class="stat-card">
                <div class="stat-number">${stats.turmasAtivas}</div>
                <div class="stat-label">Turmas Ativas</div>
            </div>
        </div>

        <%--
            Formulário de busca e filtro. Ele envia os dados via método GET para a mesma URL, adicionando os
            termos de busca como parâmetros (ex: ?search=TERMO&status=ativo).
        --%>
        <div class="search-section">
            <form class="search-form" action="${pageContext.request.contextPath}/admin/turmas" method="get">
                <%--
                    O valor do campo de busca é preenchido com ${param.search}. Isso garante que, após a
                    busca ser realizada, o termo pesquisado permaneça visível no campo.
                --%>
                <input type="text" name="search" class="search-input" placeholder="Buscar por nome ou código..." value="${param.search}">
                <select name="status" class="search-input">
                    <option value="">Todos os Status</option>
                    <%--
                        A lógica EL com operador ternário verifica o parâmetro 'status' na URL e adiciona o
                        atributo 'selected' à opção correspondente, mantendo o filtro selecionado após o recarregamento da página.
                    --%>
                    <option value="ativo" ${param.status == 'ativo' ? 'selected' : ''}>Apenas Ativas</option>
                    <option value="inativo" ${param.status == 'inativo' ? 'selected' : ''}>Apenas Inativas</option>
                </select>
                <button type="submit" class="btn-search">🔍 Buscar</button>
                <a href="${pageContext.request.contextPath}/admin/turmas" class="btn-clear">🗑️ Limpar</a>
            </form>
        </div>

        <div class="table-container">
            <%--
                A tag <c:choose> verifica se a lista de turmas ('listTurmas') está vazia.
                - Se sim (<c:when>), exibe uma mensagem amigável de "estado vazio".
                - Se não (<c:otherwise>), renderiza a tabela com os dados.
                Isso melhora a experiência do usuário ao não mostrar uma tabela vazia sem contexto.
            --%>
            <c:choose>
                <c:when test="${empty listTurmas}">
                    <div class="empty-state">
                        <h3>👥 Nenhuma turma encontrada</h3>
                        <p>Crie uma nova turma ou ajuste os filtros da sua busca.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nome da Turma</th>
                                <th>Código</th>
                                <th>Ano/Semestre</th>
                                <th>Status</th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%--
                                A tag <c:forEach> itera sobre a coleção 'listTurmas'. Para cada objeto 'turma'
                                na lista, um novo bloco <tr> (linha da tabela) é criado, exibindo seus dados.
                            --%>
                            <c:forEach var="turma" items="${listTurmas}">
                                <tr>
                                    <td>${turma.idTurma}</td>
                                    <td class="main-info">${turma.nomeTurma}</td>
                                    <td>${turma.codigoTurma}</td>
                                    <td>
                                        <span class="periodo-badge">${turma.anoLetivo}/${turma.semestre}</span>
                                    </td>
                                    <td>
                                        <%--
                                            Renderização condicional do status. A EL seleciona dinamicamente a classe CSS
                                            ('status-ativo' ou 'status-inativo') com base no valor booleano do atributo 'ativo' da turma,
                                            proporcionando um feedback visual imediato. O texto e o ícone também são dinâmicos.
                                        --%>
                                        <span class="status-badge ${turma.ativo ? 'status-ativo' : 'status-inativo'}">
                                            ${turma.ativo ? '✅ Ativo' : '❌ Inativo'}
                                        </span>
                                    </td>
                                    <td>
                                        <div class="actions">
                                            <%-- Link para editar: passa a ação 'edit' e o ID da turma para o servlet. --%>
                                            <a href="turmas?action=edit&id=${turma.idTurma}" class="btn-action btn-edit" title="Editar">✏️ Editar</a>
                                            
                                            <%-- Link para vincular disciplinas e professores na turma escolhida. --%>
                                            <a href="${pageContext.request.contextPath}/admin/gerenciar-disciplinas-turma?idTurma=${turma.idTurma}" class="btn-action" style="background-color: #17a2b8; color: white;" title="Disciplinas da Turma">🔗 Disciplinas</a>
                                            
                                            <%-- Link para excluir: passa a ação 'delete' e o ID. Um script JS adiciona uma confirmação de segurança. --%>
                                            <a href="turmas?action=delete&id=${turma.idTurma}" class="btn-action btn-delete" title="Excluir">🗑️ Excluir</a>
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
            <a href="${pageContext.request.contextPath}/" class="btn-action btn-toggle" style="background-color: #6c757d; color: white;">
                🏠 Voltar ao Início
            </a>
        </div>

        <%--
            Este script JavaScript é executado no navegador do usuário para adicionar uma camada de segurança.
            Ele previne a exclusão acidental de uma turma ao exibir uma caixa de diálogo de confirmação.
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
                        if (!confirm('Tem certeza que deseja excluir esta turma? Esta ação não pode ser desfeita.')) {
                            // Impede a ação padrão do link (que seria navegar para a URL de exclusão), cancelando a operação.
                            e.preventDefault();
                        }
                    });
                });
            });
        </script>
    </body>
</html>