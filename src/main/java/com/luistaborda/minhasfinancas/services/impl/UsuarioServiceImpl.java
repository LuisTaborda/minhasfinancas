package com.luistaborda.minhasfinancas.services.impl;

import com.luistaborda.minhasfinancas.exception.RegraNegocioException;
import com.luistaborda.minhasfinancas.model.entity.Usuario;
import com.luistaborda.minhasfinancas.model.repository.UsuarioRepository;
import com.luistaborda.minhasfinancas.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired private UsuarioRepository repository;

    public UsuarioServiceImpl(UsuarioRepository repository) {
        super();
        this.repository = repository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        return null;
    }

    @Override
    public Usuario salvarUsuario(Usuario usuario) {
        return null;
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if(existe){
            throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
        }
    }
}
