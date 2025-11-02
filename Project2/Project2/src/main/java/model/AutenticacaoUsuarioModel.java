
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class AutenticacaoUsuarioModel {
    static String url = "jdbc:postgresql://localhost:5432/Banco_Chef";
    static String user = "postgres";
    static String senha = "rodaminha0309";
    static String driver = "org.postgresql.Driver";
    Connection conn = null;
    Statement st = null;
    
    public void cadastrar(String senha,String nome,String email) throws SQLException{
        
    
         Random rand = new Random();
         int id = 10000 + rand.nextInt(90000);
         
         String sql = "INSERT INTO usuario (id_usuario,nome,senha,nivel_conta,pontuação) VALUES (" + id + ", '" + nome + "', '" + email + "',0,0)";
         
         try{
          Class.forName(driver);
          conn = DriverManager.getConnection(url,user,senha);
          st = conn.createStatement();
          st.executeUpdate(sql);
          st.close();
          conn.close();
          
         }catch(Exception e){
              conn.rollback();
         }
         
         } 
    
         public void login(){
        
         }   
    }
