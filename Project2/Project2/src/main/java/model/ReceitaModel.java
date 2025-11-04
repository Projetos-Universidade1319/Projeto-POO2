package model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class ReceitaModel {
    
    private int idReceita; 
    private String nome;
    private String descricao;
    private String modoPreparo;
    private String categoria;
    private int idUsuario;      
    private int curtidas;       
    private LocalDateTime dataCriacao;
    private List<ReceitaIngredienteModel> listaIngredientes = new ArrayList<>();
    private List<String> caracteristicas = new ArrayList<>();

    public ReceitaModel() {
        this.dataCriacao = LocalDateTime.now();
    }
    
    public ReceitaModel(String nome, String descricao, String modoPreparo, int idUsuario, String categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.modoPreparo = modoPreparo;
        this.idUsuario = idUsuario;
        this.categoria = categoria;
        this.curtidas = 0;
        this.dataCriacao = LocalDateTime.now();
    }
    
    public int getIdReceita() {
        return idReceita;
    }

    public void setIdReceita(int idReceita) {
        this.idReceita = idReceita;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getModoPreparo() {
        return modoPreparo;
    }

    public void setModoPreparo(String modoPreparo) {
        this.modoPreparo = modoPreparo;
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public int getCurtidas() {
        return curtidas;
    }

    public void setCurtidas(int curtidas) {
        this.curtidas = curtidas;
    }

    public List<ReceitaIngredienteModel> getListaIngredientes() {
        return listaIngredientes;
    }

    public void setListaIngredientes(List<ReceitaIngredienteModel> listaIngredientes) {
        this.listaIngredientes = listaIngredientes;
    }
    
    public void incrementarCurtidas() {
        this.curtidas++;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public List<String> getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(List<String> caracteristicas) {
        this.caracteristicas = caracteristicas;
    }
}