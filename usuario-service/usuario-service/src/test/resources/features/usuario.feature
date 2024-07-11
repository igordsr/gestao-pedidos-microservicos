# language: pt

Funcionalidade: Usuario

  Cenario: Cadastrar Usuario
    Dado uma requisição para criar um novo usuario com um payload UsuarioDTO válido
    Quando o usuário envia uma requisição POST para /usuario
    Então o serviço deve retornar uma resposta com o código de status HTTP Criado
    E o corpo da resposta deve conter o UsuarioDTO criado

  Cenario: Listar usuarios
    Quando o usuario envia uma requisição para /usuario
    Então o serviço deve retornar a lista de usuarios cadastrados

  Cenario: Atualizar Usuario
    Dado que um usuario já foi cadastrado "95629455028"
    Quando o usuário envia uma requisição PUT para /usuario/{id}
    Então o serviço deve atualizar o usuario com os detalhes fornecidos
    E retornar uma resposta com o código de status HTTP OK
    E o corpo da resposta deve conter o UsuarioDTO atualizado

