# LP1

Sistema de Gestão desenvolvido em Java com arquitetura MVC.

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/example/lp3_grupo1/
│   │   ├── BLL/            # Business Logic Layer (Serviços)
│   │   ├── Controller/     # Controladores REST
│   │   ├── DAL/            # Data Access Layer (Repositórios)
│   │   ├── Model/          # Entidades e modelos de dados
│   │   ├── Utils/          # Classes utilitárias
│   │   └── Main.java       # Classe principal
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/lp3_grupo1/  # Testes unitários
```

## Tecnologias

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring REST API** - Controladores REST
- **Spring Data JPA** - Persistência
- **H2 Database** - Desenvolvimento
- **MySQL** - Produção (opcional)
- **Lombok** - Redução de boilerplate
- **Maven** - Gerenciamento de dependências

## Como Executar

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6 ou superior

### Executar a aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

Console H2: `http://localhost:8080/h2-console`

### Compilar o projeto

```bash
mvn clean install
```

### Executar testes

```bash
mvn test
```

## Arquitetura

### Model (Modelo)
- Localização: `src/main/java/com/example/lp3_grupo1/Model/`
- Responsabilidade: Entidades JPA e modelos de domínio

### BLL (Business Logic Layer)
- Localização: `src/main/java/com/example/lp3_grupo1/BLL/`
- Responsabilidade: Lógica de negócio e regras da aplicação

### DAL (Data Access Layer)
- Localização: `src/main/java/com/example/lp3_grupo1/DAL/`
- Responsabilidade: Acesso e persistência de dados (Repositórios JPA)

### Controller (Controlador)
- Localização: `src/main/java/com/example/lp3_grupo1/Controller/`
- Responsabilidade: Endpoints REST API

### Utils
- Localização: `src/main/java/com/example/lp3_grupo1/Utils/`
- Responsabilidade: Classes utilitárias e helpers

## Configuração

Edite `src/main/resources/application.properties` para configurar:
- Porta do servidor
- Conexão com banco de dados
- Propriedades do Hibernate
- Logging

## Licença

Projeto privado - Todos os direitos reservados
