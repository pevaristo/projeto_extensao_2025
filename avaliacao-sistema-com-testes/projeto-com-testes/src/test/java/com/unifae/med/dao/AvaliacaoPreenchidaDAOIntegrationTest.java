package com.unifae.med.dao;

import com.unifae.med.entity.AvaliacaoPreenchida;
import com.unifae.med.entity.Questionario;
import com.unifae.med.entity.Usuario;
import com.unifae.med.util.BaseIntegrationTest;
import com.unifae.med.util.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes de Integração: AvaliacaoPreenchidaDAO")
class AvaliacaoPreenchidaDAOIntegrationTest extends BaseIntegrationTest {

    private AvaliacaoPreenchidaDAO avaliacaoDAO;
    private Usuario avaliador;
    private Usuario avaliado;
    private Questionario questionario;

    @BeforeEach
    void setUpTest() {
        avaliacaoDAO = new AvaliacaoPreenchidaDAO();

        // CORREÇÃO: Persiste as dependências ANTES de cada teste
        // Isso garante que elas existam no banco quando o DAO for chamado
        executeInTransaction(() -> {
            avaliador = TestDataBuilder.criarUsuarioProfessor();
            avaliado = TestDataBuilder.criarUsuarioEstudante();
            questionario = TestDataBuilder.criarQuestionario();

            em.persist(avaliador);
            em.persist(avaliado);
            em.persist(questionario);
            em.flush(); // Força sincronização com o banco
        });
    }

    @Test
    @DisplayName("save() - Deve criar nova avaliação preenchida")
    void testSave_NovaAvaliacao_DeveCriar() {
        // Arrange
        AvaliacaoPreenchida avaliacao = TestDataBuilder.criarAvaliacaoPreenchida(questionario, avaliador, avaliado);

        // Act
        AvaliacaoPreenchida salva = avaliacaoDAO.save(avaliacao);

        // Assert
        assertThat(salva.getIdAvaliacaoPreenchida()).isNotNull();
        assertThat(salva.getAvaliador().getIdUsuario()).isEqualTo(avaliador.getIdUsuario());
        assertThat(salva.getAlunoAvaliado().getIdUsuario()).isEqualTo(avaliado.getIdUsuario());
    }

    @Test
    @DisplayName("findById() - Deve encontrar avaliação existente")
    void testFindById_AvaliacaoExiste_DeveRetornar() {
        // Arrange
        AvaliacaoPreenchida avaliacao = TestDataBuilder.criarAvaliacaoPreenchida(questionario, avaliador, avaliado);
        AvaliacaoPreenchida salva = avaliacaoDAO.save(avaliacao);
        Integer idSalvo = salva.getIdAvaliacaoPreenchida();

        // Act
        Optional<AvaliacaoPreenchida> resultado = avaliacaoDAO.findById(idSalvo);

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getIdAvaliacaoPreenchida()).isEqualTo(idSalvo);
    }

    @Test
    @DisplayName("findAll() - Deve retornar todas as avaliações")
    void testFindAll_DeveRetornarTodas() {
        // Arrange
        AvaliacaoPreenchida a1 = TestDataBuilder.criarAvaliacaoPreenchida(questionario, avaliador, avaliado);
        avaliacaoDAO.save(a1);

        // Act
        List<AvaliacaoPreenchida> avaliacoes = avaliacaoDAO.findAll();

        // Assert
        assertThat(avaliacoes).hasSize(1);
    }

    @Test
    @DisplayName("deleteById() - Deve remover avaliação")
    void testDeleteById_DeveRemover() {
        // Arrange
        AvaliacaoPreenchida avaliacao = TestDataBuilder.criarAvaliacaoPreenchida(questionario, avaliador, avaliado);
        AvaliacaoPreenchida salva = avaliacaoDAO.save(avaliacao);
        Integer id = salva.getIdAvaliacaoPreenchida();

        // Act
        avaliacaoDAO.deleteById(id);

        // Assert
        Optional<AvaliacaoPreenchida> resultado = avaliacaoDAO.findById(id);
        assertThat(resultado).isEmpty();
    }
}