package org.example;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.math.BigDecimal;

public class CaixaEletronico {
    private final ServicoRemoto servicoRemoto;
    private final Hardware hardware;
    private ContaCorrente contaLogada;

    public CaixaEletronico(ServicoRemoto servicoRemoto, Hardware hardware) {
        this.servicoRemoto = servicoRemoto;
        this.hardware = hardware;
        this.contaLogada = null;
    }

    public String logar() {
        try {
            String numero = this.hardware.pegarNumeroDaContaCartao();
            ContaCorrente conta = this.servicoRemoto.recuperarConta(numero);
            if (conta != null) {
                this.contaLogada = conta;
                return "Usuário Autenticado";
            } else {
                return "Não foi possível autenticar o usuário";
            }
        } catch (Exception e) {
            return "Não foi possível autenticar o usuário";
        }
    }

    public String saldo() {
        if (this.contaLogada == null) {
            return "Usuário não autenticado";
        } else {
            DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
            String saldoFormatado = df.format(this.contaLogada.getSaldo());
            return "O saldo é R$" + saldoFormatado;
        }
    }

    public String sacar(BigDecimal valor) {
        if (this.contaLogada == null) {
            return "Usuário não autenticado";
        } else if (valor.compareTo(this.contaLogada.getSaldo()) > 0) {
            return "Saldo insuficiente";
        } else {
            try {
                this.hardware.entregarDinheiro();
                this.contaLogada.setSaldo(this.contaLogada.getSaldo().subtract(valor));
                this.servicoRemoto.persistirConta(this.contaLogada);
                return "Retire seu dinheiro";
            } catch (Exception e) {
                return "Falha no hardware";
            }
        }
    }

    public String depositar(BigDecimal valor) {
        if (this.contaLogada == null) {
            return "Usuário não autenticado";
        } else {
            try {
                this.hardware.lerEnvelope();
                this.contaLogada.setSaldo(this.contaLogada.getSaldo().add(valor));
                this.servicoRemoto.persistirConta(this.contaLogada);
                return "Depósito recebido com sucesso";
            } catch (Exception e) {
                return "Falha no hardware";
            }
        }
    }
}