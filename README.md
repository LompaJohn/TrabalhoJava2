# Agencia de Viagens

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

1. usar o [mvnw](mvnw) OU usar o IntelliJ
2. acessar a interface por meio do [https://localhost:8080](https://localhost:8080)
2. acessar a documentacao da API por meio
   do [https://localhost:8080/api-docs.html](https://localhost:8080/api-docs.html)
