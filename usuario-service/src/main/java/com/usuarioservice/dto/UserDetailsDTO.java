package com.usuarioservice.dto;

import com.usuarioservice.jwt.JwtUserDetails;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

@Getter
@ToString
public class UserDetailsDTO {
    private Collection<? extends GrantedAuthority> authorities;
    private UUID userId;
    private String username;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public UserDetailsDTO(final JwtUserDetails userDetails) {
        this.userId = userDetails.getUsuario().getId();
        this.authorities = userDetails.getAuthorities();
        this.username = userDetails.getUsername();
        this.accountNonExpired = userDetails.isAccountNonExpired();
        this.accountNonLocked = userDetails.isAccountNonLocked();
        this.credentialsNonExpired = userDetails.isCredentialsNonExpired();
        this.enabled = userDetails.isEnabled();
    }
}
