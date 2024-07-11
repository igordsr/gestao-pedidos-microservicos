package com.usuarioservice.repository;

import com.usuarioservice.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByEmailOrCpf(String email, String cpf);

    List<Usuario> findByStatusTrue();

    Optional<Usuario> findByEmail(String username);
}
