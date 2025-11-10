package service;

import java.util.Comparator;
import java.util.List;
import model.ReceitaModel;
import model.UsuarioModel;
import dao.ReceitaDAO;
import dao.CurtidaDAO;
import dao.UsuarioDAO;
import dao.ConnectionFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.stream.Collectors;

public class ReceitaService {
    
    private final ReceitaDAO receitaDAO;
    private final CurtidaDAO curtidaDAO;
    private final UsuarioDAO usuarioDAO;
    
    public ReceitaService() {
        this.receitaDAO = new ReceitaDAO();
        this.curtidaDAO = new CurtidaDAO();
        this.usuarioDAO = new UsuarioDAO();
    }

    public boolean processarPublicacao(ReceitaModel novaReceita, UsuarioModel autor) throws SQLException {
        
        ReceitaModel receitaExistente = receitaDAO.buscarPorNomeEAutor(novaReceita.getNome(), autor.getIdUsuario());
        
        if (receitaExistente != null) {
            System.out.println("Receita '" + novaReceita.getNome() + "' já existe. Aplicando regra de competição...");
            return receitaDAO.atualizarReceita(receitaExistente); 
        } else {
            System.out.println("Publicando nova receita...");
            return receitaDAO.salvarNovaReceita(novaReceita);
        }
    }

    public List<ReceitaModel> buscarEOrdenar(String termoBusca, String categoria) throws SQLException {
        
        List<ReceitaModel> resultados = receitaDAO.buscarPorTermoECategoria(termoBusca, categoria);
        
        List<ReceitaModel> resultadosComNivel = resultados.stream()
            .map(r -> {
                try {
                    r.setCategoria(usuarioDAO.getNivelAutor(r.getIdUsuario()));
                } catch (SQLException e) {
                    System.err.println("Erro ao carregar nível do autor " + r.getIdUsuario());
                }
                return r;
            }).collect(Collectors.toList());

        resultadosComNivel.sort((r1, r2) -> {
            
            int comparacaoCurtidas = Integer.compare(r2.getCurtidas(), r1.getCurtidas());
            if (comparacaoCurtidas != 0) {
                return comparacaoCurtidas;
            }

            String nivel1 = r1.getCategoria();
            String nivel2 = r2.getCategoria();
            

            int comparacaoNivel = nivel2.compareTo(nivel1);
            if (comparacaoNivel != 0) {
                return comparacaoNivel;
            }
            
            if (r1.getDataCriacao() != null && r2.getDataCriacao() != null) {
                return r2.getDataCriacao().compareTo(r1.getDataCriacao());
            }

            return 0;
        });
        
        return resultadosComNivel;
    }


    /**
     * @param idReceita
     * @param idUsuario
     * @throws Exception
     */
    public void registrarCurtida(int idReceita, int idUsuario) throws Exception {

        if (curtidaDAO.verificarCurtidaExistente(idReceita, idUsuario)) {
            throw new Exception("O usuário já curtiu esta receita.");
        }
        
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            curtidaDAO.salvarCurtida(idReceita, idUsuario);
            receitaDAO.aumentarContadorCurtidas(idReceita);
            
            conn.commit();
            
            System.out.println("Curtida registrada com sucesso.");
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); 
                } catch (SQLException rollbackEx) {
                    System.err.println("Erro durante o rollback: " + rollbackEx.getMessage());
                }
            }
            System.err.println("Transação de curtida falhou. Operações desfeitas.");
            throw new Exception("Falha ao registrar curtida devido a erro no banco de dados.", e); 
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    System.err.println("Erro ao fechar conexão: " + closeEx.getMessage());
                }
            }
        }
    }
}