
package model;
import java.sql.*;

public class VerificaLogin implements VerificaPadrao {
    @Override
    public boolean casoVerifica(UsuarioModel caso) {
        if (caso.getNome() == null || caso.getNome().length() >= 30) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM usuarios WHERE nome_usuario = ?";
        try (Connection conn = DriverManager.getConnection(BD.url, BD.user, BD.senha);
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, caso.getNome());

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0; 
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return true;
    }
}
