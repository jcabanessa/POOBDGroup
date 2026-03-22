package poobdgroup.DAO;

import poobdgroup.modelo.Cliente;
import poobdgroup.modelo.ClienteEstandar;
import poobdgroup.modelo.ClientePremium;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClienteDAO {

    // Método para GUARDAR
    public void guardarCliente(Cliente cliente) throws SQLException {
        String sql = "{CALL insertar_cliente(?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionDB.obtenerConexion();
             java.sql.CallableStatement cs = conn.prepareCall(sql)) {


            cs.setString(1, cliente.getNombre());
            cs.setString(2, cliente.getDomicilio());
            cs.setString(3, cliente.getNif());
            cs.setString(4, cliente.getEmail());
            cs.setString(5, cliente.tipoCliente()); // Aquí guardamos 'Estandar' o 'Premium'

            cs.executeUpdate();
        }
    }

    // Método para LEER
    public ArrayList<Cliente> obtenerClientes() throws SQLException {
        ArrayList<Cliente> listaClientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String domicilio = rs.getString("domicilio");
                String nif = rs.getString("nif");
                String email = rs.getString("email");
                String tipo = rs.getString("tipo");

                // El "truco": instanciamos la clase correcta según lo que diga la base de datos
                if (tipo.equalsIgnoreCase("Premium")) {
                    listaClientes.add(new ClientePremium(nombre, domicilio, nif, email));
                } else {
                    listaClientes.add(new ClienteEstandar(nombre, domicilio, nif, email));
                }
            }
        }
        return listaClientes;
    }
}
