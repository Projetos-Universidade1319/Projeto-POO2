




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Conexao {
    static String url = "jdbc:postgresql://localhost:5432/Banco_Chef";
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
        } finally {
            try {
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (Exception e) {
                System.out.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}
