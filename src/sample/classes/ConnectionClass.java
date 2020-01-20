/* creates connection to database */

package sample.classes;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {
    public static Connection connection;
    public Connection getConnection(){
        String url = "DATABASE_NAME";
        String userName = "USERNAME";
        String pw = "PASSWORD";
        System.out.println("loading driver...");
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find driver", e);
        }
        System.out.println("Driver loaded");
        connection = null;
        System.out.println("Trying to load Database...");
        try{
            connection = DriverManager.getConnection(url, userName, pw);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Connected to the database");
        return connection;
    }
    public static void closeConnection() throws SQLException{
        try{
            if (connection != null && !connection.isClosed()){
                connection.close();
            }
        }catch(Exception e){
            throw e;
        }
    }
}

