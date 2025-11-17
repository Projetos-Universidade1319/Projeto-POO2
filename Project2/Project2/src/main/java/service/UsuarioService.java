package service;

import dao.UsuarioDAO;
import model.UsuarioModel; 
import model.TipoAcaoPontuacao;
import java.sql.SQLException;
import java.sql.Connection; 
import org.mindrot.jbcrypt.BCrypt; 
import model.NivelConta;
import java.util.Arrays;
import java.util.Comparator;
import dao.ConnectionFactory; 

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
        
        try (Connection conn = ConnectionFactory.getConnection()) {
            return usuarioDAO.cadastrar(novoUsuario, conn); 
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

    /**
     * @param idUsuario 
     * @param tipoAcao 
     * @param conn 
     * @return 
     */
    public boolean darPontosPorAcao(int idUsuario, TipoAcaoPontuacao tipoAcao, Connection conn) throws SQLException {
        
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

        return usuarioDAO.atualizarPontuacao(idUsuario, pontos, conn); 
    }

    /**
     * @param pontuacao 
     * @return 
     */
    public NivelConta calcularNivelUsuario(double pontuacao) {
        
        return Arrays.stream(NivelConta.values())
                .filter(nivel -> pontuacao >= nivel.getPontuacaoMinima())
                .max(Comparator.comparingInt(NivelConta::getPontuacaoMinima))
                .orElse(NivelConta.PADRAO);
    }

    /**
     * @param usuario 
     * @throws Exception
     */
    public void verificarEAtualizarNivel(UsuarioModel usuario) throws Exception {
        NivelConta nivelAtual = calcularNivelUsuario(usuario.getPontuacao());

        if (!nivelAtual.name().equals(usuario.getNivelConta().name())) {
            
            System.out.println("🎉 Usuário " + usuario.getNome() + " subiu para o nível: " + nivelAtual.getNomeNivel());

            usuario.setNivelConta(nivelAtual);
        }
    }

    /**
     * @param idUsuario 
     * @return 
     * @throws Exception
     */
    public boolean removerUsuario(int idUsuario) throws Exception {
        
        try (Connection conn = ConnectionFactory.getConnection()) {
            
            boolean sucesso = usuarioDAO.deletarUsuario(idUsuario, conn); 
            
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