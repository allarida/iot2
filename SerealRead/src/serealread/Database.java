/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serealread;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 *
 * @author PERSO
 */
public class Database {
    
    
    
    public static Connection connect() throws ClassNotFoundException, SQLException{
         String host = "projetquizzmysql.mysql.database.azure.com";
        String database = "dbzouhair";
        String user = "projetquizz@projetquizzmysql";
        String password = "Admin@dmin";

        // check that the driver is installed
        try
        {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            throw new ClassNotFoundException("MySQL JDBC driver NOT detected in library path.", e);
        }

        System.out.println("MySQL JDBC driver detected in library path.");

        Connection connection = null;

        // Initialize connection object
      
        {
            //String url = "jdbc:mysql://projetquizzmysql.mysql.database.azure.com:3306/dbzouhair?useLegacyDatetimeCode=false&serverTimezone=UTC";
String url =String.format("jdbc:mysql://%s/%s", host, database);

            // Set connection properties.
            Properties properties = new Properties();
            properties.setProperty("user", user);
            properties.setProperty("password", password);
            properties.setProperty("useSSL", "true");
            properties.setProperty("verifyServerCertificate", "true");
            properties.setProperty("requireSSL", "false");

            // get connection
            connection = DriverManager.getConnection(url, properties);
    }
    
 return connection;}
    
    public boolean adduser(String username,String rfid) throws ClassNotFoundException, SQLException{
        boolean ok = false;
             try{  
              
                PreparedStatement preparedStatement = connect().prepareStatement("INSERT INTO utilisateurs (username, rfid) VALUES (?, ?);");
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, rfid);
                preparedStatement.executeUpdate();
                ok=true;
            }
            catch (SQLException e)
            {
                ok=false;
                throw new SQLException("Encountered an error when executing given sql statement.", e);
            }    
        
    return ok;}
    
    public String getUsernameByRfid(String rfid) throws ClassNotFoundException, SQLException{
        String username="";
        
        try
            {
    
                Statement statement = connect().createStatement();
                ResultSet results = statement.executeQuery("SELECT * from utilisateurs where rfid='"+rfid+"';");
                while (results.next())
                {
                    username=results.getString(2);
                }
            }
            catch (SQLException e)
            {
                username="";
                throw new SQLException("Encountered an error when executing given sql statement", e);
            }       
        
    return username;}
    
    
    
    public boolean createUtilisateursTable() throws ClassNotFoundException, SQLException{
        boolean ok =false;
        
        try{
        
            Statement statement = connect().createStatement();
                statement.execute("DROP TABLE IF EXISTS utilisateurs;");
                statement.execute("CREATE TABLE utilisateurs (id serial PRIMARY KEY, username VARCHAR(50),rfid VARCHAR(50));");
                ok=true;
                
            }
            catch (SQLException e)
            {
                ok=false;
                throw new SQLException("Encountered an error when executing given sql statement.", e);
            } 
            
    return ok;}
    
        public boolean createDonneesTable() throws ClassNotFoundException, SQLException{
        boolean ok =false;
        
        try{
        
            Statement statement = connect().createStatement();
                statement.execute("DROP TABLE IF EXISTS donnees;");
                statement.execute("CREATE TABLE donnees (id serial PRIMARY KEY, date VARCHAR(90),lumin VARCHAR(50),temp VARCHAR(50),rfid  VARCHAR(50),idarduino VARCHAR(50) );");
                ok=true;
                
            }
            catch (SQLException e)
            {
                ok=false;
                throw new SQLException("Encountered an error when executing given sql statement.", e);
            } 
            
    return ok;}
        
        
            public boolean addDonnees(String time,String lumin,String temp,String rfid,String idarduino) throws ClassNotFoundException, SQLException{
        boolean ok = false;
             try{  
               
                PreparedStatement preparedStatement = connect().prepareStatement("INSERT INTO donnees (date, lumin,temp,rfid,idarduino) VALUES (?, ?,?,?,?);");
                preparedStatement.setString(1, time);
                preparedStatement.setString(2, lumin);
                preparedStatement.setString(3, temp);
                preparedStatement.setString(4, rfid);
                preparedStatement.setString(5, idarduino);
                preparedStatement.executeUpdate();
                ok=true;
            }
            catch (SQLException e)
            {
                ok=false;
                throw new SQLException("Encountered an error when executing given sql statement.", e);
            }    
        
    return ok;}
}