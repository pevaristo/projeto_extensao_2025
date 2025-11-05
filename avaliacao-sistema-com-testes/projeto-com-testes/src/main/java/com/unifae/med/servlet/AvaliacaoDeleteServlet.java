/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `AvaliacaoDeleteServlet`, é um Servlet Java com uma finalidade única e
 * muito específica: gerenciar a exclusão de uma `AvaliacaoPreenchida` e todos os seus
 * dados associados.
 *
 * Diferente de um servlet de CRUD completo, este é focado apenas na operação de "Delete".
 * Sua existência se justifica pela necessidade de realizar uma exclusão em cascata de
 * forma manual e controlada, garantindo a integridade referencial do banco de dados.
 *
 * O fluxo de operação é o seguinte:
 * 1.  **Recebe a Requisição:** É mapeado para a URL `/avaliacao/delete` e espera um
 * parâmetro `id` que identifica a avaliação a ser excluída.
 * 2.  **Orquestra a Exclusão:** Para evitar erros de chave estrangeira (foreign key
 * constraint violation), o servlet primeiro utiliza o `RespostaItemAvaliacaoDAO` para
 * deletar todas as "respostas" (registros filhos) que pertencem à avaliação.
 * 3.  **Deleta o Registro Principal:** Somente após a exclusão bem-sucedida dos registros
 * filhos, ele utiliza o `AvaliacaoPreenchidaDAO` para deletar o registro "pai"
 * (a avaliação em si).
 * 4.  **Redireciona o Usuário:** Após a conclusão, o usuário é redirecionado para a
 * página de listagem de avaliações com uma mensagem de sucesso ou erro.
 *
 * Notavelmente, ele implementa a mesma lógica tanto para `doGet` quanto para `doPost`,
 * o que permite que a exclusão seja acionada tanto por um link (GET) quanto por um
 * formulário (POST).
 * =================================================================================================
 */
package com.unifae.med.servlet;

import com.unifae.med.dao.AvaliacaoPreenchidaDAO;
import com.unifae.med.dao.RespostaItemAvaliacaoDAO;
import com.unifae.med.entity.AvaliacaoPreenchida;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/avaliacao/delete")
public class AvaliacaoDeleteServlet extends HttpServlet {

    private AvaliacaoPreenchidaDAO avaliacaoDAO;
    private RespostaItemAvaliacaoDAO respostaDAO;

    /**
     * Método de inicialização do Servlet. Instancia os DAOs necessários para a
     * operação de exclusão em cascata.
     */
    @Override
    public void init() throws ServletException {
        super.init();
        this.avaliacaoDAO = new AvaliacaoPreenchidaDAO();
        this.respostaDAO = new RespostaItemAvaliacaoDAO();
    }

    /**
     * Trata requisições HTTP POST para deletar uma avaliação. É o método
     * preferencial para operações destrutivas.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // A lógica de deleção é centralizada em um método para ser reutilizada.
        deleteAvaliacaoAndRedirect(request, response);
    }

    /**
     * Trata requisições HTTP GET para deletar uma avaliação. Permite a exclusão
     * através de um link, por exemplo.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Embora POST seja o ideal para exclusão, GET é tratado para flexibilidade.
        deleteAvaliacaoAndRedirect(request, response);
    }

    /**
     * Método privado que encapsula a lógica de exclusão para ser chamado por
     * doGet e doPost.
     *
     * @param request O objeto de requisição.
     * @param response O objeto de resposta.
     * @throws IOException Em caso de erro de redirecionamento.
     */
    private void deleteAvaliacaoAndRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // 1. Pega o ID da avaliação a ser excluída a partir dos parâmetros da requisição.
            String avaliacaoId = request.getParameter("id");

            // 2. Valida se o ID foi fornecido.
            if (avaliacaoId == null || avaliacaoId.isEmpty()) {
                throw new RuntimeException("ID da avaliação não fornecido");
            }

            // 3. Busca a entidade AvaliacaoPreenchida no banco de dados. Lança exceção se não encontrar.
            AvaliacaoPreenchida avaliacao = avaliacaoDAO.findById(Integer.parseInt(avaliacaoId))
                    .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

            // 4. PASSO CRÍTICO: Deleta todos os registros filhos (respostas) primeiro.
            // Isso é necessário para satisfazer as restrições de chave estrangeira do banco.
            respostaDAO.deleteByAvaliacaoPreenchida(avaliacao);

            // 5. Após os filhos serem removidos, deleta o registro pai (a avaliação).
            avaliacaoDAO.delete(avaliacao);

            // 6. Redireciona o usuário para a página de listagem com um parâmetro de sucesso.
            response.sendRedirect(request.getContextPath() + "/avaliacoes?deleted=true");

        } catch (Exception e) {
            // 7. Em caso de qualquer erro, imprime o stack trace no log do servidor.
            e.printStackTrace();
            // E redireciona o usuário para a página de listagem com uma mensagem de erro na URL.
            response.sendRedirect(request.getContextPath() + "/avaliacoes?error=" + e.getMessage());
        }
    }
}
