import org.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CaixaEletronicoTest {

    @Mock
    private ServicoRemoto servicoRemoto;

    @Mock
    private Hardware hardware;

    private CaixaEletronico caixaEletronico;
    private ContaCorrente conta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        caixaEletronico = new CaixaEletronico(servicoRemoto, hardware);
        conta = new ContaCorrente("12345", BigDecimal.valueOf(1000.0));
    }

    @Test
    void testLogarSuccess() throws Exception {
        when(hardware.pegarNumeroDaContaCartao()).thenReturn("12345");
        when(servicoRemoto.recuperarConta("12345")).thenReturn(conta);

        String result = caixaEletronico.logar();

        assertEquals("Usuário Autenticado", result);
        verify(servicoRemoto).recuperarConta("12345");
    }

    @Test
    void testLogarHardwareFailure() throws Exception {
        when(hardware.pegarNumeroDaContaCartao()).thenThrow(new Exception("Hardware failure"));

        String result = caixaEletronico.logar();

        assertEquals("Não foi possível autenticar o usuário", result);
    }

    @Test
    void testLogarContaNotFound() throws Exception {
        when(hardware.pegarNumeroDaContaCartao()).thenReturn("12345");
        when(servicoRemoto.recuperarConta("12345")).thenReturn(null);

        String result = caixaEletronico.logar();

        assertEquals("Não foi possível autenticar o usuário", result);
    }

    @Test
    void testSaldoNotLoggedIn() {
        String result = caixaEletronico.saldo();

        assertEquals("Usuário não autenticado", result);
    }

    @Test
    void testSaldoLoggedIn() throws Exception {
        when(hardware.pegarNumeroDaContaCartao()).thenReturn("12345");
        when(servicoRemoto.recuperarConta("12345")).thenReturn(conta);
        caixaEletronico.logar();

        String result = caixaEletronico.saldo();

        assertEquals("O saldo é R$1.000,00", result);
    }

    @Test
    void testSacarNotLoggedIn() {
        String result = caixaEletronico.sacar(BigDecimal.valueOf(100.0));

        assertEquals("Usuário não autenticado", result);
    }

    @Test
    void testSacarInsufficientBalance() throws Exception {
        when(hardware.pegarNumeroDaContaCartao()).thenReturn("12345");
        when(servicoRemoto.recuperarConta("12345")).thenReturn(conta);
        caixaEletronico.logar();

        String result = caixaEletronico.sacar(BigDecimal.valueOf(1500.0));

        assertEquals("Saldo insuficiente", result);
    }

    @Test
    void testSacarHardwareFailure() throws Exception {
        when(hardware.pegarNumeroDaContaCartao()).thenReturn("12345");
        when(servicoRemoto.recuperarConta("12345")).thenReturn(conta);
        caixaEletronico.logar();
        doThrow(new Exception("Hardware failure")).when(hardware).entregarDinheiro();

        String result = caixaEletronico.sacar(BigDecimal.valueOf(100.0));

        assertEquals("Falha no hardware", result);
        verify(servicoRemoto, never()).persistirConta(any());
        assertEquals(BigDecimal.valueOf(1000.0), conta.getSaldo());
    }

    @Test
    void testSacarSuccess() throws Exception {
        when(hardware.pegarNumeroDaContaCartao()).thenReturn("12345");
        when(servicoRemoto.recuperarConta("12345")).thenReturn(conta);
        caixaEletronico.logar();

        String result = caixaEletronico.sacar(BigDecimal.valueOf(100.0));

        assertEquals("Retire seu dinheiro", result);
        assertEquals(BigDecimal.valueOf(900.0), conta.getSaldo());
        verify(hardware).entregarDinheiro();
        verify(servicoRemoto).persistirConta(conta);
    }

    @Test
    void testDepositarNotLoggedIn() {
        String result = caixaEletronico.depositar(BigDecimal.valueOf(100.0));

        assertEquals("Usuário não autenticado", result);
    }

    @Test
    void testDepositarHardwareFailure() throws Exception {
        when(hardware.pegarNumeroDaContaCartao()).thenReturn("12345");
        when(servicoRemoto.recuperarConta("12345")).thenReturn(conta);
        caixaEletronico.logar();
        doThrow(new Exception("Hardware failure")).when(hardware).lerEnvelope();

        String result = caixaEletronico.depositar(BigDecimal.valueOf(100.0));

        assertEquals("Falha no hardware", result);
        verify(servicoRemoto, never()).persistirConta(any());
        assertEquals(BigDecimal.valueOf(1000.0), conta.getSaldo());
    }

    @Test
    void testDepositarSuccess() throws Exception {
        when(hardware.pegarNumeroDaContaCartao()).thenReturn("12345");
        when(servicoRemoto.recuperarConta("12345")).thenReturn(conta);
        caixaEletronico.logar();

        String result = caixaEletronico.depositar(BigDecimal.valueOf(100.0));

        assertEquals("Depósito recebido com sucesso", result);
        assertEquals(BigDecimal.valueOf(1100.0), conta.getSaldo());
        verify(hardware).lerEnvelope();
        verify(servicoRemoto).persistirConta(conta);
    }
}
