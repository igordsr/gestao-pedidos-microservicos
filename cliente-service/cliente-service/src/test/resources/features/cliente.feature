# language: pt

Funcionalidade: Cliente

  Cenario: Cadastrar Cliente
    Dado uma requisição para criar um novo cliente com um payload ClienteDTO válido
    Quando o usuário envia uma requisição POST para /cliente
    Então o serviço deve retornar uma resposta com o código de status HTTP Criado
    E o corpo da resposta deve conter o ClienteDTO criado

  Cenario: Listar clientes
    Quando o cliente envia uma requisição para /cliente
    Então o serviço deve retornar a lista de clientes cadastrados

  Cenario: Atualizar Cliente
    Dado que um cliente já foi cadastrado "95629455028"
    Quando o usuário envia uma requisição PUT para /cliente/{id}
    Então o serviço deve atualizar o cliente com os detalhes fornecidos
    E retornar uma resposta com o código de status HTTP OK
    E o corpo da resposta deve conter o ClienteDTO atualizado

