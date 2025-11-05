package com.unifae.med.dao;

import com.unifae.med.entity.Questionario;
import com.unifae.med.util.BaseIntegrationTest;
import com.unifae.med.util.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Testes de integração para QuestionarioDAO.
 */
@DisplayName("Testes de Integração: QuestionarioDAO")
class QuestionarioDAOIntegrationTest extends BaseIntegrationTest {

    private QuestionarioDAO questionarioDAO;

    @BeforeEach
    void setUpTest() {
        questionarioDAO = new QuestionarioDAO();
    }

    @Test
    @DisplayName("save() - Deve criar novo questionário")
    void testSave_NovoQuestionario_DeveCriar() {
        // Arrange
        Questionario questionario = TestDataBuilder.criarQuestionario();

        // Act
        Questionario salvo = questionarioDAO.save(questionario);

        // Assert
        assertThat(salvo.getIdQuestionario()).isNotNull();
        assertThat(salvo.getNomeModelo()).isEqualTo("Mini CEX");
        assertThat(salvo.getDescricao()).isEqualTo("Avaliação de competências clínicas");
    }

    @Test
    @DisplayName("save() - Deve atualizar questionário existente")
    void testSave_QuestionarioExistente_DeveAtualizar() {
        // Arrange
        Questionario questionario = TestDataBuilder.criarQuestionario();
        Questionario salvo = questionarioDAO.save(questionario);
        salvo.setNomeModelo("Mini CEX Atualizado");

        // Act
        Questionario atualizado = questionarioDAO.save(salvo);

        // Assert
        assertThat(atualizado.getIdQuestionario()).isEqualTo(salvo.getIdQuestionario());
        assertThat(atualizado.getNomeModelo()).isEqualTo("Mini CEX Atualizado");
    }

    @Test
    @DisplayName("findById() - Deve encontrar questionário existente")
    void testFindById_QuestionarioExiste_DeveRetornar() {
        // Arrange
        Questionario questionario = TestDataBuilder.criarQuestionario();
        Questionario salvo = questionarioDAO.save(questionario);

        // Act
        Optional<Questionario> resultado = questionarioDAO.findById(salvo.getIdQuestionario());

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNomeModelo()).isEqualTo("Mini CEX");
    }

    @Test
    @DisplayName("findById() - Deve retornar empty para ID inexistente")
    void testFindById_IdInexistente_DeveRetornarEmpty() {
        // Act
        Optional<Questionario> resultado = questionarioDAO.findById(99999);

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("findAll() - Deve retornar todos os questionários")
    void testFindAll_DeveRetornarTodos() {
        // Arrange
        Questionario q1 = TestDataBuilder.criarQuestionario();
        Questionario q2 = TestDataBuilder.criarQuestionario();
        q2.setNomeModelo("Avaliação 360°");
        
        questionarioDAO.save(q1);
        questionarioDAO.save(q2);

        // Act
        List<Questionario> questionarios = questionarioDAO.findAll();

        // Assert
        assertThat(questionarios).hasSize(2);
    }

    @Test
    @DisplayName("deleteById() - Deve remover questionário")
    void testDeleteById_DeveRemover() {
        // Arrange
        Questionario questionario = TestDataBuilder.criarQuestionario();
        Questionario salvo = questionarioDAO.save(questionario);

        // Act
        questionarioDAO.deleteById(salvo.getIdQuestionario());

        // Assert
        Optional<Questionario> resultado = questionarioDAO.findById(salvo.getIdQuestionario());
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("count() - Deve retornar número correto de questionários")
    void testCount_DeveRetornarNumeroCorreto() {
        // Arrange
        questionarioDAO.save(TestDataBuilder.criarQuestionario());

        // Act
        long count = questionarioDAO.count();

        // Assert
        assertThat(count).isEqualTo(1);
    }
}
