import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion{
    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/gimnasio";
    String username = "root";
    String password = "";
    Connection connection = null;
    public Conexion(){
        try{
            Class.forName(driver);

            connection = DriverManager.getConnection(url, username, password);

        }catch (Exception e){
        System.out.println(e.getMessage());}
    }
    public Connection getConnection(){
        return connection;
    }
    public void desconectar(){
        try {
            connection.close();
        }catch (Exception e){
            System.out.println(e);
        }
        }
}