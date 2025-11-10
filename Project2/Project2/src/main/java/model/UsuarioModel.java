
package model;

public class UsuarioModel {
    
    private int idUsuario; 
    private String nome;
    private String email;
    private String senha;
    private NivelConta nivelConta; 
    private float pontuacao;
    
    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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
    public NivelConta getNivelConta() {
        return nivelConta;
    }
    public void setNivelConta(NivelConta nivelConta) {
        this.nivelConta = nivelConta;
    }
    public float getPontuacao() {
        return pontuacao;
    }
    public void setPontuacao(float pontuacao) {
        this.pontuacao = pontuacao;
    }
    

}
