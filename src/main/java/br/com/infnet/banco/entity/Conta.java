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
        this.titular = titular;
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return String.format(
                """
                ╭─────┬──────────────────────┬───────────────╮
                │ ID  │ Titular              │ Saldo         │
                ├─────┼──────────────────────┼───────────────┤
                │ %-3d │ %-20s │ R$ %-10.2f │
                ╰─────┴──────────────────────┴───────────────╯
                """,
                id,
                titular,
                saldo.doubleValue()
        );
    }
}
