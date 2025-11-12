package br.com.infnet.banco.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "conta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titular;

    @Column(nullable = false)
    private BigDecimal saldo;

    public Conta(String titular, BigDecimal saldo) {
        validarSaldoInicial(saldo);
        this.titular = titular;
        this.saldo = saldo.setScale(2);
    }

    public void creditar(BigDecimal valor) {
        validarValorPositivo(valor);
        this.saldo = this.saldo.add(valor);
    }

    public void debitar(BigDecimal valor) {
        validarValorPositivo(valor);
        if (valor.compareTo(this.saldo) > 0) {
            throw new br.com.infnet.banco.exception.SaldoInsuficienteException();
        }
        this.saldo = this.saldo.subtract(valor);
    }

    private void validarSaldoInicial(BigDecimal saldoInicial) {
        if (saldoInicial == null || saldoInicial.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Saldo inicial deve ser maior que zero");
        }
    }

    private void validarValorPositivo(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
    }

    @Override
    public String toString() {
        return String.format(
                """
                ╭─────┬──────────────────────┬───────────────╮
                │ ID  │ Titular              │ Saldo         │
                ├─────┼──────────────────────┼───────────────┤
                │ %-3d │ %-20s │ R$ %-10s │
                ╰─────┴──────────────────────┴───────────────╯
                """,
                id,
                titular,
                String.format("%.2f", saldo)
        );
    }
}
