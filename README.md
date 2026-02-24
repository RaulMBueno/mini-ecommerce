# Mini E-commerce API

![Java](https://img.shields.io/badge/Java-21-blue?style=for-the-badge&logo=openjdk)
![Spring](https://img.shields.io/badge/Spring_Boot-3.x-green?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=for-the-badge&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-blue?style=for-the-badge&logo=docker)

API RESTful para um sistema de e-commerce de produtos de beleza/maquiagem, desenvolvida com Java e Spring Boot. Este projeto começou como um backend de estudo para uma pequena loja virtual e hoje é utilizado em produção, aplicando boas práticas de arquitetura de software, testes automatizados e integração com banco de dados relacional.

## Sobre o Projeto

O objetivo deste projeto é fornecer um backend robusto para uma loja virtual de produtos de beleza/maquiagem, com base preparada para diferentes tipos de produto (físicos, via link de afiliado e produtos digitais no futuro). A aplicação foi estruturada seguindo os princípios da arquitetura em camadas para garantir que o código seja limpo, organizado e de fácil manutenção, separando as responsabilidades de API, lógica de negócio e acesso a dados.

## Recursos Principais

-   **Gerenciamento de Produtos e Categorias:** Operações CRUD completas para produtos e categorias.
-   **Sistema de Usuários e Clientes:** Estrutura para cadastro e gerenciamento de usuários.
-   **Sistema de Pedidos:** Modelagem de pedidos, incluindo itens, preço histórico e status.
-   **Estrutura de Segurança:** Base para autenticação e autorização com Spring Security.
-   **Tratamento de Exceções:** Respostas de erro padronizadas para a API.

## Tecnologias Utilizadas

-   **Linguagem:** Java 21
-   **Framework:** Spring Boot 3
-   **Persistência de Dados:**
    -   Spring Data JPA (Hibernate)
    -   PostgreSQL 16 (executado via Docker)
-   **Versionamento de Banco de Dados:** Flyway
-   **Testes:**
    -   JUnit 5
    -   Mockito
-   **Build & Dependências:** Maven

## Como Executar o Projeto

Siga os passos abaixo para executar a aplicação em seu ambiente local.

### Pré-requisitos

-   JDK 21 ou superior
-   Docker Desktop
-   Maven (já incluído no projeto via `mvnw`)
-   Um cliente de banco de dados (Ex: DBeaver, pgAdmin)

### Passos

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/RaulMBueno/mini-ecommerce.git](https://github.com/RaulMBueno/mini-ecommerce.git)
    cd mini-ecommerce
    ```

2.  **Inicie o banco de dados com Docker:**
    * Certifique-se de que o Docker Desktop está em execução.
    * No terminal, execute o comando para criar e iniciar o container do PostgreSQL:
    ```bash
    docker run --name mini-ecommerce-db -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=<SUA_SENHA> -d postgres:16
    ```

3.  **Crie o banco de dados:**
    * Conecte-se ao servidor PostgreSQL (em `localhost:5432`) usando seu cliente de banco de dados.
    * Execute o seguinte comando SQL para criar o banco que a aplicação usará:
    ```sql
    CREATE DATABASE mini_ecommerce;
    ```

4.  **Configure as variáveis de ambiente** (obrigatório desde o security hotfix):
    * `JWT_SECRET`, `DB_PASSWORD`, `CLOUDINARY_*` — Veja [docs/security-hotfix.md](docs/security-hotfix.md)
    * **Produção (perfil prod):** `SPRING_PROFILES_ACTIVE=prod`, `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`, `ADMIN_EMAIL` (email que recebe ROLE_ADMIN no login Google), `FRONTEND_URL` (ex.: `https://seu-app.vercel.app`; fallback local: `http://localhost:5173`)

5.  **Execute a aplicação:**
    * Na pasta raiz do projeto, execute o comando Maven:
    ```bash
    .\mvnw spring-boot:run
    ```
    * O Flyway irá rodar automaticamente, criando todas as tabelas. A API estará disponível em `http://localhost:8080`.
    * **Importante:** `DB_PASSWORD` deve ser igual ao `POSTGRES_PASSWORD` usado no passo 2.

## Boas Práticas e Padrões de Projeto Aplicados

-   **Arquitetura em Camadas:** Código organizado em `Controller`, `Service`, `Repository` e `Entity`

## 🌐 Ambientes

### Local (desenvolvimento)
- URL base: http://localhost:8080
- Descrição: ambiente usado para desenvolver e testar novas funcionalidades na máquina local, antes de enviar para a nuvem.

### Produção
- URL base: https://mini-ecommerce-production-c2d9.up.railway.app
- Descrição: backend em produção, hospedado na Railway. Este ambiente é consumido pelo frontend do Makeup E-commerce publicado na Vercel.

### Login Google (OAuth2) e JWT
- **Iniciar login Google:** `GET /oauth2/authorization/google` (redireciona para o Google; após sucesso, redireciona para o frontend com JWT).
- **Callback do frontend:** o backend redireciona para `{FRONTEND_URL}/oauth2/redirect?token={JWT}`. Em falha (ex.: email não retornado), redireciona para `{FRONTEND_URL}/login?error=oauth2`.
- **Frontend (React):** criar rota `/oauth2/redirect` que lê `?token=` da query, salva em `localStorage.setItem("token", token)` e redireciona para `/admin` ou home. Garantir que o cliente HTTP da API envie `Authorization: Bearer <token>` (token lido do localStorage).
- **Validar token:** `GET /me` (requer `Authorization: Bearer <token>`); retorna `{ "email": "...", "roles": ["ROLE_CLIENT"] }`. Se o email do Google for igual a `ADMIN_EMAIL`, o usuário recebe `ROLE_ADMIN`.
