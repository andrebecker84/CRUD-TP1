package br.com.infnet.banco;

import br.com.infnet.banco.entity.Conta;
import br.com.infnet.banco.exception.ContaNaoEncontradaException;
import br.com.infnet.banco.service.ContaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.Scanner;

@SpringBootApplication
public class CrudTp1Application implements CommandLineRunner {

    private final ContaService contaService;

    public CrudTp1Application(ContaService contaService) {
        this.contaService = contaService;
    }

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "h2");
        SpringApplication.run(CrudTp1Application.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("╭───────────────────────────────────────────────╮");
        System.out.println("│     Bem-vindo ao Sistema Bancário Becker!     │");
        System.out.println("│   Gerenciamento de Contas Bancárias via CLI.  │");
        System.out.println("╰───────────────────────────────────────────────╯");

        try (Scanner scanner = new Scanner(System.in)) {
            int opcao;
            do {
                exibirMenu();
                opcao = lerInteiro(scanner, "Informe a opção: ");
                tratarOpcao(scanner, opcao);
            } while (opcao != 0);
        }
    }

    private void exibirMenu() {
        System.out.println("╭───────────────────────────────────────────────╮");
        System.out.println("│ 1 - Listar contas                             │");
        System.out.println("│ 2 - Consultar conta por ID                    │");
        System.out.println("│ 3 - Criar nova conta                          │");
        System.out.println("│ 4 - Alterar saldo da conta                    │");
        System.out.println("│ 5 - Excluir conta                             │");
        System.out.println("│ 0 - Sair                                      │");
        System.out.println("╰───────────────────────────────────────────────╯");
    }

    private void tratarOpcao(Scanner scanner, int opcao) {
        try {
            switch (opcao) {
                case 1 -> contaService.imprimirContasFormatadas();
                case 2 -> consultarPorId(scanner);
                case 3 -> criarConta(scanner);
                case 4 -> alterarSaldo(scanner);
                case 5 -> excluirConta(scanner);
                case 0 -> System.out.println("Encerrando o sistema. Até logo!");
                default -> System.out.println("Opção inválida.");
            }
        } catch (ContaNaoEncontradaException e) {
            // aqui a gente trata e não deixa o app cair
            System.out.println("⚠ " + e.getMessage());
        } catch (RuntimeException e) {
            // fallback pra qualquer outro erro de runtime
            System.out.println("Erro ao processar a operação: " + e.getMessage());
        }
    }

    private void consultarPorId(Scanner scanner) {
        long id = lerLong(scanner, "ID da conta: ");
        Conta conta = contaService.buscarPorId(id);
        System.out.println(conta);
    }

    private void criarConta(Scanner scanner) {
        System.out.print("Titular: ");
        String titular = scanner.nextLine();
        BigDecimal saldo = lerBigDecimal(scanner, "Saldo inicial: ");
        Conta contaCriada = contaService.criar(titular, saldo);
        System.out.println("Conta criada:");
        System.out.println(contaCriada);
    }

    private void alterarSaldo(Scanner scanner) {
        long id = lerLong(scanner, "ID da conta: ");
        BigDecimal novoSaldo = lerBigDecimal(scanner, "Novo saldo: ");
        Conta contaAtualizada = contaService.alterarSaldo(id, novoSaldo);
        System.out.println("Conta atualizada:");
        System.out.println(contaAtualizada);
    }

    private void excluirConta(Scanner scanner) {
        long id = lerLong(scanner, "ID da conta: ");
        contaService.excluir(id);
        System.out.println("Conta excluída com sucesso.");
    }

    private int lerInteiro(Scanner scanner, String mensagem) {
        System.out.print(mensagem);
        while (!scanner.hasNextInt()) {
            System.out.print("Valor inválido. " + mensagem);
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine();
        return valor;
    }

    private long lerLong(Scanner scanner, String mensagem) {
        System.out.print(mensagem);
        while (!scanner.hasNextLong()) {
            System.out.print("Valor inválido. " + mensagem);
            scanner.next();
        }
        long valor = scanner.nextLong();
        scanner.nextLine();
        return valor;
    }

    private BigDecimal lerBigDecimal(Scanner scanner, String mensagem) {
        System.out.print(mensagem);
        while (!scanner.hasNextBigDecimal()) {
            System.out.print("Valor inválido. " + mensagem);
            scanner.next();
        }
        BigDecimal valor = scanner.nextBigDecimal();
        scanner.nextLine();
        return valor;
    }
}
