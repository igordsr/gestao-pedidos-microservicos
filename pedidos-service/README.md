# Micro serviço de Gerenciamento de Pedidos

- Para executar a Aplicação:
```shell
mvn spring-boot:run
```
## Testes

- Para executar os Testes Unitários:
```shell
mvn test
```
- Para executar os Testes de Integração:
```shell
mvn integration-test
```

Informações Adicionais:

- **Versão do Spring Boot**: 3.2.5
- **Porta de Execução**: A aplicação será executada na porta 8083.

Documentação das APIs:

A documentação das APIs pode ser acessada em [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html).

Gerando uma imagem  da Aplicação com Docker
```shell
docker build -t pedidos-service-api:latest  .
```
Executando a Aplicação com Docker
```shell
docker run `
  -e SPRING_DATASOURCE_URL=<valor> `
  -e SPRING_DATASOURCE_USERNAME=<valor> `
  -e SPRING_DATASOURCE_PASSWORD=<valor> `
  -e CLIENTE_SERVICE_URL=<valor> `
  -e PRODUTO_SERVICE_URL=<valor> `
  -p 8083:8083 `
  pedidos-service-api:latest
```
