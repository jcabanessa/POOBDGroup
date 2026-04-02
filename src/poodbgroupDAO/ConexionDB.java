package poodbgroupDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    public static Connection obtenerConexion() throws SQLException {

        return DriverManager.getConnection(Config.URL, Config.USER, Config.PASSWORD);

    }

    public  static void cerrarConexion(Connection con) throws SQLException {

        if ( con != null){

            con.close();

        }

    }

}
