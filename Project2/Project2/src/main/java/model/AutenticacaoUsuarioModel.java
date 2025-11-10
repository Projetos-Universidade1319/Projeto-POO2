package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class AutenticacaoUsuarioModel {
    static String url = "jdbc:postgresql://localhost:5432/Banco_Chef";
    static String user = "postgres";
    static String senha = "rodaminha0309";
    static String driver = "org.postgresql.Driver";

    Connection conn = null;
    PreparedStatement ps = null;

    public void cadastrar(String senhaUsuarioHash, String nome, String email) throws SQLException {
        
        Random rand = new Random();
        int id = 10000 + rand.nextInt(90000); 

        String sql = "INSERT INTO usuario (id_usuario, nome, email, senha, nivel_conta, pontuacao) " +
                     "VALUES (?, ?, ?, ?, 0, 0)"; 
        
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, senha);
            
            ps = conn.prepareStatement(sql);
            
            ps.setInt(1, id);
            ps.setString(2, nome); 
            ps.setString(3, email); 
            ps.setString(4, senhaUsuarioHash);
            
            ps.executeUpdate();
            
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
            throw new SQLException("Falha na execução do cadastro.", e); 
            
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar recursos: " + ex.getMessage());
            }
        }
    } 
    
    public void login() {

    }   
}
