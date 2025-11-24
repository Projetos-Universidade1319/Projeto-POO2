package dao;

import model.AvaliacaoModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AvaliacaoDAO {
    
    /**
     * @param avaliacao
     * @return 
     * @throws SQLException
     */
    public boolean salvarNovaAvaliacao(AvaliacaoModel avaliacao, Connection conn) throws SQLException { 
    String sql = "INSERT INTO avaliacao (id_receita, id_usuario, nome, comentario, curtidas, data_avaliacao) " +
                 "VALUES (?, ?, ?, ?, ?, ?)";
    
    try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        stmt.setInt(1, avaliacao.getId_receita());
        stmt.setInt(2, avaliacao.getId_usuario()); 
        stmt.setString(3, avaliacao.getNome() != null ? avaliacao.getNome() : "");
        stmt.setString(4, avaliacao.getComentario());
        stmt.setInt(5, avaliacao.getCurtidas());
        
        stmt.setTimestamp(6, java.sql.Timestamp.valueOf(avaliacao.getData_avaliacao())); 

        int linhasAfetadas = stmt.executeUpdate();
        
        try (ResultSet rs = stmt.getGeneratedKeys()) { 
            if (rs.next()) {
                avaliacao.setId_avaliacao(rs.getInt(1));
            }
        }
        
        return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar avaliação: " + e.getMessage());
            throw e; 
        }
    }
    
    /**
     * @param idReceita
     * @param idUsuario 
     * @return 
     * @throws SQLException
     */
    public boolean verificarAvaliacaoExistente(int idReceita, int idUsuario) throws SQLException {
        String sql = "SELECT 1 FROM avaliacao WHERE id_receita = ? AND id_usuario = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idReceita);
            stmt.setInt(2, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); 
            }

        } catch (SQLException e) {
            System.err.println("Erro ao verificar avaliação existente: " + e.getMessage());
            throw e; 
        }
    }

    public List<AvaliacaoModel> buscarPorReceita(int idReceita) throws SQLException {
        List<AvaliacaoModel> avaliacoes = new ArrayList<>();
        String sql = "SELECT id_avaliacao, id_usuario, nome, comentario, curtidas, data_avaliacao FROM avaliacao WHERE id_receita = ? ORDER BY data_avaliacao DESC";
        
        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idReceita);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AvaliacaoModel avaliacao = new AvaliacaoModel();
                    avaliacao.setId_avaliacao(rs.getInt("id_avaliacao"));
                    avaliacao.setId_usuario(rs.getInt("id_usuario"));
                    avaliacao.setNome(rs.getString("nome"));
                    avaliacao.setComentario(rs.getString("comentario"));
                    avaliacao.setCurtidas(rs.getInt("curtidas"));
                    avaliacao.setData_avaliacao(rs.getObject("data_avaliacao", LocalDateTime.class)); 
                    avaliacoes.add(avaliacao);
                }
            }
        }
        return avaliacoes;
    }

    /**
     * @param avaliacao
     * @return
     * @throws SQLException
     */
    public boolean atualizarAvaliacao(AvaliacaoModel avaliacao) throws SQLException {
        String sql = "UPDATE avaliacao SET comentario = ?, curtidas = ? WHERE id_avaliacao = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, avaliacao.getComentario());
            stmt.setInt(2, avaliacao.getCurtidas());
            stmt.setInt(3, avaliacao.getId_avaliacao()); 
            
            return stmt.executeUpdate() > 0;
        }
    }
    /**
     * @param idAvaliacao
     * @return
     * @throws SQLException
     */
    public boolean deletarAvaliacao(int idAvaliacao) throws SQLException {
        String sql = "DELETE FROM avaliacao WHERE id_avaliacao = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idAvaliacao);
            return stmt.executeUpdate() > 0;
        }
    }
    
}
