/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, `RestApplication`, é a classe de configuração central e o ponto de
 * entrada para a aplicação JAX-RS (Jakarta RESTful Web Services). Sua principal
 * função é "ativar" o framework JAX-RS e definir um prefixo de URL base para todos
 * os endpoints da API.
 *
 * A anotação `@ApplicationPath("/api")` é o elemento mais importante aqui. Ela
 * instrui o servidor de aplicação (como WildFly, Payara, Tomcat, etc.) que todas as
 * requisições para os serviços REST devem começar com o caminho "/api".
 *
 * Por exemplo, um recurso que antes seria acessado em `http://localhost:8080/seu-app/disciplinas`,
 * agora será acessível em `http://localhost:8080/seu-app/api/disciplinas`.
 *
 * Ao estender `jakarta.ws.rs.core.Application`, a classe sinaliza ao contêiner
 * que ele deve escanear o projeto em busca de outras classes com anotações JAX-RS
 * (como `@Path` e `@Provider`) e registrá-las automaticamente. Como o corpo da
 * classe está vazio, ela utiliza o comportamento padrão de auto-descoberta, que é
 * a abordagem mais comum e prática para a maioria das aplicações.
 * =================================================================================================
 */
package com.unifae.med.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * A anotação `@ApplicationPath` define o prefixo base da URL para todos os
 * recursos (endpoints) da API REST nesta aplicação. Todas as URLs dos seus
 * recursos JAX-RS (classes com @Path) serão relativas a este caminho.
 */
@ApplicationPath("/api")
public class RestApplication extends Application {
    // O corpo da classe pode ficar vazio.
    // Ao estender `Application` sem sobrescrever métodos, o comportamento padrão do
    // servidor é escanear o classpath em busca de todas as classes de recursos (@Path)
    // e provedores (@Provider) e registrá-los automaticamente.
}
