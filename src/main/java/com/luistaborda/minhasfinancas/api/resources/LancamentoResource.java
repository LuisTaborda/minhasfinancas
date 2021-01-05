package com.luistaborda.minhasfinancas.api.resources;

import com.luistaborda.minhasfinancas.api.dto.AtualizaStatusDTO;
import com.luistaborda.minhasfinancas.api.dto.LancamentoDTO;
import com.luistaborda.minhasfinancas.exception.RegraNegocioException;
import com.luistaborda.minhasfinancas.model.entity.Lancamento;
import com.luistaborda.minhasfinancas.model.entity.Usuario;
import com.luistaborda.minhasfinancas.model.enums.StatusLancamento;
import com.luistaborda.minhasfinancas.model.enums.TipoLancamento;
import com.luistaborda.minhasfinancas.services.LancamentoService;
import com.luistaborda.minhasfinancas.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoResource {

    private final LancamentoService lancamentoService;
    private final UsuarioService usuarioService;


    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
        try {
            Lancamento entidade = converter(dto);
            entidade = lancamentoService.salvar(entidade);
            return new ResponseEntity(entidade, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {
        return lancamentoService.obterPorId(id).map(entity -> {
            try {
                Lancamento lancamento = converter(dto);
                lancamento.setId(entity.getId());
                lancamentoService.atualizar(lancamento);
                return ResponseEntity.ok(lancamento);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados.", HttpStatus.BAD_REQUEST));
    }

    @PutMapping("{id}/atualiza-status")
    public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizaStatusDTO dto) {
        return lancamentoService.obterPorId(id).map(entity -> {
            StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());
            if (statusSelecionado == null) {
                return ResponseEntity.badRequest().body("Não foi possivel atualizar o status do lançamento, envie um status valido");
            }
            try {
                entity.setStatus(statusSelecionado);
                lancamentoService.atualizar(entity);
                return ResponseEntity.ok(entity);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados.", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id) {
        return lancamentoService.obterPorId(id).map(entity -> {
            lancamentoService.deletar(entity);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados.", HttpStatus.BAD_REQUEST));
    }

    @GetMapping
    public ResponseEntity buscar(
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam(value = "mes", required = false) Integer mes,
            @RequestParam(value = "ano", required = false) Integer ano,
            @RequestParam(value = "tipo", required = false) TipoLancamento tipoLancamento,
            @RequestParam("usuario") Long idUsuario
    ) {
        Lancamento lancamentoFiltro = new Lancamento();
        lancamentoFiltro.setDescricao(descricao);
        lancamentoFiltro.setMes(mes);
        lancamentoFiltro.setAno(ano);
        lancamentoFiltro.setTipo(tipoLancamento);

        Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
        if (!usuario.isPresent()) {
            return ResponseEntity.badRequest().body("Não foi possivel realizar a consulta. Usuário não encontrado para o id informado");
        } else {
            lancamentoFiltro.setUsuario(usuario.get());
        }

        List<Lancamento> lancamentos = lancamentoService.buscar(lancamentoFiltro);
        return ResponseEntity.ok(lancamentos);
    }

    private Lancamento converter(LancamentoDTO dto) {
        Lancamento lancamento = new Lancamento();
        lancamento.setId(dto.getId());
        lancamento.setAno(dto.getAno());
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setMes(dto.getMes());
        lancamento.setValor(dto.getValor());

        Usuario usuario = usuarioService.obterPorId(dto.getUsuario()).orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o id informado"));

        lancamento.setUsuario(usuario);

        if (dto.getTipo() != null) {
            lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
        }
        if (dto.getStatus() != null) {
            lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
        }
        return lancamento;
    }
}
