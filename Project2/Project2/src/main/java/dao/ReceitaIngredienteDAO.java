package dao;

import model.ReceitaIngredienteModel; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ReceitaIngredienteDAO {

    /**
     * @param idReceita
     * @param itensReceita
     * @param conn 
     * @throws SQLException
     */
    public void salvarItensReceita(int idReceita, List<ReceitaIngredienteModel> itensReceita, Connection conn) throws SQLException {
        String sql = "INSERT INTO receita_ingrediente (id_receita, id_ingrediente, quantidade) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (ReceitaIngredienteModel item : itensReceita) {
                stmt.setInt(1, idReceita);
                stmt.setInt(2, item.getId_ingrediente()); 
                stmt.setDouble(3, item.getQuantidade()); 
                stmt.addBatch();
            }
            
            stmt.executeBatch();
        }
    }
    
    public void deletarIngredientesPorReceita(int idReceita, Connection conn) throws SQLException {
        String sql = "DELETE FROM receita_ingrediente WHERE id_receita = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idReceita);
            stmt.executeUpdate();
        }
    }
}
