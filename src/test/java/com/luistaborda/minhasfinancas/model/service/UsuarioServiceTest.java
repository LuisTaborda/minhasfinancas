package com.luistaborda.minhasfinancas.model.service;


import com.luistaborda.minhasfinancas.exception.RegraNegocioException;
import com.luistaborda.minhasfinancas.model.repository.UsuarioRepository;
import com.luistaborda.minhasfinancas.services.UsuarioService;
import com.luistaborda.minhasfinancas.services.impl.UsuarioServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @MockBean UsuarioRepository usuarioRepository;
    UsuarioService usuarioService;

    @Before
    public void setup() {
        usuarioService = new UsuarioServiceImpl(usuarioRepository);
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
