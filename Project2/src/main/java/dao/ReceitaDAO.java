package dao;

import model.ReceitaModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReceitaDAO {

    public ReceitaModel buscarPorId(int idReceita) throws SQLException {
        String sql = "SELECT * FROM receita WHERE id_receita = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idReceita);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearReceita(rs);
                }
            }
        }
        return null;
    }

    public ReceitaModel buscarPorNomeEAutor(String nome, int idAutor) throws SQLException {
        String sql = "SELECT * FROM receita WHERE nome = ? AND id_usuario = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setInt(2, idAutor);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearReceita(rs);
                }
            }
        }
        return null;
    }
    
    public List<ReceitaModel> buscarPorTermoECategoria(String termo, String categoria) throws SQLException {
        List<ReceitaModel> receitas = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM receita WHERE 1=1");
        
        if (termo != null && !termo.trim().isEmpty()) {
            sql.append(" AND (nome ILIKE ? OR descricao ILIKE ?)");
        }
        if (categoria != null && !categoria.trim().isEmpty()) {
            // Comparar categoria de forma case-insensitive
            sql.append(" AND UPPER(categoria) = UPPER(?)");
        }
        sql.append(" ORDER BY data_criacao DESC");

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (termo != null && !termo.trim().isEmpty()) {
                stmt.setString(index++, "%" + termo + "%");
                stmt.setString(index++, "%" + termo + "%");
            }
            if (categoria != null && !categoria.trim().isEmpty()) {
                stmt.setString(index++, categoria);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    receitas.add(mapearReceita(rs));
                }
            }
        }
        return receitas;
    }

    public List<ReceitaModel> buscarPorAutor(int idAutor) throws SQLException {
        List<ReceitaModel> receitas = new ArrayList<>();
        String sql = "SELECT * FROM receita WHERE id_usuario = ? ORDER BY data_criacao DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAutor);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    receitas.add(mapearReceita(rs));
                }
            }
        }
        return receitas;
    }

    public boolean salvarNovaReceita(ReceitaModel receita, Connection conn) throws SQLException {
    String sql = "INSERT INTO receita (nome, descricao, modo_preparo, id_usuario, categoria, data_criacao, curtidas) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    try (PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id_receita"})) {
        stmt.setString(1, receita.getNome());
        stmt.setString(2, receita.getDescricao());
        stmt.setString(3, receita.getModoPreparo());
        stmt.setInt(4, receita.getIdUsuario());
        stmt.setString(5, receita.getCategoria());
        stmt.setTimestamp(6, Timestamp.valueOf(receita.getDataCriacao()));
        stmt.setInt(7, 0);

        int linhasAfetadas = stmt.executeUpdate();
        
        if (linhasAfetadas > 0) {
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    receita.setIdReceita(rs.getInt(1)); 
                    return true;
                }
            }
        }
        
        return false;
        }
    }

    public void aumentarContadorCurtidas(int idReceita, Connection conn) throws SQLException {
        String sql = "UPDATE receita SET curtidas = curtidas + 1 WHERE id_receita = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idReceita);
            stmt.executeUpdate();
        }
    }

    public boolean atualizarReceitaCompleta(ReceitaModel receita, Connection conn) throws SQLException {
        String sql = "UPDATE receita SET nome = ?, descricao = ?, modo_preparo = ?, categoria = ? WHERE id_receita = ? AND id_usuario = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, receita.getNome());
            stmt.setString(2, receita.getDescricao());
            stmt.setString(3, receita.getModoPreparo());
            stmt.setString(4, receita.getCategoria());
            stmt.setInt(5, receita.getIdReceita());
            stmt.setInt(6, receita.getIdUsuario());
            
            return stmt.executeUpdate() > 0;
        }
    }
    

    public boolean deletarReceita(int idReceita, Connection conn) throws SQLException {
        String sql = "DELETE FROM receita WHERE id_receita = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idReceita);
            return stmt.executeUpdate() > 0;
        }
    }
    
    
    private ReceitaModel mapearReceita(ResultSet rs) throws SQLException {
        ReceitaModel receita = new ReceitaModel();
        receita.setIdReceita(rs.getInt("id_receita"));
        receita.setNome(rs.getString("nome"));
        receita.setDescricao(rs.getString("descricao"));
        receita.setModoPreparo(rs.getString("modo_preparo"));
        receita.setIdUsuario(rs.getInt("id_usuario"));
        receita.setCategoria(rs.getString("categoria"));
        Timestamp ts = rs.getTimestamp("data_criacao");
        if (ts != null) {
            receita.setDataCriacao(ts.toLocalDateTime());
        }
        receita.setCurtidas(rs.getInt("curtidas"));
        return receita;
    }
    
}