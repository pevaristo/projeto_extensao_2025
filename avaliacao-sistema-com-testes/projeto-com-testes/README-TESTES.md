# Guia de Execução dos Testes Automatizados

**Data**: 06 de outubro de 2025
**Autor**: Manus AI

## 1. Visão Geral

Este documento fornece as instruções para configurar o ambiente e executar a suíte de testes automatizados implementada para o **Sistema de Avaliação UNIFAE**.

A suíte de testes foi desenvolvida utilizando as seguintes tecnologias:

- **JUnit 5**: Framework de testes.
- **Mockito**: Para criação de mocks em testes unitários.
- **AssertJ**: Para asserções fluentes e legíveis.
- **H2 Database**: Banco de dados em memória para testes de integração.
- **REST Assured**: Para testes de API REST.

## 2. Pré-requisitos

- **JDK 21**: O projeto está configurado para usar o Java 21.
- **Maven 3.8+**: Para gerenciamento de dependências e execução do build.
- **Servidor de Aplicação (para testes de API)**: Um servidor como o Apache Tomcat 10.1+ deve estar disponível para deploy da aplicação.

## 3. Tipos de Testes Implementados

A suíte está organizada em três categorias principais, seguindo uma convenção de nomenclatura para fácil execução.

### 3.1. Testes Unitários (`*Test.java`)

- **Objetivo**: Validar os menores componentes da aplicação de forma isolada.
- **Exemplo**: `UsuarioDAOTest.java`
- **Tecnologia**: JUnit 5, Mockito, AssertJ.
- **Como funciona**: Mocka as dependências externas (como o `EntityManager`) para testar a lógica interna da classe.

### 3.2. Testes de Integração (`*IntegrationTest.java`)

- **Objetivo**: Validar a integração entre a camada de acesso a dados (DAOs) e o banco de dados.
- **Exemplo**: `UsuarioDAOIntegrationTest.java`
- **Tecnologia**: JUnit 5, H2 Database, AssertJ.
- **Como funciona**: Executa as operações do DAO contra um banco de dados H2 em memória, validando o mapeamento JPA e as queries JPQL.

### 3.3. Testes de API (`*APITest.java`)

- **Objetivo**: Validar os endpoints da API REST de ponta a ponta.
- **Exemplo**: `UsuarioResourceAPITest.java`
- **Tecnologia**: JUnit 5, REST Assured.
- **Como funciona**: Realiza chamadas HTTP reais para a aplicação (que deve estar rodando em um servidor) e valida os códigos de status, cabeçalhos e corpos das respostas JSON.

## 4. Como Executar os Testes

Todos os comandos devem ser executados a partir do diretório raiz do projeto (`/projeto-com-testes`).

### 4.1. Executando Todos os Testes

Este comando executará todos os testes (unitários e de integração). Os testes de API falharão se a aplicação não estiver rodando.

```bash
# Compila o projeto e executa todos os testes (exceto os de API, que precisam do servidor)
mvn clean test
```

### 4.2. Executando Testes por Categoria

O `maven-surefire-plugin` está configurado para permitir a execução de testes com base em padrões de nome.

#### A. Apenas Testes Unitários

```bash
# Executa apenas os testes que terminam com "Test.java"
mvn test -Dtest="*Test"
```

#### B. Apenas Testes de Integração

```bash
# Executa apenas os testes que terminam com "IntegrationTest.java"
mvn test -Dtest="*IntegrationTest"
```

#### C. Apenas Testes de API

**IMPORTANTE**: Antes de executar este comando, a aplicação **deve estar compilada e rodando** em um servidor na URL base `http://localhost:8080/avaliacao-sistema/`.

**Passo 1: Compilar e empacotar a aplicação**
```bash
mvn clean package
```

**Passo 2: Fazer o deploy do arquivo `target/avaliacao-sistema.war`** no seu servidor Tomcat (ou similar).

**Passo 3: Executar os testes de API**
```bash
# Executa apenas os testes que terminam com "APITest.java"
mvn test -Dtest="*APITest"
```

## 5. Verificando os Resultados

- **Console**: O Maven exibirá os resultados da execução no console, mostrando o número de testes executados, falhas, erros e testes pulados.
- **Relatórios do Surefire**: Relatórios detalhados em XML e TXT são gerados em `target/surefire-reports/`.

## 6. Estrutura dos Testes

O código-fonte dos testes está localizado em `src/test/java`, espelhando a estrutura de pacotes da aplicação principal.

```
src/test/
├── java/com/unifae/med/
│   ├── dao/              # Testes para a camada DAO
│   │   ├── UsuarioDAOTest.java
│   │   └── UsuarioDAOIntegrationTest.java
│   ├── entity/           # Testes de validação para as entidades
│   │   └── UsuarioValidationTest.java
│   ├── rest/             # Testes para a API REST
│   │   └── UsuarioResourceAPITest.java
│   └── util/             # Classes base e utilitários de teste
│       ├── BaseAPITest.java
│       ├── BaseIntegrationTest.java
│       └── TestDataBuilder.java
└── resources/
    └── META-INF/
        └── persistence.xml # Configuração do H2 para testes
```

## 7. Conclusão

Esta suíte de testes fornece uma base sólida para garantir a qualidade e a estabilidade do Sistema de Avaliação UNIFAE. Recomenda-se a integração desses comandos em um pipeline de Integração Contínua (CI/CD) para automatizar a verificação a cada novo commit.
