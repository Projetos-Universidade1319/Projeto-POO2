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
                stmt.setInt(3, item.getQuantidade());
                stmt.addBatch();
            }
            
            stmt.executeBatch();
        }
    }
    
    public java.util.List<ReceitaIngredienteModel> buscarPorReceita(int idReceita) throws SQLException {
        java.util.List<ReceitaIngredienteModel> itens = new java.util.ArrayList<>();
        String sql = "SELECT ri.id_receita, ri.id_ingrediente, ri.quantidade, i.nome, i.unidade_medida " +
                     "FROM receita_ingrediente ri " +
                     "JOIN ingrediente i ON i.id_ingrediente = ri.id_ingrediente " +
                     "WHERE ri.id_receita = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idReceita);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ReceitaIngredienteModel rim = new ReceitaIngredienteModel();
                    rim.setId_receita(rs.getInt("id_receita"));
                    rim.setId_ingrediente(rs.getInt("id_ingrediente"));
                    rim.setQuantidade(rs.getInt("quantidade"));

                    model.IngredienteModel ing = new model.IngredienteModel();
                    ing.setId_ingrediente(rs.getInt("id_ingrediente"));
                    ing.setNome(rs.getString("nome"));
                    ing.setUnidade_medida(rs.getString("unidade_medida"));

                    rim.setIngrediente(ing);
                    itens.add(rim);
                }
            }
        }
        return itens;
    }

    public void deletarIngredientesPorReceita(int idReceita, Connection conn) throws SQLException {
        String sql = "DELETE FROM receita_ingrediente WHERE id_receita = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idReceita);
            stmt.executeUpdate();
        }
    }
}
