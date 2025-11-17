package util;

import dao.ConnectionFactory; 
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseInitializer {

    public static void createAllTables() {
        
        String[] sqlDrop = {
            "DROP TABLE IF EXISTS curtida CASCADE",
            "DROP TABLE IF EXISTS avaliacao CASCADE",
            "DROP TABLE IF EXISTS receita_ingrediente CASCADE",
            "DROP TABLE IF EXISTS receita CASCADE",
            "DROP TABLE IF EXISTS ingrediente CASCADE",
            "DROP TABLE IF EXISTS usuario CASCADE"
        };
        
        String sqlUsuario = "CREATE TABLE IF NOT EXISTS usuario (" +
                            "id_usuario SERIAL PRIMARY KEY, " +
                            "nome VARCHAR(100) NOT NULL, " +
                            "email VARCHAR(100) UNIQUE NOT NULL, " +
                            "senha VARCHAR(60) NOT NULL, " +
                            "nivel_conta VARCHAR(20) NOT NULL, " +
                            "pontuacao FLOAT DEFAULT 0.0)";
                            
        String sqlIngrediente = "CREATE TABLE IF NOT EXISTS ingrediente (" +
                                "id_ingrediente SERIAL PRIMARY KEY, " +
                                "nome VARCHAR(100) UNIQUE NOT NULL, " +
                                "unidade_medida VARCHAR(50) NOT NULL)";
                                
        String sqlReceita = "CREATE TABLE IF NOT EXISTS receita (" +
                            "id_receita SERIAL PRIMARY KEY, " +
                            "nome VARCHAR(100) NOT NULL, " +
                            "descricao TEXT, " + 
                            "modo_preparo TEXT, " + 
                            "categoria VARCHAR(50), " + 
                            "id_usuario INT NOT NULL, " + 
                            "curtidas INT DEFAULT 0, " +
                            "data_criacao TIMESTAMP WITHOUT TIME ZONE NOT NULL, " + 
                            "FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE)"; // ⚠️ Referência corrigida
        
        String sqlReceitaIngrediente = "CREATE TABLE IF NOT EXISTS receita_ingrediente (" +
                                       "id_receita INT NOT NULL, " +
                                       "id_ingrediente INT NOT NULL, " +
                                       "quantidade DOUBLE PRECISION, " + 
                                       "PRIMARY KEY (id_receita, id_ingrediente), " +
                                       "FOREIGN KEY (id_receita) REFERENCES receita(id_receita) ON DELETE CASCADE, " +
                                       "FOREIGN KEY (id_ingrediente) REFERENCES ingrediente(id_ingrediente) ON DELETE RESTRICT)";

        String sqlAvaliacao = "CREATE TABLE IF NOT EXISTS avaliacao (" +
                              "id_avaliacao SERIAL PRIMARY KEY, " +
                              "id_receita INT NOT NULL, " +
                              "id_usuario INT NOT NULL, " + 
                              "comentario TEXT, " +
                              "curtidas INT DEFAULT 0, " +
                              "data_avaliacao TIMESTAMP WITHOUT TIME ZONE NOT NULL, " +
                              "FOREIGN KEY (id_receita) REFERENCES receita(id_receita) ON DELETE CASCADE, " +
                              "FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE)";

        String sqlCurtidas = "CREATE TABLE IF NOT EXISTS curtida ("+
                              "id_curtida SERIAL PRIMARY KEY, " +
                              "id_usuario INT NOT NULL, " +
                              "id_receita INT NOT NULL, " +
                              "UNIQUE (id_usuario, id_receita), " +
                              "FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE, " +
                              "FOREIGN KEY (id_receita) REFERENCES receita(id_receita) ON DELETE CASCADE" +
                              ")";

        String sqlReceitaSalva = "CREATE TABLE IF NOT EXISTS receita_salva (\r\n" + 
                                "id_receita INT NOT NULL," + 
                                "id_usuario INT NOT NULL," + 
                                "PRIMARY KEY (id_receita, id_usuario)," + 
                                "FOREIGN KEY (id_receita) REFERENCES receita(id_receita) ON DELETE CASCADE," + 
                                "FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE" + 
                                ")";

        String[] allSqlCreate = {
            sqlUsuario, 
            sqlIngrediente,
            sqlReceita,
            sqlReceitaIngrediente,
            sqlAvaliacao,
            sqlCurtidas ,
            sqlReceitaSalva
        };

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement()) {

            System.out.println("Iniciando a limpeza de tabelas...");
            for (String sql : sqlDrop) {
                try {
                    stmt.executeUpdate(sql);
                } catch (SQLException e) {
                }
            }
            
            System.out.println("Iniciando a criação das tabelas...");
            for (String sql : allSqlCreate) {
                stmt.executeUpdate(sql);
            }
            System.out.println("✅ Todas as tabelas foram criadas com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro CRÍTICO ao inicializar o banco de dados: " + e.getMessage());
            throw new RuntimeException("Falha na inicialização do banco.", e); 
        }
    }
    
    public static void main(String[] args) {
        createAllTables();
    }
}