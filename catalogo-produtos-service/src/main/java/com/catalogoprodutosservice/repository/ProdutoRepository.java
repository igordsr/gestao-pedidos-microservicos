package com.catalogoprodutosservice.repository;

import com.catalogoprodutosservice.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, UUID> {

    List<Produto> findByStatusTrue();
    Optional<Produto> findByNomeIgnoreCase(String nome);
}
