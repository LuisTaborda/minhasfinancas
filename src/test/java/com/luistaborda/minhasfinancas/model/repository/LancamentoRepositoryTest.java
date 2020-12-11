package com.luistaborda.minhasfinancas.model.repository;

import com.luistaborda.minhasfinancas.model.entity.Lancamento;
import com.luistaborda.minhasfinancas.model.enums.StatusLancamento;
import com.luistaborda.minhasfinancas.model.enums.TipoLancamento;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LancamentoRepositoryTest {

    @Autowired
    LancamentoRepository lancamentoRepository;
    @Autowired
    TestEntityManager entityManager;


    @Test
    public void deveSalvarUmLancamento() {
        Lancamento lancamento = criarLancamento();
        lancamento = lancamentoRepository.save(lancamento);
        assertThat(lancamento.getId()).isNotNull();
    }

    @Test
    public void deveDeletarUmLancamento() {
        Lancamento lancamento = criarEPersistirUmLancamento();

        lancamento = entityManager.find(Lancamento.class, lancamento.getId());

        lancamentoRepository.delete(lancamento);

        Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());
        assertThat(lancamentoInexistente).isNull();
    }

    @Test
    public void deveAtualizarUmLancamento() {
        Lancamento lancamento = criarEPersistirUmLancamento();

        lancamento.setAno(2018);
        lancamento.setDescricao("teste atualizar");
        lancamento.setStatus(StatusLancamento.CANCELADO);

        lancamentoRepository.save(lancamento);

        Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());

        assertThat(lancamentoAtualizado.getAno()).isEqualTo(2018);
        assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("teste atualizar");
        assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
    }

    @Test
    public void deveBuscarUmLancamentoPorId() {
        Lancamento lancamento = criarEPersistirUmLancamento();

        Optional<Lancamento> lancamentoEncontrado = lancamentoRepository.findById(lancamento.getId());

        assertThat(lancamentoEncontrado.isPresent()).isTrue();
    }

    public Lancamento criarEPersistirUmLancamento() {
        Lancamento lancamento = criarLancamento();
        entityManager.persist(lancamento);
        return lancamento;
    }

    public static Lancamento criarLancamento() {
        return Lancamento
                .builder()
                .ano(2019)
                .mes(10)
                .descricao("qualquer")
                .valor(BigDecimal.valueOf(10))
                .tipo(TipoLancamento.RECEITA)
                .status(StatusLancamento.PENDENTE)
                .dataCadastro(LocalDate.now())
                .build();
    }
}
