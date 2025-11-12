package br.com.infnet.banco.service;

import br.com.infnet.banco.entity.Conta;
import br.com.infnet.banco.exception.ContaNaoEncontradaException;
import br.com.infnet.banco.repository.ContaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ContaService {

    private final ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    // comandos (C de CQS)
    public Conta criar(String titular, BigDecimal saldoInicial) {
        Conta conta = new Conta(titular, saldoInicial);
        return contaRepository.save(conta);
    }

    public void excluir(Long id) {
        if (!contaRepository.existsById(id)) {
            throw new ContaNaoEncontradaException("Conta não encontrada para exclusão: " + id);
        }
        contaRepository.deleteById(id);
    }

    public Conta alterarSaldo(Long id, BigDecimal novoSaldo) {
        if (novoSaldo == null || novoSaldo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Saldo deve ser maior que zero");
        }
        Conta conta = buscarPorId(id);
        conta.setSaldo(novoSaldo.setScale(2, java.math.RoundingMode.HALF_UP));
        return contaRepository.save(conta);
    }

    // queries (Q de CQS)
    public Conta buscarPorId(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new ContaNaoEncontradaException("Conta não encontrada: " + id));
    }

    public List<Conta> buscarTodas() {
        return contaRepository.findAll();
    }

    // saída formatada para o CLI
    public void imprimirContasFormatadas() {
        List<Conta> contas = buscarTodas();

        if (contas.isEmpty()) {
            System.out.println("Nenhuma conta cadastrada.");
            return;
        }

        System.out.println("╭──────┬──────────────────────┬─────────────────╮");
        System.out.println("│ ID   │ Titular              │ Saldo           │");
        System.out.println("├──────┼──────────────────────┼─────────────────┤");
        contas.forEach(conta ->
                System.out.printf(
                        "│ %-4d │ %-20s │ R$ %-12s │%n",
                        conta.getId(),
                        conta.getTitular(),
                        String.format("%.2f", conta.getSaldo())
                )
        );
        System.out.println("╰──────┴──────────────────────┴─────────────────╯");
    }
}
