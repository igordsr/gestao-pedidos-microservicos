package com.catalogoprodutosservice.util;


import com.catalogoprodutosservice.dto.ProdutoDTO;
import com.catalogoprodutosservice.feign.vo.AuthorityDTO;
import com.catalogoprodutosservice.feign.vo.UserDetailsVO;
import com.catalogoprodutosservice.model.Produto;

import java.util.List;
import java.util.UUID;

public abstract class InstanceGeneratorHelper {
    public static final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJheWxhX2JhcnJvc0BnbWFpbC5jb20iLCJpYXQiOjE3MjEwNTA0OTQsImV4cCI6MTcyMTEzNjg5NCwicm9sZSI6IkFETUlOIn0.P4YYBQKxgtdDtm1pVx1FF_V3eIsgMGtPrChNm_ivkGg";

    public static Produto getProduto() {
        final Produto produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setNome("Camiseta");
        produto.setDescricao("Camiseta de algodão com estampa");
        produto.setPreco(29.99);
        produto.setQtdEstoque(3);
        return produto;
    }

    public static ProdutoDTO getProdutoDTO() {
        final Produto produto = InstanceGeneratorHelper.getProduto();
        return new ProdutoDTO(produto.getId(), produto.getNome(), produto.getDescricao(), produto.getPreco(), produto.getQtdEstoque());
    }

    public static List<ProdutoDTO> getProdutosDTO() {
        return List.of(
                new ProdutoDTO(UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"), "Produto 1", "Descrição do Produto 1", 10.50, 100),
                new ProdutoDTO(UUID.fromString("4e22b5fc-d4c2-4c50-aebf-1f935246ee0c"), "Produto 2", "Descrição do Produto 2", 20.75, 150),
                new ProdutoDTO(UUID.fromString("7fa6c49e-0ad2-4a1d-95a6-956e1534e3c6"), "Produto 3", "Descrição do Produto 3", 15.20, 80),
                new ProdutoDTO(UUID.fromString("9a1f4dd7-151e-4b33-8a60-30cf5e5f0dd0"), "Produto 4", "Descrição do Produto 4", 30.00, 200),
                new ProdutoDTO(UUID.fromString("c456d30c-6573-4e71-b6b0-6b6ebd5a5b9e"), "Produto 5", "Descrição do Produto 5", 25.99, 120)
        );
    }

    public static UserDetailsVO getUserDetailsVO() {
        UserDetailsVO userDetailsVO = new UserDetailsVO();
        userDetailsVO.setAuthorities(List.of(new AuthorityDTO("ROLE_ADMIN")));
        userDetailsVO.setUserId(UUID.fromString("9f97258c-b144-4f77-9c65-e2c25542be3e"));
        userDetailsVO.setUsername("ayla_barros@gmail.com");
        userDetailsVO.setAccountNonExpired(Boolean.TRUE);
        userDetailsVO.setAccountNonLocked(Boolean.TRUE);
        userDetailsVO.setCredentialsNonExpired(Boolean.TRUE);
        userDetailsVO.setEnabled(Boolean.TRUE);
        return userDetailsVO;
    }
}
