import java.io.Serializable;

/**
 * Representa uma conta bancária.
 * 
 * Uma conta bancária possui um número de identificação e um saldo. E possui
 * operações para consultar esses valores e fazer saques, depósitos e
 * transferências.
 * 
 * @author Julio César Alves
 */
public class Conta implements Serializable {    
    // Número da conta    
    private int numero;
    // Saldo da conta
    private double saldo;
    // Versão da classe para serialização
    private static final long serialVersionUID = 1L;

    /**
     * Cria um aconta com um número e um saldo.
     */
    public Conta(int numero, double saldoInicial) {
        this.numero = numero;
        saldo = saldoInicial;
    }

    /**
     * Cria um aconta com saldo zerado.
     */
    public Conta(int numero) {
        this(numero, 0.0);
    }

    /**
     * Retorna o saldo da conta.
     * 
     * @return Saldo da conta
     */
    public double getSaldo() {
        return saldo;
    }

    /**
     * Retorna o número que identifica a conta
     * 
     * @return Número da conta
     */
    public int getNumero() {
        return numero;
    }

    /**
     * Deposita um valor na conta.
     * 
     * @param valor Valor a ser depositado.
     */
    public void depositar(double valor) {
        saldo += valor;
    }

    /**
     * Saca um valor da conta (se ela tiver saldo suficiente)
     * 
     * @param valor Valor a ser sacado.
     */
    public void sacar(double valor) {
        // se a conta tem saldo suficiente, faz o saque
        if (saldo >= valor) {
            saldo -= valor;
        } else
            throw new SaldoInsuficienteException(numero, saldo);
    }

    /**
     * Transfere um valor da conta para uma outra conta.
     * 
     * @param contaDestino Conta para o qual o valor será transferido
     * @param valor        Valor a ser transferido para a outra conta.
     */
    public void transferir(Conta contaDestino, double valor) {
        // tenta sacar o valor da conta; se for possível deposita na de destino
        double saldoAnterior = saldo;
        try {
            sacar(valor);
            contaDestino.depositar(valor);
        } catch (Exception e) {
            if (saldo == saldoAnterior - valor) {
                depositar(valor);
            }
            throw e; // relançamos a exceção para avisar quem chamou o método
        }
    }

    /**
     * Retorna um extrato da conta (número e saldo).
     * 
     * @return O extrato da conta no formato Conta NNN - saldo: R$ NNNN)
     */
    public String extrato() {
        return "Conta " + numero + " - saldo: R$ " + saldo;
    }
}