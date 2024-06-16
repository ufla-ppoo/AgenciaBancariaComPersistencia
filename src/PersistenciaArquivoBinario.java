import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação da interface Persistencia que salva e carrega os dados de
 * contas em arquivo binário
 */
public class PersistenciaArquivoBinario implements Persistencia {
    // Nome do arquivo onde os dados serão salvos
    private final String NOME_ARQUIVO = "contas.dat";

    /**
     * Realiza a configuração inicial da persistência, usada apenas na primeira vez
     */
    @Override
    public boolean criarPersistencia() {
        // no caso de arquivos binários não faz nada, pois o arquivo será criado
        // automaticamente ao salvar os dados se ainda não existir.
        return true;
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
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NOME_ARQUIVO));

            // ATENÇÃO: a diretiva de compilação SuppressWarnings NÃO deve ser usada sem ter
            // certeza de que é necessária, pois ela pode esconder problemas no seu código.
            // Neste caso precisamos dela porque o método readObject retorna um Object e não
            // tem como o compilador ter certeza de que a conversão de tipos para um tipo
            // genérico (como um ArraList) está correta. Neste caso, temos certeza que o
            // objeto que está sendo lido é um ArrayList<Conta>, então podemos usar a
            // diretiva @SuppressWarnings.
            @SuppressWarnings("unchecked")
            List<Conta> contas = (ArrayList<Conta>) ois.readObject();

            ois.close();

            return contas;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Houve um erro ao tentar carregar o arquivo " + NOME_ARQUIVO);
            System.out.println("Mensagem do erro: " + e);
            return new ArrayList<>();
        }
    }

    /**
     * Persiste as contas no arquivo texto definido no atributo NOME_ARQUIVO.
     * Retorna true se os dados forem salvos corretamente e false se houver algum
     * problema.
     */
    @Override
    public boolean salvarContas(List<Conta> contas) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOME_ARQUIVO));

            oos.writeObject(contas);

            oos.close();

            return true;
        } catch (IOException e) {
            System.out.println("Houve um erro ao tentar salvar o arquivo " + NOME_ARQUIVO);
            System.out.println("Mensagem do erro: " + e);
            return false;
        }
    }

}
