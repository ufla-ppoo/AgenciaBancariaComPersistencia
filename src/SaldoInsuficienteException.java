/**
 * Exceção usada quando se tenta realizar uma operação em uma conta bancária que
 * não é possível porque o saldo é insuficiente
 * 
 * @author Julio César Alves
 */
public class SaldoInsuficienteException extends RuntimeException {
    // número da conta sobre a qual a operação foi tentada
    private int nroConta;
    // saldo da conta no momento que a operação foi tentada
    private double saldo;

    /**
     * Cria uma exceção de saldo insuficiente
     * 
     * @param nroConta número da conta
     * @param saldo    saldo da conta
     */
    public SaldoInsuficienteException(int nroConta, double saldo) {
        super("Saldo insuficiente na conta " + nroConta);

        this.nroConta = nroConta;
        this.saldo = saldo;
    }

    /**
     * Retorna o número da conta
     * 
     * @return número da conta
     */
    public int getNroConta() {
        return nroConta;
    }

    /**
     * Retorna o saldo da conta
     * 
     * @return saldo da conta
     */
    public double getSaldo() {
        return saldo;
    }

}
