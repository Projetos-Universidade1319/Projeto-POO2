/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.Statement;

/**
 *
 * @author rsbuf
 */
public class BD {
    static String url = "jdbc:postgresql://localhost:5432/Banco_Chef";
    static String user = "postgres";
    static String senha = "rodaminha0309";
    static String driver = "org.postgresql.Driver";
    Connection con = null;
    Statement st = null;
}
