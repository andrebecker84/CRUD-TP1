package br.com.infnet.banco.exception;

public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException() {
        super("Saldo insuficiente para realizar a operação.");
    }
}
