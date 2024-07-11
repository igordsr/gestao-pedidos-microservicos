package com.usuarioservice.jwt;

import com.usuarioservice.model.Usuario;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.UUID;

public class JwtUserDetails extends User {
    private final Usuario usuario;
    public JwtUserDetails(Usuario usuario) {
        super(usuario.getEmail(), usuario.getPassword(), AuthorityUtils.createAuthorityList(usuario.getRole().name()));
        this.usuario = usuario;
    }

    public UUID getId(){
        return usuario.getId();
    }

    public String getRole(){
        return usuario.getRole().name();
    }
}
