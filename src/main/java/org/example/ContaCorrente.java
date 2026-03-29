package org.example;

import java.math.BigDecimal;

public class ContaCorrente {
    private String numero;
    private BigDecimal saldo;

    public ContaCorrente(String numero, BigDecimal saldo) {
        this.numero = numero;
        this.saldo = saldo;
    }

    public String getNumero() {
        return this.numero;
    }

    public BigDecimal getSaldo() {
        return this.saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
