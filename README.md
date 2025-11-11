# 💳 CRUD-TP1 – Sistema de Contas Bancárias (CLI)

Projeto desenvolvido como parte do **Teste de Performance 1 – Engenharia Disciplinada de Software (Instituto Infnet)**.  
O sistema implementa um **CRUD completo** (Create, Read, Update, Delete) para contas bancárias, executado via **linha de comando (CLI)**, seguindo princípios de **Clean Code, SOLID** e **Command Query Separation (CQS)**.  

---

## 🧩 Objetivo do Projeto

Criar um sistema de **gerenciamento de contas bancárias** com foco em qualidade de código, clareza e testes automatizados.  
O projeto utiliza **Java 21 + Spring Boot 3.5.7**, integrando banco de dados **H2 (padrão)** e opcionalmente **MySQL**, com cobertura de testes via **JaCoCo** e validações com **Jqwik**.

---

## 🚀 Funcionalidades

- Criar conta bancária  
- Consultar todas as contas  
- Buscar conta por ID  
- Atualizar saldo (depósito/ajuste direto)  
- Excluir conta  
- Menu interativo com mensagens claras no CLI  

---

## 🏗️ Arquitetura e Padrões

- **Camada Entity:** Representa os objetos persistentes (`Conta`).  
- **Camada Repository:** Interface para acesso ao banco de dados com Spring Data JPA.  
- **Camada Service:** Regras de negócio e separação Command/Query.  
- **Camada CLI (Main):** Interação com o usuário via terminal.  
- **Camada Exception:** Tratamento robusto de erros e mensagens claras.

**Princípios aplicados:**
- ✅ **Clean Code:** legibilidade e simplicidade  
- ✅ **SOLID:** baixo acoplamento, alta coesão  
- ✅ **CQS (Command Query Separation):** leitura e escrita separadas  
- ✅ **Imutabilidade e validações consistentes**

---

## ⚙️ Tecnologias

| Categoria | Tecnologia |
|------------|-------------|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.5.7 |
| Banco de Dados | H2 (padrão) / MySQL (opcional) |
| ORM | Spring Data JPA |
| Testes Unitários | JUnit 5 |
| Testes de Propriedade | Jqwik |
| Cobertura | JaCoCo |
| Utilitários | Lombok |

---

## 🧭 Estrutura de Diretórios

```
CRUD-TP1/
│
├── src/
│   ├── main/
│   │   ├── java/br/com/infnet/banco/
│   │   │   ├── CrudTp1Application.java
│   │   │   ├── entity/Conta.java
│   │   │   ├── service/ContaService.java
│   │   │   ├── repository/ContaRepository.java
│   │   │   └── exception/*.java
│   │   └── resources/
│   │       ├── application-h2.properties
│   │       ├── application-mysql.properties
│   │       └── data.sql
│   └── test/java/br/com/infnet/banco/
│       ├── service/ContaServiceTest.java
│       ├── repository/ContaRepositoryTest.java
│       └── property/ContaPropertyTest.java
│
├── pom.xml
└── README.md
```

---

## 💬 Menu CLI

Ao rodar o sistema, o usuário verá o seguinte menu:

```
=== Bem-vindo ao Sistema Bancário TP1 ===
Gerenciamento de contas bancárias via CLI
-----------------------------------------

Escolha uma opção:
1 - Listar contas
2 - Consultar conta por ID
3 - Criar nova conta
4 - Alterar saldo da conta
5 - Excluir conta
0 - Sair
```

Cada operação realiza o CRUD completo sobre o banco em memória (H2).

---

## ⚡ Execução do Sistema

### 1️⃣ Clonar e compilar:
```bash
mvn clean install
```

### 2️⃣ Executar com H2:
```bash
mvn spring-boot:run
```

### 3️⃣ (Opcional) Usar MySQL:
Edite `CrudTp1Application.java`:
```java
System.setProperty("spring.profiles.active", "mysql");
```
E configure `application-mysql.properties` conforme suas credenciais.

---

## 🧪 Testes Automatizados

### 📘 Unitários (JUnit 5)
Verificam comportamentos de serviço e repositório:
```bash
mvn test
```

### 📗 Propriedades (Jqwik)
Validação com geração automática de dados, garantindo robustez:
```bash
mvn test -Dtest=ContaPropertyTest
```

### 📙 Cobertura (JaCoCo)
Gera relatório de cobertura mínima exigida (80%+):
```bash
mvn verify
```

Relatório disponível em:
```
target/site/jacoco/index.html
```

---

## 🔥 Tratamento de Erros

- `ContaNaoEncontradaException` → Quando o ID não existe.  
- `OperacaoInvalidaException` → Quando valores inválidos são informados.  
- `SaldoInsuficienteException` → Ao tentar debitar mais que o saldo.  

---

## 📦 Dependências principais

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>net.jqwik</groupId>
        <artifactId>jqwik</artifactId>
        <version>1.8.4</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## 🧱 Padrões e Boas Práticas

- Separação de responsabilidades (CQS, SOLID)
- Validações consistentes e explícitas
- Evita valores mágicos
- Uso de `BigDecimal` para valores monetários
- Código testável e modular
- Exceções significativas em português

---

## 📚 Documentações Oficiais

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Lombok](https://projectlombok.org/)
- [JUnit 5](https://junit.org/junit5/)
- [Jqwik](https://jqwik.net/)
- [JaCoCo](https://www.jacoco.org/jacoco/trunk/doc/)
- [H2 Database](https://www.h2database.com/html/main.html)

---

## 🧠 Autor

**André Becker**  
Curso: Engenharia de Software – Instituto Infnet  
Disciplina: Engenharia Disciplinada de Software  
Entrega: TP1 – CRUD Java CLI  
Data: Novembro/2025  
