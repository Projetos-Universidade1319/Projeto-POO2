package controller;

import java.util.List;
import java.util.ArrayList;
import model.ReceitaModel;
import model.UsuarioModel;
import model.TipoAcaoPontuacao;
import service.ReceitaService;
import service.UsuarioService;
import java.sql.SQLException;

public class ReceitaController {
    private final ReceitaService receitaService;
    private final UsuarioService usuarioService;
    
    public ReceitaController(){
       this.receitaService = new ReceitaService();
       this.usuarioService = new UsuarioService();
    }

    public boolean publicarNovaReceita(ReceitaModel novaReceita, UsuarioModel autor){
        if(novaReceita.getNome() == null || novaReceita.getModoPreparo() == null || novaReceita.getModoPreparo().length() < 10){
            System.out.println("Erro: Dados da receita incompletos (Nome ou Modo de Preparo).");
            return false;
        }

        try{
            boolean sucesso = receitaService.processarPublicacao(novaReceita, autor);

            if (sucesso) {
                usuarioService.darPontosPorAcao(autor.getIdUsuario(), TipoAcaoPontuacao.PUBLICACAO_RECEITA);
                System.out.println("Publicação bem-sucedida e pontos atribuídos.");
            }

            return sucesso;
            
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de validação: " + e.getMessage());
            return false;
        }
        catch (Exception e){
            System.err.println("Erro ao publicar receita: " + e.getMessage());
            return false;
        }
    }


    public List<ReceitaModel> buscarEClassificar(String termoBusca, String categoria, UsuarioModel usuarioLogado){
        try {
            List<ReceitaModel> resultados = receitaService.buscarEOrdenar(termoBusca, categoria);
            System.out.println("Busca por '" + termoBusca + "' realizada e ordenada.");
            return resultados;
            
        } catch (SQLException e) {
            System.err.println("Erro de conexão/banco de dados durante a busca: " + e.getMessage());
            return new ArrayList<>(); 
        }
    }

    public boolean curtirReceita(int idReceita, int idUsuario) {
        
        try {
            receitaService.registrarCurtida(idReceita, idUsuario); 
            usuarioService.darPontosPorAcao(idUsuario, TipoAcaoPontuacao.CURTIDA);
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Erro ao curtir receita: " + e.getMessage());
            return false;
        }
    }
}