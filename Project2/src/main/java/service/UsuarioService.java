package service;

import dao.UsuarioDAO;
import model.UsuarioModel;
import model.TipoAcaoPontuacao;
import model.NivelUsuario;
import java.sql.SQLException;
import java.sql.Connection;
import org.mindrot.jbcrypt.BCrypt;
import dao.ConnectionFactory;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

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

    public NivelUsuario calcularNivelUsuario(double pontuacao) {
        if (pontuacao >= 1500) {
            return NivelUsuario.CHEF_PRO;
        }
        if (pontuacao >= 500) {
            return NivelUsuario.CHEF_AMADOR;
        }
        return NivelUsuario.INICIANTE;
    }

    public void verificarEAtualizarNivel(UsuarioModel usuario) throws Exception {
        NivelUsuario nivelAtual = calcularNivelUsuario(usuario.getPontuacao());

        if (!nivelAtual.equals(usuario.getNivel())) {
            System.out.println("Usuário " + usuario.getNome() + " subiu para o nível: " + nivelAtual.name());
            usuario.setNivel(nivelAtual);
        }
    }

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
