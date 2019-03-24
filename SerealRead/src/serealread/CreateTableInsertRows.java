/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serealread;

/**
 *
 * @author PERSO
 */

//import com.mysql.jdbc.*;
import java.sql.*;
import java.util.Properties;
import org.jfree.data.time.Millisecond;

public class CreateTableInsertRows {

    public static void main (String[] args)  throws Exception
    {
        // Initialize connection variables. 
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
        try
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
        catch (SQLException e)
        {
            throw new SQLException("Failed to create connection to database.", e);
        }
        if (connection != null) 
        { 
            System.out.println("Successfully created connection to database.");
        
            // Perform some SQL queries over the connection.
            try
            {
                //Drop previous table of same name if one exists.
                Statement statement = connection.createStatement();
                statement.execute("DROP TABLE IF EXISTS donnees;");
                System.out.println("Finished dropping table (if existed).");
    
                // Create table.
                statement.execute("CREATE TABLE donnees (id serial PRIMARY KEY, date VARCHAR(90),lumin VARCHAR(50),temp VARCHAR(50),rfid  VARCHAR(50),idarduino VARCHAR(50) );");
                System.out.println("Created table.");
                
                // Insert some data into table.
                int nRowsInserted = 0;
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO donnees (date, lumin,temp,rfid,idarduino) VALUES (?, ?,?,?,?);");
                final Millisecond now = new Millisecond();
                String time =""+now;
                preparedStatement.setString(1, time);
                preparedStatement.setString(2, "700");
                preparedStatement.setString(3, "29");
                preparedStatement.setString(4, "0300AFBD8690");
                preparedStatement.setString(5, "1");
                nRowsInserted += preparedStatement.executeUpdate();

                //preparedStatement.setString(1, "rida");
                //preparedStatement.setString(2, "0300AEC8FA9F");
                //nRowsInserted += preparedStatement.executeUpdate();

                System.out.println(String.format("Inserted %d row(s) of data.", nRowsInserted));
    
                // NOTE No need to commit all changes to database, as auto-commit is enabled by default.
    
            }
            catch (SQLException e)
            {
                throw new SQLException("Encountered an error when executing given sql statement.", e);
            }       
        }
        else {
            System.out.println("Failed to create connection to database.");
        }
        System.out.println("Execution finished.");
    }
}
