package dao;

import model.IngredienteModel;
import model.UnidadeMedida;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredienteDAO {


    public IngredienteModel buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM ingrediente WHERE nome ILIKE ?"; 
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome); 
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearIngrediente(rs);
                }
            }
        }
        return null;
    }

    public List<IngredienteModel> buscarTodos() throws SQLException {
        List<IngredienteModel> ingredientes = new ArrayList<>();
        String sql = "SELECT * FROM ingrediente ORDER BY nome";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ingredientes.add(mapearIngrediente(rs));
            }
        }
        return ingredientes;
    }

    public boolean salvarNovoIngrediente(IngredienteModel ingrediente, Connection conn) throws SQLException {
        String sql = "INSERT INTO ingrediente (nome, unidade_medida) VALUES (?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id_ingrediente"})) {
            stmt.setString(1, ingrediente.getNome());
            stmt.setString(2, ingrediente.getUnidade_medida().name()); 

            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        ingrediente.setId_ingrediente(rs.getInt("id_ingrediente")); 
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    private IngredienteModel mapearIngrediente(ResultSet rs) throws SQLException {
        IngredienteModel ingrediente = new IngredienteModel();
        ingrediente.setId_ingrediente(rs.getInt("id_ingrediente"));
        ingrediente.setNome(rs.getString("nome"));
        
        String unidadeStr = rs.getString("unidade_medida");
        try {
            ingrediente.setUnidade_medida(UnidadeMedida.valueOf(unidadeStr));
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de mapeamento de UnidadeMedida: " + unidadeStr);
        }
        return ingrediente;
    }
}