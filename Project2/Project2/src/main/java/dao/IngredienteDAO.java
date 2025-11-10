package dao;

import model.IngredienteModel;
import model.UnidadeMedida;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class IngredienteDAO {

    /**
     * @param ingrediente 
     * @return 
     * @throws SQLException
     */
    public boolean salvarNovoIngrediente(IngredienteModel ingrediente) throws SQLException {
        String sql = "INSERT INTO ingrediente (nome, unidade_medida) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, ingrediente.getNome());
            stmt.setString(2, ingrediente.getUnidade_medida().name()); 

            int linhasAfetadas = stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    ingrediente.setId_ingrediente(rs.getInt(1));
                }
            }
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar novo ingrediente: " + e.getMessage());
            throw e; 
        }
    }

    /**
     * @param nome 
     * @return 
     * @throws SQLException
     */
    public IngredienteModel buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT id_ingrediente, nome, unidade_medida FROM ingrediente WHERE nome = ?";
        IngredienteModel ingrediente = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ingrediente = new IngredienteModel();
                    ingrediente.setId_ingrediente(rs.getInt("id_ingrediente"));
                    ingrediente.setNome(rs.getString("nome"));
                    String unidadeStr = rs.getString("unidade_medida");
                    ingrediente.setUnidade_medida(UnidadeMedida.valueOf(unidadeStr));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar ingrediente por nome: " + e.getMessage());
            throw e;
        }
        return ingrediente;
    }
    
    /**
     * @return 
     * @throws SQLException
     */
    public List<IngredienteModel> buscarTodosIngredientes() throws SQLException {
        List<IngredienteModel> ingredientes = new ArrayList<>();
        String sql = "SELECT id_ingrediente, nome, unidade_medida FROM ingrediente ORDER BY nome";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                IngredienteModel ingrediente = new IngredienteModel();
                ingrediente.setId_ingrediente(rs.getInt("id_ingrediente"));
                ingrediente.setNome(rs.getString("nome"));
                ingrediente.setUnidade_medida(UnidadeMedida.valueOf(rs.getString("unidade_medida")));
                ingredientes.add(ingrediente);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os ingredientes: " + e.getMessage());
            throw e;
        }
        return ingredientes;
    }
}
