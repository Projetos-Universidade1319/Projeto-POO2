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
            "DROP TABLE IF EXISTS receita_salva CASCADE",
            "DROP TABLE IF EXISTS receita CASCADE",
            "DROP TABLE IF EXISTS ingrediente CASCADE",
            "DROP TABLE IF EXISTS usuario CASCADE"
        };
        
        String sqlUsuario = "CREATE TABLE IF NOT EXISTS usuario (" +
                            "id_usuario SERIAL PRIMARY KEY, " +
                            "nome VARCHAR(100) NOT NULL, " +
                            "email VARCHAR(100) UNIQUE NOT NULL, " +
                            "senha VARCHAR(60) NOT NULL, " +
                            "nivel_usuario VARCHAR(20) NOT NULL, " +
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
                                       "quantidade INT NOT NULL, " + 
                                       "PRIMARY KEY (id_receita, id_ingrediente), " +
                                       "FOREIGN KEY (id_receita) REFERENCES receita(id_receita) ON DELETE CASCADE, " +
                                       "FOREIGN KEY (id_ingrediente) REFERENCES ingrediente(id_ingrediente) ON DELETE RESTRICT)";

        String sqlAvaliacao = "CREATE TABLE IF NOT EXISTS avaliacao (" +
                              "id_avaliacao SERIAL PRIMARY KEY, " +
                              "id_receita INT NOT NULL, " +
                              "id_usuario INT NOT NULL, " + 
                              "nome VARCHAR(100), " +
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

        String[] allSqlCreate = {
            sqlUsuario, 
            sqlIngrediente,
            sqlReceita,
            sqlReceitaIngrediente,
            sqlAvaliacao,
            sqlCurtidas
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
    
    public static void inserirIngredientesIniciais() {
        // Lista de ingredientes iniciais com suas unidades de medida
        String[][] ingredientes = {
            {"Farinha de trigo", "g"},
            {"Açúcar", "g"},
            {"Ovo", "un"},
            {"Leite", "ml"},
            {"Manteiga", "g"},
            {"Fermento em pó", "colher de chá"},
            {"Sal", "pitada"},
            {"Óleo", "ml"},
            {"Chocolate em pó", "g"},
            {"Canela em pó", "colher de chá"},
            {"Baunilha", "colher de chá"},
            {"Fermento químico", "colher de chá"},
            {"Açúcar de confeiteiro", "g"},
            {"Leite em pó", "g"},
            {"Coco ralado", "g"},
            {"Achocolatado", "g"},
            {"Leite condensado", "ml"},
            {"Creme de leite", "ml"},
            {"Chocolate meio amargo", "g"},
            {"Manteiga sem sal", "g"}
        };

        String sql = "INSERT INTO ingrediente (nome, unidade_medida) VALUES (?, ?) " +
                    "ON CONFLICT (nome) DO NOTHING";

        try (Connection conn = ConnectionFactory.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            // Iniciar transação
            conn.setAutoCommit(false);
            
            // Inserir cada ingrediente
            for (String[] ingrediente : ingredientes) {
                stmt.setString(1, ingrediente[0]);
                stmt.setString(2, ingrediente[1]);
                stmt.addBatch();
            }
            
            // Executar em lote
            int[] resultados = stmt.executeBatch();
            conn.commit();
            
            int inseridos = 0;
            for (int r : resultados) {
                if (r >= 0) { // 1 para inserções, 0 para conflitos (ingrediente já existe)
                    inseridos++;
                }
            }
            
            System.out.println("✅ " + inseridos + " ingredientes iniciais adicionados com sucesso!");
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir ingredientes iniciais: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        createAllTables();
        inserirIngredientesIniciais();
    }
}