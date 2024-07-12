package com.catalogoprodutosservice.feign.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
public class UserDetailsVO {
    private Collection<AuthorityDTO> authorities;
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

}
