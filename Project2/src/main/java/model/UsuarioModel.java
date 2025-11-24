package model;

public class UsuarioModel {
    private long id_usuario;
    private String nome;
    private String email;
    private String senha;
    private NivelUsuario nivel = NivelUsuario.INICIANTE;
    private float pontuacao;

    public UsuarioModel() {
    }

    public UsuarioModel(long id_usuario, String nome, String email, String senha, NivelUsuario nivel, float pontuacao) {
        this.id_usuario = id_usuario;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.nivel = nivel;
        this.pontuacao = pontuacao;
    }

    public long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getIdUsuario() {
        return (int) id_usuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.id_usuario = idUsuario;
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

    public NivelUsuario getNivel() {
        return nivel;
    }

    public void setNivel(NivelUsuario nivel) {
        this.nivel = nivel;
    }

    public float getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(float pontuacao) {
        this.pontuacao = pontuacao;
    }

    public void adicionarPontos(int pontos) {
        if (pontos > 0) {
            this.pontuacao += pontos;
            verificarPromocao();
        }
    }

    private void verificarPromocao() {
        if (this.pontuacao >= 1500) {
            this.nivel = NivelUsuario.CHEF_PRO;
        } else if (this.pontuacao >= 500) {
            this.nivel = NivelUsuario.CHEF_AMADOR;
        } else {
            this.nivel = NivelUsuario.INICIANTE;
        }
    }
}

