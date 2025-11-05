package com.unifae.med.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de validação Bean Validation para entidade Usuario.
 * 
 * Valida as anotações de validação (@NotBlank, @Email, @Size, etc.)
 * definidas na entidade Usuario.
 * 
 * Execute com: mvn test -Dtest="UsuarioValidationTest"
 */
@DisplayName("Testes de Validação: Usuario")
class UsuarioValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // ========================================
    // TESTES DE USUÁRIO VÁLIDO
    // ========================================

    @Test
    @DisplayName("Usuário válido não deve ter violações")
    void testUsuarioValido_NaoDeveTerViolacoes() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto("João Silva");
        usuario.setEmail("joao@unifae.edu.br");
        usuario.setSenhaHash("hash123");
        usuario.setTipoUsuario(TipoUsuario.ESTUDANTE);

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertThat(violations).isEmpty();
    }

    // ========================================
    // TESTES DE NOME COMPLETO
    // ========================================

    @Test
    @DisplayName("Nome completo vazio deve gerar violação")
    void testNomeCompleto_QuandoVazio_DeveTerViolacao() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto("");
        usuario.setEmail("joao@unifae.edu.br");
        usuario.setSenhaHash("hash123");
        usuario.setTipoUsuario(TipoUsuario.ESTUDANTE);

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> 
            v.getPropertyPath().toString().equals("nomeCompleto") &&
            v.getMessage().contains("obrigatório")
        );
    }

    @Test
    @DisplayName("Nome completo null deve gerar violação")
    void testNomeCompleto_QuandoNull_DeveTerViolacao() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto(null);
        usuario.setEmail("joao@unifae.edu.br");
        usuario.setSenhaHash("hash123");
        usuario.setTipoUsuario(TipoUsuario.ESTUDANTE);

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> 
            v.getPropertyPath().toString().equals("nomeCompleto")
        );
    }

    @Test
    @DisplayName("Nome completo excedendo 100 caracteres deve gerar violação")
    void testNomeCompleto_QuandoExcedeTamanho_DeveTerViolacao() {
        // Arrange
        String nomeLongo = "A".repeat(101);
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto(nomeLongo);
        usuario.setEmail("joao@unifae.edu.br");
        usuario.setSenhaHash("hash123");
        usuario.setTipoUsuario(TipoUsuario.ESTUDANTE);

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> 
            v.getPropertyPath().toString().equals("nomeCompleto") &&
            v.getMessage().contains("100 caracteres")
        );
    }

    // ========================================
    // TESTES DE EMAIL
    // ========================================

    @Test
    @DisplayName("Email vazio deve gerar violação")
    void testEmail_QuandoVazio_DeveTerViolacao() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto("João Silva");
        usuario.setEmail("");
        usuario.setSenhaHash("hash123");
        usuario.setTipoUsuario(TipoUsuario.ESTUDANTE);

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("Email com formato inválido deve gerar violação")
    void testEmail_QuandoInvalido_DeveTerViolacao() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto("João Silva");
        usuario.setEmail("email-invalido-sem-arroba");
        usuario.setSenhaHash("hash123");
        usuario.setTipoUsuario(TipoUsuario.ESTUDANTE);

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> 
            v.getPropertyPath().toString().equals("email") &&
            v.getMessage().toLowerCase().contains("email")
        );
    }

    @Test
    @DisplayName("Email excedendo 254 caracteres deve gerar violação")
    void testEmail_QuandoExcedeTamanho_DeveTerViolacao() {
        // Arrange
        String emailLongo = "a".repeat(250) + "@test.com"; // > 254
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto("João Silva");
        usuario.setEmail(emailLongo);
        usuario.setSenhaHash("hash123");
        usuario.setTipoUsuario(TipoUsuario.ESTUDANTE);

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertThat(violations).isNotEmpty();
    }

    // ========================================
    // TESTES DE TELEFONE
    // ========================================

    @Test
    @DisplayName("Telefone excedendo 15 caracteres deve gerar violação")
    void testTelefone_QuandoExcedeTamanho_DeveTerViolacao() {
        // Arrange
        String telefoneLongo = "1".repeat(16);
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto("João Silva");
        usuario.setEmail("joao@unifae.edu.br");
        usuario.setSenhaHash("hash123");
        usuario.setTelefone(telefoneLongo);
        usuario.setTipoUsuario(TipoUsuario.ESTUDANTE);

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> 
            v.getPropertyPath().toString().equals("telefone")
        );
    }

    @Test
    @DisplayName("Telefone null não deve gerar violação (campo opcional)")
    void testTelefone_QuandoNull_NaoDeveTerViolacao() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto("João Silva");
        usuario.setEmail("joao@unifae.edu.br");
        usuario.setSenhaHash("hash123");
        usuario.setTelefone(null);
        usuario.setTipoUsuario(TipoUsuario.ESTUDANTE);

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertThat(violations).isEmpty();
    }

    // ========================================
    // TESTES DE MATRÍCULA/RA
    // ========================================

    @Test
    @DisplayName("MatriculaRA excedendo 6 caracteres deve gerar violação")
    void testMatriculaRA_QuandoExcedeTamanho_DeveTerViolacao() {
        // Arrange
        String matriculaLonga = "1234567"; // > 6
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto("João Silva");
        usuario.setEmail("joao@unifae.edu.br");
        usuario.setSenhaHash("hash123");
        usuario.setMatriculaRA(matriculaLonga);
        usuario.setTipoUsuario(TipoUsuario.ESTUDANTE);

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> 
            v.getPropertyPath().toString().equals("matriculaRA")
        );
    }

    // ========================================
    // TESTES DE SENHA
    // ========================================

    @Test
    @DisplayName("Senha vazia deve gerar violação")
    void testSenha_QuandoVazia_DeveTerViolacao() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto("João Silva");
        usuario.setEmail("joao@unifae.edu.br");
        usuario.setSenhaHash("");
        usuario.setTipoUsuario(TipoUsuario.ESTUDANTE);

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> 
            v.getPropertyPath().toString().equals("senhaHash")
        );
    }

    // ========================================
    // TESTES DE PERÍODO ATUAL ALUNO
    // ========================================

    @Test
    @DisplayName("PeriodoAtualAluno excedendo 2 caracteres deve gerar violação")
    void testPeriodoAtualAluno_QuandoExcedeTamanho_DeveTerViolacao() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto("João Silva");
        usuario.setEmail("joao@unifae.edu.br");
        usuario.setSenhaHash("hash123");
        usuario.setPeriodoAtualAluno("123"); // > 2
        usuario.setTipoUsuario(TipoUsuario.ESTUDANTE);

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> 
            v.getPropertyPath().toString().equals("periodoAtualAluno")
        );
    }
}
