package controller;

import model.ReceitaIngredienteModel;
import service.ReceitaService;
import java.util.List;

public class ReceitaIngredienteController {
    
    private final ReceitaService receitaService; 

    public ReceitaIngredienteController() {
        this.receitaService = new ReceitaService();
    }

    
    /**
     * @param idReceita 
     * @param idIngrediente 
     * @param quantidade 
     * @return 
     */
    public ReceitaIngredienteModel criarItemReceita(int idReceita, int idIngrediente, double quantidade) {
        
        if (quantidade <= 0) {
            System.err.println("ERRO: A quantidade deve ser um valor positivo.");
            return null; 
        }

        ReceitaIngredienteModel item = new ReceitaIngredienteModel();
        item.setId_receita(idReceita);
        item.setId_ingrediente(idIngrediente);
        item.setQuantidade(quantidade);

        return item;
    }
    
}
