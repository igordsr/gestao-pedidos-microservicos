package com.usuarioservice.service;

import com.usuarioservice.controller.exception.modal.RegistroJaExisteException;
import com.usuarioservice.controller.exception.modal.RegistroNaoEncontradoException;
import com.usuarioservice.dto.UsuarioDTO;
import com.usuarioservice.model.Usuario;
import com.usuarioservice.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(final UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioDTO cadastrar(final UsuarioDTO usuarioDTO) {
        Assert.notNull(usuarioDTO, "O objeto usuarioDTO não pode ser null");
        Optional<Usuario> usuarioOptional = this.usuarioRepository.findByEmailOrCpf(usuarioDTO.email(), usuarioDTO.cpf());
        if (usuarioOptional.isPresent()) {
            throw new RegistroJaExisteException(usuarioOptional.get().getNome());
        }
        final Usuario usuario = usuarioRepository.save(usuarioDTO.toUsuario());
        return UsuarioDTO.getInstance(usuario);
    }

    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepository.findByStatusTrue().stream().map(UsuarioDTO::getInstance).toList();
    }

    public UsuarioDTO encontrarUsuarioPorId(UUID id) {
        final Usuario usuario = this.findById(id);
        return UsuarioDTO.getInstance(usuario);
    }

    public UsuarioDTO atualizarUsuario(final UUID id, final UsuarioDTO usuarioDTO) {
        Assert.notNull(usuarioDTO, "O objeto usuarioDTO não pode ser null");
        Usuario usuario = this.findById(id);
        usuario.setNome(usuarioDTO.nome());
        usuario.setCep(usuarioDTO.cep());
        usuario.setLogradouro(usuarioDTO.logradouro());
        usuario.setComplemento(usuarioDTO.complemento());
        usuario.setBairro(usuarioDTO.bairro());
        usuario.setNumero(usuarioDTO.numero());
        usuario.setTelefone(usuarioDTO.telefone());
        usuario.setEmail(usuarioDTO.email());
        usuario.setDataNascimento(usuarioDTO.dataNascimento());
        usuario.setCpf(usuarioDTO.cpf());
        this.usuarioRepository.save(usuario);
        return UsuarioDTO.getInstance(usuario);
    }

    public void deletarUsuario(UUID id) {
        Assert.notNull(id, "O objeto id não pode ser null");
        Usuario usuario = this.findById(id);
        usuario.setStatus(Boolean.FALSE);
        this.usuarioRepository.save(usuario);
    }

    private Usuario findById(UUID id) {
        Assert.notNull(id, "O objeto id não pode ser null");
        return this.usuarioRepository.findById(id).orElseThrow(() -> new RegistroNaoEncontradoException(id.toString()));
    }
}
