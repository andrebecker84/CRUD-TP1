package br.com.infnet.banco.service;

import br.com.infnet.banco.entity.Conta;
import br.com.infnet.banco.exception.ContaNaoEncontradaException;
import br.com.infnet.banco.repository.ContaRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContaServiceTest {

    private final ContaRepository contaRepository = mock(ContaRepository.class);
    private final ContaService contaService = new ContaService(contaRepository);

    @Test
    void deveListarContas() {
        when(contaRepository.findAll()).thenReturn(List.of(
                new Conta(1L, "A", BigDecimal.TEN)
        ));

        List<Conta> contas = contaService.buscarTodas();

        assertEquals(1, contas.size());
        assertEquals("A", contas.get(0).getTitular());
    }

    @Test
    void deveBuscarContaPorId() {
        when(contaRepository.findById(1L))
                .thenReturn(Optional.of(new Conta(1L, "A", BigDecimal.TEN)));

        Conta conta = contaService.buscarPorId(1L);

        assertEquals("A", conta.getTitular());
        assertEquals(BigDecimal.TEN, conta.getSaldo());
    }

    @Test
    void deveLancarExcecaoQuandoContaNaoExiste() {
        when(contaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                ContaNaoEncontradaException.class,
                () -> contaService.buscarPorId(99L)
        );
    }

    @Test
    void deveCriarContaValida() {
        when(contaRepository.save(any(Conta.class)))
                .thenAnswer(invocation -> {
                    Conta c = invocation.getArgument(0);
                    c.setId(10L);
                    return c;
                });

        Conta conta = contaService.criar("Teste", new BigDecimal("100.00"));

        assertNotNull(conta);
        assertEquals("Teste", conta.getTitular());
        assertEquals(new BigDecimal("100.00"), conta.getSaldo());
        verify(contaRepository, times(1)).save(any(Conta.class));
    }

    @Test
    void deveAlterarSaldo() {
        Conta contaExistente = new Conta(1L, "A", new BigDecimal("50.00"));
        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaExistente));
        when(contaRepository.save(any(Conta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Conta contaAtualizada = contaService.alterarSaldo(1L, new BigDecimal("200.00"));

        assertEquals(new BigDecimal("200.00"), contaAtualizada.getSaldo());
        verify(contaRepository, times(1)).save(any(Conta.class));
    }

    @Test
    void deveExcluirContaExistente() {
        when(contaRepository.existsById(1L)).thenReturn(true);

        contaService.excluir(1L);

        verify(contaRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoExcluirContaInexistente() {
        when(contaRepository.existsById(99L)).thenReturn(false);

        assertThrows(
                ContaNaoEncontradaException.class,
                () -> contaService.excluir(99L)
        );
    }
}
