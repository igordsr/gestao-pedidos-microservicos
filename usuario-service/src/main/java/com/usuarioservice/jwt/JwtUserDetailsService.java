package com.usuarioservice.jwt;

import com.usuarioservice.model.Role;
import com.usuarioservice.model.Usuario;
import com.usuarioservice.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UsuarioService usuarioService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Usuario usuario = usuarioService.buscarPorUsername(username);
        return new JwtUserDetails(usuario);
    }

    public JwtToken getTokenAuthenticated(String username){
        final Usuario usuario = usuarioService.buscarPorUsername(username);
        return JwtUtils.createToken(username, usuario.getRole().name().substring("ROLE".length()));
    }
}
