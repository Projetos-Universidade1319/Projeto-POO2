
package controller;
import model.UsuarioModel;

public class UsuarioController {
    
     int id_usuario;
     String nome;
     String email;
     String senha;
     String nivel_conta;
     int pontuacao;
     
     public UsuarioController(){
         //abrir nova conexao com BD
     }

     public void adiciona(UsuarioModel usuario){
        String sql = "INSERT INTO usuario (nome, email, senha, nivel_conta, pontuacao) VALUES (?, ?, ?, ?, ?)";
        System.out.println("Usuário adicionado com sucesso!");
     }

     
    //declaração das var para conexão com o banco de dados
    
}
