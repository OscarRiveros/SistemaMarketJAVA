
package rsystem;
import java.sql.*;
import com.mysql.jdbc.Connection;
import java.sql.DriverManager;

public class conexionBD {
    private static Connection com;
    public static Connection ConectarBD(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            com= (Connection) DriverManager.getConnection("jdbc:mysql://localhost/rsystem","root","Root12345"); 
            System.out.println("Conexion exitosa");
        }
            catch (SQLException e){
            System.out.println("error mysql "+e.getMessage());
        }
            catch (Exception e){
            System.out.println("no se "+e.getMessage());    
            }
        return com;
    }

}
