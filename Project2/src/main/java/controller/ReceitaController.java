package controller;

import java.util.List;
import java.util.ArrayList;
import model.ReceitaModel;
import model.UsuarioModel;
import service.ReceitaService;
import java.sql.SQLException;

public class ReceitaController {
    private final ReceitaService receitaService;
    
    public ReceitaController(){
       this.receitaService = new ReceitaService();
    }

    public boolean publicarNovaReceita(ReceitaModel novaReceita, UsuarioModel autor){
        if(novaReceita.getNome() == null || novaReceita.getModoPreparo() == null || novaReceita.getModoPreparo().length() < 10){
            System.out.println("Erro: Dados da receita incompletos (Nome ou Modo de Preparo).");
            return false;
        }

        try{
            boolean sucesso = receitaService.processarPublicacao(novaReceita, autor);

            if (sucesso) {
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

    /**
     * @param termoBusca
     * @param categoria 
     * @param usuarioLogado
     * @return
     */
    public List<ReceitaModel> buscarEClassificar(String termoBusca, String categoria, UsuarioModel usuarioLogado){
        try {
            List<ReceitaModel> resultados = receitaService.buscarEOrdenar(termoBusca, categoria, usuarioLogado);
            System.out.println("Busca por '" + termoBusca + "' realizada e ordenada.");
            return resultados;
            
        } catch (SQLException e) {
            System.err.println("Erro de conexão/banco de dados durante a busca: " + e.getMessage());
            return new ArrayList<>(); 
        }
    }

    /**
     * @param idReceita 
     * @param idUsuario
     * @return 
     */
    public boolean curtirReceita(int idReceita, int idUsuario) {
        
        try {
            receitaService.curtirReceita(idReceita, idUsuario); 
            
            System.out.println("Curtida registrada com sucesso.");
            return true;
            
        } catch (Exception e) {
            System.err.println("Erro ao curtir receita: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * @param receita 
     * @param idUsuarioLogado 
     * @return 
     */
    public boolean editarReceita(ReceitaModel receita, int idUsuarioLogado) {
        try {
            return receitaService.editarReceita(receita, idUsuarioLogado);
        } catch (Exception e) {
            System.err.println("Erro ao editar receita: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * @param idReceita
     * @param idUsuarioLogado 
     * @return
     */
    public String excluirReceita(int idReceita, int idUsuarioLogado) {
        try {
            boolean sucesso = receitaService.excluirReceita(idReceita, idUsuarioLogado);
            if (sucesso) {
                return "SUCESSO: Receita ID " + idReceita + " excluída.";
            } else {
                return "FALHA: Receita não encontrada ou sem permissão.";
            }
        } catch (Exception e) {
            return "ERRO INTERNO: " + e.getMessage();
        }
    }

    public java.util.List<ReceitaModel> listarReceitasDoUsuario(UsuarioModel usuario) {
        try {
            if (usuario == null) {
                return new java.util.ArrayList<>();
            }
            return receitaService.buscarPorAutor(usuario.getIdUsuario());
        } catch (Exception e) {
            System.err.println("Erro ao buscar receitas do usuário: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
}