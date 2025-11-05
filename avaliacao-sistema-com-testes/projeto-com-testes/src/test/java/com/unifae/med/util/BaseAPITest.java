package com.unifae.med.util;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 * Classe base para testes de API REST com REST Assured.
 * 
 * Fornece configuração comum para todos os testes de API:
 * - Base URI e porta do servidor
 * - Content-Type padrão (JSON)
 * - Logging de requisições e respostas
 * 
 * IMPORTANTE: Esta classe assume que a aplicação está rodando em:
 * http://localhost:8080/avaliacao-sistema/api
 * 
 * Para executar os testes de API, você deve:
 * 1. Compilar o projeto: mvn clean package
 * 2. Iniciar o servidor Tomcat com a aplicação deployada
 * 3. Executar os testes de API: mvn test -Dtest="*APITest"
 * 
 * Alternativamente, você pode usar um servidor embarcado nos testes
 * ou configurar um profile Maven para iniciar/parar o servidor automaticamente.
 */
public abstract class BaseAPITest {

    protected static RequestSpecification requestSpec;

    /**
     * Configuração executada uma vez antes de todos os testes.
     * Define a configuração base do REST Assured.
     */
    @BeforeAll
    public static void setUpClass() {
        // Configurar base URI e porta
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/avaliacao-sistema";                    

        // Criar especificação de requisição reutilizável
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(LogDetail.URI)
                .log(LogDetail.METHOD)
                .build();
    }

    /**
     * Configuração executada antes de cada teste.
     * Pode ser sobrescrita por classes filhas para setup específico.
     */
    @BeforeEach
    public void setUp() {
        // Implementação padrão vazia
        // Classes filhas podem sobrescrever para setup específico
    }

    /**
     * Retorna a especificação de requisição configurada.
     * Use este método nos testes para obter a configuração base.
     * 
     * Exemplo:
     * given()
     *     .spec(getRequestSpec())
     *     .body(json)
     * .when()
     *     .post("/usuarios")
     * .then()
     *     .statusCode(201);
     */
    protected RequestSpecification getRequestSpec() {
        return requestSpec;
    }
}
