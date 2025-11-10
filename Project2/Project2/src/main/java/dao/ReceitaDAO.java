package dao;

import model.ReceitaModel;
import model.ReceitaIngredienteModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReceitaDAO {
    
    public boolean salvarNovaReceita(ReceitaModel receita) throws SQLException {

        String sqlReceita = "INSERT INTO receita (nome, descricao, id_usuario_autor, categoria, curtidas, data_criacao) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlIngrediente = "INSERT INTO receita_ingrediente (id_receita, id_ingrediente, quantidade) VALUES (?, ?, ?)";
        
        Connection conn = null;
        boolean sucesso = false;

        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false); 
            
            try (PreparedStatement stmtReceita = conn.prepareStatement(
                 sqlReceita, 
                 Statement.RETURN_GENERATED_KEYS
            )) {
                stmtReceita.setString(1, receita.getNome());
                stmtReceita.setString(2, receita.getDescricao());
                
                stmtReceita.setInt(3, receita.getIdUsuario());
                
                stmtReceita.setString(4, receita.getCategoria());
                stmtReceita.setInt(5, receita.getCurtidas());
                stmtReceita.setObject(6, receita.getDataCriacao());
                
                stmtReceita.executeUpdate();

                try (ResultSet rs = stmtReceita.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idReceitaGerado = rs.getInt(1);
                        receita.setIdReceita(idReceitaGerado);

                        try (PreparedStatement stmtIngrediente = conn.prepareStatement(sqlIngrediente)) {
                            for (ReceitaIngredienteModel item : receita.getListaIngredientes()) { 
                                stmtIngrediente.setInt(1, idReceitaGerado);
                                stmtIngrediente.setInt(2, item.getId_ingrediente());
                                stmtIngrediente.setDouble(3, item.getQuantidade()); 
                                
                                stmtIngrediente.addBatch();
                            }
                            stmtIngrediente.executeBatch(); 
                        }
                        conn.commit(); 
                        sucesso = true;
                    } else {
                        throw new SQLException("Falha ao obter o ID da nova receita.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Falha na transação de salvar receita. Desfazendo operações...");
            if (conn != null) {
                conn.rollback();
            }
            throw e; 
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
        return sucesso;
    }

    public ReceitaModel buscarPorNomeEAutor(String nome, int idAutor) throws SQLException {
        String sql = "SELECT id_receita, nome, curtidas, id_usuario_autor FROM receita WHERE nome = ? AND id_usuario_autor = ?";
        ReceitaModel receita = null;
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nome);
            stmt.setInt(2, idAutor);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    receita = new ReceitaModel();
                    receita.setIdReceita(rs.getInt("id_receita"));
                    receita.setNome(rs.getString("nome"));
                    receita.setIdUsuario(rs.getInt("id_usuario_autor")); 
                    receita.setCurtidas(rs.getInt("curtidas"));
                }
            }
        }
        return receita;
    }
    
    public boolean atualizarReceita(ReceitaModel receita) throws SQLException {
        String sql = "UPDATE receita SET descricao = ?, categoria = ? WHERE id_receita = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, receita.getDescricao());
            stmt.setString(2, receita.getCategoria());
            stmt.setInt(3, receita.getIdReceita());
            
            return stmt.executeUpdate() > 0;
        }
    }

    public void aumentarContadorCurtidas(int idReceita) throws SQLException {
        String sql = "UPDATE receita SET curtidas = curtidas + 1 WHERE id_receita = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idReceita);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Falha ao incrementar curtidas da receita " + idReceita + ": " + e.getMessage());
            throw e;
        }
    }
    
    public List<ReceitaModel> buscarPorTermoECategoria(String termoBusca, String categoria) throws SQLException {
        List<ReceitaModel> resultados = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder("SELECT id_receita, nome, descricao, id_usuario_autor, curtidas, data_criacao FROM receita WHERE 1=1 ");
        
        if (termoBusca != null && !termoBusca.trim().isEmpty()) {
            sql.append(" AND (nome LIKE ? OR descricao LIKE ?)");
        }
        if (categoria != null && !categoria.trim().isEmpty()) {
            sql.append(" AND categoria = ?");
        }
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            int index = 1;
            if (termoBusca != null && !termoBusca.trim().isEmpty()) {
                stmt.setString(index++, "%" + termoBusca + "%");
                stmt.setString(index++, "%" + termoBusca + "%");
            }
            if (categoria != null && !categoria.trim().isEmpty()) {
                stmt.setString(index++, categoria);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ReceitaModel receita = new ReceitaModel();
                    receita.setIdReceita(rs.getInt("id_receita"));
                    receita.setNome(rs.getString("nome"));
                    receita.setIdUsuario(rs.getInt("id_usuario_autor")); 
                    receita.setCurtidas(rs.getInt("curtidas"));
                    receita.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime()); 
                    
                    resultados.add(receita);
                }
            }
        }
        
        return resultados;
    }
}