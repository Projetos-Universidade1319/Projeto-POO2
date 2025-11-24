package model;

import java.time.LocalDateTime;

public class AvaliacaoModel {

    private int id_avaliacao;
    private int id_receita;
    private int id_usuario;
    private String nome;
    private String comentario;
    private int curtidas;
    private LocalDateTime data_avaliacao;

    public AvaliacaoModel() {
    }

    public AvaliacaoModel(int id_avaliacao, int id_receita, String nome, String comentario, int curtidas, LocalDateTime data_avaliacao) {
        this.id_avaliacao = id_avaliacao;
        this.id_receita = id_receita;
        this.nome = nome;
        this.comentario = comentario;
        this.curtidas = curtidas;
        this.data_avaliacao = data_avaliacao;
    }

    public int getId_avaliacao() {
        return id_avaliacao;
    }

    public void setId_avaliacao(int id_avaliacao) {
        this.id_avaliacao = id_avaliacao;
    }

    public int getId_receita() {
        return id_receita;
    }

    public void setId_receita(int id_receita) {
        this.id_receita = id_receita;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getCurtidas() {
        return curtidas;
    }

    public void setCurtidas(int curtidas) {
        this.curtidas = curtidas;
    }

    public LocalDateTime getData_avaliacao() {
        return data_avaliacao;
    }

    public void setData_avaliacao(LocalDateTime data_avaliacao) {
        this.data_avaliacao = data_avaliacao;
    }
}
