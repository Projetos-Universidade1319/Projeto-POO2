package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CurtidaDAO {
    
    /**
     * * @param idReceita 
     * @param idUsuario 
     * @return 
     * @throws SQLException 
     */
    public boolean salvarCurtida(int idReceita, int idUsuario) throws SQLException {
        String sql = "INSERT INTO curtida (id_receita, id_usuario) VALUES (?, ?)";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idReceita);
            stmt.setInt(2, idUsuario);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar curtida (Receita: " + idReceita + ", Usuário: " + idUsuario + "): " + e.getMessage());
            throw e; 
        }
    }
    
    /**
     * * @param idReceita 
     * @param idUsuario 
     * @return
     * @throws SQLException 
     */
    public boolean verificarCurtidaExistente(int idReceita, int idUsuario) throws SQLException {
        String sql = "SELECT 1 FROM curtida WHERE id_receita = ? AND id_usuario = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idReceita);
            stmt.setInt(2, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("Erro ao verificar curtida existente: " + e.getMessage());
            throw e; 
        }
    }
}