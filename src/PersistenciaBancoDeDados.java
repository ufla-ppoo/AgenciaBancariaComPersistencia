import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação da interface Persistencia que salva e carrega os dados de
 * contas em um banco de dados SQLite
 * 
 * Esta classe usa o driver JDBC SQLite para acessar o banco de dados, você pode
 * encontrá-lo em: https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc
 */
public class PersistenciaBancoDeDados implements Persistencia {
    // Atributo que guarda o nome do banco de dados a ser criado
    private final String NOME_BD = "contas.db";

    /**
     * Método interno (privado) para fazer a conexão com o banco de dados.
     * 
     * @return Retorna a conexão que permite acessar o banco de dados.
     */
    private Connection conectar() {
        // A conexão com o banco é feita através de uma "String de Conexão". Ela
        // tem o formato: "jdbc:tipo_de_banco:nome_do_banco". No caso estamos
        // usando um banco do SQLite e o nome está guardado no atributo NOME_BD.
        // Para se conectar em um banco de tipo diferente bastaria usar a String
        // de conexão apropriada
        String stringConexao = "jdbc:sqlite:" + NOME_BD;

        try {
            // A classe DriverManager possui um método estático getConnection que
            // retorna a conexão com o banco a partir da String de conexão
            Connection conexao = DriverManager.getConnection(stringConexao);
            return conexao;
        } catch (Exception e) {
            // Obs: para manter o foco no que está sendo aprendido, o tratamento
            // de exceção está apenas exibindo a mensagem de erro. Em uma
            // aplicação normal deveria ser feito o tratamento adequando.
            // Esse comentário vale para todos os tratamentos da classe.
            System.out.println("Erro ao tentar abrir conexao: " + e.getMessage());
            return null;
        }
    }

    /**
     * Realiza a configuração inicial da persistência, usada apenas na primeira vez
     */
    @Override
    public boolean criarPersistencia() {
        // Obtém a conexão com o banco de dados
        Connection conexao = conectar();

        // Se conseguiu conectar
        if (conexao != null) {
            try {
                // cria um objeto que permitirá executar um comando no banco de dados
                Statement comando = conexao.createStatement();

                // Monta uma string com o comando SQL para criar a tabela CONTA
                // que guardará os dados das contas.
                //
                // Campos String são criados com o tipo CHAR(tamanho), inteiros
                // com o tipo INT e float ou double com o tipo REAL. O 'NOT NULL'
                // indica que a coluna não pode ser vazia na tabela (tem que ter
                // algum valor).
                String sql = "CREATE TABLE CONTA " +
                        "(NUMERO   INT    NOT NULL, " +
                        " SALDO    REAL   NOT NULL);";

                // executa o comando SQL
                comando.executeUpdate(sql);

                // ATENÇÃO: é muito importante fechar o comando e a conexão com
                // o banco de dados para liberar os recursos e acessos.
                comando.close();
                conexao.close();
                return true;
            } catch (Exception e) {
                System.out.println("Houve um erro ao tentar criar o banco de dados " + NOME_BD);
                System.out.println("Mensagem do erro: " + e);
            }
        }
        return false;
    }

    /**
     * Retorna se a persistência já foi criada, ou seja, se o banco de dados NOME_BD
     * existe
     */
    @Override
    public boolean criada() {
        return new java.io.File(NOME_BD).exists();
    }

