/**
 * Classe principal da aplicação.
 */
public class App {
    /**
     * Método principal da aplicação.
     */
    public static void main(String[] args) throws Exception {

        // A interface de usuário é criada passando-se o nome da agência e a
        // persistência a ser utilizada
        // Comente/descomente as linhas abaixo para usar diferentes tipos de
        // persistência

        Persistencia persistencia;

        // persistencia = new PersistenciaArquivoTexto();
        // persistencia = new PersistenciaArquivoBinario();
        persistencia = new PersistenciaBancoDeDados();

        InterfaceUsuario iuAgencia = new InterfaceUsuario("Agência UFLA", persistencia);
        iuAgencia.exibir();
    }
}
