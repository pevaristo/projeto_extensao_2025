<%--
    INDEX.JSP - PÁGINA INICIAL DO SISTEMA DE AVALIAÇÃO UNIFAE
    =======================================================================
    
    Esta é a página inicial (landing page) do Sistema de Avaliação UNIFAE.
    Combina tecnologias JSP, HTML5 e CSS3 para criar uma interface que serve
    como ponto de entrada para todas as funcionalidades.
    
    VERSÃO ATUALIZADA com os novos módulos:
    - Gerenciamento de Disciplinas
    - Gerenciamento de Alunos
    - Sistema de Notas
    
    RESPONSABILIDADES:
    - Apresentar status do sistema e informações técnicas
    - Fornecer navegação principal para funcionalidades
    - Exibir menu dropdown para criação de avaliações
    - Mostrar informações de configuração e ambiente
    - Servir como dashboard inicial para usuários
    - Integrar novos módulos acadêmicos
    
    TECNOLOGIAS UTILIZADAS:
    - JSP (JavaServer Pages): Geração dinâmica de conteúdo
    - HTML5: Estrutura semântica da página
    - CSS3: Estilização moderna e responsiva
    - JavaScript: Interatividade (menu dropdown)
   
    
    RELACIONAMENTO COM OUTROS ARQUIVOS:
    - css/formularios.css: Estilos compartilhados do sistema
    - Servlets: Links para controladores (test, avaliacoes, avaliacao/form)
    - web.xml: Configuração de context path e encoding
    - Outras JSPs: Navegação para formulários e listagens
    
    PADRÃO MVC:
    - View: Esta página JSP (apresentação)
    - Controller: Servlets referenciados nos links
    - Model: Dados do sistema exibidos dinamicamente
    
    FUNCIONALIDADES:
    - Menu de navegação principal
    - Menu dropdown para tipos de avaliação
    - Informações técnicas do ambiente
    - Status de configuração do sistema
    - Links para funcionalidades principais
    - NOVO: Links para módulos acadêmicos (disciplinas, alunos, notas)
--%>