    /**
     * Carrega as contas persistidas do banco de dados.
     * Retorna uma coleção vazia se houver algum problema para carregar os dados
     */
    @Override
    public List<Conta> carregarContas() {
        // Obtém a conexão com o banco de dados
        Connection conexao = conectar();

        // Se conseguiu conectar
        if (conexao != null) {
            try {
                // cria um objeto que permitirá executar um comando no banco de dados
                Statement comando = conexao.createStatement();

                // Monta uma string com o comando SQL para buscar os dados das
                // contas na tabela CONTA
                String sql = "SELECT * FROM CONTA;";

                // Repare que para buscar os dados usamos um outro método: o
                // executeQuery. Ele retorna os resultados em um objeto do tipo
                // ResultSet que pode ser percorrido para se obter os dados de
                // cada linha da tabela
                ResultSet resultado = comando.executeQuery(sql);

                // Criamos um ArrayList a ser preenchido com as contas buscadas no
                // no banco.
                List<Conta> contas = new ArrayList<>();

                // O método next() faz o 'cursor" do ResultSet apontar cada hora
                // para uma linha da tabela. Enquanto existirem linhas ele muda
                // o cursor e retorna true.
                while (resultado.next()) {
                    // Pegamos então os dados de uma linha da tabela especificando
                    // o nome da coluna a qual queremos os dados. Para obtermos
                    // os dados de acordo com o tipo desejado usamos os gets
                    // apropriados (getString, getInt, etc.)
                    int numero = resultado.getInt("NUMERO");
                    double saldo = resultado.getDouble("SALDO");

                    // Agora que já temos os dados podemos criar um objeto da
                    // classe Conta e adicioná-lo à lista a ser retornada
                    Conta conta = new Conta(numero, saldo);
                    contas.add(conta);
                }

                // ATENÇÃO: é muito importante fechar o comando e a conexão com
                // o banco de dados para liberar os recursos e acessos. Nesse
                // caso precisamos fechar também o ResultSet.
                resultado.close();
                comando.close();
                conexao.close();

                // Enfim, retornamos as contas que foram buscadas do banco de dados
                return contas;
            } catch (Exception e) {
                System.out.println("Houve um erro ao tentar carregar os dados do banco " + NOME_BD);
                System.out.println("Mensagem do erro: " + e);
            }
        }
        return new ArrayList<>();
    }

    /**
     * Persiste todas as contas no banco de dados.
     * Retorna true se os dados forem salvos corretamente e false se houver algum
     * problema.
     * 
     * ATENÇÃO: essa não é a forma comum de se fazer a persistência de dados em
     * Banco de Dados. Geralmente inserimos, atualizamos e/ou removemos cada conta
     * quando é criada, alterada ou removida. Aqui estamos fazendo uma operação de
     * "bulk insert" para simplificar o exemplo. Estamos fazendo dessa forma para
     * seguir a interface Persistencia, que não prevê essas operações
     */
    @Override
    public boolean salvarContas(List<Conta> contas) {
        try {
            // percorre a coleção de contas e, para cada uma delas, se ela ainda não
            // existe no banco de dados, é criada; se já existe, é atualizada.
            for (Conta conta : contas) {
                if (contaExiste(conta.getNumero())) {
                    atualizarConta(conta);
                } else {
                    inserirConta(conta);
                }
            }
            return true;
        } catch (SQLException e) {
            // apenas retorna false porque os métodos chamados acima já exibem suas
            // próprias mensagens de erro.
            return false;
        }
    }

    public boolean contaExiste(int numero) {
        // Obtém a conexão com o banco de dados
        Connection conexao = conectar();

        // Se conseguiu conectar
        if (conexao != null) {
            try {
                // cria um objeto que permitirá executar um comando no banco de dados
                Statement comando = conexao.createStatement();

                // Monta uma string com o comando SQL para buscar os dados das
                // contas na tabela CONTA
                String sql = "SELECT * FROM CONTA WHERE NUMERO = " + numero + ";";

                // Repare que para buscar os dados usamos um outro método: o
                // executeQuery. Ele retorna os resultados em um objeto do tipo
                // ResultSet que pode ser percorrido para se obter os dados de
                // cada linha da tabela
                ResultSet resultado = comando.executeQuery(sql);

                // O método next() faz o 'cursor" do ResultSet apontar cada hora
                // para uma linha da tabela. Enquanto existirem linhas ele muda
                // o cursor e retorna true.
                boolean existe = resultado.next();

                // ATENÇÃO: é muito importante fechar o comando e a conexão com
                // o banco de dados para liberar os recursos e acessos. Nesse
                // caso precisamos fechar também o ResultSet.
                resultado.close();
                comando.close();
                conexao.close();

                // Enfim, retornamos se existe no banco de dados uma conta com o número passado.
                return existe;
            } catch (Exception e) {
                System.out.println(
                        "Houve um erro ao tentar verificar se a conta " + numero + " existe no banco " + NOME_BD);
                System.out.println("Mensagem do erro: " + e);
            }
        }
        return false;
    }

