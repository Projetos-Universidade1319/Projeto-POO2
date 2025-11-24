package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    
    private static final String URL = "jdbc:postgresql://localhost:5432/Banco_Chef";
    private static final String USER = "postgres";
    private static final String PASSWORD = "rodaminha0309";
    private static final String DRIVER = "org.postgresql.Driver";

    /**
     * @return 
     * @throws SQLException 
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DRIVER); 
            return DriverManager.getConnection(URL, USER, PASSWORD);
            
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC do PostgreSQL não encontrado. Verifique o seu pom.xml/JARs.", e);
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}
