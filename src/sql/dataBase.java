package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author leoxi
 */
public class dataBase {
    Connection Conect = null;   
    public Connection conectar(){
        try{
//            Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.mysql.cj.jdbc.Driver");
            Conect = (Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1/mdsr","MORJANDEV","Morjan*1025544889");
//                JOptionPane.showMessageDialog(null, "Conectado a la Base de Datos !!!");
                System.out.println("Conectado a la Base de Datos !!!");
        }catch(Exception e){
            System.out.println(""+e);
            JOptionPane.showMessageDialog(null,e,"ERROR EN CONEXIÓN A DB",JOptionPane.ERROR_MESSAGE);
        }
        return Conect;
    }
}
