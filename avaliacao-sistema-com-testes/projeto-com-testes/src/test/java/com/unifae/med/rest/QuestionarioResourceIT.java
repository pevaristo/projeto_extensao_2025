package com.unifae.med.rest;

import com.unifae.med.util.BaseAPITest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Testes de API REST para QuestionarioResource.
 */
@DisplayName("Testes de API: QuestionarioResource")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QuestionarioResourceIT extends BaseAPITest {

    private static Integer questionarioIdCriado;

    @Test
    @Order(1)
    @DisplayName("POST /questionarios - Deve criar questionário (201)")
    void testCriarQuestionario_ComDadosValidos_DeveRetornar201() {
        String novoQuestionarioJson = """
            {
                "nomeModelo": "Mini CEX Teste API",
                "descricao": "Avaliação de competências clínicas - Teste"
            }
            """;

        questionarioIdCriado = given()
                .contentType(ContentType.JSON)
                .body(novoQuestionarioJson)
            .when()
                .post("/questionarios")
            .then()
                .statusCode(201)
                .body("nomeModelo", equalTo("Mini CEX Teste API"))
                .body("descricao", containsString("competências clínicas"))
                .body("idQuestionario", notNullValue())
            .extract()
                .path("idQuestionario");

        System.out.println("✓ Questionário criado com ID: " + questionarioIdCriado);
    }

    @Test
    @Order(2)
    @DisplayName("GET /questionarios/{id} - Deve retornar questionário existente (200)")
    void testBuscarQuestionarioPorId_QuandoExiste_DeveRetornar200() {
        given()
                .pathParam("id", questionarioIdCriado)
        .when()
                .get("/questionarios/{id}")
        .then()
                .statusCode(200)
                .body("idQuestionario", equalTo(questionarioIdCriado))
                .body("nomeModelo", equalTo("Mini CEX Teste API"));
    }

    @Test
    @Order(3)
    @DisplayName("GET /questionarios/{id} - Deve retornar 404 para ID inexistente")
    void testBuscarQuestionarioPorId_QuandoNaoExiste_DeveRetornar404() {
        given()
                .pathParam("id", 999999)
        .when()
                .get("/questionarios/{id}")
        .then()
                .statusCode(404);
    }

    @Test
    @Order(4)
    @DisplayName("GET /questionarios - Deve retornar lista de questionários (200)")
    void testListarTodosQuestionarios_DeveRetornar200() {
        given()
        .when()
                .get("/questionarios")
        .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class))
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    @Order(5)
    @DisplayName("PUT /questionarios/{id} - Deve atualizar questionário (200)")
    void testAtualizarQuestionario_ComDadosValidos_DeveRetornar200() {
        String questionarioAtualizadoJson = String.format("""
            {
                "idQuestionario": %d,
                "nomeModelo": "Mini CEX Atualizado",
                "descricao": "Descrição atualizada"
            }
            """, questionarioIdCriado);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", questionarioIdCriado)
                .body(questionarioAtualizadoJson)
        .when()
                .put("/questionarios/{id}")
        .then()
                .statusCode(200)
                .body("nomeModelo", equalTo("Mini CEX Atualizado"))
                .body("descricao", equalTo("Descrição atualizada"));
    }

    @Test
    @Order(6)
    @DisplayName("DELETE /questionarios/{id} - Deve deletar questionário (204)")
    void testDeletarQuestionario_DeveRetornar204() {
        given()
                .pathParam("id", questionarioIdCriado)
        .when()
                .delete("/questionarios/{id}")
        .then()
                .statusCode(204);

        // Verificar que foi deletado
        given()
                .pathParam("id", questionarioIdCriado)
        .when()
                .get("/questionarios/{id}")
        .then()
                .statusCode(404);

        System.out.println("✓ Questionário deletado com sucesso");
    }
}
