package org.example;

public interface Hardware {
    String pegarNumeroDaContaCartao() throws Exception;

    void entregarDinheiro() throws Exception;

    void lerEnvelope() throws Exception;
}

