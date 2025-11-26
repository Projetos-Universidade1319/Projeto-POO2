package model;

public class ReceitaIngredienteModel {
    private int id_receita;
    private int id_ingrediente;
    private int quantidade;
    private IngredienteModel ingrediente; // Referência ao objeto IngredienteModel

    public ReceitaIngredienteModel() {
    }

    public ReceitaIngredienteModel(int id_receita, int id_ingrediente, int quantidade) {
        this.id_receita = id_receita;
        this.id_ingrediente = id_ingrediente;
        this.quantidade = quantidade;
    }

    public int getId_receita() {
        return id_receita;
    }

    public void setId_receita(int id_receita) {
        this.id_receita = id_receita;
    }

    public int getId_ingrediente() {
        return id_ingrediente;
    }

    public void setId_ingrediente(int id_ingrediente) {
        this.id_ingrediente = id_ingrediente;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    
    public IngredienteModel getIngrediente() {
        return ingrediente;
    }
    
    public void setIngrediente(IngredienteModel ingrediente) {
        this.ingrediente = ingrediente;
        if (ingrediente != null) {
            this.id_ingrediente = ingrediente.getId_ingrediente();
        }
    }
}
