# Backend de Gestão de Restaurante e Pedidos

Este projeto consiste numa API RESTful desenvolvida em **Spring Boot 3** e **Kotlin**, com foco em boas práticas de arquitetura (*Package by Feature*), persistência de dados relacional, autenticação via **JWT**, validação de dados e tratamento global de exceções conforme a especificação **RFC 9457**.

## Tecnologias e Dependências
* **Linguagem:** Kotlin 1.9 (JVM 21)
* **Framework:** Spring Boot 3.2.2
* **Segurança:** Spring Security + Auth0 Java JWT
* **Persistência:** Spring Data JPA + Banco em Memória H2
* **Validação:** Jakarta Bean Validation (`@field:NotBlank`, `@field:Min`, etc.)
* **Documentação:** SpringDoc OpenAPI / Swagger UI
* **Build System:** Gradle (Kotlin DSL)

---

## Mapeamento Detalhado dos Critérios de Avaliação (Para Correção)

Abaixo está o detalhamento exato de onde cada requisito do projeto foi implementado, para facilitar a validação do professor:

### 1. Mínimo 4 classes novas e relacionamentos (1:1, 1:N, N:N)
* **Onde encontrar:** Pacotes `restaurante-backend`, `produtos` e `pedidos`.
* **Como foi aplicado:** 
  * As 4 classes principais são: `Usuario`, `PerfilCliente`, `Produto` e `Pedido`.
  * **@OneToOne:** Implementado entre `Usuario` e `PerfilCliente` (`PerfilCliente.kt`).
  * **@ManyToOne:** Implementado no `Pedido` apontando para o `Usuario` autor da compra.
  * **@ManyToMany (com Tabela Intermediária):** Implementado no `Pedido.kt` através da anotação `@JoinTable(name = "pedido_produto")`, gerando a tabela associativa que recebe a lista de `Produto`s.

### 2. CRUD completo de pelo menos duas classes e Autenticação
* **Onde encontrar:** `ProdutoController.kt`, `PedidoController.kt` e `SecurityConfig.kt`.
* **Como foi aplicado:**
  * **CRUD 1 (Produtos):** GET, POST, PUT, DELETE implementados com validações DTO (`ProdutoDTO`).
  * **CRUD 2 (Pedidos):** GET por ID, POST (criar), DELETE (cancelar/apagar).
  * **Autenticação:** Em `SecurityConfig.kt`, a rota `GET /api/produtos` foi definida como `permitAll()`. Todas as ações destrutivas ou de mutação (`POST`, `PUT`, `DELETE` em produtos e pedidos) recaem na regra `anyRequest().authenticated()`, exigindo o envio do Token JWT (Bearer) validado pelo `SecurityFilter.kt`.

### 3. Endpoints de Associação (Inclusão/Remoção de Itens)
* **Onde encontrar:** `PedidoController.kt` e `PedidoService.kt`.
* **Como foi aplicado:**
  * Endpoint de Inclusão: `POST /api/pedidos/{id}/produtos/{produtoId}`
  * Endpoint de Remoção: `DELETE /api/pedidos/{id}/produtos/{produtoId}`
  * A regra de negócio no `PedidoService.kt` busca ambas as entidades no banco de dados e adiciona/remove o produto diretamente da `MutableList<Produto>` existente dentro de `Pedido`, deixando o Hibernate (JPA) gerenciar a inserção e exclusão na tabela associativa `pedido_produto`.

### 4. Consulta com Filtro e Ordenação (Query Parameters)
* **Onde encontrar:** `ProdutoController.kt` (`GET /api/produtos`) e `ProdutoRepository.kt`.
* **Como foi aplicado:**
  * A listagem de produtos recebe `categoria` e `sortDir` como `@RequestParam`.
  * No Repositório, foi criado o método Spring Data JPA: `findByCategoriaContainingIgnoreCase(categoria, sort)`.
  * Exemplo de uso: `GET /api/produtos?categoria=LANCHE&sortDir=DESC`. Se os parâmetros não forem passados, ele lista todos ordenados por ASC (padrão).

### 5. Boas práticas: Logs e Exceções em pelo menos 2 serviços
* **Onde encontrar:** Pacote `config` (`GlobalExceptionHandler.kt`) e os Services (`ProdutoService.kt`, `PedidoService.kt`).
* **Como foi aplicado:**
  * **Exceções:** O `GlobalExceptionHandler` captura erros de negócio (`RegraDeNegocioException`), registros não encontrados (`ElementoNaoEncontradoException`) e erros de validação de DTOs (`MethodArgumentNotValidException`). Tudo é devolvido ao cliente padronizado com `ProblemDetail` (RFC 9457), retornando o exato status HTTP adequado (404, 400).
  * **Logs:** Utilizando o `SLF4J` (`LoggerFactory.getLogger`), tanto o `ProdutoService` quanto o `PedidoService` registram no console da aplicação os eventos principais, como a criação de entidades e o momento exato em que um produto é atrelado ou retirado de um pedido.

---

## Estrutura do Projeto (*Package by Feature*)
```text
restaurante-backend
├── autenticacao      # Configurações de Segurança, Filtro JWT e Login
├── config            # DataInitializer (H2 Bootstrap), GlobalExceptionHandler e OpenAPI
├── pedidos           # Entidade Pedido, DTOs, Repository, Service e Controller
├── produtos          # Entidade Produto, DTOs, Repository, Service e Controller
└── usuarios          # Entidades Usuario e PerfilCliente, Repository
```

## Como Executar o Projeto
1. Clone o repositório e abra na sua IDE.
2. Certifique-se de estar utilizando o Java/JDK 21.
3. No terminal da raiz do projeto, execute:
   ```bash
   gradle bootRun
   ```
4. Acesse o **Swagger UI** para testar as requisições: `http://localhost:8080/swagger-ui/index.html`
5. Acesse o banco em memória para auditoria: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:restaurantedb`).

*Nota: Um usuário admin padrão já é gerado em tempo de inicialização pelo `DataInitializer` (email: `admin@restaurante.com` / senha: `senha123`), dispensando a criação manual de usuários para testar o token JWT.*