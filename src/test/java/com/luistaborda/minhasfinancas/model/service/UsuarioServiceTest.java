package com.luistaborda.minhasfinancas.model.service;


import com.luistaborda.minhasfinancas.exception.RegraNegocioException;
import com.luistaborda.minhasfinancas.model.entity.Usuario;
import com.luistaborda.minhasfinancas.model.repository.UsuarioRepository;
import com.luistaborda.minhasfinancas.services.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @Autowired UsuarioService usuarioService;
    @Autowired UsuarioRepository usuarioRepository;

    @Test(expected = Test.None.class)
    public void deveValidarEmail(){
        usuarioRepository.deleteAll();

        usuarioService.validarEmail("email@email.com");
    }

    @Test(expected = RegraNegocioException.class)
    public void deveLancarErroQuandoExistirEmailCadastro(){

        Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").build();

        usuarioRepository.save(usuario);

        usuarioService.validarEmail("email@email.com");

    }
}
