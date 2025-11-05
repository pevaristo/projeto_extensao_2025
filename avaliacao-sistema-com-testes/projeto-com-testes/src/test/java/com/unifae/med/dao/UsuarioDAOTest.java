package com.unifae.med.dao;

import com.unifae.med.entity.TipoUsuario;
import com.unifae.med.entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para UsuarioDAO usando Mockito.
 * 
 * Estes testes validam a lógica do DAO sem necessidade de banco de dados,
 * mockando o EntityManager e suas dependências.
 * 
 * Execute com: mvn test -Dtest="UsuarioDAOTest"
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários: UsuarioDAO")
class UsuarioDAOTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Usuario> typedQuery;

    @Mock
    private TypedQuery<Long> longQuery;

    private UsuarioDAO usuarioDAO;

    @BeforeEach
    void setUp() {
        // Criar UsuarioDAO com EntityManager mockado
        usuarioDAO = new UsuarioDAO() {
            @Override
            protected EntityManager getEntityManager() {
                return entityManager;
            }
        };
    }

    // ========================================
    // TESTES DO MÉTODO FINDBYEMAIL
    // ========================================

    @Test
    @DisplayName("findByEmail() - Deve retornar usuário quando email existe")
    void testFindByEmail_QuandoUsuarioExiste_DeveRetornarUsuario() {
        // Arrange
        String email = "joao.silva@unifae.edu.br";
        Usuario usuarioEsperado = new Usuario();
        usuarioEsperado.setIdUsuario(1);
        usuarioEsperado.setEmail(email);
        usuarioEsperado.setNomeCompleto("João Silva");

        when(entityManager.createQuery(anyString(), eq(Usuario.class)))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("email"), eq(email)))
                .thenReturn(typedQuery);
        when(typedQuery.getSingleResult())
                .thenReturn(usuarioEsperado);

        // Act
        Optional<Usuario> resultado = usuarioDAO.findByEmail(email);

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEmail()).isEqualTo(email);
        assertThat(resultado.get().getNomeCompleto()).isEqualTo("João Silva");

        verify(entityManager).createQuery(anyString(), eq(Usuario.class));
        verify(typedQuery).setParameter("email", email);
        verify(entityManager).close();
    }

    @Test
    @DisplayName("findByEmail() - Deve retornar empty quando email não existe")
    void testFindByEmail_QuandoUsuarioNaoExiste_DeveRetornarEmpty() {
        // Arrange
        String email = "inexistente@unifae.edu.br";

        when(entityManager.createQuery(anyString(), eq(Usuario.class)))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("email"), eq(email)))
                .thenReturn(typedQuery);
        when(typedQuery.getSingleResult())
                .thenThrow(new NoResultException());

        // Act
        Optional<Usuario> resultado = usuarioDAO.findByEmail(email);

        // Assert
        assertThat(resultado).isEmpty();
        verify(entityManager).close();
    }

    // ========================================
    // TESTES DO MÉTODO EXISTSBYEMAIL
    // ========================================

    @Test
    @DisplayName("existsByEmail() - Deve retornar true quando email existe")
    void testExistsByEmail_EmailExiste_DeveRetornarTrue() {
        // Arrange
        String email = "joao.silva@unifae.edu.br";

        when(entityManager.createQuery(anyString(), eq(Long.class)))
                .thenReturn(longQuery);
        when(longQuery.setParameter(eq("email"), eq(email)))
                .thenReturn(longQuery);
        when(longQuery.getSingleResult())
                .thenReturn(1L);

        // Act
        boolean existe = usuarioDAO.existsByEmail(email);

        // Assert
        assertThat(existe).isTrue();
        verify(entityManager).createQuery(anyString(), eq(Long.class));
        verify(longQuery).setParameter("email", email);
        verify(entityManager).close();
    }

    @Test
    @DisplayName("existsByEmail() - Deve retornar false quando email não existe")
    void testExistsByEmail_EmailNaoExiste_DeveRetornarFalse() {
        // Arrange
        String email = "inexistente@unifae.edu.br";

        when(entityManager.createQuery(anyString(), eq(Long.class)))
                .thenReturn(longQuery);
        when(longQuery.setParameter(eq("email"), eq(email)))
                .thenReturn(longQuery);
        when(longQuery.getSingleResult())
                .thenReturn(0L);

        // Act
        boolean existe = usuarioDAO.existsByEmail(email);

        // Assert
        assertThat(existe).isFalse();
        verify(entityManager).close();
    }

    // ========================================
    // TESTES DO MÉTODO FINDBYMATRICULARA
    // ========================================

    @Test
    @DisplayName("findByMatriculaRA() - Deve retornar usuário quando matrícula existe")
    void testFindByMatriculaRA_QuandoMatriculaExiste_DeveRetornarUsuario() {
        // Arrange
        String matricula = "123456";
        Usuario usuarioEsperado = new Usuario();
        usuarioEsperado.setIdUsuario(1);
        usuarioEsperado.setMatriculaRA(matricula);
        usuarioEsperado.setNomeCompleto("João Silva");

        when(entityManager.createQuery(anyString(), eq(Usuario.class)))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("matriculaRA"), eq(matricula)))
                .thenReturn(typedQuery);
        when(typedQuery.getSingleResult())
                .thenReturn(usuarioEsperado);

        // Act
        Optional<Usuario> resultado = usuarioDAO.findByMatriculaRA(matricula);

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getMatriculaRA()).isEqualTo(matricula);
        verify(entityManager).close();
    }

    @Test
    @DisplayName("findByMatriculaRA() - Deve retornar empty quando matrícula não existe")
    void testFindByMatriculaRA_QuandoMatriculaNaoExiste_DeveRetornarEmpty() {
        // Arrange
        String matricula = "999999";

        when(entityManager.createQuery(anyString(), eq(Usuario.class)))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("matriculaRA"), eq(matricula)))
                .thenReturn(typedQuery);
        when(typedQuery.getSingleResult())
                .thenThrow(new NoResultException());

        // Act
        Optional<Usuario> resultado = usuarioDAO.findByMatriculaRA(matricula);

        // Assert
        assertThat(resultado).isEmpty();
        verify(entityManager).close();
    }

    // ========================================
    // TESTES DO MÉTODO EXISTSBYMATRICULARA
    // ========================================

    @Test
    @DisplayName("existsByMatriculaRA() - Deve retornar true quando matrícula existe")
    void testExistsByMatriculaRA_MatriculaExiste_DeveRetornarTrue() {
        // Arrange
        String matricula = "123456";

        when(entityManager.createQuery(anyString(), eq(Long.class)))
                .thenReturn(longQuery);
        when(longQuery.setParameter(eq("matriculaRA"), eq(matricula)))
                .thenReturn(longQuery);
        when(longQuery.getSingleResult())
                .thenReturn(1L);

        // Act
        boolean existe = usuarioDAO.existsByMatriculaRA(matricula);

        // Assert
        assertThat(existe).isTrue();
        verify(entityManager).close();
    }

    @Test
    @DisplayName("existsByMatriculaRA() - Deve retornar false quando matrícula não existe")
    void testExistsByMatriculaRA_MatriculaNaoExiste_DeveRetornarFalse() {
        // Arrange
        String matricula = "999999";

        when(entityManager.createQuery(anyString(), eq(Long.class)))
                .thenReturn(longQuery);
        when(longQuery.setParameter(eq("matriculaRA"), eq(matricula)))
                .thenReturn(longQuery);
        when(longQuery.getSingleResult())
                .thenReturn(0L);

        // Act
        boolean existe = usuarioDAO.existsByMatriculaRA(matricula);

        // Assert
        assertThat(existe).isFalse();
        verify(entityManager).close();
    }
}
