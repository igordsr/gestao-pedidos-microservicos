package com.pedidos.service.infrastructure.config;


import com.pedidos.service.infrastructure.feign.vo.UserDetailsVO;
import org.springframework.security.core.userdetails.User;

import java.security.Principal;
import java.util.UUID;

public class JwtUserDetails extends User implements Principal {
    private UserDetailsVO usuario;

    public JwtUserDetails(UserDetailsVO userDetailsVO) {
        super(userDetailsVO.getUsername(), UUID.randomUUID().toString(), userDetailsVO.getAuthorities());
        this.usuario = userDetailsVO;
    }

    public UUID getId() {
        return usuario.getUserId();
    }

    @Override
    public String getName() {
        return usuario.getUsername();
    }
}
