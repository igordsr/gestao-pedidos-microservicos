# language: pt

Funcionalidade: Produto

  Cenario: Cadastrar Produto
    Dado uma requisição para criar um novo produto com um payload ProdutoDTO válido
    Quando o cliente envia uma requisição POST para /produto
    Então o serviço deve retornar uma resposta com o código de status HTTP Criado
    E o corpo da resposta deve conter o ProdutoDTO criado

  Cenario: Atualizar Produto
    Dado que um produto já foi cadastrado
    Quando o cliente envia uma requisição PUT para /produto/{id}
    Então o serviço deve atualizar o produto com os detalhes fornecidos
    E retornar uma resposta com o código de status HTTP OK
    E o corpo da resposta deve conter o ProdutoDTO atualizado

  Cenario: Deletar Produto
    Dado que um produto esta cadastrado
    Quando o cliente envia uma requisição DELETE para /produto/{id}
    Então o serviço deve retornar uma resposta com o código de status HTTP Sem Conteúdo
