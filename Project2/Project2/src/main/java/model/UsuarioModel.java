
package model;

public class UsuarioModel {
    
    private int id_usuario;
    private String nome;
    private String email;
    private String senha;
    private String nivelconta;
    private float pontuacao;

    public int getIdUsuario() {
        return id_usuario;
    }

    public void setIdUsuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
    
    public String getNivelConta() {
        return nivelconta;
    }

    public void setNivelConta(String nivel_conta) {
        this.nivelconta = nivel_conta;
    }

    public float getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(float pontuacao) {
        this.pontuacao = pontuacao;
    }
    

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
}
