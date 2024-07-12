package com.catalogoprodutosservice.controller;

import com.catalogoprodutosservice.feign.UsuarioServiceClient;
import com.catalogoprodutosservice.feign.vo.UserDetailsVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(value = "/teste", produces = {"application/json"})
@Tag(name = "teste")
public class TesteController {
    private final UsuarioServiceClient usuarioServiceClient;

    @Autowired
    public TesteController(UsuarioServiceClient usuarioServiceClient) {
        this.usuarioServiceClient = usuarioServiceClient;
    }

}
