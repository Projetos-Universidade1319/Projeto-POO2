package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReceitaModel {
    private int id_receita;
    private String nome;
    private String descricao;
    private String modo_preparo;
    private String categoria;
    private int id_usuario;
    private int curtidas;
    private LocalDateTime data_criacao;
    private List<ReceitaIngredienteModel> listaIngredientes;
    private List<String> caracteristicas;

    public ReceitaModel() {
        this.data_criacao = LocalDateTime.now();
        this.listaIngredientes = new ArrayList<>();
        this.caracteristicas = new ArrayList<>();
    }

    public ReceitaModel(int id_receita, String nome, String descricao, String modo_preparo, int id_usuario, String categoria, List<ReceitaIngredienteModel> listaIngredientes, int curtidas) {
        this.id_receita = id_receita;
        this.nome = nome;
        this.descricao = descricao;
        this.modo_preparo = modo_preparo;
        this.id_usuario = id_usuario;
        this.categoria = categoria;
        this.listaIngredientes = listaIngredientes != null ? listaIngredientes : new ArrayList<>();
        this.curtidas = curtidas;
        this.data_criacao = LocalDateTime.now();
        this.caracteristicas = new ArrayList<>();
    }

    public int getId_Receita() {
        return id_receita;
    }

    public void setId_Receita(int id_receita) {
        this.id_receita = id_receita;
    }

    public int getId_receita() {
        return id_receita;
    }

    public void setId_receita(int id_receita) {
        this.id_receita = id_receita;
    }

    public int getIdReceita() {
        return id_receita;
    }

    public void setIdReceita(int idReceita) {
        this.id_receita = idReceita;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getModo_Preparo() {
        return modo_preparo;
    }

    public void setModo_Preparo(String modo_preparo) {
        this.modo_preparo = modo_preparo;
    }

    public String getModoPreparo() {
        return modo_preparo;
    }

    public void setModoPreparo(String modoPreparo) {
        this.modo_preparo = modoPreparo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getIdUsuario() {
        return id_usuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.id_usuario = idUsuario;
    }

    public int getCurtidas() {
        return curtidas;
    }

    public void setCurtidas(int curtidas) {
        this.curtidas = curtidas;
    }

    public void incrementarCurtidas() {
        this.curtidas++;
    }

    public LocalDateTime getDataCriacao() {
        return data_criacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.data_criacao = dataCriacao;
    }

    public LocalDateTime getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(LocalDateTime data_criacao) {
        this.data_criacao = data_criacao;
    }

    public List<ReceitaIngredienteModel> getListaIngredientes() {
        if (listaIngredientes == null) {
            listaIngredientes = new ArrayList<>();
        }
        return listaIngredientes;
    }

    public void setListaIngredientes(List<ReceitaIngredienteModel> listaIngredientes) {
        this.listaIngredientes = listaIngredientes;
    }

    public List<String> getCaracteristicas() {
        if (caracteristicas == null) {
            caracteristicas = new ArrayList<>();
        }
        return caracteristicas;
    }

    public void setCaracteristicas(List<String> caracteristicas) {
        this.caracteristicas = caracteristicas;
    }
}
