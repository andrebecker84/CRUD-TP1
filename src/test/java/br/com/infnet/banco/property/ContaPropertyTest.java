package br.com.infnet.banco.property;

import br.com.infnet.banco.entity.Conta;
import net.jqwik.api.*;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;

class ContaPropertyTest {

    /***********************************************************************************************
     * Garante que qualquer conta criada com saldo positivo
     * mantenha o saldo maior que zero após a inicialização.
     */
    @Property
    void criarContaSempreComSaldoPositivo(@ForAll("saldosPositivos") BigDecimal saldo) {
        Conta conta = new Conta("Usuário Teste", saldo);
        Assertions.assertTrue(
                conta.getSaldo().compareTo(BigDecimal.ZERO) > 0,
                () -> "Saldo inválido: " + saldo
        );
    }

    /***********************************************************************************************
     * Garante que o construtor rejeita saldos negativos ou nulos.
     */
    @Property
    void naoPermiteSaldoNegativoOuNulo(@ForAll("saldosInvalidos") BigDecimal saldo) {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Conta("Usuário Inválido", saldo),
                "A conta deveria rejeitar saldo " + saldo
        );
    }

    /***********************************************************************************************
     * Garante que operações de crédito aumentam o saldo.
     */
    @Property
    void creditarAumentaSaldo(@ForAll("saldosPositivos") BigDecimal valor) {
        Conta conta = new Conta("Teste Crédito", BigDecimal.valueOf(100));
        conta.creditar(valor);
        Assertions.assertTrue(conta.getSaldo().compareTo(BigDecimal.valueOf(100)) > 0);
    }

    /***********************************************************************************************
     * Garante que o débito nunca permita saldo negativo.
     */
    @Property
    void debitarNuncaDeixaSaldoNegativo(@ForAll("valoresDebito") BigDecimal valor) {
        Conta conta = new Conta("Teste Débito", BigDecimal.valueOf(200));
        if (valor.compareTo(conta.getSaldo()) <= 0) {
            conta.debitar(valor);
            Assertions.assertTrue(conta.getSaldo().compareTo(BigDecimal.ZERO) >= 0);
        } else {
            Assertions.assertThrows(br.com.infnet.banco.exception.SaldoInsuficienteException.class,
                () -> conta.debitar(valor));
        }
    }

    /***********************************************************************************************
     * Geradores customizados
     */
    @Provide
    Arbitrary<BigDecimal> saldosPositivos() {
        return Arbitraries.bigDecimals()
                .greaterThan(BigDecimal.ZERO)
                .lessThan(BigDecimal.valueOf(1_000_000))
                .ofScale(2);
    }

    @Provide
    Arbitrary<BigDecimal> saldosInvalidos() {
        return Arbitraries.of(
                BigDecimal.ZERO,
                BigDecimal.valueOf(-1),
                BigDecimal.valueOf(-9999)
        );
    }

    @Provide
    Arbitrary<BigDecimal> valoresDebito() {
        return Arbitraries.bigDecimals()
                .greaterThan(BigDecimal.ZERO)
                .lessThan(BigDecimal.valueOf(500))
                .ofScale(2);
    }
}
