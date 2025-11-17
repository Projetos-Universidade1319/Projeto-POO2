package controller;

import model.ReceitaIngredienteModel;

public class ReceitaIngredienteController {
    
    
    public ReceitaIngredienteController() {
    }

    /**
     * @param idReceita 
     * @param idIngrediente 
     * @param quantidade
     * @return 
     */
    public ReceitaIngredienteModel criarItemReceita(int idReceita, int idIngrediente, double quantidade) {

        if (quantidade <= 0) {
            System.err.println("ERRO: A quantidade deve ser um valor positivo para a receita.");
            return null; 
        }

        ReceitaIngredienteModel item = new ReceitaIngredienteModel();
        item.setId_receita(idReceita);
        item.setId_ingrediente(idIngrediente);
        item.setQuantidade(quantidade);

        return item;
    }
}