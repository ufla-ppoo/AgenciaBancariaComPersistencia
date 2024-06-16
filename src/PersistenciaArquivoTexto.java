
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação da interface Persistencia que salva e carrega os dados de
 * contas em arquivo texto
 */
public class PersistenciaArquivoTexto implements Persistencia {
    // Nome do arquivo onde os dados serão salvos
    private final String NOME_ARQUIVO = "contas.txt";

    /**
     * Realiza a configuração inicial da persistência, usada apenas na primeira vez
     */
    @Override
    public boolean criarPersistencia() {
        try {
            FileWriter arquivo = new FileWriter(NOME_ARQUIVO);
            arquivo.close();
            return true;
        } catch (IOException e) {
            System.out.println("Houve um erro ao tentar criar o arquivo " + NOME_ARQUIVO);
            System.out.println("Detalhes do erro: " + e);
            return false;
        }
    }

    /**
     * Retorna se a persistência já foi criada, ou seja, se o arquivo NOME_ARQUIVO
     * existe
     */
    @Override
    public boolean criada() {
        return new java.io.File(NOME_ARQUIVO).exists();
    }

    /**
     * Carrega as contas persistidas do arquivo texto definido no atributo
     * NOME_ARQUIVO.
     * Retorna uma coleção vazia se houver algum problema para carregar os dados
     */
    @Override
    public List<Conta> carregarContas() {
        List<Conta> contas = new ArrayList<>();
        try {
            BufferedReader arquivo = new BufferedReader(new FileReader(NOME_ARQUIVO));

            String linha = arquivo.readLine();
            while (linha != null) {
                String[] dados = linha.split(",");
                int numero = Integer.parseInt(dados[0]);
                double saldo = Double.parseDouble(dados[1]);

                Conta conta = new Conta(numero, saldo);
                contas.add(conta);

                linha = arquivo.readLine();
            }
            arquivo.close();
        } catch (IOException e) {
            System.out.println("Houve um erro ao tentar carregar o arquivo " + NOME_ARQUIVO);
            System.out.println("Detalhes do erro: " + e);
        }
        return contas;
    }

    /**
     * Persiste as contas no arquivo texto definido no atributo NOME_ARQUIVO.
     * Retorna true se os dados forem salvos corretamente e false se houver algum
     * problema.
     */
    @Override
    public boolean salvarContas(List<Conta> contas) {
        try {
            FileWriter arquivo = new FileWriter(NOME_ARQUIVO);

            for (Conta conta : contas) {
                arquivo.write(conta.getNumero() + "," + conta.getSaldo() + "\n");
            }
            arquivo.close();
            return true;
        } catch (IOException e) {
            System.out.println("Houve um erro ao tentar salvar o arquivo " + NOME_ARQUIVO);
            System.out.println("Detalhes do erro: " + e);
            return false;
        }
    }

}
