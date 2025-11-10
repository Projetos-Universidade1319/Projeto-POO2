package controller;

import model.IngredienteModel;
import model.UnidadeMedida; 
import service.IngredienteService; 
import java.util.List;

public class IngredienteController {
    
    
    private final IngredienteService ingredienteService; 

    public IngredienteController() {
        this.ingredienteService = new IngredienteService();
    }
    
    /**
     * @param nome
     * @param unidadeMedidaStr
     * @return 
     */
    public String registrarNovoIngrediente(String nome, String unidadeMedidaStr) {

        if (nome == null || nome.trim().isEmpty()) {
            return "ERRO: O nome do ingrediente não pode ser vazio.";
        }
        
        try {
            UnidadeMedida unidade = UnidadeMedida.valueOf(unidadeMedidaStr.toUpperCase());
            
            IngredienteModel novoIngrediente = new IngredienteModel();
            novoIngrediente.setNome(nome);
            novoIngrediente.setUnidade_medida(unidade); 
            boolean sucesso = ingredienteService.salvarIngrediente(novoIngrediente);

            if (sucesso) {
                return "SUCESSO: Ingrediente " + nome + " registrado.";
            } else {
                return "FALHA: Não foi possível salvar o ingrediente.";
            }

        } catch (IllegalArgumentException e) {
            return "ERRO DE DADOS: Unidade de medida inválida. Use um valor como KG ou UNIDADE.";
        } catch (Exception e) {
            System.err.println("Erro no registro de ingrediente: " + e.getMessage());
            return "ERRO INTERNO: Falha ao processar a requisição.";
        }
    }
    
    public List<IngredienteModel> buscarTodosIngredientes() throws Exception {
        return ingredienteService.buscarTodos();
    }
}
