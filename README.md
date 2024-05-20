# Sistema de Gestão de Pedidos

## Sumário

1. [Introdução](#introdução)
2. [Visão Geral do Sistema](#visão-geral-do-sistema)
3. [Arquitetura](#arquitetura)
4. [Funcionalidades Principais](#funcionalidades-principais)
5. [Design dos Serviços](#design-dos-serviços)
6. [Endpoints de API](#endpoints-de-api)
7. [Guias de Implantação](#guias-de-implantação)
8. [Guias de Uso](#guias-de-uso)
9. [Contribuição](#contribuição)
10. [Licença](#licença)

## Introdução

O sistema de gerenciamento de pedidos é altamente eficiente, explorando profundamente a arquitetura de microsserviços utilizando o ecossistema Spring. Este sistema abrange desde a gestão de clientes e produtos até o processamento e entrega de pedidos, enfatizando a autonomia dos serviços, comunicação eficaz, e persistência de dados isolada.

## Visão Geral do Sistema

O objetivo do sistema é organizar eficientemente o estoque dos produtos e calcular a melhor rota de entrega, agrupando os pedidos de regiões próximas.

## Arquitetura

A arquitetura do projeto é baseada em microsserviços, garantindo a autonomia e a comunicação eficiente entre os componentes. Utilizamos o ecossistema Spring para a implementação dos serviços e cada serviço possui uma base de dados isolada.

![Diagrama da Arquitetura](GestaoPedidosMicroservicos.svg)

### Componentes Principais:

- **Backend**: Baseado em microsserviços com Spring Boot.
- **Banco de Dados**: Utilizamos MySql para persistência de dados.

## Funcionalidades Principais

1. **Registro de Cliente**
    - Operações CRUD para gerenciar clientes.
    - Dados básicos e endereço de entrega do cliente.

2. **Registro de Produtos**
    - Operações CRUD para gerenciar produtos.
    - Controle de estoque.

3. **Carga de Produtos**
    - Importação em massa de dados de produtos.
    - Upload de arquivo e processamento assíncrono para atualizar o catálogo.

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
- **CargaProdutoService**: Lida com a importação e processamento de dados de produtos.
- **PedidoService**: Centraliza o processamento e gerenciamento de pedidos.
- **EntregaService**: Gerencia a logística e rastreamento de entregas.

## Endpoints de API

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

### EntregaService

- **GET /entregas**
    - **Descrição**: Lista todas as entregas.
    - **Parâmetros**: Nenhum.
    - **Resposta**: JSON com lista de entregas.

- **POST /entregas**
    - **Descrição**: Atualiza o status da entrega.
    - **Parâmetros**: Dados da entrega.
    - **Resposta**: Status da entrega atualizado.

## Guias de Implantação

### Requisitos

- [Lista de requisitos de sistema, ex: Java 11, Docker]
- [Lista de dependências, ex: Spring Boot, PostgreSQL]

### Passos de Implantação

1. **Clone o repositório**:
    ```sh
    git clone https://github.com/usuario/projeto.git
    ```
2. **Instale as dependências**:
    ```sh
    cd projeto
    ./mvnw install
    ```
3. **Configure as variáveis de ambiente**:
    - Crie um arquivo `.env` baseado no `.env.example`.

4. **Execute a aplicação**:
    ```sh
    ./mvnw spring-boot:run
    ```

## Guias de Uso

### Acesso à Aplicação

Depois de implantar o projeto, acesse a aplicação em [URL da aplicação].

### Funcionalidades Principais

- **Registro de Cliente**: Adicionar, editar, visualizar e deletar clientes.
- **Registro de Produtos**: Adicionar, editar, visualizar e deletar produtos.
- **Carga de Produtos**: Importar dados de produtos via upload de arquivo.
- **Gestão de Pedidos**: Criar e gerenciar pedidos.
- **Logística de Entrega**: Gerenciar e rastrear entregas.

### Exemplos de Uso

- **Adicionar Cliente**:
    ```sh
    curl -X POST http://api.seuprojeto.com/clientes -d '{"nome": "João", "endereco": "Rua X, 123"}'
    ```

- **Adicionar Produto**:
    ```sh
    curl -X POST http://api.seuprojeto.com/produtos -d '{"nome": "Produto Y", "preco": 100.00}'
    ```

## Contribuição

Se você deseja contribuir com este projeto, siga as diretrizes abaixo:

1. **Fork o repositório**
2. **Crie uma branch para sua feature** (`git checkout -b feature/nova-feature`)
3. **Commit suas alterações** (`git commit -m 'Adiciona nova feature'`)
4. **Envie para o branch** (`git push origin feature/nova-feature`)
5. **Abra um Pull Request**

## Licença

Este projeto está licenciado sob a licença [Nome da Licença]. Veja o arquivo [LICENSE](link-para-arquivo-LICENSE) para mais detalhes.