    /**
     * Insere uma conta no banco de dados.
     * 
     * @param conta Conta a ser inserida
     */
    public void inserirConta(Conta conta) throws SQLException {
        // Obtém a conexão com o banco de dados
        Connection conexao = conectar();

        // Se conseguiu conectar
        if (conexao != null) {
            try {
                // cria um objeto que permitirá executar um comando no banco de dados
                Statement comando = conexao.createStatement();

                // Monta uma string com o comando SQL para inserir uma conta na
                // tabela CONTA. Os dados devem ser fornecidos na mesma ordem
                // que as colunas foram listadas.
                //
                // ATENÇÃO: dentro da String SQL os campos que são string precisam
                // estar entre aspas simples.
                String sql = "INSERT INTO CONTA(NUMERO, SALDO) " +
                        "VALUES (" + conta.getNumero() + ", " + conta.getSaldo() + ");";

                // executa o comando SQL
                comando.executeUpdate(sql);

                // ATENÇÃO: é muito importante fechar o comando e a conexão com
                // o banco de dados para liberar os recursos e acessos.
                comando.close();
                conexao.close();
            } catch (SQLException e) {
                System.out.println(
                        "Houve um erro ao tentar persistir a conta " + conta.getNumero() + " no banco " + NOME_BD);
                System.out.println("Mensagem do erro: " + e);
                throw e; // relança a exceção para avisar quem chamou o método
            }
        }
    }

    /**
     * Atualiza uma conta no banco de dados.
     * 
     * @param conta Conta a ser atualizada
     */
    public void atualizarConta(Conta conta) throws SQLException {
        // Obtém a conexão com o banco de dados
        Connection conexao = conectar();

        // Se conseguiu conectar
        if (conexao != null) {
            try {
                // cria um objeto que permitirá executar um comando no banco de dados
                Statement comando = conexao.createStatement();

                // Monta uma string com o comando SQL para alterar os dados de
                // um conta na tabela CONTA.
                // O comando WHERE é usado para indicar qual(is) linha(s) será(ão)
                // alterada(s). Serão alteradas todas as linhas que casarem com
                // a condição colocada.
                //
                // ATENÇÃO 1: se você esquer do WHERE o comando atualizará todas
                // as linhas da tabela.
                // ATENÇÃO: dentro da String SQL os campos que são string precisam
                // estar entre aspas simples.
                String sql = "UPDATE CONTA " +
                        "SET SALDO = " + conta.getSaldo() + " " +
                        "WHERE NUMERO = " + conta.getNumero() + ";";

                // executa o comando SQL
                comando.executeUpdate(sql);

                // ATENÇÃO: é muito importante fechar o comando e a conexão com
                // o banco de dados para liberar os recursos e acessos.
                comando.close();
                conexao.close();
            } catch (Exception e) {
                System.out.println(
                        "Houve um erro ao tentar atualizar a conta " + conta.getNumero() + " no banco " + NOME_BD);
                System.out.println("Mensagem do erro: " + e);
                throw e; // relança a exceção para avisar quem chamou o método
            }
        }
    }

    /**
     * Remove uma conta do banco de dados.
     * 
     * @param conta Conta a ser removida
     */
    public void removerConta(Conta conta) throws SQLException {
        // Obtém a conexão com o banco de dados
        Connection conexao = conectar();

        // Se conseguiu conectar
        if (conexao != null) {
            try {
                // cria um objeto que permitirá executar um comando no banco de dados
                Statement comando = conexao.createStatement();

                // Monta uma string com o comando SQL para remover uma conta da
                // tabela CONTA. O comando WHERE é usado para indicar qual(is)
                // linha(s) será(ão) removida(s). Serão removidas todas as linhas
                // que casarem com a condição colocada.
                //
                // ATENÇÃO: se você esquer do WHERE o comando removerá todas
                // as linhas da tabela.
                String sql = "DELETE FROM CONTA " +
                        "WHERE NUMERO = " + conta.getNumero() + ";";

                // executa o comando SQL
                comando.executeUpdate(sql);

                // ATENÇÃO: é muito importante fechar o comando e a conexão com
                // o banco de dados para liberar os recursos e acessos.
                comando.close();
                conexao.close();
            } catch (Exception e) {
                System.out.println(
                        "Houve um erro ao tentar remover a conta " + conta.getNumero() + " do banco " + NOME_BD);
                System.out.println("Mensagem do erro: " + e);
                throw e; // relança a exceção para avisar quem chamou o método
            }
        }
    }
}
