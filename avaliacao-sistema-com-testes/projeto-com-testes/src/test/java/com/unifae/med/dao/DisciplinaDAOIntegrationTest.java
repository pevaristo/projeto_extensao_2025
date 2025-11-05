package com.unifae.med.dao;

import com.unifae.med.entity.Disciplina;
import com.unifae.med.util.BaseIntegrationTest;
import com.unifae.med.util.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes de Integração: DisciplinaDAO")
class DisciplinaDAOIntegrationTest extends BaseIntegrationTest {

    private DisciplinaDAO disciplinaDAO;

    @BeforeEach
    void setUpTest() {
        disciplinaDAO = new DisciplinaDAO();
    }

    @Test
    @DisplayName("save() - Deve criar nova disciplina")
    void testSave_NovaDisciplina_DeveCriar() {
        // Arrange
        Disciplina disciplina = TestDataBuilder.criarDisciplina();

        // Act
        Disciplina salva = disciplinaDAO.save(disciplina);

        // Assert
        assertThat(salva.getIdDisciplina()).isNotNull();
        assertThat(salva.getNomeDisciplina()).isEqualTo("Clínica Médica");
        assertThat(salva.getCodigoDisciplina()).isEqualTo(disciplina.getCodigoDisciplina());
        assertThat(salva.getSiglaDisciplina()).isEqualTo("CLINMED");
        assertThat(salva.getAtiva()).isTrue();
    }

    @Test
    @DisplayName("findById() - Deve encontrar disciplina existente")
    void testFindById_DisciplinaExiste_DeveRetornar() {
        // Arrange
        Disciplina disciplina = TestDataBuilder.criarDisciplina();
        Disciplina salva = disciplinaDAO.save(disciplina);
        Integer id = salva.getIdDisciplina();

        // Act
        Optional<Disciplina> resultado = disciplinaDAO.findById(id);

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNomeDisciplina()).isEqualTo("Clínica Médica");
    }

    @Test
    @DisplayName("findAll() - Deve retornar todas as disciplinas")
    void testFindAll_DeveRetornarTodas() {
        // Arrange
        Disciplina d1 = TestDataBuilder.criarDisciplina();
        disciplinaDAO.save(d1);

        Disciplina d2 = TestDataBuilder.criarDisciplina();
        d2.setNomeDisciplina("Cirurgia");
        d2.setSiglaDisciplina("CIRUR");
        disciplinaDAO.save(d2);

        // Act
        List<Disciplina> disciplinas = disciplinaDAO.findAll();

        // Assert
        assertThat(disciplinas).hasSize(2);
    }

    @Test
    @DisplayName("deleteById() - Deve remover disciplina")
    void testDeleteById_DeveRemover() {
        // Arrange
        Disciplina disciplina = TestDataBuilder.criarDisciplina();
        Disciplina salva = disciplinaDAO.save(disciplina);
        Integer id = salva.getIdDisciplina();

        // Act
        disciplinaDAO.deleteById(id);

        // Assert
        Optional<Disciplina> resultado = disciplinaDAO.findById(id);
        assertThat(resultado).isEmpty();
    }
}