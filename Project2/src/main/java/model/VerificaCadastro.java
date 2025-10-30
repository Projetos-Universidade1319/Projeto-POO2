/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author rsbuf
 */
public class VerificaCadastro {
    private VerificaPadrao verificaPadrao;
    
    public void verificaCadastro(VerificaPadrao verificaPadrao) {
       this.verificaPadrao = verificaPadrao;
    }
    public int casoVerifica(UsuarioModel usuario) {
       return verificaPadrao.casoVerifica(usuario);
    }
}
