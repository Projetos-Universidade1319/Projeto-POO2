package dao;

import model.UsuarioModel;
import model.NivelUsuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    
    public String getNivelAutor(int idUsuario) throws SQLException {
        String sql = "SELECT nivel_usuario FROM usuario WHERE id_usuario = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nivel_usuario");
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao obter nível do autor (ID: " + idUsuario + "): " + e.getMessage());
            throw e;
        }
    }
    
    public boolean cadastrar(UsuarioModel usuario, Connection conn) throws SQLException {
        String sql = "INSERT INTO usuario (nome, email, senha, nivel_usuario, pontuacao) VALUES (?, ?, ?, ?, ?)";
    
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getNivel().name());
            stmt.setFloat(5, usuario.getPontuacao());
            
            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
            throw e;
        }
    }
    
    public UsuarioModel buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT id_usuario, nome, email, senha, nivel_usuario, pontuacao FROM usuario WHERE email = ?";
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
                    String nivelStr = rs.getString("nivel_usuario");
                    if (nivelStr != null) {
                        usuario.setNivel(NivelUsuario.valueOf(nivelStr));
                    }
                    
                    usuario.setPontuacao(rs.getFloat("pontuacao"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por email: " + e.getMessage());
            throw e;
        }
        return usuario;
    }

    public boolean atualizarPontuacao(int idUsuario, int pontos, Connection conn) throws SQLException {
        String sql = "UPDATE usuario SET pontuacao = pontuacao + ? WHERE id_usuario = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pontos);
            stmt.setInt(2, idUsuario);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar pontuação do usuário " + idUsuario + ": " + e.getMessage());
            throw e;
        }
    }
    
    public boolean deletarUsuario(int idUsuario, Connection conn) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            int linhasAfetadas = stmt.executeUpdate();
            
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar usuário (ID: " + idUsuario + "): " + e.getMessage());
            throw e;
        }
    }
}
