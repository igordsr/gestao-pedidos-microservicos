package com.usuarioservice.dto;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@ToString
public class UserDetailsDTO {
    private Collection<? extends GrantedAuthority> authorities;
    private String username;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public UserDetailsDTO(final UserDetails userDetails) {
        this.authorities = userDetails.getAuthorities();
        this.username = userDetails.getUsername();
        this.accountNonExpired = userDetails.isAccountNonExpired();
        this.accountNonLocked = userDetails.isAccountNonLocked();
        this.credentialsNonExpired = userDetails.isCredentialsNonExpired();
        this.enabled = userDetails.isEnabled();
    }
}
