# Agencia de Viagens

## Dependencias

1. JDK 21
2. Maven

## Configuracao

### Base de dados

Criar e popular a base de dados:

- [tabela.sql](database/tabela.sql)
- [Dadosclientes_db.sql](database/Dadosclientes_db.sql)

Configurar url, senha e usuario no arquivo: [application.properties](src/main/resources/application.properties)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/agencia_viagens?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo
spring.datasource.username=guest
spring.datasource.password=guest123
```

## Rodar

### Com Maven

```sh
mvn clean compile exec:java
```

### Com o IntelliJ

1. Abrir o arquivo da classe [ApplicationMain](./src/main/java/com/agencia/viagens/sistema/swing/ApplicationMain.java)
2. Rodar a classe
