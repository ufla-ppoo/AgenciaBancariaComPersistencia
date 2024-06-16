
import java.util.Scanner;

/**
 * Interface de usuário para o programa
 * 
 * @author Julio César Alves
 */
public class InterfaceUsuario {
    // Agência bancária que trata as contas
    private Agencia agencia;
    // Scanner para entrada de dados
    private Scanner entrada;
    // Número da opção para sair do programa
    private final int OPCAO_SAIR = 6;

    /*
     * Cria a interface de usuário com o nome da agência e a persistência
     */
    public InterfaceUsuario(String nomeAgencia, Persistencia persistencia) {
        agencia = new Agencia(nomeAgencia, persistencia);
        entrada = new Scanner(System.in);
    }

    /**
     * Trata o menu da agência
     */
    public void exibir() {
        int opcao;
        do {
            opcao = exibirMenu();
            tratarOpcaoMenu(opcao);
        } while (opcao != OPCAO_SAIR);

        agencia.finalizar();
    }

    /*
     * Exibe o menu e retorna a opção escolhida pelo usuário
     */
    private int exibirMenu() {
        System.out.println("Bem-vindo à agência " + agencia.getNome() + "!\n");
        System.out.println(
                "1 - Criar conta\n" +
                        "2 - Relatório\n" +
                        "3 - Depósito\n" +
                        "4 - Saque\n" +
                        "5 - Transferência\n" +
                        "6 - Sair\n");
        System.out.print("Digite sua opcao: ");
        return Integer.parseInt(entrada.nextLine());
    }

    /*
     * Trata a opção escolhida pelo usuário
     */
    private void tratarOpcaoMenu(int opcao) {
        System.out.println();
        switch (opcao) {
            case 1:
                criarConta();
                break;
            case 2:
                exibirRelatorio();
                break;
            case 3:
                fazerDeposito();
                break;
            case 4:
                fazerSaque();
                break;
            case 5:
                fazerTransferencia();
                break;
            case OPCAO_SAIR:
                System.out.println("\nObrigado por usar os servicos da Agência " + agencia.getNome() + "!\n");
                break;
            default:
                System.out.println("\nOpção inválida!\n");
                break;
        }

        if (opcao != OPCAO_SAIR) {
            aguardarEnter();
        }
    }

    /*
     * Cria uma conta na agência
     */
    private void criarConta() {
        try {
            int nroConta = agencia.criarConta();
            System.out.println("Conta " + nroConta + " criada!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Exibe o relatório da agência
     */
    private void exibirRelatorio() {
        System.out.println(agencia.gerarRelatorio());
    }

    /*
     * Pede o número da conta ao usuário
     */
    private int pedirConta(String complemento) {
        System.out.print("Digite o número da conta " + complemento + ": ");
        return Integer.parseInt(entrada.nextLine());
    }

    /*
     * Aguarda o usuário pressionar ENTER
     */
    private void aguardarEnter() {
        System.out.print("\n... pressione ENTER para continuar...");
        entrada.nextLine();
        System.out.println("\n");
    }

    /*
     * Pede um valor ao usuário
     */
    private double pedirValor() {
        System.out.print("Digite o valor: ");
        return Double.parseDouble(entrada.nextLine());
    }

    /*
     * Realiza um depósito
     */
    private void fazerDeposito() {
        try {
            agencia.depositar(pedirConta("para depósito"), pedirValor());
            System.out.println("Depósito realizado com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Realiza um saque
     */
    private void fazerSaque() {
        try {
            agencia.sacar(pedirConta("para saque"), pedirValor());
            System.out.println("Saque realizado com sucesso!");
        } catch (SaldoInsuficienteException e) {
            System.out.println(e.getMessage());
            System.out.println("A conta tinha apenas " + e.getSaldo() + " de saldo!");
        } catch (Exception e) {
            System.out.println("Não foi possível sacar!");
            System.out.println(e.getMessage());
        }
    }

    /*
     * Realiza uma transferência
     */
    private void fazerTransferencia() {
        try {
            agencia.transferir(
                    pedirConta("de origem"),
                    pedirConta("de destino"),
                    pedirValor());
            System.out.println("Transferencia realizada com sucesso!");
        } catch (SaldoInsuficienteException e) {
            System.out.println(e.getMessage());
            System.out.println("A conta tinha apenas " + e.getSaldo() + " de saldo!");
        } catch (Exception e) {
            System.out.println("Não foi possível transferir!");
            System.out.println(e.getMessage());
        }
    }
}
