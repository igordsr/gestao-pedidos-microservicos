package com.pedidos.service.infrastructure.feign.vo;

import com.pedidos.service.infrastructure.config.JwtUserDetails;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class UserDetailsVO {
    private Collection<AuthorityDTO> authorities;
    private UUID userId;
    private String username;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public Collection<AuthorityDTO> getRoles() {
        List<AuthorityDTO> roles = new ArrayList<>();
        if (authorities != null) {
            roles = authorities.stream()
                    .map(AuthorityDTO::getAuthority)
                    .map(authority -> authority.substring("ROLE_".length()))
                    .map(AuthorityDTO::new)
                    .toList();
        }
        return roles;
    }

    public UUID getUserId() {
        return userId;
    }

    public JwtUserDetails toJwtUserDetails() {
        return new JwtUserDetails(this);
    }
}
