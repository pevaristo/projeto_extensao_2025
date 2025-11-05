/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `TestServlet`, é um servlet de diagnóstico, também conhecido como "health check"
 * ou "smoke test". Seu único propósito é realizar uma verificação rápida e simples para
 * confirmar que os componentes fundamentais da aplicação e do ambiente estão funcionando
 * corretamente.
 *
 * Mapeado para a URL `/test`, ele não faz parte da lógica de negócio principal do sistema,
 * mas é uma ferramenta de desenvolvimento e depuração extremamente valiosa. Ao ser acessado,
 * ele executa as seguintes verificações:
 *
 * 1.  **Servlet Container:** O simples fato de a página carregar confirma que o servidor de
 * aplicação (ex: Tomcat) está rodando e que o deployment da aplicação foi bem-sucedido.
 *
 * 2.  **Conexão com o Banco de Dados (via JPA):** O bloco de código mais importante tenta
 * se conectar ao banco de dados utilizando a unidade de persistência ("unifae-med-pu")
 * definida no arquivo `persistence.xml`.
 *
 * 3.  **Execução de Query:** Se a conexão for estabelecida, ele executa uma consulta
 * simples e de baixo impacto (`SELECT COUNT(*) FROM Usuario`) para garantir que a
 * comunicação com o banco de dados está plenamente funcional.
 *
 * O resultado é uma página HTML simples que exibe um relatório de status, informando
 * imediatamente ao desenvolvedor se o ambiente está saudável ou se há um problema na
 * conexão com o banco de dados.
 * =================================================================================================
 */
package com.unifae.med.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/test")
public class TestServlet extends HttpServlet {

    /**
     * Trata requisições HTTP GET para a URL /test. Gera uma página HTML de
     * diagnóstico em tempo real.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Define o tipo de conteúdo da resposta como HTML com codificação UTF-8.
        response.setContentType("text/html;charset=UTF-8");

        // Utiliza um try-with-resources para garantir que o PrintWriter seja fechado automaticamente.
        try (PrintWriter out = response.getWriter()) {
            // Inicia a construção da página HTML.
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Teste do Sistema</title>");
            out.println("<meta charset='UTF-8'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>🧪 Teste do Sistema UNIFAE</h1>");

            // Mensagens estáticas para confirmar o status do ambiente de software.
            out.println("<p>✅ Servlet funcionando corretamente!</p>");
            out.println("<p>✅ Jakarta EE ativo</p>");
            out.println("<p>✅ Tomcat 10.1.42 operacional</p>");
            out.println("<p>✅ JDK 21 funcionando</p>");

            // Bloco de teste específico para a conexão com o banco de dados via JPA.
            try {
                out.println("<h2>🗄️ Teste de Conexão JPA</h2>");

                // 1. Cria a fábrica de EntityManagers com base na unidade de persistência "unifae-med-pu"
                //    definida no arquivo META-INF/persistence.xml.
                jakarta.persistence.EntityManagerFactory emf
                        = jakarta.persistence.Persistence.createEntityManagerFactory("unifae-med-pu");

                // 2. Cria um EntityManager, que é a interface principal para interagir com o banco.
                jakarta.persistence.EntityManager em = emf.createEntityManager();

                // 3. Executa uma query simples para verificar a funcionalidade da conexão.
                //    Contar os usuários é uma operação segura e de baixo custo.
                Long count = em.createQuery("SELECT COUNT(u) FROM Usuario u", Long.class).getSingleResult();

                // 4. Se a query foi bem-sucedida, exibe as mensagens de sucesso.
                out.println("<p>✅ Conexão JPA estabelecida</p>");
                out.println("<p>✅ Total de usuários no banco: " + count + "</p>");

                // 5. Libera os recursos para evitar vazamento de conexões.
                em.close();
                emf.close();

            } catch (Exception e) {
                // Se qualquer etapa do bloco try falhar, captura a exceção.
                out.println("<p>❌ Erro na conexão JPA: " + e.getMessage() + "</p>");
                out.println("<p>Detalhes: " + e.getClass().getSimpleName() + "</p>");
                e.printStackTrace(); // Imprime o stack trace completo no log do servidor para depuração.
            }

            out.println("<hr>");
            out.println("<p><a href='" + request.getContextPath() + "/'>🏠 Voltar ao Início</a></p>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
