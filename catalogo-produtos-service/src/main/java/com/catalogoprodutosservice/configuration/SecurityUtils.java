package com.catalogoprodutosservice.configuration;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.UUID;

public class SecurityUtils {
    public static String getCurrentToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;
            if (authToken.getCredentials() instanceof String) {
                return (String) authToken.getCredentials();
            }
        }
        return null;
    }

    public static JwtUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            return (JwtUserDetails) authentication.getPrincipal();
        }
        return null;
    }

    public static UUID getCurrentUserUUID() {
        final JwtUserDetails currentUser = getCurrentUser();
        if(!Objects.isNull(currentUser)){
            return currentUser.getUserUUID();
        }
        return null;
    }
}
