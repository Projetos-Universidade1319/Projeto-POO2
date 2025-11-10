package service;

import dao.UsuarioDAO;
import model.UsuarioModel; 
import model.TipoAcaoPontuacao;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt; 

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * @param novoUsuario 
     * @return
     * @throws Exception 
     */
    public boolean cadastrar(UsuarioModel novoUsuario) throws Exception {
        
        if (usuarioDAO.buscarPorEmail(novoUsuario.getEmail()) != null) {
            throw new IllegalArgumentException("Erro: O e-mail " + novoUsuario.getEmail() + " já está cadastrado.");
        }
        
        String senhaTextoPuro = novoUsuario.getSenha();
        String senhaHash = BCrypt.hashpw(senhaTextoPuro, BCrypt.gensalt(10)); 
        novoUsuario.setSenha(senhaHash);
        try {
            return usuarioDAO.cadastrar(novoUsuario);
        } catch (SQLException e) {
            System.err.println("Erro SQL ao cadastrar usuário: " + e.getMessage());
            throw new Exception("Falha ao salvar usuário no banco de dados.", e);
        }
    }

    /**
     * @param email
     * @param senhaTextoPuro 
     * @return
     * @throws SQLException
     */
    public UsuarioModel login(String email, String senhaTextoPuro) throws SQLException {
        UsuarioModel usuarioArmazenado = usuarioDAO.buscarPorEmail(email);

        if (usuarioArmazenado == null) {
            return null;
        }

        boolean senhaCorreta = BCrypt.checkpw(senhaTextoPuro, usuarioArmazenado.getSenha());
        
        if (senhaCorreta) {
            usuarioArmazenado.setSenha(null); 
            return usuarioArmazenado;
        } else {
            return null;
        }
    }

    public boolean darPontosPorAcao(int idUsuario, TipoAcaoPontuacao tipoAcao) {
        
        int pontos = 0;

        switch (tipoAcao) {
            case PUBLICACAO_RECEITA:
                pontos = 10;
                break;
            case CURTIDA:
                pontos = 1;
                break;
            case COMENTARIO:
                pontos = 5;
                break;
            default:
                System.out.println("Ação desconhecida ou não mapeada. Nenhum ponto adicionado.");
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

    /**
     * @param idUsuario
     * @return t
     * @throws Exception 
     */
    public boolean removerUsuario(int idUsuario) throws Exception {
        
        try {
            boolean sucesso = usuarioDAO.deletarUsuario(idUsuario);
            
            if (sucesso) {
                System.out.println("Usuário ID " + idUsuario + " removido com sucesso.");
                return true;
            } else {
                throw new Exception("Falha: Não foi possível encontrar o usuário com ID " + idUsuario + " para deletar.");
            }
            
        } catch (SQLException e) {
            System.err.println("Erro SQL ao tentar remover usuário: " + e.getMessage());
            throw new Exception("Falha no sistema ao tentar deletar o usuário.", e);
        }
    }
}