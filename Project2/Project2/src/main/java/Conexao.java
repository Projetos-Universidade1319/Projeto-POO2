
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Conexao {
    static String url = "jdbc:postgresql://localhost:5432/Banco_Chef.g";
    static String user = "postgres";
    static String senha = "rodaminha0309";
    static String driver = "org.postgresql.Driver";

    public static void main(String[] args) {
        Connection con = null;
        Statement st = null;

        String sqll = "CREATE TABLE IF NOT EXISTS usuario (" +
                      "id_usuario INT PRIMARY KEY NOT NULL, " +
                      "nome VARCHAR(100), " +
                      "email VARCHAR(100)," +
                      "senha VARCHAR(100)," +
                      "nivel_conta INT, " +
                      "pontuacao FLOAT) "; 


        try {
            System.out.println("Conectando ao banco de dados...");
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, senha);
            System.out.println("Conexão aberta com sucesso!");

            st = con.createStatement();
            st.executeUpdate(sqll);
            System.out.println("Tabela criada com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }
        ResultSet rs = null;
        String sql2 = "SELECT *FROM usuario";
        try{
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, senha);
            st = con.createStatement();
            rs = st.executeQuery(sql2);
            while(rs.next()){
                System.out.println("ID: " + rs.getInt("id_usuario"));
                System.out.println("Nome: " + rs.getString("nome"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Senha: " + rs.getString("senha"));
                System.out.println("Nivel da Conta: " + rs.getInt("nivel_conta"));
                System.out.println("Pontuação: " + rs.getFloat("pontuacao"));
                System.out.println("---------------------------");
            }
            st.close();
            con.close();
            rs.close();
        }catch(Exception e){
                System.out.println("Erro ao inserir usuario: " + e.getMessage());
            }
        /*String sql3 = "INSERT INTO usuario VALUES(1, 'Exemplo', 'exemplo@blabla.com', 'exemplo123', 6, 600.0)";
        try{
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, senha);
            st = con.createStatement();
            st.executeUpdate(sql3);
            System.out.println("Usuario inserido com sucesso!");
        }catch(Exception e){
                System.out.println("Erro ao inserir usuario: " + e.getMessage());
            }
        String sql4 = "DELETE FROM usuario WHERE id_usuario = 1";
        try{
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, senha);
            st = con.createStatement();
            st.executeUpdate(sql4);
            System.out.println("Usuario deletado com sucesso!");
            st.close();
            con.close();
            rs.close();
        }catch(Exception e){
                System.out.println("Erro ao deletar usuario: " + e.getMessage());
            }*/
        String sql5 = "CREATE TABLE IF NOT EXISTS avaliacao (" +
                      "id_avaliacao INT PRIMARY KEY NOT NULL, " +
                      "id_receita INT, " +
                      "nome VARCHAR(100), " +
                      "comentario TEXT," +
                      "curtidas INT, " +
                      "data_avaliacao INT) ";
        try {
            System.out.println("Conectando ao banco de dados...");
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, senha);
            System.out.println("Conexão aberta com sucesso!");

            st = con.createStatement();
            st.executeUpdate(sql5);
            System.out.println("Tabela criada com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }

        String sql6 = "CREATE TABLE IF NOT EXISTS ingrediente (" +
                      "id_ingrediente INT PRIMARY KEY NOT NULL, " +
                      "nome VARCHAR(100), " +
                      "unidade_medida VARCHAR(50)) ";
        try {
            System.out.println("Conectando ao banco de dados...");
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, senha);
            System.out.println("Conexão aberta com sucesso!");

            st = con.createStatement();
            st.executeUpdate(sql6);
            System.out.println("Tabela criada com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }

        String sql7 = "CREATE TABLE IF NOT EXISTS receita_ingrediente (" +
                      "id_receita INT NOT NULL, " +
                      "id_ingrediente INT NOT NULL, " +
                      "quantidade INT, " +
                      "PRIMARY KEY (id_receita, id_ingrediente)) ";
        try {
            System.out.println("Conectando ao banco de dados...");
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, senha);
            System.out.println("Conexão aberta com sucesso!");

            st = con.createStatement();
            st.executeUpdate(sql7);
            System.out.println("Tabela criada com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }

        String sql8 = "CREATE TABLE IF NOT EXISTS receita (" +
                      "id_receita INT PRIMARY KEY NOT NULL, " +
                      "nome VARCHAR(100), " +
                      "descricao TEXT, " +
                      "tempo_preparo INT, " +
                      "dificuldade INT, " +
                      "id_usuario INT) ";
        try {
            System.out.println("Conectando ao banco de dados...");
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, senha);
            System.out.println("Conexão aberta com sucesso!");

            st = con.createStatement();
            st.executeUpdate(sql8);
            System.out.println("Tabela criada com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }

        finally {
            try {
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (Exception e) {
                System.out.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}
