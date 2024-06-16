
import java.util.List;

/**
 * Interface para a persistência de contas.
 * Desacopla a persistência de contas do restante do sistema; é um contrato que
 * permite que a Agência trabalhe com diferentes tipos de persistência.
 */
public interface Persistencia {
    /**
     * Realiza a configuração inicial da persistência, usada apenas na primeira vez
     */
    boolean criarPersistencia();

    /**
     * Retorna se a persistência já foi criada
     */
    boolean criada();

    /**
     * Carrega as contas persistidas.
     * Retorna uma coleção vazia se houver algum problema para carregar os dados
     */
    List<Conta> carregarContas();

    /**
     * Persiste as contas.
     * Retorna true se os dados forem salvos corretamente e
     * false se houver algum problema.
     */
    boolean salvarContas(List<Conta> contas);
}
