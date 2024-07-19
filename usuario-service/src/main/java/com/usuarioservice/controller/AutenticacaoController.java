package com.usuarioservice.controller;

import com.usuarioservice.dto.UserDetailsDTO;
import com.usuarioservice.dto.UsuarioLoginDTO;
import com.usuarioservice.jwt.JwtToken;
import com.usuarioservice.jwt.JwtUserDetails;
import com.usuarioservice.jwt.JwtUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


@Slf4j
@RestController()
public class AutenticacaoController {
    private final JwtUserDetailsService jwtUserDetailsService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AutenticacaoController(JwtUserDetailsService jwtUserDetailsService, AuthenticationManager authenticationManager) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtToken> autenticar(@RequestBody @Valid UsuarioLoginDTO dto, HttpServletRequest request) {
        log.info("Processo de Autenticação pelo login {}", dto.getEmail());
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
            this.authenticationManager.authenticate(authenticationToken);
            final JwtToken tokenAuthenticated = this.jwtUserDetailsService.getTokenAuthenticated(dto.getEmail());
            return ResponseEntity.ok(tokenAuthenticated);
        } catch (AuthenticationException ex) {
            log.warn("Bad Credentials from user '{}'", dto.getEmail());
        }
        return ResponseEntity.badRequest().build();
    }


    @GetMapping(value = "/auth", produces = "application/json")
    @Operation(summary = "Listar todos os usuarios", description = "Recupera as permissões do usuário por meio do token", method = "GET", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserDetailsDTO> autenticar() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!Objects.isNull(authentication) && authentication.isAuthenticated()) {
            final JwtUserDetails principal = (JwtUserDetails) authentication.getPrincipal();
            final UserDetailsDTO userDetailsDTO = new UserDetailsDTO(principal);
            return ResponseEntity.ok(userDetailsDTO);
        }
        return ResponseEntity.badRequest().build();
    }
}
