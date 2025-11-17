package service;

import model.ReceitaModel;
import model.UsuarioModel;
import model.TipoAcaoPontuacao;
import dao.ReceitaDAO;
import dao.CurtidaDAO;
import dao.UsuarioDAO;
import dao.ReceitaIngredienteDAO; 
import dao.ConnectionFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReceitaService {
    
    private final ReceitaDAO receitaDAO;
    private final CurtidaDAO curtidaDAO;
    private final UsuarioDAO usuarioDAO;
    private final ReceitaIngredienteDAO receitaIngredienteDAO; 
    private final UsuarioService usuarioService; 

    public ReceitaService() {
        this.receitaDAO = new ReceitaDAO();
        this.curtidaDAO = new CurtidaDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.receitaIngredienteDAO = new ReceitaIngredienteDAO(); 
        this.usuarioService = new UsuarioService(); 
    }
    
    public boolean processarPublicacao(ReceitaModel novaReceita, UsuarioModel autor) throws Exception {
        
        ReceitaModel receitaExistente = receitaDAO.buscarPorNomeEAutor(novaReceita.getNome(), autor.getIdUsuario());
        if (receitaExistente != null) {
            System.out.println("Receita '" + novaReceita.getNome() + "' já existe. Atualizando...");
            return false;
        } 
        
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false); 
            
            if (!receitaDAO.salvarNovaReceita(novaReceita, conn)) {
                 throw new SQLException("Falha ao salvar a receita principal.");
            }
            if (novaReceita.getListaIngredientes() != null && !novaReceita.getListaIngredientes().isEmpty()) {
                receitaIngredienteDAO.salvarItensReceita(novaReceita.getIdReceita(), novaReceita.getListaIngredientes(), conn);
            } else {
                 throw new IllegalArgumentException("Receita sem ingredientes. Transação abortada.");
            }
            
            usuarioService.darPontosPorAcao(autor.getIdUsuario(), TipoAcaoPontuacao.PUBLICACAO_RECEITA, conn);
            
            conn.commit();
            System.out.println("Publicação bem-sucedida e pontos atribuídos.");
            return true;
            
        } catch (SQLException | IllegalArgumentException e) {
            if (conn != null) conn.rollback();
            System.err.println("Falha na transação de publicação. Operações desfeitas.");
            throw new Exception("Falha transacional ao publicar receita: " + e.getMessage(), e); 
        } finally {
             if (conn != null) { conn.setAutoCommit(true); conn.close(); }
        }
    }

    public void curtirReceita(int idReceita, int idUsuario) throws Exception { 
        
        if (curtidaDAO.verificarCurtidaExistente(idReceita, idUsuario)) {
            throw new Exception("O usuário já curtiu esta receita.");
        }
        
        ReceitaModel receita = receitaDAO.buscarPorId(idReceita); 
        if (receita == null) {
            throw new Exception("Receita não encontrada.");
        }
        int idAutorReceita = receita.getIdUsuario();
        
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            
            curtidaDAO.salvarCurtida(idReceita, idUsuario, conn);
            
            receitaDAO.aumentarContadorCurtidas(idReceita, conn);

            usuarioService.darPontosPorAcao(idAutorReceita, TipoAcaoPontuacao.CURTIDA, conn);
            
            conn.commit();
            System.out.println("Curtida e pontuação registrada com sucesso.");
            
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            System.err.println("Transação de curtida falhou. Operações desfeitas.");
            throw new Exception("Falha ao registrar curtida devido a erro no banco de dados.", e); 
        } finally {
            if (conn != null) { conn.setAutoCommit(true); conn.close(); }
        }
    }

    public boolean editarReceita(ReceitaModel receita, int idUsuarioLogado) throws Exception {
        
        ReceitaModel receitaOriginal = receitaDAO.buscarPorId(receita.getIdReceita());
        if (receitaOriginal == null || receitaOriginal.getIdUsuario() != idUsuarioLogado) {
             throw new Exception("Erro de permissão: Usuário não é o autor desta receita.");
        }
        
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false); 

            if (!receitaDAO.atualizarReceitaCompleta(receita, conn)) {
                 throw new SQLException("Falha ao atualizar dados principais da receita.");
            }
            
            receitaIngredienteDAO.deletarIngredientesPorReceita(receita.getIdReceita(), conn); 

            if (receita.getListaIngredientes() != null && !receita.getListaIngredientes().isEmpty()) {
                receitaIngredienteDAO.salvarItensReceita(receita.getIdReceita(), receita.getListaIngredientes(), conn);
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            System.err.println("Transação falhou: Edição de receita desfeita. " + e.getMessage());
            throw new Exception("Falha transacional ao editar a receita.", e); 
        } finally {
            if (conn != null) { conn.setAutoCommit(true); conn.close(); }
        }
    }

    public List<ReceitaModel> buscarEOrdenar(String termoBusca, String categoria, UsuarioModel usuarioLogado) throws SQLException {
        
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

        resultadosComNivel.sort(Comparator
            .comparingInt((ReceitaModel r) -> r.getCurtidas()).reversed() 
            .thenComparing(r -> r.getCategoria()).reversed() 
            .thenComparing(r -> r.getDataCriacao(), Comparator.nullsLast(Comparator.reverseOrder()))
        );
        
        return resultadosComNivel;
    }
    
    public boolean excluirReceita(int idReceita, int idUsuarioLogado) throws Exception {
        
        ReceitaModel receitaOriginal = receitaDAO.buscarPorId(idReceita);
        
        if (receitaOriginal == null) { return false; }
        
        if (receitaOriginal.getIdUsuario() != idUsuarioLogado) {
             throw new Exception("Erro de permissão: Usuário não é o autor desta receita.");
        }

        try (Connection conn = ConnectionFactory.getConnection()) {
            return receitaDAO.deletarReceita(idReceita, conn); 
        } catch (SQLException e) {
            throw new Exception("Falha ao excluir a receita no BD: " + e.getMessage(), e);
        }
    }
}