<%--
    DIRETIVA PAGE
    =============
    Define configurações básicas da página JSP.
    - contentType: Tipo MIME e encoding da resposta
    - language: Linguagem de script (Java)
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
    <head>
        <%-- META INFORMAÇÕES E CONFIGURAÇÕES --%>
        <title>Ferramenta para Avaliação Médica em Ambientes de Saúde Comunitária</title>
        <meta charset="UTF-8">
        <%-- Viewport para responsividade mobile --%>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <%-- 
            IMPORTAÇÃO DE CSS EXTERNO
            =========================
            Importa estilos compartilhados do sistema.
            Caminho relativo ao context root da aplicação.
        --%>
        <link rel="stylesheet" href="css/formularios.css">

        <%--
            ESTILOS CSS INTERNOS
            ====================
            Estilos específicos para esta página.
            Inclui layout responsivo, cores do tema e componentes.
        --%>
        <style>
            /* ===== ESTILOS GLOBAIS ===== */
            body {
                font-family: Arial, sans-serif; /* Fonte padrão do sistema */
                margin: 20px; /* Margem externa */
                background-color: #f5f5f5; /* Cor de fundo neutra */
            }

            /* ===== CABEÇALHO PRINCIPAL ===== */
            .header {
                background-color: #007bff; /* Azul tema UNIFAE */
                color: white; /* Texto branco */
                padding: 20px; /* Espaçamento interno */
                margin-bottom: 20px; /* Espaço abaixo */
                text-align: center; /* Centralização */
                border-radius: 8px; /* Bordas arredondadas */
            }

            /* ===== MENSAGENS DE SUCESSO ===== */
            .success {
                background-color: #d4edda; /* Verde claro */
                border: 1px solid #c3e6cb; /* Borda verde */
                color: #155724; /* Texto verde escuro */
                padding: 15px; /* Espaçamento interno */
                border-radius: 5px; /* Bordas arredondadas */
                margin: 10px 0; /* Margem vertical */
            }

            /* ===== SEÇÃO ACADÊMICA (ATUALIZADA) ===== */
            .academic-section {
                background-color: #d1ecf1; /* Azul claro */
                border: 1px solid #c3e6cb; /* Borda verde */
                border-radius: 8px; /* Bordas arredondadas */
                padding: 20px; /* Espaçamento interno */
                margin: 20px 0; /* Margem vertical */
            }

            .academic-section h3 {
                color: #155724; /* Texto verde escuro */
                margin-top: 0; /* Remove margem superior */
                margin-bottom: 15px; /* Espaço abaixo */
            }

            .academic-menu {
                display: flex; /* Layout flexível */
                gap: 15px; /* Espaço entre itens */
                flex-wrap: wrap; /* Quebra linha se necessário */
                justify-content: flex-start; /* Alinhamento à esquerda */
                align-items: center; /* Alinhamento vertical */
            }

            /* Estilo unificado para links e botão dropdown */
            .academic-menu a, .academic-menu .btn-menu {
                background-color: #007bff; /* azul */
                color: white; /* Texto branco */
                padding: 10px 20px; /* Espaçamento interno */
                text-decoration: none; /* Remove sublinhado */
                border-radius: 5px; /* Bordas arredondadas */
                transition: background-color 0.3s; /* Animação suave */
                font-weight: 500; /* Peso da fonte */
                font-family: Arial, sans-serif; /* Fonte consistente */
                font-size: 14px; /* Tamanho da fonte */
            }

            /* Efeito hover unificado */
            .academic-menu a:hover,
            .academic-menu .dropdown:hover .btn-menu {
                background-color: #218838; /* Verde mais escuro */
            }

            /* ===== NOVA SEÇÃO: MÓDULOS ACADÊMICOS ===== */
            .modules-section {
                background-color: #e8f5e8; /* Verde muito claro */
                border: 1px solid #c3e6cb; /* Borda verde */
                border-radius: 8px; /* Bordas arredondadas */
                padding: 20px; /* Espaçamento interno */
                margin: 20px 0; /* Margem vertical */
            }

            .modules-section h3 {
                color: #155724; /* Texto verde escuro */
                margin-top: 0; /* Remove margem superior */
                margin-bottom: 15px; /* Espaço abaixo */
            }

            .modules-menu {
                display: flex; /* Layout flexível */
                gap: 15px; /* Espaço entre itens */
                flex-wrap: wrap; /* Quebra linha se necessário */
                justify-content: flex-start; /* Alinhamento à esquerda */
            }

            .modules-menu a {
                background-color: #28a745; /* Verde */
                color: white; /* Texto branco */
                padding: 10px 20px; /* Espaçamento interno */
                text-decoration: none; /* Remove sublinhado */
                border-radius: 5px; /* Bordas arredondadas */
                transition: background-color 0.3s; /* Animação suave */
                font-weight: 500; /* Peso da fonte */
            }

            .modules-menu a:hover {
                background-color: #218838; /* Verde mais escuro */
            }

            /* ===== MENU DROPDOWN ===== */
            /* Container do dropdown */
            .dropdown {
                position: relative; /* Posicionamento relativo */
                display: inline-block; /* Display inline-block */
            }

            /* Botão principal do dropdown (agora estilizado pelo academic-menu) */
            .dropdown .btn-menu {
                border: none; /* Remove borda padrão */
                cursor: pointer; /* Cursor de clique */
            }

            /* Container do conteúdo dropdown */
            .dropdown-content {
                display: none; /* Oculto por padrão */
                position: absolute; /* Posicionamento absoluto */
                background-color: #f1f1f1; /* Fundo cinza claro */
                min-width: 260px; /* Largura mínima */
                box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2); /* Sombra */
                z-index: 1; /* Camada superior */
                border-radius: 5px; /* Bordas arredondadas */
                overflow: hidden; /* Oculta overflow */
            }

            /* Links dentro do dropdown */
            .dropdown-content a {
                color: black; /* Texto preto */
                padding: 12px 16px; /* Espaçamento interno */
                text-decoration: none; /* Remove sublinhado */
                display: block; /* Display em bloco */
                background-color: #f1f1f1; /* Fundo cinza */
                text-align: left; /* Alinhamento à esquerda */
                border-radius: 0; /* Remove bordas arredondadas */
            }

            /* Efeito hover nos links do dropdown */
            .dropdown-content a:hover {
                background-color: #ddd; /* Cinza mais escuro */
            }

            /* Mostra dropdown no hover */
            .dropdown:hover .dropdown-content {
                display: block; /* Exibe conteúdo */
            }

            /* ===== SEÇÃO ADMINISTRATIVA ===== */
            .admin-section {
                background-color: #fff3cd; /* Amarelo claro */
                border: 1px solid #ffeaa7; /* Borda amarela */
                border-radius: 8px; /* Bordas arredondadas */
                padding: 20px; /* Espaçamento interno */
                margin: 20px 0; /* Margem vertical */
            }

            .admin-section h3 {
                color: #856404; /* Texto amarelo escuro */
                margin-top: 0; /* Remove margem superior */
                margin-bottom: 15px; /* Espaço abaixo */
            }

            .admin-menu {
                display: flex; /* Layout flexível */
                gap: 15px; /* Espaço entre itens */
                flex-wrap: wrap; /* Quebra linha se necessário */
                justify-content: flex-start; /* Alinhamento à esquerda */
            }

            .admin-menu a {
                background-color: #ffc107; /* Amarelo */
                color: #212529; /* Texto escuro */
                padding: 10px 20px; /* Espaçamento interno */
                text-decoration: none; /* Remove sublinhado */
                border-radius: 5px; /* Bordas arredondadas */
                transition: background-color 0.3s; /* Animação suave */
                font-weight: 500; /* Peso da fonte */
            }

            .admin-menu a:hover {
                background-color: #e0a800; /* Amarelo mais escuro */
            }

            /* ===== MENSAGENS INFORMATIVAS ===== */
            .info {
                background-color: #d1ecf1; /* Azul claro */
                border: 1px solid #bee5eb; /* Borda azul */
                color: #0c5460; /* Texto azul escuro */
                padding: 15px; /* Espaçamento interno */
                border-radius: 5px; /* Bordas arredondadas */
                margin: 10px 0; /* Margem vertical */
            }

            /* ===== LAYOUT GRID RESPONSIVO ===== */
            .grid {
                display: grid; /* Layout grid */
                grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); /* Colunas responsivas */
                gap: 20px; /* Espaço entre itens */
                margin: 20px 0; /* Margem vertical */
            }

            /* ===== CARDS DE CONTEÚDO ===== */
            .card {
                background: white; /* Fundo branco */
                padding: 20px; /* Espaçamento interno */
                border-radius: 8px; /* Bordas arredondadas */
                box-shadow: 0 2px 4px rgba(0,0,0,0.1); /* Sombra sutil */
            }

            /* ===== INDICADORES DE STATUS ===== */
            .status {
                display: inline-block; /* Display inline-block */
                padding: 4px 8px; /* Espaçamento interno */
                border-radius: 4px; /* Bordas arredondadas */
                font-size: 12px; /* Fonte pequena */
                font-weight: bold; /* Texto em negrito */
            }

            /* Status OK (verde) */
            .status.ok {
                background-color: #d4edda; /* Fundo verde claro */
                color: #155724; /* Texto verde escuro */
            }

            /* Status Info (azul) */
            .status.info {
                background-color: #007bff; /* Fundo azul */
                color: #0c5460; /* Texto azul escuro */
            }

            /* ===== RESPONSIVIDADE ===== */
            @media (max-width: 768px) {
                .academic-menu, .modules-menu, .admin-menu {
                    flex-direction: column;
                    align-items: stretch;
                }
                
                .academic-menu a, .modules-menu a, .admin-menu a, .academic-menu .btn-menu {
                    text-align: center;
                    margin-bottom: 10px;
                }
                
                .dropdown-content {
                    position: static;
                    display: block;
                    box-shadow: none;
                    background-color: #e9ecef;
                    margin-top: 10px;
                }
                
                .dropdown:hover .dropdown-content {
                    display: block;
                }
            }
        </style>
    </head>
    <body>
        <%--
            CABEÇALHO PRINCIPAL
            ===================
            Seção de apresentação do sistema com informações básicas.
        --%>
        <div class="header">
            <h1>🎓 Ferramenta para Avaliação Médica em Ambientes de Saúde Comunitária</h1>
            <p>NetBeans 21 + Tomcat 10.1.42 + JDK21 + Jakarta EE</p>            
        </div>

        <%--
            SEÇÃO DE STATUS DO SISTEMA
            ==========================
            Exibe informações dinâmicas sobre o ambiente e configuração.
            Usa scriptlets JSP para obter dados do servidor.
        --%>
        <div class="success">
            <h2>🚀 Sistema Configurado e Funcionando!</h2>
            <div class="grid">
                <div>
                    <strong>Informações do Sistema:</strong>
                    <ul>
                        <%-- 
                            SCRIPTLETS JSP
                            ==============
                            Código Java executado no servidor para obter informações dinâmicas.
                        --%>
                        <li><strong>Java:</strong> <%= System.getProperty("java.version") %></li>
                        <li><strong>Servidor:</strong> <%= application.getServerInfo() %></li>
                        <li><strong>Context Path:</strong> <%= request.getContextPath() %></li>
                        <li><strong>Servlet API:</strong> <%= application.getMajorVersion() %>.<%= application.getMinorVersion() %></li>
                    </ul>
                </div>
                <div>
                    <strong>Status da Aplicação:</strong>
                    <ul>
                        <li><span class="status ok">✅</span> Deploy NetBeans funcionando</li>
                        <li><span class="status ok">✅</span> Context path configurado</li>
                        <li><span class="status ok">✅</span> Jakarta EE ativo</li>
                        <li><span class="status ok">✅</span> Encoding UTF-8 configurado</li>
                        <li><span class="status ok">✅</span> Módulos acadêmicos integrados</li>
                    </ul>
                </div>
            </div>
        </div>

        <%--
            SEÇÃO ACADÊMICA (ATUALIZADA)
            ============================
            Seção com links para os módulos de avaliação.
        --%>
        <div class="academic-section">
            <h3>📚 Módulo de Avaliações</h3>
            <p>Sistema de avaliações formativas e feedback acadêmico:</p>
            <div class="academic-menu">
                <%-- Link para servlet de teste --%>
                <a href="test">🧪 Teste do Sistema</a>

                <%-- Link para listagem de avaliações --%>
                <a href="avaliacoes">📋 Lista de Avaliações</a>

                <%-- Link para exibição da agenda --%>
                <a href="${pageContext.request.contextPath}/agenda">📅 Agenda</a>

                <%--
                    MENU DROPDOWN PARA NOVA AVALIAÇÃO
                    ==================================
                    Menu suspenso com diferentes tipos de avaliação.
                    Cada link passa parâmetros específicos para o servlet.
                --%>
                <div class="dropdown">
                    <button class="btn-menu">📝 Nova Avaliação ▼</button>
                    <div class="dropdown-content">
                        <%-- 
                            LINKS PARA FORMULÁRIOS ESPECÍFICOS
                            ==================================
                            Cada link inclui parâmetros para identificar:
                            - action=new: Indica criação de nova avaliação
                            - questionarioId: ID do tipo de questionário
                        --%>
                        <a href="avaliacao/form?action=new&questionarioId=1">📝 Nova Mini CEX</a>
                        <a href="avaliacao/form?action=new&questionarioId=2">🎯 Avaliação 360 - Professor</a>
                        <a href="avaliacao/form?action=new&questionarioId=3">👥 Avaliação 360 - Pares</a>
                        <a href="avaliacao/form?action=new&questionarioId=4">⚕️ Avaliação 360 - Equipe</a>
                        <a href="avaliacao/form?action=new&questionarioId=5">🩺 Avaliação 360 - Paciente</a>                    
                    </div>
                </div>
            </div>
        </div>

        <%--
            SEÇÃO: MÓDULOS ACADÊMICOS
            ==============================
            Seção com links para os novos módulos acadêmicos implementados.
        --%>
        
        <div class="modules-section">
            <h3>🎓 Módulos Acadêmicos</h3>
            <p>Gerenciamento completo do ambiente acadêmico:</p>
            <div class="modules-menu">
                <a href="${pageContext.request.contextPath}/admin/disciplinas">📚 Gerenciar Disciplinas</a>
                <a href="${pageContext.request.contextPath}/admin/alunos">👨‍🎓 Gerenciar Alunos</a>
                <a href="${pageContext.request.contextPath}/admin/notas">📊 Sistema de Notas</a>
                <a href="${pageContext.request.contextPath}/admin/turmas">📋 Gerenciar Turmas</a>                                
            </div>
        </div>
        
        <%--
            SEÇÃO ADMINISTRATIVA
            ====================
            Seção com links para os módulos CRUD administrativos.
        --%>
        <div class="admin-section">
            <h3>⚙️ Módulos Administrativos</h3>
            <p>Gerenciamento de cadastros básicos do sistema:</p>
            <div class="admin-menu">
                <%-- Links para os módulos CRUD administrativos --%>
                <a href="${pageContext.request.contextPath}/admin/usuarios">👥 Gerenciar Usuários</a>
                <a href="${pageContext.request.contextPath}/admin/locais">🏢 Gerenciar Locais</a>
                <a href="${pageContext.request.contextPath}/admin/permissoes">🔐 Gerenciar Permissões</a>
                <a href="${pageContext.request.contextPath}/admin/questionarios">📋 Gerenciar Questionários</a>
            </div>
        </div>

        <%--
            GRID DE CARDS INFORMATIVOS
            ==========================
            Layout responsivo com informações sobre funcionalidades.
        --%>
        <div class="grid">
            <div class="card">
                <h3>📊 Sistema de Notas</h3>
                <p>Novo módulo para lançamento e controle de notas dos alunos:</p>
                <ul>
                    <li>✅ Lançamento de notas por disciplina</li>
                    <li>✅ Diferentes tipos de avaliação</li>
                    <li>✅ Cálculo automático de médias</li>
                    <li>✅ Relatórios de desempenho</li>
                    <li>✅ Controle de pesos das avaliações</li>
                </ul>
                <p><strong>Status:</strong> <span class="status ok">✅ IMPLEMENTADO</span></p>
            </div>

            <div class="card">
                <h3>👨‍🎓 Gestão de Alunos</h3>
                <p>Módulo completo para gerenciamento de estudantes:</p>
                <ul>
                    <li>✅ Cadastro completo de alunos</li>
                    <li>✅ Controle de RA e períodos</li>
                    <li>✅ Observações acadêmicas</li>
                    <li>✅ Integração com sistema de notas</li>
                    <li>✅ Filtros e relatórios</li>
                </ul>
                <p><strong>Status:</strong> <span class="status ok">✅ IMPLEMENTADO</span></p>
            </div>

            <div class="card">
                <h3>📚 Gestão de Disciplinas</h3>
                <p>Controle completo das disciplinas do curso:</p>
                <ul>
                    <li>✅ Cadastro de disciplinas</li>
                    <li>✅ Códigos e siglas</li>
                    <li>✅ Controle de carga horária</li>
                    <li>✅ Status ativo/inativo</li>
                    <li>✅ Integração com notas</li>
                </ul>
                <p><strong>Status:</strong> <span class="status ok">✅ IMPLEMENTADO</span></p>
            </div>

            <div class="card">
                <h3>📋 Avaliações Formativas</h3>
                <p>Sistema original de avaliações qualitativas:</p>
                <ul>
                    <li>✅ Mini-CEX (Clinical Evaluation Exercise)</li>
                    <li>✅ Avaliação 360° - Professor/Preceptor</li>
                    <li>✅ Avaliação 360° - Pares</li>
                    <li>✅ Avaliação 360° - Equipe de Saúde</li>
                    <li>✅ Avaliação 360° - Paciente/Família</li>
                </ul>
                <p><strong>Status:</strong> <span class="status ok">✅ FUNCIONANDO</span></p>
            </div>

            <div class="card">
                <h3>🏢 Infraestrutura</h3>
                <p>Módulos de apoio e infraestrutura:</p>
                <ul>
                    <li>✅ Gerenciamento de usuários</li>
                    <li>✅ Controle de permissões</li>
                    <li>✅ Locais de eventos</li>
                    <li>✅ Questionários personalizados</li>
                    <li>✅ Agenda de eventos</li>
                </ul>
                <p><strong>Status:</strong> <span class="status ok">✅ FUNCIONANDO</span></p>
            </div>

            <div class="card">
                <h3>🔧 Tecnologias</h3>
                <p>Stack tecnológico utilizado:</p>
                <ul>
                    <li>✅ Java 21 + Jakarta EE</li>
                    <li>✅ Apache Tomcat 10.1.42</li>
                    <li>✅ JPA/Hibernate</li>
                    <li>✅ MySQL Database</li>
                    <li>✅ JSP + JSTL</li>
                    <li>✅ CSS3 + JavaScript</li>
                </ul>
                <p><strong>Status:</strong> <span class="status ok">✅ ATUALIZADO</span></p>
            </div>
        </div>

        <%--
            SEÇÃO DE INFORMAÇÕES TÉCNICAS
            =============================
            Informações adicionais sobre o sistema.
        --%>
        <div class="info">
            <h3>ℹ️ Informações do Sistema</h3>
            <p><strong>Versão:</strong> 2.0 - Sistema Completo com Módulos Acadêmicos</p>
            <p><strong>Última Atualização:</strong> <%= new java.util.Date() %></p>
            <p><strong>Funcionalidades Implementadas:</strong></p>
            <ul>
                <li>✅ Sistema de Avaliações Formativas (Original)</li>
                <li>✅ Gerenciamento de Disciplinas (Novo)</li>
                <li>✅ Gerenciamento de Alunos (Novo)</li>
                <li>✅ Sistema de Notas (Novo)</li>
                <li>✅ Integração Completa entre Módulos</li>
                <li>✅ Interface Responsiva e Moderna</li>
            </ul>
            <p><strong>Próximas Funcionalidades:</strong> Relatórios avançados, Dashboard analítico, Integração com sistemas externos</p>
        </div>

        <%--
            RODAPÉ COM INFORMAÇÕES ADICIONAIS
            =================================
        --%>
        <div style="text-align: center; margin-top: 30px; padding: 20px; background-color: #f8f9fa; border-radius: 8px;">
            <p><strong>🎓 Sistema de Avaliação UNIFAE - Versão Completa</strong></p>
            <p>Desenvolvido com Jakarta EE, JPA/Hibernate e tecnologias modernas</p>
            <p><em>Sistema integrado para gestão acadêmica e avaliações formativas</em></p>
        </div>

        <%--
            JAVASCRIPT PARA INTERATIVIDADE
            ==============================
        --%>
        <script>
            // Adiciona interatividade aos menus dropdown em dispositivos móveis
            document.addEventListener('DOMContentLoaded', function() {
                const dropdowns = document.querySelectorAll('.dropdown');
                
                dropdowns.forEach(dropdown => {
                    const button = dropdown.querySelector('.btn-menu');
                    const content = dropdown.querySelector('.dropdown-content');
                    
                    // Para dispositivos móveis, adiciona clique para toggle
                    if (window.innerWidth <= 768) {
                        button.addEventListener('click', function(e) {
                            e.preventDefault();
                            content.style.display = content.style.display === 'block' ? 'none' : 'block';
                        });
                    }
                });
                
                // Fecha dropdowns ao clicar fora
                document.addEventListener('click', function(e) {
                    if (!e.target.closest('.dropdown')) {
                        dropdowns.forEach(dropdown => {
                            const content = dropdown.querySelector('.dropdown-content');
                            if (window.innerWidth <= 768) {
                                content.style.display = 'none';
                            }
                        });
                    }
                });
            });
            
            // Adiciona animações suaves aos cards
            const cards = document.querySelectorAll('.card');
            cards.forEach((card, index) => {
                card.style.animationDelay = `${index * 0.1}s`;
                card.style.animation = 'fadeInUp 0.6s ease forwards';
            });
            
            // Define animação CSS
            const style = document.createElement('style');
            style.textContent = `
                @keyframes fadeInUp {
                    from {
                        opacity: 0;
                        transform: translateY(20px);
                    }
                    to {
                        opacity: 1;
                        transform: translateY(0);
                    }
                }
                
                .card {
                    opacity: 0;
                }
            `;
            document.head.appendChild(style);
        </script>
    </body>
</html>
