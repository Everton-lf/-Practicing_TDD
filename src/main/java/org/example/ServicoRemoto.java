package org.example;

public interface ServicoRemoto {
    ContaCorrente recuperarConta(String numeroconta);

    void persistirConta(ContaCorrente numeroconta);
}

