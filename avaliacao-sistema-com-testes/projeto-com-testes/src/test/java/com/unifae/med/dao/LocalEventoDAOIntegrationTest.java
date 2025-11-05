package com.unifae.med.dao;

import com.unifae.med.entity.LocalEvento;
import com.unifae.med.util.BaseIntegrationTest;
import com.unifae.med.util.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de integração para LocalEventoDAO.
 */
@DisplayName("Testes de Integração: LocalEventoDAO")
class LocalEventoDAOIntegrationTest extends BaseIntegrationTest {

    private LocalEventoDAO localEventoDAO;

    @BeforeEach
    void setUpTest() {
        localEventoDAO = new LocalEventoDAO();
    }

    @Test
    @DisplayName("save() - Deve criar novo local de evento")
    void testSave_NovoLocal_DeveCriar() {
        // Arrange
        LocalEvento local = TestDataBuilder.criarLocalEvento();

        // Act
        LocalEvento salvo = localEventoDAO.save(local);

        // Assert
        // Assert
        assertThat(salvo.getIdLocalEvento()).isNotNull();
        assertThat(salvo.getNomeLocal()).isEqualTo("Hospital Universitário");
        assertThat(salvo.getTipoLocal()).isEqualTo("Hospital");
        assertThat(salvo.getCidade()).isEqualTo("Londrina");
        assertThat(salvo.getEstado()).isEqualTo("PR");
    }

    @Test
    @DisplayName("findById() - Deve encontrar local existente")
    void testFindById_LocalExiste_DeveRetornar() {
        // Arrange
        LocalEvento local = TestDataBuilder.criarLocalEvento();
        LocalEvento salvo = localEventoDAO.save(local);

        // Act
        Optional<LocalEvento> resultado = localEventoDAO.findById(salvo.getIdLocalEvento());


        
        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNomeLocal()).isEqualTo("Hospital Universitário");
    }

    @Test
    @DisplayName("findAll() - Deve retornar todos os locais")
    void testFindAll_DeveRetornarTodos() {
        // Arrange
        LocalEvento l1 = TestDataBuilder.criarLocalEvento();
        LocalEvento l2 = TestDataBuilder.criarLocalEvento();
        l2.setNomeLocal("Clínica Escola");
        
        localEventoDAO.save(l1);
        localEventoDAO.save(l2);

        // Act
        List<LocalEvento> locais = localEventoDAO.findAll();

        // Assert
        assertThat(locais).hasSize(2);
    }

    @Test
    @DisplayName("deleteById() - Deve remover local")
    void testDeleteById_DeveRemover() {
        // Arrange
        LocalEvento local = TestDataBuilder.criarLocalEvento();
        LocalEvento salvo = localEventoDAO.save(local);

        // Act
        localEventoDAO.deleteById(salvo.getIdLocalEvento());

        // Assert
        Optional<LocalEvento> resultado = localEventoDAO.findById(salvo.getIdLocalEvento());
        assertThat(resultado).isEmpty();
    }
}
