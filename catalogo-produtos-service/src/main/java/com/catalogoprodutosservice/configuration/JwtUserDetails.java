package com.catalogoprodutosservice.configuration;

import com.catalogoprodutosservice.feign.vo.UserDetailsVO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

public class JwtUserDetails implements UserDetails {
    private UserDetailsVO usuario;

    public JwtUserDetails(UserDetailsVO userDetailsVO) {
        this.usuario = userDetailsVO;
    }


    public UUID getUserUUID() {
        return this.usuario.getUserId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.usuario.getAuthorities();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.usuario.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.usuario.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.usuario.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.usuario.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.usuario.isEnabled();
    }
}
