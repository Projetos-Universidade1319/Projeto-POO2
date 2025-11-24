package model;

public class IngredienteModel {
    private int id_ingrediente;
    private String nome;
    private String unidade_medida;

    public IngredienteModel() {
    }

    public IngredienteModel(int id_ingrediente, String nome, String unidade_medida) {
        this.id_ingrediente = id_ingrediente;
        this.nome = nome;
        this.unidade_medida = unidade_medida;
    }

    public int getId_ingrediente() {
        return id_ingrediente;
    }

    public void setId_ingrediente(int id_ingrediente) {
        this.id_ingrediente = id_ingrediente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUnidade_medida() {
        return unidade_medida;
    }

    public void setUnidade_medida(String unidade_medida) {
        this.unidade_medida = unidade_medida;
    }
}