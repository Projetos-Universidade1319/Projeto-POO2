package dao;

import model.ReceitaModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReceitaSalvaDAO {

    public boolean salvar(int idUsuario, int idReceita) throws SQLException {
        String sql = "INSERT INTO receita_salva (id_usuario, id_receita) VALUES (?, ?)";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idReceita);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) { 
                return false;
            }
            throw e;
        }
    }

    public boolean remover(int idUsuario, int idReceita) throws SQLException {
        String sql = "DELETE FROM receita_salva WHERE id_usuario = ? AND id_receita = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idReceita);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean estaSalva(int idUsuario, int idReceita) throws SQLException {
        String sql = "SELECT 1 FROM receita_salva WHERE id_usuario = ? AND id_receita = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idReceita);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<ReceitaModel> buscarReceitasSalvas(int idUsuario) throws SQLException {
        List<ReceitaModel> receitas = new ArrayList<>();
        String sql = "SELECT r.* FROM receita_salva rs JOIN receita r ON rs.id_receita = r.id_receita WHERE rs.id_usuario = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                ReceitaDAO receitaDAO = new ReceitaDAO();
                while (rs.next()) {
                    ReceitaModel receita = new ReceitaModel();
                    receita.setIdReceita(rs.getInt("id_receita"));
                    receita.setNome(rs.getString("nome"));
                    receita.setDescricao(rs.getString("descricao"));
                    receitas.add(receita);
                }
            }
        }
        return receitas;
    }
}
