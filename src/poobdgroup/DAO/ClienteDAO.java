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
        String sql = "INSERT INTO cliente (email, nombre, domicilio, nif, tipo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cliente.getEmail());
            pstmt.setString(2, cliente.getNombre());
            pstmt.setString(3, cliente.getDomicilio());
            pstmt.setString(4, cliente.getNif());
            pstmt.setString(5, cliente.tipoCliente()); // Aquí guardamos 'Estandar' o 'Premium'

            pstmt.executeUpdate();
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
                String email = rs.getString("email");
                String nombre = rs.getString("nombre");
                String domicilio = rs.getString("domicilio");
                String nif = rs.getString("nif");
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
