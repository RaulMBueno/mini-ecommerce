# Mini E-commerce API

![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![H2 Database](https://img.shields.io/badge/H2-4479A1?style=for-the-badge&logo=h2)

Este é um projeto de API RESTful para um mini e-commerce, desenvolvido com Spring Boot. A aplicação gerencia produtos, clientes e pedidos, demonstrando as melhores práticas de desenvolvimento de backend, como arquitetura em camadas, injeção de dependência e DTOs.

---

### Visão Geral da Arquitetura

A aplicação foi construída com uma arquitetura em camadas, seguindo o padrão MVC (Model-View-Controller) para backend.

* **Controller**: Recebe as requisições HTTP e delega as operações para a camada de serviço.
* **Service**: Contém a lógica de negócio principal, orquestrando as operações de dados.
* **Repository**: Interage diretamente com o banco de dados usando Spring Data JPA, abstraindo a complexidade das consultas SQL.
* **Entities**: Representam as tabelas do banco de dados, mapeadas pelo JPA.
* **DTOs**: Objetos de transferência de dados que protegem as entidades e garantem que a API retorne apenas os dados necessários.

---

### Tecnologias Utilizadas

* **Java 17**: Linguagem de programação.
* **Spring Boot**: Framework para o desenvolvimento da API.
* **Spring Data JPA**: Abstração para a camada de persistência.
* **H2 Database**: Banco de dados em memória para desenvolvimento e testes.
* **Lombok**: Ferramenta para reduzir o código boilerplate (getters, setters, construtores).
* **Git**: Sistema de controle de versão.
* **Thunder Client**: Ferramenta para testar os endpoints da API.

---

### Endpoints da API

A API expõe os seguintes endpoints REST para operações CRUD.

**Clientes (`/clients`)**
| Método | Endpoint | Descrição |
| --- | --- | --- |
| `POST` | `/clients` | Cria um novo cliente |
| `GET` | `/clients` | Lista todos os clientes |

**Produtos (`/products`)**
| Método | Endpoint | Descrição |
| --- | --- | --- |
| `POST` | `/products` | Cria um novo produto |
| `GET` | `/products` | Lista todos os produtos |

**Pedidos (`/orders`)**
| Método | Endpoint | Descrição |
| --- | --- | --- |
| `POST` | `/orders` | Cria um novo pedido |
| `GET` | `/orders` | Lista todos os pedidos |

---

### Como Rodar a Aplicação

1.  Clone o repositório:
    `git clone https://github.com/RaulMBueno/mini-ecommerce.git`
2.  Navegue até o diretório do projeto:
    `cd mini-ecommerce`
3.  Execute a aplicação usando o Maven Wrapper:
    `./mvnw spring-boot:run`

A aplicação será iniciada na porta 8080.

---

### Contato

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/raul-martins-bueno-12a848222/)
[![Gmail](https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:raulmartinsbueno@gmail.com)