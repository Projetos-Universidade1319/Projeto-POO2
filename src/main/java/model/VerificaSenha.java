

package model;

public class VerificaSenha implements VerificaPadrao {
    @Override
    public boolean casoVerifica(UsuarioModel caso) {
        return !(caso.getSenha() == null || caso.getSenha().length() >= 30);
  
    } 
}