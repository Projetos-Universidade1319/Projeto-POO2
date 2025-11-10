package service;

import model.AvaliacaoModel;
import dao.AvaliacaoDAO;
import dao.UsuarioDAO; 
import model.TipoAcaoPontuacao;
import java.sql.SQLException;
import java.util.List;

public class AvaliacaoService {
    
    private final AvaliacaoDAO avaliacaoDAO;
    private final UsuarioService usuarioService;
    
    public AvaliacaoService() {
        this.avaliacaoDAO = new AvaliacaoDAO(); 
        this.usuarioService = new UsuarioService(); 
    }

    /**
     * * @param avaliacao
     * @return 
     * @throws Exception 
     */
    public boolean processarAvaliacao(AvaliacaoModel avaliacao) throws Exception {
        
        if (avaliacao.getComentario() == null || avaliacao.getComentario().length() < 5) {
            throw new IllegalArgumentException("O comentário deve ter pelo menos 5 caracteres.");
        }
        
        boolean sucesso = false;
        try {
            sucesso = avaliacaoDAO.salvarNovaAvaliacao(avaliacao);
            
            if (sucesso) {
                boolean pontosSucesso = usuarioService.darPontosPorAcao(
                    avaliacao.getId_usuario(), 
                    TipoAcaoPontuacao.COMENTARIO 
                );
                
                if (!pontosSucesso) {
                    System.err.println("Aviso: Falha ao atribuir pontos ao usuário " + avaliacao.getId_usuario());
                }
            }
            
            return sucesso;
            
        } catch (SQLException e) {
            System.err.println("Erro no Service ao salvar avaliação: " + e.getMessage());
            throw new Exception("Falha ao salvar a avaliação no sistema.", e); 
        }
    }
    

    public List<AvaliacaoModel> buscarAvaliacoesPorReceita(int idReceita) throws SQLException {
        return avaliacaoDAO.buscarPorReceita(idReceita);
    }
}
