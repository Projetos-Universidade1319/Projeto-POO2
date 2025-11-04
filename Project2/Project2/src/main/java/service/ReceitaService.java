package service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import model.ReceitaModel;
import model.UsuarioModel;
import dao.ReceitaDAO;
import dao.CurtidaDAO;
import dao.UsuarioDAO;
import java.sql.SQLException;

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
        
        Collections.sort(resultados, new Comparator<ReceitaModel>() {
            @Override
            public int compare(ReceitaModel r1, ReceitaModel r2) {

                int pontuacao1 = r1.getCurtidas(); 
                int pontuacao2 = r2.getCurtidas();
                
                if (pontuacao1 != pontuacao2) {
                    return Integer.compare(pontuacao2, pontuacao1);
                }
                
                try {
                    int nivel1 = usuarioDAO.getNivelAutor(r1.getIdUsuario()); 
                    int nivel2 = usuarioDAO.getNivelAutor(r2.getIdUsuario());
                    
                    if (nivel1 != nivel2) {
                        return Integer.compare(nivel2, nivel1);
                    }
                } catch (SQLException e) {
                    System.err.println("Falha temporária ao buscar nível do autor. Tratando como empate: " + e.getMessage());
                    return 0; 
                }

                if (r1.getDataCriacao() != null && r2.getDataCriacao() != null) {
                    return r2.getDataCriacao().compareTo(r1.getDataCriacao());
                }

                return 0;
            }
        });
        
        return resultados;
    }

    public void registrarCurtida(int idReceita, int idUsuario) throws Exception {
        
        if (curtidaDAO.verificarCurtidaExistente(idReceita, idUsuario)) {
            throw new Exception("O usuário já curtiu esta receita.");
        }
        
        curtidaDAO.salvarCurtida(idReceita, idUsuario);
        receitaDAO.aumentarContadorCurtidas(idReceita);
        
        System.out.println("Curtida registrada com sucesso.");
    }
}