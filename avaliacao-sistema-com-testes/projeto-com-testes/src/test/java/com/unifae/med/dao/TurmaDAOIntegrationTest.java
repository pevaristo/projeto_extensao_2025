package com.unifae.med.dao;

import com.unifae.med.entity.Turma;
import com.unifae.med.util.BaseIntegrationTest;
import com.unifae.med.util.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de integração para TurmaDAO.
 */
@DisplayName("Testes de Integração: TurmaDAO")
class TurmaDAOIntegrationTest extends BaseIntegrationTest {

    private TurmaDAO turmaDAO;

    @BeforeEach
    void setUpTest() {
        turmaDAO = new TurmaDAO();
    }

    @Test
    @DisplayName("save() - Deve criar nova turma")
    void testSave_NovaTurma_DeveCriar() {
        // Arrange
        Turma turma = TestDataBuilder.criarTurma();

        // Act
        Turma salva = turmaDAO.save(turma);

        // Assert
        assertThat(salva.getIdTurma()).isNotNull();
        assertThat(salva.getNomeTurma()).isEqualTo("Medicina 2024");
        assertThat(salva.getAnoLetivo()).isEqualTo(2024);
        assertThat(salva.getSemestre()).isEqualTo(1);
    }

    @Test
    @DisplayName("findById() - Deve encontrar turma existente")
    void testFindById_TurmaExiste_DeveRetornar() {
        // Arrange
        Turma turma = TestDataBuilder.criarTurma();
        Turma salva = turmaDAO.save(turma);

        // Act
        Optional<Turma> resultado = turmaDAO.findById(salva.getIdTurma());

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNomeTurma()).isEqualTo("Medicina 2024");
    }

    @Test
    @DisplayName("findAll() - Deve retornar todas as turmas")
    void testFindAll_DeveRetornarTodas() {
        // Arrange
        Turma t1 = TestDataBuilder.criarTurma();
        Turma t2 = TestDataBuilder.criarTurma();
        t2.setNomeTurma("Medicina 2025");
        t2.setAnoLetivo(2025);
        
        turmaDAO.save(t1);
        turmaDAO.save(t2);

        // Act
        List<Turma> turmas = turmaDAO.findAll();

        // Assert
        assertThat(turmas).hasSize(2);
    }

    @Test
    @DisplayName("deleteById() - Deve remover turma")
    void testDeleteById_DeveRemover() {
        // Arrange
        Turma turma = TestDataBuilder.criarTurma();
        Turma salva = turmaDAO.save(turma);

        // Act
        turmaDAO.deleteById(salva.getIdTurma());

        // Assert
        Optional<Turma> resultado = turmaDAO.findById(salva.getIdTurma());
        assertThat(resultado).isEmpty();
    }
}
