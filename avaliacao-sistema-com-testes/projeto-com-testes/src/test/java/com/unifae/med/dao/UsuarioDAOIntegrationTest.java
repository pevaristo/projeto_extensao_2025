package com.unifae.med.dao;

import com.unifae.med.entity.Permissao;
import com.unifae.med.entity.TipoUsuario;
import com.unifae.med.entity.Usuario;
import com.unifae.med.util.BaseIntegrationTest;
import com.unifae.med.util.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes de Integração: UsuarioDAO")
class UsuarioDAOIntegrationTest extends BaseIntegrationTest {

    private UsuarioDAO usuarioDAO;
    private Permissao permissao;

    @BeforeEach
    void setUpTest() {
        usuarioDAO = new UsuarioDAO();

        // CORREÇÃO: Persiste Permissao ANTES de criar Usuarios
        // Usuario depende de Permissao (FK)
        executeInTransaction(() -> {
            permissao = TestDataBuilder.criarPermissao();
            em.persist(permissao);
            em.flush();
        });
    }

    @Test
    @DisplayName("save() - Deve criar novo usuário com ID gerado")
    void testSave_NovoUsuario_DeveCriarComIdGerado() {
        // Arrange
        Usuario usuario = TestDataBuilder.criarUsuarioEstudante();
        usuario.setPermissao(permissao);

        // Act
        Usuario usuarioSalvo = usuarioDAO.save(usuario);

        // Assert
        assertThat(usuarioSalvo.getIdUsuario()).isNotNull();
        assertThat(usuarioSalvo.getNomeCompleto()).isEqualTo("João da Silva");
        assertThat(usuarioSalvo.getEmail()).isEqualTo(usuario.getEmail());
        assertThat(usuarioSalvo.getTipoUsuario()).isEqualTo(TipoUsuario.ESTUDANTE);
        assertThat(usuarioSalvo.getAtivo()).isTrue();
    }

    @Test
    @DisplayName("save() - Deve atualizar usuário existente")
    void testSave_UsuarioExistente_DeveAtualizar() {
        // Arrange
        Usuario usuario = TestDataBuilder.criarUsuarioEstudante();
        usuario.setPermissao(permissao);
        Usuario salvo = usuarioDAO.save(usuario);

        // Modificar dados
        salvo.setNomeCompleto("João da Silva Atualizado");
        salvo.setTelefone("43988776655");

        // Act
        Usuario usuarioAtualizado = usuarioDAO.save(salvo);

        // Assert
        assertThat(usuarioAtualizado.getIdUsuario()).isEqualTo(salvo.getIdUsuario());
        assertThat(usuarioAtualizado.getNomeCompleto()).isEqualTo("João da Silva Atualizado");
        assertThat(usuarioAtualizado.getTelefone()).isEqualTo("43988776655");
    }

    @Test
    @DisplayName("findById() - Deve encontrar usuário existente")
    void testFindById_UsuarioExiste_DeveRetornarUsuario() {
        // Arrange
        Usuario usuario = TestDataBuilder.criarUsuarioEstudante();
        usuario.setPermissao(permissao);
        Usuario salvo = usuarioDAO.save(usuario);
        Integer id = salvo.getIdUsuario();

        // Act
        Optional<Usuario> resultado = usuarioDAO.findById(id);

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getIdUsuario()).isEqualTo(id);
        assertThat(resultado.get().getEmail()).isEqualTo(usuario.getEmail());
    }

    @Test
    @DisplayName("deleteById() - Deve remover usuário existente")
    void testDeleteById_UsuarioExiste_DeveRemover() {
        // Arrange
        Usuario usuario = TestDataBuilder.criarUsuarioEstudante();
        usuario.setPermissao(permissao);
        Usuario salvo = usuarioDAO.save(usuario);
        Integer id = salvo.getIdUsuario();

        // Act
        usuarioDAO.deleteById(id);

        // Assert
        Optional<Usuario> resultado = usuarioDAO.findById(id);
        assertThat(resultado).isEmpty();
    }
}