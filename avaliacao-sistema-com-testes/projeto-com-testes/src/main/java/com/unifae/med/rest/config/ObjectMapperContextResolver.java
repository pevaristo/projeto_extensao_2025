/**
 * =================================================================================================
 * ENTENDIMENTO DO CÓDIGO
 * =================================================================================================
 * Esta classe, ObjectMapperContextResolver, atua como uma configuração centralizada para o
 * processo de conversão de objetos Java para JSON (serialização) e de JSON para objetos Java
 * (desserialização) em uma aplicação JAX-RS (APIs REST em Java).
 *
 * O objetivo principal é garantir que a aplicação lide de forma consistente e correta com
 * datas e horas do Java 8 (como LocalDate, LocalDateTime). Sem essa configuração, o Jackson
 * (biblioteca padrão para manipulação de JSON) não saberia como converter esses tipos,
 * resultando em erros.
 *
 * Em resumo, a classe cria e fornece uma instância personalizada do ObjectMapper para toda a
 * aplicação, ensinando-o a formatar datas como texto (ex: "2025-09-27T21:30:00") em vez de
 * um número longo (timestamp), o que torna o JSON mais legível para humanos e sistemas externos.
 * =================================================================================================
 */
package com.unifae.med.rest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

/**
 * A anotação @Provider registra esta classe no ambiente JAX-RS. Isso permite
 * que o JAX-RS a descubra e a utilize automaticamente como um provedor de
 * contexto, neste caso, para fornecer uma instância configurada do
 * ObjectMapper.
 */
@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    // Mantém a instância única e configurada do ObjectMapper que será usada em toda a aplicação.
    private final ObjectMapper objectMapper;

    /**
     * O construtor é responsável por inicializar e configurar o ObjectMapper.
     * Esta configuração é feita uma única vez quando a aplicação é iniciada.
     */
    public ObjectMapperContextResolver() {
        // Cria uma nova instância do ObjectMapper.
        this.objectMapper = new ObjectMapper();

        // Registra o JavaTimeModule. Este módulo ensina o Jackson a serializar e desserializar
        // os tipos de data e hora da API do Java 8 (pacote java.time), como LocalDate e LocalDateTime.
        this.objectMapper.registerModule(new JavaTimeModule());

        // Desabilita a funcionalidade padrão de escrever datas como timestamps numéricos (ex: 1678886400).
        // Com isso desabilitado, as datas serão formatadas como strings no padrão ISO 8601 (ex: "2025-09-27T18:40:00"),
        // que é mais legível e interoperável.
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Este método é implementado da interface ContextResolver. O ambiente
     * JAX-RS o invoca sempre que precisa de um ObjectMapper para processar uma
     * requisição ou resposta JSON.
     *
     * @param type O tipo da classe para a qual o contexto (ObjectMapper) está
     * sendo solicitado.
     * @return A instância pré-configurada do ObjectMapper.
     */
    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }
}
