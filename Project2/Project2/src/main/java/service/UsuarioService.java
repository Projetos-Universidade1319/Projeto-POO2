package service;

import dao.UsuarioDAO;
import java.sql.SQLException;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * @param idUsuario 
     * @param tipoAcao 
     * @return 
     */
    public boolean darPontosPorAcao(int idUsuario, String tipoAcao) {
        
        int pontos = 0;
        
        switch (tipoAcao) {
            case "PUBLICACAO_RECEITA":
                pontos = 10;
                break;
            case "CURTIDA":
                pontos = 1;
                break;
            case "COMENTARIO":
                pontos = 5;
                break;
            default:
                System.out.println("Ação desconhecida. Nenhum ponto adicionado.");
                return false;
        }

        try {
            return usuarioDAO.atualizarPontuacao(idUsuario, pontos);
            
        } catch (SQLException e) {
            System.err.println("Erro ao dar pontos ao usuário " + idUsuario + ": " + e.getMessage());
            return false;
        }
    }
    
    public String calcularNivel(float pontuacaoAtual) {
        if (pontuacaoAtual >= 1000) return "Chef Master";
        if (pontuacaoAtual >= 500) return "Chef Sênior";
        if (pontuacaoAtual >= 100) return "Chef Júnior";
        return "Novato";
    }
}