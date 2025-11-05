<%--
* =================================================================================
* NOME DO ARQUIVO: gerenciarNotas.jsp
* ---------------------------------------------------------------------------------
* DESCRIÇÃO:
* Esta página JSP (JavaServer Pages) é o componente de VISÃO (View) principal para
* a funcionalidade de gerenciamento de notas no sistema. Ela é responsável por
* exibir uma lista de todas as notas cadastradas, fornecer estatísticas, permitir
* a filtragem dos dados e apresentar as opções de CRUD (Criar, Ler, Atualizar,
* Excluir) para cada registro de nota.
*
* A página segue o padrão de arquitetura MVC (Model-View-Controller).
* ---------------------------------------------------------------------------------
* LIGAÇÕES COM OUTROS ARQUIVOS:
*
* - CONTROLLER (Controlador):
* Esta página é controlada por uma Servlet Java (provavelmente "NotaServlet.java")
* mapeada para a URL "/admin/notas". A servlet processa as requisições,
* interage com o modelo de dados (DAO) e encaminha os dados necessários para
* que esta JSP possa renderizar a página.
*
* - MODEL (Modelo de Dados):
* Recebe dados da servlet através de atributos na requisição (request attributes).
* Os principais atributos esperados são:
* - "listNotas": Uma lista de objetos 'Nota' contendo os detalhes de cada nota.
* - "listAlunos": Uma lista de objetos 'Aluno' para popular o filtro de alunos.
* - "listDisciplinas": Uma lista de objetos 'Disciplina' para o filtro de disciplinas.
* - "tiposAvaliacao": Uma coleção de valores do Enum 'TipoAvaliacao' para o filtro.
*
* - OUTRAS VIEWS (Outras Visões):
* - A partir desta página, o usuário pode ser redirecionado para a página de
* formulário ("formularioNota.jsp", por exemplo) ao clicar em "Nova Nota"
* ou "Editar".
*
* - RECURSOS ESTÁTICOS:
* - Utiliza o arquivo de folha de estilos "/css/formularios.css" para
* estilização geral, além de conter estilos CSS específicos incorporados
* neste próprio arquivo.
* =================================================================================
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
    A diretiva 'taglib' importa a biblioteca JSTL (JSP Standard Tag Library),
    especificamente o core (núcleo). Isso nos permite usar tags como <c:forEach>,
    <c:if>, <c:set>, etc., para adicionar lógica de programação (loops, condicionais)
    à página de forma limpa e declarativa, sem usar scriptlets Java (<% ... %>).
    O prefixo "c" é o alias que usaremos para chamar as tags desta biblioteca.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Gerenciar Notas - Sistema UNIFAE</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <%--
            A tag <link> importa a folha de estilos externa.
            A Expression Language (EL) "${pageContext.request.contextPath}" é usada
            para gerar a URL base da aplicação dinamicamente. Isso garante que o caminho
            para o CSS funcione corretamente, independentemente de como a aplicação
            está implantada no servidor.
        --%>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formularios.css">

        <%--
            Bloco de estilos CSS internos. Estes estilos se aplicam especificamente
            aos elementos desta página, permitindo uma customização visual detalhada
            dos cards de estatísticas, da tabela de dados, dos filtros e dos botões
            de ação, sem afetar outras páginas do sistema.
        --%>
        <style>
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
                background-color: #28a745;
                color: white;
                padding: 10px 20px;
                text-decoration: none;
                border-radius: 5px;
                font-weight: 500;
            }

            .btn-new:hover {
                background-color: #218838;
            }

            .btn-relatorio {
                background-color: #17a2b8;
                color: white;
                padding: 10px 20px;
                text-decoration: none;
                border-radius: 5px;
                font-weight: 500;
                margin-left: 10px;
            }

            .search-section {
                background-color: #f8f9fa;
                padding: 15px;
                border-radius: 5px;
                margin-bottom: 20px;
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
                min-width: 150px;
            }

            .btn-search {
                background-color: #6f42c1;
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
                cursor: pointer;
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

            .nota-badge {
                padding: 4px 8px;
                border-radius: 4px;
                font-size: 14px;
                font-weight: bold;
                min-width: 40px;
                text-align: center;
                display: inline-block;
            }

            .nota-aprovado {
                background-color: #d4edda;
                color: #155724;
            }

            .nota-reprovado {
                background-color: #f8d7da;
                color: #721c24;
            }

            .nota-medio {
                background-color: #fff3cd;
                color: #856404;
            }

            .tipo-badge {
                background-color: #e7f3ff;
                color: #0056b3;
                padding: 3px 8px;
                border-radius: 12px;
                font-size: 11px;
                font-weight: 600;
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

            .btn-toggle {
                background-color: #6c757d;
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
                color: #6f42c1;
            }

            .stat-label {
                color: #6c757d;
                font-size: 14px;
            }

            .student-info {
                display: flex;
                flex-direction: column;
                gap: 2px;
            }

            .student-name {
                font-weight: 600;
                color: #333;
                font-size: 13px;
            }

            .student-ra {
                font-size: 11px;
                color: #6c757d;
            }

            .disciplina-info {
                font-weight: 500;
                color: #495057;
            }

            .disciplina-codigo {
                font-size: 11px;
                color: #6c757d;
            }
        </style>
    </head>
    <body>
        <div class="page-header">
            <div>
                <h1>📊 Gerenciar Notas</h1>
                <p>Lançamento e controle de notas dos alunos</p>
            </div>
            <div>
                <%--
                    Este link leva o usuário para a funcionalidade de criação de uma nova nota.
                    Ele aponta para a mesma servlet de controle ("/admin/notas"), mas com o
                    parâmetro "action=new". A servlet identificará esta ação e provavelmente
                    redirecionará para a página de formulário de notas.
                --%>
                <a href="${pageContext.request.contextPath}/admin/notas?action=new" class="btn-new">
                    ➕ Nova Nota
                </a>
            </div>
        </div>

        <%--
            Esta seção exibe um resumo dos dados de notas. Toda a lógica de cálculo é
            feita aqui na View usando JSTL, processando a lista "listNotas" recebida
            do controller.
        --%>
        <div class="stats-section">
            <div class="stat-card">
                <div class="stat-number">
                    <%--
                        Cálculo do total de notas. A tag <c:set> cria uma variável (totalNotas).
                        O loop <c:forEach> itera sobre a lista de notas e incrementa a variável.
                        Finalmente, o valor da variável é exibido.
                    --%>
                    <c:set var="totalNotas" value="0" />
                    <c:forEach var="nota" items="${listNotas}">
                        <c:set var="totalNotas" value="${totalNotas + 1}" />
                    </c:forEach>
                    ${totalNotas}
                </div>
                <div class="stat-label">Total de Notas</div>
            </div>

            <div class="stat-card">
                <div class="stat-number">
                    <%--
                        Cálculo de notas para aprovação (considerando a média 6.0).
                        Dentro do loop, a tag <c:if> verifica a condição e incrementa o
                        contador 'notasAprovadas' somente se a nota for maior ou igual a 6.0.
                    --%>
                    <c:set var="notasAprovadas" value="0" />
                    <c:forEach var="nota" items="${listNotas}">
                        <c:if test="${nota.valorNota >= 6.0}">
                            <c:set var="notasAprovadas" value="${notasAprovadas + 1}" />
                        </c:if>
                    </c:forEach>
                    ${notasAprovadas}
                </div>
                <div class="stat-label">Notas ≥ 6.0</div>
            </div>

            <div class="stat-card">
                <div class="stat-number">
                    <%--
                        Cálculo da média geral de todas as notas. O loop soma todas as notas
                        e conta o número de registros. A estrutura <c:choose> (equivalente a
                        um if-else ou switch) é usada para evitar um erro de divisão por zero
                        se a lista de notas estiver vazia. O resultado é formatado para exibir
                        apenas uma casa decimal usando String.format.
                    --%>
                    <c:set var="somaNotas" value="0" />
                    <c:set var="countNotas" value="0" />
                    <c:forEach var="nota" items="${listNotas}">
                        <c:set var="somaNotas" value="${somaNotas + nota.valorNota}" />
                        <c:set var="countNotas" value="${countNotas + 1}" />
                    </c:forEach>
                    <c:choose>
                        <c:when test="${countNotas > 0}">
                            <c:set var="media" value="${somaNotas / countNotas}" />
                            <c:out value="${String.format('%.1f', media)}" />
                        </c:when>
                        <c:otherwise>0.0</c:otherwise>
                    </c:choose>
                </div>
                <div class="stat-label">Média Geral</div>
            </div>

            <div class="stat-card">
                <div class="stat-number">
                    <%--
                        Cálculo do número de alunos únicos com notas lançadas.
                        Como JSTL não tem uma estrutura de dados "Set" para armazenar itens únicos,
                        este código utiliza uma string ('alunosContados') como um "set simulado".
                        Para cada nota, ele verifica se o ID do aluno já foi adicionado à string.
                        Se não foi, adiciona o ID e incrementa o contador.
                    --%>
                    <c:set var="alunosContados" value="" />
                    <c:set var="totalAlunosUnicos" value="0" />
                    <c:forEach var="nota" items="${listNotas}">
                        <c:set var="alunoId" value=",${nota.aluno.idUsuario}," />
                        <c:if test="${not empty alunosContados}">
                            <c:choose>
                                <c:when test="${alunosContados.indexOf(alunoId) == -1}">
                                    <c:set var="alunosContados" value="${alunosContados}${alunoId}" />
                                    <c:set var="totalAlunosUnicos" value="${totalAlunosUnicos + 1}" />
                                </c:when>
                            </c:choose>
                        </c:if>
                        <c:if test="${empty alunosContados}">
                            <c:set var="alunosContados" value="${alunoId}" />
                            <c:set var="totalAlunosUnicos" value="1" />
                        </c:if>
                    </c:forEach>
                    ${totalAlunosUnicos}
                </div>
                <div class="stat-label">Alunos com Notas</div>
            </div>
        </div>

        <div class="search-section">
            <%--
                Este formulário, quando submetido, envia uma requisição GET para a própria
                página (servlet /admin/notas), passando os valores selecionados como
                parâmetros na URL. A servlet irá então filtrar a lista de notas com base
                nestes parâmetros e recarregar a página com os resultados.
            --%>
            <form class="search-form" method="get">
                <select name="alunoId" class="search-input">
                    <option value="">Todos os Alunos</option>
                    <c:forEach var="aluno" items="${listAlunos}">
                        <%--
                            A lógica EL "${param.alunoId == aluno.idUsuario ? 'selected' : ''}" é
                            usada para manter o filtro selecionado após o envio do formulário.
                            Ela verifica se o ID do aluno atual no loop é igual ao valor do parâmetro
                            "alunoId" na URL. Se for, adiciona o atributo 'selected' à tag <option>.
                        --%>
                        <option value="${aluno.idUsuario}" ${param.alunoId == aluno.idUsuario ? 'selected' : ''}>
                            ${aluno.nomeCompleto} (${aluno.matriculaRA})
                        </option>
                    </c:forEach>
                </select>

                <select name="disciplinaId" class="search-input">
                    <option value="">Todas as Disciplinas</option>
                    <c:forEach var="disciplina" items="${listDisciplinas}">
                        <option value="${disciplina.idDisciplina}" ${param.disciplinaId == disciplina.idDisciplina ? 'selected' : ''}>
                            ${disciplina.nomeDisciplina}
                        </option>
                    </c:forEach>
                </select>

                <select name="tipoAvaliacao" class="search-input">
                    <option value="">Todos os Tipos</option>
                    <c:forEach var="tipo" items="${tiposAvaliacao}">
                        <option value="${tipo}" ${param.tipoAvaliacao == tipo ? 'selected' : ''}>
                            ${tipo.nomeAmigavel}
                        </option>
                    </c:forEach>
                </select>

                <button type="submit" class="btn-search">🔍 Filtrar</button>
                <a href="${pageContext.request.contextPath}/admin/notas" class="btn-clear">🗑️ Limpar</a>
            </form>
        </div>

        <div class="table-container">
            <%--
                A estrutura <c:choose> é usada para renderização condicional.
                Se a lista "listNotas" estiver vazia, exibe uma mensagem de "estado vazio".
                Caso contrário, renderiza a tabela com os dados.
            --%>
            <c:choose>
                <c:when test="${empty listNotas}">
                    <div class="empty-state">
                        <h3>📊 Nenhuma nota cadastrada</h3>
                        <p>Comece lançando a primeira nota clicando no botão "Nova Nota" acima.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Aluno</th>
                                <th>Disciplina</th>
                                <th>Nota</th>
                                <th>Peso</th>
                                <th>Tipo</th>
                                <th>Data</th>
                                <th>Status</th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%--
                                Loop principal que itera sobre a lista de notas ("listNotas") e cria
                                uma nova linha <tr> na tabela para cada objeto "nota" encontrado.
                            --%>
                            <c:forEach var="nota" items="${listNotas}">
                                <tr>
                                    <td>${nota.idNota}</td>
                                    <td>
                                        <div class="student-info">
                                            <%-- Acessa propriedades aninhadas dos objetos, como o nome do aluno através de nota.aluno.nomeCompleto --%>
                                            <div class="student-name">${nota.aluno.nomeCompleto}</div>
                                            <div class="student-ra">RA: ${nota.aluno.matriculaRA}</div>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="disciplina-info">${nota.disciplina.nomeDisciplina}</div>
                                        <c:if test="${not empty nota.disciplina.siglaDisciplina}">
                                            <div class="disciplina-codigo">${nota.disciplina.siglaDisciplina}</div>
                                        </c:if>
                                    </td>
                                    <td>
                                        <%--
                                            Aplicação de estilo condicional. O operador ternário em EL é usado para
                                            selecionar dinamicamente uma classe CSS ('nota-aprovado', 'nota-medio',
                                            'nota-reprovado') com base no valor da nota, fornecendo um feedback
                                            visual imediato sobre o desempenho.
                                        --%>
                                        <span class="nota-badge ${nota.valorNota >= 6.0 ? 'nota-aprovado' : (nota.valorNota >= 4.0 ? 'nota-medio' : 'nota-reprovado')}">
                                            <c:out value="${String.format('%.1f', nota.valorNota)}" />
                                        </span>
                                    </td>
                                    <td>
                                        <%-- A tag <c:out> é usada para exibir valores, e aqui é combinada com String.format para garantir a formatação correta do número. --%>
                                        <c:out value="${String.format('%.1f', nota.pesoNota)}" />
                                    </td>
                                    <td>
                                        <span class="tipo-badge">${nota.tipoAvaliacao.nomeAmigavel}</span>
                                    </td>
                                    <td>
                                        <%--
                                            Como a JSTL 1.2 (padrão em muitos servidores) não tem suporte nativo
                                            para formatar objetos do tipo java.time.LocalDate (do Java 8+),
                                            este trecho usa uma solução alternativa: converte a data para String
                                            e usa a função JSTL 'substring' para extrair e rearranjar as partes
                                            da data no formato brasileiro (dd/MM/yyyy).
                                        --%>
                                        <c:set var="dataStr" value="${nota.dataAvaliacao.toString()}" />
                                        <c:set var="ano" value="${dataStr.substring(0, 4)}" />
                                        <c:set var="mes" value="${dataStr.substring(5, 7)}" />
                                        <c:set var="dia" value="${dataStr.substring(8, 10)}" />
                                        ${dia}/${mes}/${ano}
                                    </td>
                                    <td>
                                        <%--
                                            Renderização condicional do status. A classe CSS e o texto/ícone
                                            são alterados dinamicamente com base no valor booleano do atributo
                                            'ativo' da nota.
                                        --%>
                                        <span class="status-badge ${nota.ativo ? 'status-ativo' : 'status-inativo'}">
                                            ${nota.ativo ? '✅ Ativa' : '❌ Inativa'}
                                        </span>
                                    </td>
                                    <td>
                                        <div class="actions">
                                            <%-- Botão Editar: envia para a servlet com action=edit e o ID da nota. --%>
                                            <a href="${pageContext.request.contextPath}/admin/notas?action=edit&id=${nota.idNota}" 
                                               class="btn-action btn-edit" title="Editar">
                                                ✏️ Editar
                                            </a>

                                            <%--
                                                Botão Ativar/Desativar: envia para a servlet com action=toggle.
                                                O atributo 'onclick' executa um JavaScript que exibe uma caixa de
                                                confirmação. Se o usuário cancelar, a ação do link é prevenida.
                                                O texto do botão e a mensagem de confirmação são dinâmicos.
                                            --%>
                                            <a href="${pageContext.request.contextPath}/admin/notas?action=toggle&id=${nota.idNota}" 
                                               class="btn-action btn-toggle" 
                                               title="${nota.ativo ? 'Desativar' : 'Ativar'}"
                                               onclick="return confirm('Tem certeza que deseja ${nota.ativo ? 'desativar' : 'ativar'} esta nota?')">
                                                ${nota.ativo ? '🔒 Desativar' : '🔓 Ativar'}
                                            </a>

                                            <%-- Botão Excluir: envia para a servlet com action=delete e também possui uma confirmação via JavaScript. --%>
                                            <a href="${pageContext.request.contextPath}/admin/notas?action=delete&id=${nota.idNota}" 
                                               class="btn-action btn-delete" title="Excluir"
                                               onclick="return confirm('Tem certeza que deseja excluir esta nota? Esta ação não pode ser desfeita.')">
                                                🗑️ Excluir
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>

        <div style="margin-top: 20px; text-align: center;">
            <a href="${pageContext.request.contextPath}/" class="btn-action btn-toggle">
                🏠 Voltar ao Início
            </a>
        </div>

        <%--
            Bloco de JavaScript. Este script adiciona um listener de evento de clique a todos
            os links de exclusão. Embora os links já tenham um 'onclick', esta é uma forma
            mais moderna e robusta (usando 'addEventListener') de garantir a funcionalidade
            de confirmação, separando o comportamento (JS) da estrutura (HTML).
        --%>
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const deleteLinks = document.querySelectorAll('a[href*="action=delete"]');
                deleteLinks.forEach(link => {
                    link.addEventListener('click', function (e) {
                        if (!confirm('Tem certeza que deseja excluir esta nota? Esta ação não pode ser desfeita.')) {
                            e.preventDefault(); // Impede a navegação se o usuário clicar em "Cancelar"
                        }
                    });
                });
            });
        </script>
    </body>
</html>