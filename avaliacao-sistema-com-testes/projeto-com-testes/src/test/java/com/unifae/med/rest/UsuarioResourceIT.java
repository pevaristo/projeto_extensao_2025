package com.unifae.med.rest;

import com.unifae.med.util.BaseAPITest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

/**
 * Testes de API REST para UsuarioResource.
 * 
 * IMPORTANTE: Para executar estes testes, a aplicação deve estar rodando em:
 * http://localhost:8080/avaliacao-sistema
 * 
 * Execute os testes com: mvn test -Dtest="UsuarioResourceAPITest"
 * 
 * Os testes são executados em ordem (@Order) para simular um fluxo completo:
 * 1. Criar usuário (POST)
 * 2. Buscar usuário criado (GET by ID)
 * 3. Listar todos os usuários (GET all)
 * 4. Atualizar usuário (PUT)
 * 5. Deletar usuário (DELETE)
 * 6. Validar cenários de erro
 */
@DisplayName("Testes de API: UsuarioResource")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsuarioResourceIT extends BaseAPITest {

    private static Integer usuarioIdCriado;

    // ========================================
    // TESTES DE CRIAÇÃO (POST)
    // ========================================

    /**
 * Configuração executada uma vez antes de todos os testes da classe.
 * Define a URL base, porta e o caminho de contexto para a aplicação.
 */
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        // AJUSTE CRÍTICO: Define o caminho base que inclui o nome da aplicação (context path)
        // e o caminho da API, que é "/api".
        RestAssured.basePath = "/avaliacao-sistema/api";
    }
    
    @Test
    @Order(1)
    @DisplayName("POST /usuarios - Deve criar usuário com dados válidos (201)")
    void testCriarUsuario_ComDadosValidos_DeveRetornar201() {
        String novoUsuarioJson = """
            {
                "nomeCompleto": "João da Silva Teste API",
                "email": "joao.teste.api@unifae.edu.br",
                "telefone": "43999887766",
                "matriculaRA": "999999",
                "senhaHash": "$2a$10$abcdefghijk",
                "tipoUsuario": "ESTUDANTE",
                "ativo": true
            }
            """;

        usuarioIdCriado = given()
                .contentType(ContentType.JSON)
                .body(novoUsuarioJson)
            .when()
                .post("/usuarios")
            .then()
                .statusCode(201)
                .body("nomeCompleto", equalTo("João da Silva Teste API"))
                .body("email", equalTo("joao.teste.api@unifae.edu.br"))
                .body("tipoUsuario", equalTo("ESTUDANTE"))
                .body("ativo", equalTo(true))
                // Validar que senha NÃO é retornada (segurança)
                .body("senhaHash", nullValue())
                .body("idUsuario", notNullValue())
            .extract()
                .path("idUsuario");

        System.out.println("✓ Usuário criado com ID: " + usuarioIdCriado);
    }

    @Test
    @Order(2)
    @DisplayName("POST /usuarios - Deve retornar erro ao omitir campos obrigatórios (400 ou 500)")
    void testCriarUsuario_SemCamposObrigatorios_DeveRetornarErro() {
        String usuarioInvalidoJson = """
            {
                "telefone": "43999887766"
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(usuarioInvalidoJson)
        .when()
                .post("/usuarios")
        .then()
                // Pode retornar 400 (Bad Request) ou 500 (Server Error) dependendo da validação
                .statusCode(anyOf(is(400), is(500)));
    }

    // ========================================
    // TESTES DE BUSCA (GET)
    // ========================================

    @Test
    @Order(3)
    @DisplayName("GET /usuarios/{id} - Deve retornar usuário existente (200)")
    void testBuscarUsuarioPorId_QuandoExiste_DeveRetornar200() {
        given()
                .pathParam("id", usuarioIdCriado)
        .when()
                .get("/usuarios/{id}")
        .then()
                .statusCode(200)
                .body("idUsuario", equalTo(usuarioIdCriado))
                .body("nomeCompleto", equalTo("João da Silva Teste API"))
                .body("email", containsString("@unifae.edu.br"))
                .body("tipoUsuario", equalTo("ESTUDANTE"));
    }

    @Test
    @Order(4)
    @DisplayName("GET /usuarios/{id} - Deve retornar 404 para ID inexistente")
    void testBuscarUsuarioPorId_QuandoNaoExiste_DeveRetornar404() {
        given()
                .pathParam("id", 999999)
        .when()
                .get("/usuarios/{id}")
        .then()
                .statusCode(404);
    }

    @Test
    @Order(5)
    @DisplayName("GET /usuarios - Deve retornar lista de usuários (200)")
    void testListarTodosUsuarios_DeveRetornar200EArray() {
        given()
        .when()
                .get("/usuarios")
        .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class))
                .body("size()", greaterThanOrEqualTo(1))
                .body("[0].idUsuario", notNullValue())
                .body("[0].nomeCompleto", notNullValue())
                .body("[0].email", notNullValue());
    }

    // ========================================
    // TESTES DE ATUALIZAÇÃO (PUT)
    // ========================================

    @Test
    @Order(6)
    @DisplayName("PUT /usuarios/{id} - Deve atualizar usuário existente (200)")
    void testAtualizarUsuario_ComDadosValidos_DeveRetornar200() {
        String usuarioAtualizadoJson = String.format("""
            {
                "idUsuario": %d,
                "nomeCompleto": "João da Silva Atualizado",
                "email": "joao.teste.api@unifae.edu.br",
                "telefone": "43988776655",
                "matriculaRA": "999999",
                "senhaHash": "$2a$10$abcdefghijk",
                "tipoUsuario": "ESTUDANTE",
                "ativo": true
            }
            """, usuarioIdCriado);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", usuarioIdCriado)
                .body(usuarioAtualizadoJson)
        .when()
                .put("/usuarios/{id}")
        .then()
                .statusCode(200)
                .body("nomeCompleto", equalTo("João da Silva Atualizado"))
                .body("telefone", equalTo("43988776655"))
                .body("idUsuario", equalTo(usuarioIdCriado));
    }

    @Test
    @Order(7)
    @DisplayName("PUT /usuarios/{id} - Deve retornar 400 quando ID da URL difere do corpo")
    void testAtualizarUsuario_ComIdDivergente_DeveRetornar400() {
        String usuarioJson = """
            {
                "idUsuario": 888888,
                "nomeCompleto": "Teste Divergente",
                "email": "teste@unifae.edu.br",
                "senhaHash": "hash",
                "tipoUsuario": "ESTUDANTE",
                "ativo": true
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", usuarioIdCriado)
                .body(usuarioJson)
        .when()
                .put("/usuarios/{id}")
        .then()
                .statusCode(400)
                .body(containsString("ID"));
    }

    // ========================================
    // TESTES DE EXCLUSÃO (DELETE)
    // ========================================

    @Test
    @Order(8)
    @DisplayName("DELETE /usuarios/{id} - Deve deletar usuário existente (204)")
    void testDeletarUsuario_DeveRetornar204() {
        given()
                .pathParam("id", usuarioIdCriado)
        .when()
                .delete("/usuarios/{id}")
        .then()
                .statusCode(204);

        // Verificar que o usuário foi realmente deletado
        given()
                .pathParam("id", usuarioIdCriado)
        .when()
                .get("/usuarios/{id}")
        .then()
                .statusCode(404);

        System.out.println("✓ Usuário deletado com sucesso");
    }

    // ========================================
    // TESTES DE VALIDAÇÃO DE DADOS
    // ========================================

    @Test
    @Order(9)
    @DisplayName("POST /usuarios - Deve validar formato de email")
    void testCriarUsuario_ComEmailInvalido_DeveRetornarErro() {
        String usuarioEmailInvalidoJson = """
            {
                "nomeCompleto": "Teste Email Inválido",
                "email": "email-invalido-sem-arroba",
                "senhaHash": "$2a$10$abcdefghijk",
                "tipoUsuario": "ESTUDANTE",
                "ativo": true
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(usuarioEmailInvalidoJson)
        .when()
                .post("/usuarios")
        .then()
                .statusCode(anyOf(is(400), is(500)));
    }

    @Test
    @Order(10)
    @DisplayName("POST /usuarios - Deve validar tamanho máximo de campos")
    void testCriarUsuario_ComCampoExcedendoTamanho_DeveRetornarErro() {
        String nomeLongo = "A".repeat(101); // Máximo é 100
        String usuarioNomeLongoJson = String.format("""
            {
                "nomeCompleto": "%s",
                "email": "teste.longo@unifae.edu.br",
                "senhaHash": "$2a$10$abcdefghijk",
                "tipoUsuario": "ESTUDANTE",
                "ativo": true
            }
            """, nomeLongo);

        given()
                .contentType(ContentType.JSON)
                .body(usuarioNomeLongoJson)
        .when()
                .post("/usuarios")
        .then()
                .statusCode(anyOf(is(400), is(500)));
    }
}
