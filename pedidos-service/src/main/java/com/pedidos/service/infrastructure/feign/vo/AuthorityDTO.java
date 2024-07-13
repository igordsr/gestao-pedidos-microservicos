package com.pedidos.service.infrastructure.feign.vo;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDTO implements GrantedAuthority {
    private String authority;
}
