package controller;

import model.AvaliacaoModel;
import service.AvaliacaoService; 
import java.time.LocalDateTime;

public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService; 

    public AvaliacaoController() {
        this.avaliacaoService = new AvaliacaoService();
    }
    
    /**
     * * @param idReceita
     * @param idUsuario 
     * @param comentario
     * @return 
     */
    public String registrarNovaAvaliacao(int idReceita, int idUsuario, String comentario) {
        
        if (comentario == null || comentario.trim().isEmpty()) {
            return "ERRO: O comentário da avaliação não pode ser vazio.";
        }
        
        try {
            AvaliacaoModel novaAvaliacao = new AvaliacaoModel();
            
            novaAvaliacao.setId_receita(idReceita);
            novaAvaliacao.setId_usuario(idUsuario);
            novaAvaliacao.setNome("");
            novaAvaliacao.setComentario(comentario);
            novaAvaliacao.setData_avaliacao(LocalDateTime.now()); 
            novaAvaliacao.setCurtidas(0); 
            
            boolean sucesso = avaliacaoService.processarAvaliacao(novaAvaliacao); 

            if (sucesso) {
                return "SUCESSO: Avaliação registrada.";
            } else {
                return "FALHA: Não foi possível salvar a avaliação no banco de dados.";
            }

        } catch (IllegalArgumentException e) {
            return "ERRO DE DADOS: " + e.getMessage();
        } catch (Exception e) {
            System.err.println("Erro no registro de avaliação: " + e.getMessage());
            return "ERRO INTERNO: Falha ao processar a requisição. Detalhe: " + e.getMessage();
        }
    }

    /**
     * @param avaliacao
     * @return 
     */
    public boolean processarAvaliacao(AvaliacaoModel avaliacao) {
        try {
            return avaliacaoService.processarAvaliacao(avaliacao);
        } catch (Exception e) {
            System.err.println("Erro ao processar avaliação: " + e.getMessage());
            return false;
        }
    }
}