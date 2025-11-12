package br.com.infnet.banco.property;

import br.com.infnet.banco.entity.Conta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de Limites e Partições para Conta Bancária
 * Baseado em Boundary Value Analysis e Equivalence Partitioning
 */
@DisplayName("Testes de Limites e Partições - Conta Bancária")
class ContaLimitesEParticoesTest {

    private Conta conta;

    @BeforeEach
    void setUp() {
        // Conta com saldo inicial de R$ 1000,00
        conta = new Conta("João Silva", new BigDecimal("1000.00"));
    }

    @Nested
    @DisplayName("Limites de Saldo Inicial")
    class LimitesDeSaldoInicial {

        @Test
        @DisplayName("Saldo 0.00 - deve lançar exceção (off-point)")
        void saldoZero_deveLancarExcecao() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Conta("Teste", BigDecimal.ZERO),
                    "Saldo zero não é permitido");
        }

        @Test
        @DisplayName("Saldo 0.01 - deve aceitar (on-point mínimo)")
        void saldoUmCentavo_deveAceitar() {
            Conta novaConta = new Conta("Teste", new BigDecimal("0.01"));
            assertEquals(new BigDecimal("0.01"), novaConta.getSaldo());
        }

        @Test
        @DisplayName("Saldo -0.01 - deve lançar exceção (off-point negativo)")
        void saldoNegativo_deveLancarExcecao() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Conta("Teste", new BigDecimal("-0.01")),
                    "Saldo negativo não é permitido");
        }

        @Test
        @DisplayName("Saldo 999999.99 - deve aceitar (valor muito alto)")
        void saldoMuitoAlto_deveAceitar() {
            Conta novaConta = new Conta("Teste", new BigDecimal("999999.99"));
            assertEquals(new BigDecimal("999999.99"), novaConta.getSaldo());
        }
    }

    @Nested
    @DisplayName("Partições de Operações de Crédito")
    class ParticoesDeCredito {

        @Test
        @DisplayName("Crédito de valor pequeno (1.00) - deve aumentar saldo")
        void creditoPequeno_deveAumentarSaldo() {
            BigDecimal saldoAntes = conta.getSaldo();
            conta.creditar(new BigDecimal("1.00"));
            assertEquals(saldoAntes.add(new BigDecimal("1.00")), conta.getSaldo());
        }

        @Test
        @DisplayName("Crédito de valor médio (500.00) - deve aumentar saldo")
        void creditoMedio_deveAumentarSaldo() {
            BigDecimal saldoAntes = conta.getSaldo();
            conta.creditar(new BigDecimal("500.00"));
            assertEquals(saldoAntes.add(new BigDecimal("500.00")), conta.getSaldo());
        }

        @Test
        @DisplayName("Crédito de valor grande (10000.00) - deve aumentar saldo")
        void creditoGrande_deveAumentarSaldo() {
            BigDecimal saldoAntes = conta.getSaldo();
            conta.creditar(new BigDecimal("10000.00"));
            assertEquals(saldoAntes.add(new BigDecimal("10000.00")), conta.getSaldo());
        }

        @Test
        @DisplayName("Crédito de zero - deve lançar exceção")
        void creditoZero_deveLancarExcecao() {
            assertThrows(IllegalArgumentException.class,
                    () -> conta.creditar(BigDecimal.ZERO),
                    "Crédito de zero não é permitido");
        }

        @Test
        @DisplayName("Crédito negativo - deve lançar exceção")
        void creditoNegativo_deveLancarExcecao() {
            assertThrows(IllegalArgumentException.class,
                    () -> conta.creditar(new BigDecimal("-50.00")),
                    "Crédito negativo não é permitido");
        }
    }

    @Nested
    @DisplayName("Partições de Operações de Débito")
    class ParticoesDeDebito {

        @Test
        @DisplayName("Débito menor que saldo (100.00) - deve diminuir saldo")
        void debitoMenorQueSaldo_deveDiminuirSaldo() {
            BigDecimal saldoAntes = conta.getSaldo();
            conta.debitar(new BigDecimal("100.00"));
            assertEquals(saldoAntes.subtract(new BigDecimal("100.00")), conta.getSaldo());
        }

        @Test
        @DisplayName("Débito igual ao saldo (1000.00) - deve zerar saldo")
        void debitoIgualAoSaldo_deveZerarSaldo() {
            conta.debitar(new BigDecimal("1000.00"));
            assertEquals(new BigDecimal("0.00"), conta.getSaldo());
        }

        @Test
        @DisplayName("Débito maior que saldo (1500.00) - deve lançar exceção")
        void debitoMaiorQueSaldo_deveLancarExcecao() {
            assertThrows(br.com.infnet.banco.exception.SaldoInsuficienteException.class,
                    () -> conta.debitar(new BigDecimal("1500.00")),
                    "Débito maior que saldo não é permitido");
        }

        @Test
        @DisplayName("Débito de zero - deve lançar exceção")
        void debitoZero_deveLancarExcecao() {
            assertThrows(IllegalArgumentException.class,
                    () -> conta.debitar(BigDecimal.ZERO),
                    "Débito de zero não é permitido");
        }

        @Test
        @DisplayName("Débito negativo - deve lançar exceção")
        void debitoNegativo_deveLancarExcecao() {
            assertThrows(IllegalArgumentException.class,
                    () -> conta.debitar(new BigDecimal("-50.00")),
                    "Débito negativo não é permitido");
        }
    }

    @Nested
    @DisplayName("Limites de Operações Sequenciais")
    class LimitesDeOperacoesSequenciais {

        @Test
        @DisplayName("Crédito seguido de débito total - saldo deve voltar ao original")
        void creditoSeguidoDeDebitoTotal_saldoVoltaAoOriginal() {
            BigDecimal saldoOriginal = conta.getSaldo();
            conta.creditar(new BigDecimal("500.00"));
            conta.debitar(new BigDecimal("500.00"));
            assertEquals(saldoOriginal, conta.getSaldo());
        }

        @Test
        @DisplayName("Múltiplos créditos pequenos - saldo deve acumular corretamente")
        void multiploCreditosPequenos_saldoAcumulaCorretamente() {
            conta.creditar(new BigDecimal("10.00"));
            conta.creditar(new BigDecimal("20.00"));
            conta.creditar(new BigDecimal("30.00"));
            assertEquals(new BigDecimal("1060.00"), conta.getSaldo());
        }

        @Test
        @DisplayName("Débitos sequenciais até limite - deve aceitar todos")
        void debitosSequenciaisAteLimite_deveAceitarTodos() {
            conta.debitar(new BigDecimal("300.00"));
            conta.debitar(new BigDecimal("300.00"));
            conta.debitar(new BigDecimal("400.00"));
            assertEquals(new BigDecimal("0.00"), conta.getSaldo());
        }

        @Test
        @DisplayName("Débito após esgotar saldo - deve lançar exceção")
        void debitoAposEsgotarSaldo_deveLancarExcecao() {
            conta.debitar(new BigDecimal("1000.00"));
            assertThrows(br.com.infnet.banco.exception.SaldoInsuficienteException.class,
                    () -> conta.debitar(new BigDecimal("0.01")),
                    "Débito com saldo zerado não é permitido");
        }
    }

    @Nested
    @DisplayName("Validação de Titular")
    class ValidacaoDeTitular {

        @Test
        @DisplayName("Titular válido - deve criar conta")
        void titularValido_deveCriarConta() {
            Conta novaConta = new Conta("Maria Santos", new BigDecimal("100.00"));
            assertEquals("Maria Santos", novaConta.getTitular());
        }

        @Test
        @DisplayName("Titular com nome composto - deve criar conta")
        void titularNomeComposto_deveCriarConta() {
            Conta novaConta = new Conta("João Pedro da Silva", new BigDecimal("250.00"));
            assertEquals("João Pedro da Silva", novaConta.getTitular());
        }

        @Test
        @DisplayName("Titular com nome curto - deve criar conta")
        void titularNomeCurto_deveCriarConta() {
            Conta novaConta = new Conta("Ana", new BigDecimal("50.00"));
            assertEquals("Ana", novaConta.getTitular());
        }
    }
}
