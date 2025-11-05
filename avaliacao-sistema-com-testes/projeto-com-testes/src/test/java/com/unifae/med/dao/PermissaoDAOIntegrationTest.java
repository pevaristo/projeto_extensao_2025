package com.unifae.med.dao;

import com.unifae.med.entity.Permissao;
import com.unifae.med.util.BaseIntegrationTest;
import com.unifae.med.util.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes de Integração: PermissaoDAO")
class PermissaoDAOIntegrationTest extends BaseIntegrationTest {

    private PermissaoDAO permissaoDAO;

    @BeforeEach
    void setUpTest() {
        permissaoDAO = new PermissaoDAO();
    }

    @Test
    @DisplayName("save() - Deve criar nova permissão")
    void testSave_NovaPermissao_DeveCriar() {
        // Arrange
        Permissao permissao = TestDataBuilder.criarPermissao();

        // Act
        Permissao salva = permissaoDAO.save(permissao);

        // Assert
        assertThat(salva.getIdPermissao()).isNotNull();
        assertThat(salva.getNomePermissao()).isEqualTo(permissao.getNomePermissao());
        assertThat(salva.getDescricaoPermissao()).isEqualTo("Permite criar e editar avaliações");
    }

    @Test
    @DisplayName("findById() - Deve encontrar permissão existente")
    void testFindById_PermissaoExiste_DeveRetornar() {
        // Arrange
        Permissao permissao = TestDataBuilder.criarPermissao();
        Permissao salva = permissaoDAO.save(permissao);
        Integer id = salva.getIdPermissao();

        // Act
        Optional<Permissao> resultado = permissaoDAO.findById(id);

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNomePermissao()).isEqualTo(permissao.getNomePermissao());
    }

    @Test
    @DisplayName("findAll() - Deve retornar todas as permissões")
    void testFindAll_DeveRetornarTodas() {
        // Arrange
        Permissao p1 = TestDataBuilder.criarPermissao();
        permissaoDAO.save(p1);

        Permissao p2 = TestDataBuilder.criarPermissao();
        p2.setNomePermissao("Gerenciar Usuários " + System.currentTimeMillis());
        permissaoDAO.save(p2);

        // Act
        List<Permissao> permissoes = permissaoDAO.findAll();

        // Assert
        assertThat(permissoes).hasSize(2);
    }

    @Test
    @DisplayName("deleteById() - Deve remover permissão")
    void testDeleteById_DeveRemover() {
        // Arrange
        Permissao permissao = TestDataBuilder.criarPermissao();
        Permissao salva = permissaoDAO.save(permissao);
        Integer id = salva.getIdPermissao();

        // Act
        permissaoDAO.deleteById(id);

        // Assert
        Optional<Permissao> resultado = permissaoDAO.findById(id);
        assertThat(resultado).isEmpty();
    }
}
