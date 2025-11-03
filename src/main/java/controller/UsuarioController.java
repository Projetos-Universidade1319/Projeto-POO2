
package controller;
import java.sql.SQLException;
import model.UsuarioModel;
import javax.swing.JOptionPane;
import model.AutenticacaoUsuarioModel;
import model.VerificaSenha;
import model.VerificaLogin;

public class UsuarioController {
    
  
    
     
     public UsuarioController(){
         //abrir nova conexao com BD
     }

     public void adicionar(UsuarioModel usuario) throws SQLException{
         
        VerificaSenha senha = new VerificaSenha();
        VerificaLogin nome = new VerificaLogin();
        AutenticacaoUsuarioModel autenticacao = new AutenticacaoUsuarioModel();
        
        if (!senha.casoVerifica(usuario)) {
            JOptionPane.showMessageDialog(null, "Senha inválida.");
        }
  
        if (!nome.casoVerifica(usuario)) {
            JOptionPane.showMessageDialog(null, "Nome de usuario inválido");
            return; 
        }

        
        
        autenticacao.cadastrar(usuario.getNome(),usuario.getSenha(),usuario.getEmail());
        
        JOptionPane.showMessageDialog(null, "Cadastro Concluido.");
     }

    
}
