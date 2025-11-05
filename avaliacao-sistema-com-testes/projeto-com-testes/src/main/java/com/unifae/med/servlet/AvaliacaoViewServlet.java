/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `AvaliacaoViewServlet`, é um Servlet Java com uma responsabilidade única e
 * bem definida: atuar como um "Controller" para a visualização (leitura) de uma
 * avaliação que já foi preenchida.
 *
 * Mapeado para a URL `/avaliacao/view`, seu único propósito é receber o ID de uma
 * avaliação, buscar todos os dados relacionados a ela no banco de dados e prepará-los
 * para serem exibidos em uma página JSP.
 *
 * O fluxo de operação é direto e segue o padrão MVC:
 * 1.  **Recebe a Requisição GET:** O servlet só responde a requisições `doGet`, pois a
 * visualização é uma operação de leitura, não de modificação de dados.
 * 2.  **Busca os Dados (Modelo):** Ele utiliza dois DAOs:
 * - `AvaliacaoPreenchidaDAO`: Para buscar o registro principal da avaliação (o
 * "cabeçalho", com informações do aluno, avaliador, data, etc.).
 * - `RespostaItemAvaliacaoDAO`: Para buscar a lista de todas as respostas
 * individuais (os "itens") associadas a essa avaliação.
 * 3.  **Prepara a Visão:** Os objetos encontrados (a avaliação e a lista de respostas)
 * são colocados como atributos no objeto `request`.
 * 4.  **Encaminha para a Visão (JSP):** A requisição é encaminhada para o arquivo
 * `view.jsp`, que é responsável por ler esses atributos e renderizar a página HTML
 * com todos os detalhes da avaliação para o usuário final.
 * =================================================================================================
 */
package com.unifae.med.servlet;

import com.unifae.med.dao.AvaliacaoPreenchidaDAO;
import com.unifae.med.dao.RespostaItemAvaliacaoDAO;
import com.unifae.med.entity.AvaliacaoPreenchida;
import com.unifae.med.entity.RespostaItemAvaliacao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/avaliacao/view")
public class AvaliacaoViewServlet extends HttpServlet {

    private AvaliacaoPreenchidaDAO avaliacaoDAO;
    private RespostaItemAvaliacaoDAO respostaDAO;

    /**
     * Método de inicialização do Servlet. Instancia os DAOs necessários para
     * buscar os dados da avaliação e suas respectivas respostas.
     */
    @Override
    public void init() throws ServletException {
        super.init();
        this.avaliacaoDAO = new AvaliacaoPreenchidaDAO();
        this.respostaDAO = new RespostaItemAvaliacaoDAO();
    }

    /**
     * Trata requisições HTTP GET. Este é o único método funcional do servlet,
     * pois sua única tarefa é exibir dados.
     *
     * @param request O objeto de requisição, de onde o ID da avaliação é
     * extraído.
     * @param response O objeto de resposta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 1. Obtém o ID da avaliação a ser visualizada a partir dos parâmetros da URL.
            String avaliacaoId = request.getParameter("id");

            // 2. Valida se o ID foi realmente fornecido.
            if (avaliacaoId == null || avaliacaoId.isEmpty()) {
                throw new RuntimeException("ID da avaliação não fornecido");
            }

            // 3. Busca o objeto principal da avaliação no banco de dados.
            // Se não encontrar, uma exceção será lançada.
            AvaliacaoPreenchida avaliacao = avaliacaoDAO.findById(Integer.parseInt(avaliacaoId))
                    .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

            // 4. Busca a lista de todas as respostas associadas a esta avaliação.
            List<RespostaItemAvaliacao> respostas = respostaDAO.findByAvaliacaoPreenchida(avaliacao);

            // 5. Adiciona a avaliação e suas respostas como atributos na requisição.
            // O JSP poderá acessá-los usando, por exemplo, ${avaliacao} e ${respostas}.
            request.setAttribute("avaliacao", avaliacao);
            request.setAttribute("respostas", respostas);

            // 6. Encaminha a requisição (com os dados) para o arquivo JSP responsável pela renderização.
            request.getRequestDispatcher("/WEB-INF/views/avaliacoes/view.jsp").forward(request, response);

        } catch (Exception e) {
            // 7. Em caso de erro (ex: ID inválido, avaliação não encontrada), encaminha para uma página de erro genérica.
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao carregar avaliação: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}
