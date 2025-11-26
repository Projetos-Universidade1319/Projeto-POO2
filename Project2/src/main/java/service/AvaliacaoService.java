package service;

import model.AvaliacaoModel;
import dao.AvaliacaoDAO;
import dao.ConnectionFactory; 
import model.TipoAcaoPontuacao;
import java.sql.Connection;    
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
     * @param avaliacao
     * @return
     * @throws Exception 
     */
    public boolean processarAvaliacao(AvaliacaoModel avaliacao) throws Exception {
        
        if (avaliacao.getComentario() == null || avaliacao.getComentario().length() < 5) {
            throw new IllegalArgumentException("O comentário deve ter pelo menos 5 caracteres.");
        }
        
        Connection conn = null; 
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false); 

            boolean sucesso = avaliacaoDAO.salvarNovaAvaliacao(avaliacao, conn); 
            
            if (!sucesso) {
                 throw new SQLException("Falha ao inserir avaliação. Nenhuma linha afetada.");
            }
            
            boolean pontosSucesso = usuarioService.darPontosPorAcao(
                avaliacao.getId_usuario(), 
                TipoAcaoPontuacao.COMENTARIO,
                conn 
            ); 
            
            if (!pontosSucesso) {
                 throw new SQLException("Falha ao atribuir pontos. Transação será desfeita.");
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erro no Service ao processar avaliação. Desfazendo operações...");
            if (conn != null) {
                conn.rollback();
            }
            throw new Exception("Falha transacional ao salvar a avaliação e atribuir pontos.", e); 
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar conexão após transação: " + e.getMessage());
                }
            }
        }
    }

    public List<AvaliacaoModel> buscarAvaliacoesPorReceita(int idReceita) throws SQLException {
        return avaliacaoDAO.buscarPorReceita(idReceita);
    }

    /**
     * @param avaliacao
     * @return
     * @throws Exception
     */
    public boolean editarAvaliacao(AvaliacaoModel avaliacao) throws Exception {
        try {
            return avaliacaoDAO.atualizarAvaliacao(avaliacao); 
        } catch (SQLException e) {
            throw new Exception("Falha ao atualizar a avaliação no banco de dados.", e);
        }
    }

    /**
     * @param idAvaliacao
     * @return
     * @throws Exception
     */
    public boolean excluirAvaliacao(int idAvaliacao) throws Exception {
        try {
            return avaliacaoDAO.deletarAvaliacao(idAvaliacao); 
        } catch (SQLException e) {
            throw new Exception("Falha ao excluir a avaliação. Tente novamente.", e);
        }
    }
}