package dao;

import model.UsuarioModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UsuarioDAO {
    
    /**
     * @param idUsuario 
     * @return 
     * @throws SQLException
     */
    public int getNivelAutor(int idUsuario) throws SQLException {
        String sql = "SELECT nivel_conta FROM usuario WHERE id_usuario = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("nivel_conta");
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao obter nível do autor (ID: " + idUsuario + "): " + e.getMessage());
            throw e; 
        }
    }
    
    /**
     * @param usuario
     * @return
     * @throws SQLException
     */
    public boolean cadastrar(UsuarioModel usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nome, email, senha, nivel_conta, pontuacao) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha()); 
            stmt.setString(4, usuario.getNivelConta()); 
            stmt.setFloat(5, usuario.getPontuacao());
            
            int linhasAfetadas = stmt.executeUpdate();
            
            System.out.println("Usuário cadastrado com sucesso!");
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
            throw e; 
        }
    }
    
    /**
     * @param email
     * @return 
     * @throws SQLException
     */
    public UsuarioModel buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT id_usuario, nome, email, senha, nivel_conta, pontuacao FROM usuario WHERE email = ?";
        UsuarioModel usuario = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new UsuarioModel();
                    
                    usuario.setIdUsuario(rs.getInt("id_usuario")); 
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setSenha(rs.getString("senha"));
                    usuario.setNivelConta(rs.getString("nivel_conta"));
                    usuario.setPontuacao(rs.getFloat("pontuacao"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por email: " + e.getMessage());
            throw e;
        }
        return usuario;
    }

    /**
     * @param idUsuario
     * @param pontos
     * @return
     * @throws SQLException
     */
    public boolean atualizarPontuacao(int idUsuario, int pontos) throws SQLException {
        String sql = "UPDATE usuario SET pontuacao = pontuacao + ? WHERE id_usuario = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pontos);
            stmt.setInt(2, idUsuario);
            
            int linhasAfetadas = stmt.executeUpdate();
            
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar pontuação do usuário " + idUsuario + ": " + e.getMessage());
            throw e; 
        }
    }
}