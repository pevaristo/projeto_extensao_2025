package com.unifae.med.rest;

import com.unifae.med.util.BaseAPITest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Testes de API REST para TurmaResource.
 */
@DisplayName("Testes de API: TurmaResource")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TurmaResourceIT extends BaseAPITest {

    private static Integer turmaIdCriada;

    @Test
    @Order(1)
    @DisplayName("POST /turmas - Deve criar turma (201)")
    void testCriarTurma_ComDadosValidos_DeveRetornar201() {
        String novaTurmaJson = """
            {
                "nomeTurma": "Medicina 2024 Teste API",
                "anoLetivo": 2024,
                "semestre": 1
            }
            """;

        turmaIdCriada = given()
                .contentType(ContentType.JSON)
                .body(novaTurmaJson)
            .when()
                .post("/turmas")
            .then()
                .statusCode(201)
                .body("nomeTurma", equalTo("Medicina 2024 Teste API"))
                .body("anoLetivo", equalTo(2024))
                .body("semestre", equalTo(1))
                .body("idTurma", notNullValue())
            .extract()
                .path("idTurma");

        System.out.println("✓ Turma criada com ID: " + turmaIdCriada);
    }

    @Test
    @Order(2)
    @DisplayName("GET /turmas/{id} - Deve retornar turma existente (200)")
    void testBuscarTurmaPorId_QuandoExiste_DeveRetornar200() {
        given()
                .pathParam("id", turmaIdCriada)
        .when()
                .get("/turmas/{id}")
        .then()
                .statusCode(200)
                .body("idTurma", equalTo(turmaIdCriada))
                .body("nomeTurma", equalTo("Medicina 2024 Teste API"));
    }

    @Test
    @Order(3)
    @DisplayName("GET /turmas - Deve retornar lista de turmas (200)")
    void testListarTodasTurmas_DeveRetornar200() {
        given()
        .when()
                .get("/turmas")
        .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class));
    }

    @Test
    @Order(4)
    @DisplayName("PUT /turmas/{id} - Deve atualizar turma (200)")
    void testAtualizarTurma_ComDadosValidos_DeveRetornar200() {
        String turmaAtualizadaJson = String.format("""
            {
                "idTurma": %d,
                "nomeTurma": "Medicina 2024 Atualizada",
                "anoLetivo": 2024,
                "semestre": 2
            }
            """, turmaIdCriada);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", turmaIdCriada)
                .body(turmaAtualizadaJson)
        .when()
                .put("/turmas/{id}")
        .then()
                .statusCode(200)
                .body("nomeTurma", equalTo("Medicina 2024 Atualizada"))
                .body("semestre", equalTo(2));
    }

    @Test
    @Order(5)
    @DisplayName("DELETE /turmas/{id} - Deve deletar turma (204)")
    void testDeletarTurma_DeveRetornar204() {
        given()
                .pathParam("id", turmaIdCriada)
        .when()
                .delete("/turmas/{id}")
        .then()
                .statusCode(204);

        System.out.println("✓ Turma deletada com sucesso");
    }
}
