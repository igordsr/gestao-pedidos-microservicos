# language: pt

Funcionalidade: Pedido

  Cenario: Cadastrar Pedido
    Dado um cliente válido
    Quando o cliente envia uma requisição POST para /pedido
    Então o serviço deve retornar uma resposta com o código de status HTTP Criado
    E o corpo da resposta deve conter o PedidoDTO criado

  Cenario: Consultar Pedido
    Dado que um produto já foi cadastrado
    Quando o cliente envia uma requisição GET para /pedido/{id}
    Então o serviço deve retornar os dados desse pedido
    E retornar uma resposta com o código de status HTTP OK

