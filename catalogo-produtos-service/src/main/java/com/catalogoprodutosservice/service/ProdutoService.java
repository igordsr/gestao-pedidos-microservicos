package com.catalogoprodutosservice.service;

import com.catalogoprodutosservice.controller.exception.modal.EntidadeNaoProcessavelException;
import com.catalogoprodutosservice.controller.exception.modal.RegistroJaExisteException;
import com.catalogoprodutosservice.controller.exception.modal.RegistroNaoEncontradoException;
import com.catalogoprodutosservice.dto.ProdutoDTO;
import com.catalogoprodutosservice.model.Produto;
import com.catalogoprodutosservice.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public ProdutoDTO cadastrar(final ProdutoDTO produtoDTO) {
        Optional<Produto> produtoOptional = this.produtoRepository.findByNomeIgnoreCase(produtoDTO.nome());
        if (produtoOptional.isPresent()) {
            throw new RegistroJaExisteException(produtoOptional.get().getNome());
        }
        final Produto produto = produtoRepository.save(produtoDTO.toProduto());
        return ProdutoDTO.getInstance(produto);
    }

    public List<ProdutoDTO> listarProdutos() {
        return produtoRepository.findByStatusTrue().stream().map(ProdutoDTO::getInstance).toList();
    }

    public ProdutoDTO encontrarProdutoPorId(UUID id) {
        final Produto produto = this.findById(id);
        return ProdutoDTO.getInstance(produto);
    }

    public List<ProdutoDTO> encontrarProdutosPorIds(List<UUID> id) {
        List<Produto> produtos = this.produtoRepository.findAllById(id);
        final List<ProdutoDTO> produtoDTOS = new ArrayList<>();
        for (Produto produto : produtos) {
            ProdutoDTO instance = ProdutoDTO.getInstance(produto);
            produtoDTOS.add(instance);
        }
        return produtoDTOS;
    }

    public ProdutoDTO atualizarProduto(final UUID id, final ProdutoDTO produtoDTO) {
        Produto produto = this.findById(id);
        produto.setNome(produtoDTO.nome());
        produto.setDescricao(produtoDTO.descricao());
        produto.setPreco(produtoDTO.preco());
        produto.setQtdEstoque(produtoDTO.qtdEstoque());
        produto = this.produtoRepository.save(produto);
        return ProdutoDTO.getInstance(produto);
    }

    public void deletarProduto(UUID id) {
        Produto produto = this.findById(id);
        produto.setStatus(Boolean.FALSE);
        this.produtoRepository.save(produto);
    }

    @Transactional
    public ProdutoDTO decrementarEstoque(UUID id, int quantidade) {
        Produto produto = this.findById(id);
        if (produto.getQtdEstoque() < quantidade) {
            final String resp = "Não há quantidade [%s] suficiente disponível do produto [%s] para a venda solicitada. Por favor, ajuste a quantidade ou escolha outro produto";
            throw new EntidadeNaoProcessavelException(String.format(resp, produto.getQtdEstoque(), produto.getNome()));
        }
        int novoEstoque = produto.getQtdEstoque() - quantidade;
        produto.setQtdEstoque(novoEstoque);
        produto = this.produtoRepository.save(produto);
        return ProdutoDTO.getInstance(produto);
    }

    @Transactional
    public ProdutoDTO incrementarEstoque(UUID id, Integer quantidade) {
        Produto produto = this.findById(id);
        int novoEstoque = produto.getQtdEstoque() + quantidade;
        produto.setQtdEstoque(novoEstoque);
        produto = this.produtoRepository.save(produto);
        return ProdutoDTO.getInstance(produto);
    }

    private Produto findById(UUID id) {
        return this.produtoRepository.findById(id).orElseThrow(() -> new RegistroNaoEncontradoException(id.toString()));
    }
}
