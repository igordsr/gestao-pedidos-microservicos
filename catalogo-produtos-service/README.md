# Micro serviço de Gerenciamento de Produtos

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
- **Porta de Execução**: A aplicação será executada na porta 8081.

Documentação das APIs:

A documentação das APIs pode ser acessada em [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html).

Gerando uma imagem  da Aplicação com Docker
```shell
docker build -t catalogo-produtos-service-api.jar:latest  .
```
Executando a Aplicação com Docker
```shell
docker run `
  -e SPRING_DATASOURCE_URL=<valor> `
  -e SPRING_DATASOURCE_USERNAME=<valor> `
  -e SPRING_DATASOURCE_PASSWORD=<valor> `
  -e AZURE_STORAGE_CONNECTION_STRING=<valor> `
  -e AZURE_STORAGE_CONTAINER_NAME=<valor> `
  -p 8082:8082 `
  catalogo-produtos-service-api.jar:latest
```
