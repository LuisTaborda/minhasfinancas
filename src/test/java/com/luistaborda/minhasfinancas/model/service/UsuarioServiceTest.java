package com.luistaborda.minhasfinancas.model.service;


import com.luistaborda.minhasfinancas.exception.ErroAutenticacao;
import com.luistaborda.minhasfinancas.exception.RegraNegocioException;
import com.luistaborda.minhasfinancas.model.entity.Usuario;
import com.luistaborda.minhasfinancas.model.repository.UsuarioRepository;
import com.luistaborda.minhasfinancas.services.UsuarioService;
import com.luistaborda.minhasfinancas.services.impl.UsuarioServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @MockBean UsuarioRepository usuarioRepository;
    @SpyBean UsuarioService usuarioService;

    @Test(expected = Test.None.class)
    public void deveSalvarUmUsuario(){
        Mockito.doNothing().when(usuarioService).validarEmail(Mockito.anyString());
        Usuario usuario = Usuario.builder().id(1L).nome("nome").email("email@email.com").senha("senha").build();

        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        Usuario usuarioSalvo = usuarioService.salvarUsuario(new Usuario());

        Assertions.assertThat(usuarioSalvo).isNotNull();
        Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1L);
        Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
        Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
        Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
    }

    @Test(expected = RegraNegocioException.class)
    public void naoDeveSalvarUmUsuarioComEmailJaCadastrado(){
        String email = "email@email.com";
        Usuario usuario = Usuario.builder().email(email).build();
        Mockito.doThrow(RegraNegocioException.class).when(usuarioService).validarEmail(email);

        usuarioService.salvarUsuario(usuario);

        Mockito.verify(usuarioRepository, Mockito.never()).save(usuario);
    }


    @Test(expected = Test.None.class)
    public void deveAutenticarUmUsuarioComSucesso(){
        String email = "email@email.com";
        String senha = "senha";

        Usuario usuario = Usuario.builder().email(email).senha(senha).id(1L).build();
        Mockito.when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.autenticar(email, senha);

        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComEmailInformado(){

        Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        Throwable exception = Assertions.catchThrowable( () -> usuarioService.autenticar("email@email.com","senha"));
        Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuário não encontrado para o e-mail informado.");
    }

    @Test
    public void deveLancarErroQuandoSenhaNaoBater(){

        String senha = "senha";
        Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();

        Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

        Throwable exception = Assertions.catchThrowable( () -> usuarioService.autenticar("email@email.com","123"));
        Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida.");
    }


    @Test(expected = Test.None.class)
    public void deveValidarEmail() {

        Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        usuarioService.validarEmail("email@email.com");
    }

    @Test(expected = RegraNegocioException.class)
    public void deveLancarErroQuandoExistirEmailCadastro() {

        Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(true);
        usuarioService.validarEmail("email@email.com");

    }

}
