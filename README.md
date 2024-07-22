# Sistema de Gestão de Pedidos

## Sumário

1. [Introdução](#introdução)
2. [Visão Geral do Sistema](#visão-geral-do-sistema)
3. [Arquitetura](#arquitetura)
4. [Funcionalidades Principais](#funcionalidades-principais)
5. [Design dos Serviços](#design-dos-serviços)
6. [Principais Endpoints](#principais-endpoints)
7. [Guias de Implantação](#guias-de-implantação)
8. [Documentação da API com Swagger](#documentação-da-api-com-swagger)
9. [Fluxo de Uso](#fluxo-de-uso)


## Introdução

O sistema de gerenciamento de pedidos é altamente eficiente, explorando profundamente a arquitetura de microsserviços utilizando o ecossistema Spring. Este sistema abrange desde a gestão de clientes e produtos até o processamento e entrega de pedidos, enfatizando a autonomia dos serviços, comunicação eficaz, e persistência de dados isolada.

## Visão Geral do Sistema

O objetivo do sistema é organizar eficientemente o estoque dos produtos e calcular a melhor rota de entrega, agrupando os pedidos de regiões próximas.

## Arquitetura

A arquitetura do projeto é baseada em microsserviços, garantindo a autonomia e a comunicação eficiente entre os componentes. Utilizamos o ecossistema Spring para a implementação dos serviços e cada serviço possui uma base de dados isolada.

![Diagrama da Arquitetura](GestaoPedidosMicroservicos.svg)

### Componentes Principais:

- **Backend**: Baseado em microsserviços com Spring Boot.
- **Banco de Dados**: Utilizamos MYSQL para persistência de dados.

## Funcionalidades Principais

1. **Registro de Cliente**
    - Operações CRUD para gerenciar clientes.
    - Dados básicos e endereço de entrega do cliente.

2. **Registro de Produtos**
    - Gerenciamento do catálogo de produtos.
    - Controle de estoque.

3. **Carga de Produtos**
    - Importação em massa de dados de produtos.
    - Upload de arquivo e processamento assíncrono para atualizar o catálogo.
    - Há um documento na raiz desse projeto que se chama produtos.csv que pode ser usado para dar a carga inical no projeto

4. **Gestão de Pedidos**
    - Processamento de pedidos desde a criação até a conclusão.
    - Estados do pedido: aguardando pagamento, pago, aguardando entrega e entregue.

5. **Logística de Entrega**
    - Gerenciamento da logística de entrega.
    - Cálculo de rotas, estimativas de tempo e rastreamento em tempo real.
    - Processamento de pedidos por sub-setor de CEP, geração de relatórios e atualização de status.

## Design dos Serviços

Os serviços foram desenhados seguindo princípios de arquitetura de microsserviços, com comunicação via HTTP/REST. Cada serviço é responsável por um domínio específico e comunica-se com outros serviços conforme necessário.

### Serviços:

- **ClienteService**: Gerencia operações relacionadas aos clientes.
- **ProdutoService**: Gerencia o catálogo de produtos e controle de estoque.
- **PedidoService**: Centraliza o processamento e gerenciamento de pedidos.


## Principais Endpoints

### ClienteService

- **GET /clientes**
    - **Descrição**: Lista todos os clientes.
    - **Parâmetros**: Nenhum.
    - **Resposta**: JSON com lista de clientes.

- **POST /clientes**
    - **Descrição**: Cria um novo cliente.
    - **Parâmetros**: Dados do cliente.
    - **Resposta**: Cliente criado.

### ProdutoService

- **GET /produtos**
    - **Descrição**: Lista todos os produtos.
    - **Parâmetros**: Nenhum.
    - **Resposta**: JSON com lista de produtos.

- **POST /produtos**
    - **Descrição**: Cria um novo produto.
    - **Parâmetros**: Dados do produto.
    - **Resposta**: Produto criado.

### PedidoService

- **GET /pedidos**
    - **Descrição**: Lista todos os pedidos.
    - **Parâmetros**: Nenhum.
    - **Resposta**: JSON com lista de pedidos.

- **POST /pedidos**
    - **Descrição**: Cria um novo pedido.
    - **Parâmetros**: Dados do pedido.
    - **Resposta**: Pedido criado.

## Guias de Implantação

### Requisitos

- Docker e Docker Compose instalados.

### Passos de Implantação

1. **Clone o repositório**:
    ```sh
    https://github.com/igordsr/gestao-pedidos-microservicos.git
    ```
2. **Navegue até o diretório do projeto**:
    ```sh
    cd gestao-pedidos-microservicos
    ```
3. **Execute o Docker Compose**:
    ```sh
    docker-compose up --build

    ```

## Documentação da API com Swagger

Para acessar a documentação detalhada dos endpoints das APIs, utilize os seguintes links:

- **Cliente**: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
- **Produto**: [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html)
- **Pedidos**: [http://localhost:8083/swagger-ui/index.html](http://localhost:8083/swagger-ui/index.html)

### Fluxo de Uso

1. **O cliente pode se cadastrar**:
    - **Requisição**: `POST http://localhost:8081/cliente`
    - **Body**:
        ```json
        {
          "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
          "nome": "Yago Thomas Aparício",
          "cep": "01001-000",
          "logradouro": "Praça da Sé",
          "complemento": "lado ímpar",
          "bairro": "Sé",
          "numero": "138",
          "telefone": "5435008794",
          "email": "ayla_barros@gmail.com",
          "dataNascimento": "1991-02-15",
          "cpf": "59694668247"
        }
        ```

2. **O funcionário pode cadastrar produto**:
    - **Requisição**: `POST http://localhost:8082/produto`
    - **Body**:
        ```json
        {
          "nome": "Camiseta",
          "descricao": "Camiseta de algodão com estampa",
          "preco": 29.99,
          "qtdEstoque": 3
        }
        ```

3. **O cliente pode fazer o pedido**:
    - **Requisição**: `POST http://localhost:8083/pedido`
    - **Body**:
        ```json
        {
          "cliente": "389c95b1-c5d6-40ff-abb3-d59fececc973",
          "itemList": [
            {
              "produto": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
              "quantidade": 1
            }
          ]
        }
        ```

4. **O cliente pode pagar o pedido**:
    - **Requisição**: `PUT http://localhost:8083/pedido/9248db63-3a14-4399-b191-07dc018616f1/efetuar-pagamento`
    - **Body**: Nenhum

5. **O funcionário gera relatório**:
    - **Requisição**: `GET http://localhost:8084/entrega`
    - **Body**: Nenhum

6. **O entregador faz a entrega**:
    - **Requisição**: `PATCH http://localhost:8084/entrega/6cb79855-3180-4490-81f9-c7fc2c7780e2`
    - **Body**: Nenhum

### Exemplos de Uso

- **Adicionar Cliente**:
    ```sh
    curl -X POST http://localhost:8081/cliente -d '{
      "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "nome": "Yago Thomas Aparício",
      "cep": "01001-000",
      "logradouro": "Praça da Sé",
      "complemento": "lado ímpar",
      "bairro": "Sé",
      "numero": "138",
      "telefone": "5435008794",
      "email": "ayla_barros@gmail.com",
      "dataNascimento": "1991-02-15",
      "cpf": "59694668247"
    }'
    ```

- **Adicionar Produto**:
    ```sh
    curl -X POST http://localhost:8082/produto -d '{
      "nome": "Camiseta",
      "descricao": "Camiseta de algodão com estampa",
      "preco": 29.99,
      "qtdEstoque": 3
    }'
    ```

- **Fazer Pedido**:
    ```sh
    curl -X POST http://localhost:8083/pedido -d '{
      "cliente": "389c95b1-c5d6-40ff-abb3-d59fececc973",
      "itemList": [
        {
          "produto": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
          "quantidade": 1
        }
      ]
    }'
    ```

- **Pagar Pedido**:
    ```sh
    curl -X PUT http://localhost:8083/pedido/9248db63-3a14-4399-b191-07dc018616f1/efetuar-pagamento
    ```
