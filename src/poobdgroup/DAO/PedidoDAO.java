package poobdgroup.DAO;

import poobdgroup.modelo.Articulo;
import poobdgroup.modelo.Cliente;
import poobdgroup.modelo.Pedido;
import poobdgroup.modelo.Repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;

public class PedidoDAO {

    // Método para GUARDAR
    public void guardarPedido(Pedido pedido) throws SQLException {
        String sql = "{CALL insertar_pedido(?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionDB.obtenerConexion();
             java.sql.CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, pedido.getNumPedido());
            cs.setInt(2, pedido.getCantidad());

            // Truco: Convertimos el LocalDateTime de Java al formato que entiende SQL
            cs.setTimestamp(3, Timestamp.valueOf(pedido.getFecha()));

            // Sacamos el ID directamente de los objetos que están dentro del pedido
            cs.setInt(4, pedido.getArticulo().getId());
            cs.setInt(5, pedido.getCliente().getId());

            cs.executeUpdate();
        }
    }

    // Método para ELIMINAR
    public void eliminarPedido(String numPedido) throws SQLException {
        String sql = "{CALL eliminar_pedido(?)}";

        try (Connection conn = ConexionDB.obtenerConexion();
             java.sql.CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, numPedido);
            cs.executeUpdate();
        }
    }

    public java.util.ArrayList<poobdgroup.modelo.Pedido> obtenerPedidos(
            Repositorio<Articulo> catalogoArticulos,
            Repositorio<Cliente> listaClientes) throws SQLException {

        java.util.ArrayList<poobdgroup.modelo.Pedido> listaPedidos = new java.util.ArrayList<>();
        String sql = "SELECT * FROM pedido";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String numPedido = rs.getString("numPedido");
                int cantidad = rs.getInt("cantidad");

                // Convertimos el Timestamp de SQL de vuelta a LocalDateTime de Java
                java.time.LocalDateTime fecha = rs.getTimestamp("fecha").toLocalDateTime();

                int codArticulo = rs.getInt("id_articulo");
                int emailCliente = rs.getInt("id_cliente");

                poobdgroup.modelo.Pedido pedido = new poobdgroup.modelo.Pedido(numPedido, cantidad, fecha);

                // Buscamos el artículo y el cliente correspondientes usando Streams
                poobdgroup.modelo.Articulo art = catalogoArticulos.getAll().stream()
                        .filter(a -> a.getId() == codArticulo)
                        .findFirst().orElse(null);

                poobdgroup.modelo.Cliente cli = listaClientes.getAll().stream()
                        .filter(c -> c.getId() == emailCliente)
                        .findFirst().orElse(null);

                pedido.setArticulo(art);
                pedido.setCliente(cli);

                listaPedidos.add(pedido);
            }
        }
        return listaPedidos;
    }
}
