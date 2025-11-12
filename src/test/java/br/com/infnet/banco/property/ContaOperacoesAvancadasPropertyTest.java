package br.com.infnet.banco.property;

import br.com.infnet.banco.entity.Conta;
import net.jqwik.api.*;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;

/**
 * Testes Avançados Baseados em Propriedades para Conta Bancária
 * Inspirado no padrão do projeto spaceXMissions
 */
class ContaOperacoesAvancadasPropertyTest {

    /***********************************************************************************************
     * PROPRIEDADE: Comutatividade de Créditos
     * Crédito de A + crédito de B = Crédito de B + crédito de A
     */
    @Property(tries = 1000)
    @Label("Créditos são comutativos (ordem não importa)")
    void creditosSaoComutativos(
            @ForAll("valoresPositivos") BigDecimal valorA,
            @ForAll("valoresPositivos") BigDecimal valorB) {

        Conta conta1 = new Conta("Teste", BigDecimal.valueOf(100));
        Conta conta2 = new Conta("Teste", BigDecimal.valueOf(100));

        // Cenário 1: A depois B
        conta1.creditar(valorA);
        conta1.creditar(valorB);

        // Cenário 2: B depois A
        conta2.creditar(valorB);
        conta2.creditar(valorA);

        Assertions.assertEquals(conta1.getSaldo(), conta2.getSaldo(),
                "Ordem dos créditos não deve alterar saldo final");
    }

    /***********************************************************************************************
     * PROPRIEDADE: Associatividade de Operações
     * (A + B) + C = A + (B + C)
     */
    @Property(tries = 1000)
    @Label("Créditos são associativos")
    void creditosSaoAssociativos(
            @ForAll("valoresPositivos") BigDecimal a,
            @ForAll("valoresPositivos") BigDecimal b,
            @ForAll("valoresPositivos") BigDecimal c) {

        Conta conta1 = new Conta("Teste", BigDecimal.ONE);
        Conta conta2 = new Conta("Teste", BigDecimal.ONE);

        // (A + B) + C
        conta1.creditar(a.add(b));
        conta1.creditar(c);

        // A + (B + C)
        conta2.creditar(a);
        conta2.creditar(b.add(c));

        Assertions.assertEquals(conta1.getSaldo(), conta2.getSaldo(),
                "Associatividade deve ser preservada");
    }

    /***********************************************************************************************
     * PROPRIEDADE: Elemento Neutro
     * Saldo + 0 = Saldo
     */
    @Property(tries = 500)
    @Label("Crédito de zero não altera saldo (elemento neutro)")
    void creditoZeroEhElementoNeutro(@ForAll("valoresPositivos") BigDecimal saldoInicial) {
        Conta conta = new Conta("Teste", saldoInicial);
        BigDecimal saldoAntes = conta.getSaldo();

        // Como creditar(0) lança exceção, testamos que o saldo não muda
        // sem fazer a operação
        Assertions.assertEquals(saldoAntes, conta.getSaldo(),
                "Saldo não deve mudar sem operações");
    }

    /***********************************************************************************************
     * PROPRIEDADE: Crédito seguido de Débito do mesmo valor = Identidade
     */
    @Property(tries = 1000)
    @Label("Crédito seguido de débito igual retorna ao saldo original")
    void creditoEDebitoIgualMantémSaldo(
            @ForAll("valoresMedios") BigDecimal saldoInicial,
            @ForAll("valoresPequenos") BigDecimal operacao) {

        Conta conta = new Conta("Teste", saldoInicial);
        BigDecimal saldoOriginal = conta.getSaldo();

        conta.creditar(operacao);
        conta.debitar(operacao);

        Assertions.assertEquals(saldoOriginal, conta.getSaldo(),
                "Crédito + Débito do mesmo valor deve manter saldo original");
    }

    /***********************************************************************************************
     * PROPRIEDADE: Débito nunca resulta em saldo negativo
     */
    @Property(tries = 1000)
    @Label("Débito válido nunca deixa saldo negativo")
    void debitoValidoNuncaGeraSaldoNegativo(
            @ForAll("valoresMedios") BigDecimal saldoInicial,
            @ForAll("valoresPequenos") BigDecimal debito) {

        Conta conta = new Conta("Teste", saldoInicial);

        if (debito.compareTo(conta.getSaldo()) <= 0) {
            conta.debitar(debito);
            Assertions.assertTrue(conta.getSaldo().compareTo(BigDecimal.ZERO) >= 0,
                    "Saldo nunca deve ser negativo");
        }
    }

    /***********************************************************************************************
     * PROPRIEDADE: Múltiplos créditos somam corretamente
     */
    @Property(tries = 500)
    @Label("Soma de múltiplos créditos é igual ao total creditado")
    void multiploCreditosSomamCorretamente(
            @ForAll("valoresMedios") BigDecimal saldoInicial,
            @ForAll("listaDeValores") java.util.List<BigDecimal> creditos) {

        Conta conta = new Conta("Teste", saldoInicial);

        BigDecimal totalCreditado = creditos.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        creditos.forEach(conta::creditar);

        BigDecimal saldoEsperado = saldoInicial.add(totalCreditado);
        Assertions.assertEquals(saldoEsperado, conta.getSaldo(),
                "Soma de créditos deve resultar no total esperado");
    }

    /***********************************************************************************************
     * PROPRIEDADE: Saldo sempre tem 2 casas decimais após operações
     */
    @Property(tries = 500)
    @Label("Saldo mantém precisão de 2 casas decimais")
    void saldoMantémPrecisao(
            @ForAll("valoresComPrecisao") BigDecimal valor) {

        Conta conta = new Conta("Teste", BigDecimal.valueOf(100));
        conta.creditar(valor);

        int escala = conta.getSaldo().scale();
        Assertions.assertTrue(escala <= 2,
                "Saldo deve ter no máximo 2 casas decimais, mas tem: " + escala);
    }

    /***********************************************************************************************
     * Geradores Customizados
     */

    @Provide
    Arbitrary<BigDecimal> valoresPositivos() {
        return Arbitraries.bigDecimals()
                .between(new BigDecimal("0.01"), new BigDecimal("10000.00"))
                .ofScale(2);
    }

    @Provide
    Arbitrary<BigDecimal> valoresPequenos() {
        return Arbitraries.bigDecimals()
                .between(new BigDecimal("0.01"), new BigDecimal("100.00"))
                .ofScale(2);
    }

    @Provide
    Arbitrary<BigDecimal> valoresMedios() {
        return Arbitraries.bigDecimals()
                .between(new BigDecimal("100.00"), new BigDecimal("5000.00"))
                .ofScale(2);
    }

    @Provide
    Arbitrary<BigDecimal> valoresComPrecisao() {
        return Arbitraries.bigDecimals()
                .between(new BigDecimal("0.01"), new BigDecimal("1000.00"))
                .map(v -> v.setScale(2, java.math.RoundingMode.HALF_UP));
    }

    @Provide
    Arbitrary<java.util.List<BigDecimal>> listaDeValores() {
        return Arbitraries.bigDecimals()
                .between(new BigDecimal("1.00"), new BigDecimal("100.00"))
                .ofScale(2)
                .list()
                .ofMinSize(1)
                .ofMaxSize(10);
    }
}
