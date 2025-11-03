package model;
import java.util.List;
import java.util.ArrayList;

public class ReceitaModel {
   private int id_receita;
   private String nome;
   private String descricao;
   private char modo_preparo;
   private int id_usuario;
   private String categoria;
   private List<String> caracteristicas = new ArrayList<>();
   private List<ReceitaIngredienteModel> listaIngredientes;
   private int curtidas;

    public int getId_Receita(){
        return id_receita;
    }

    public void setId_Receita(int id_receita){
        this.id_receita = id_receita;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getDescricao(){
        return descricao;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    public char getModo_Preparo(){
        return modo_preparo;
    }

    public void setModo_Preparo(char modo_preparo){
        this.modo_preparo = modo_preparo;
    }

    public int getId_Usuario(){
        return id_usuario;
    }

    public void setId_Usuario(int id_usuario){
        this.id_usuario = id_usuario;
    }

    public String getCategoria(){
        return categoria;
    }

    public void setCategoria(String categoria){
        this.categoria = categoria;
    }

    public List<String> getCaracteristicas(){
        return caracteristicas;
    }

    public void setCaracteristicas(List<String> caracteristicas){
        this.caracteristicas = caracteristicas;
    }

    public int getCurtidas(){
        return curtidas;
    }

    public void setCurtidas(int curtidas){
        this.curtidas = curtidas;
    }
    public int getId_receita() {
        return id_receita;
    }

    public void setId_receita(int id_receita) {
        this.id_receita = id_receita;
    }

    public char getModo_preparo() {
        return modo_preparo;
    }

    public void setModo_preparo(char modo_preparo) {
        this.modo_preparo = modo_preparo;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public List<ReceitaIngredienteModel> getListaIngredientes() {
        return listaIngredientes;
    }

    public void setListaIngredientes(List<ReceitaIngredienteModel> listaIngredientes) {
        this.listaIngredientes = listaIngredientes;
    }

}