/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Random;

public class AutenticacaoUsuarioModel {
    static String url = "jdbc:postgresql://localhost:5432/Banco_Chef";
    static String user = "postgres";
    static String senha = "rodaminha0309";
    static String driver = "org.postgresql.Driver";
    Connection con = null;
    Statement st = null;
    
    public void cadastrar(String senha,String nome,String email){
         Random rand = new Random();
         int id = 10000 + rand.nextInt(90000);
         String sqll = "INSERT INTO usuario (id_usuario,nome,senha,nivel_conta,pontuação) VALUES (" + id + ", '" + nome + "', '" + email + "',0,0)";


        try {
            System.out.println("Conectando ao banco de dados...");
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, this.senha);
            System.out.println("Conexão aberta com sucesso!");

            st = con.createStatement();
            st.executeUpdate(sqll);
            System.out.println("Usuario Cadastrado!");

        } catch (Exception e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        } finally {
            try {
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (Exception e) {
                System.out.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
    public void login(){
        
    }   
}
