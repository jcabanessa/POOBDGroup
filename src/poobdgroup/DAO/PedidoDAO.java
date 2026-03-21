package poobdgroup.DAO;

import poobdgroup.modelo.Articulo;
import poobdgroup.modelo.Cliente;
import poobdgroup.modelo.Pedido;
import poobdgroup.modelo.Repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PedidoDAO {

    // Método para GUARDAR
    public void guardarPedido(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedido (num_pedido, cantidad, fecha, articulo_codigo, cliente_email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, pedido.getNumPedido());
            pstmt.setInt(2, pedido.getCantidad());

            // Truco: Convertimos el LocalDateTime de Java al formato que entiende SQL
            pstmt.setTimestamp(3, Timestamp.valueOf(pedido.getFecha()));

            // Sacamos el ID directamente de los objetos que están dentro del pedido
            pstmt.setString(4, pedido.getArticulo().getCodigo());
            pstmt.setString(5, pedido.getCliente().getEmail());

            pstmt.executeUpdate();
        }
    }

    // Método para ELIMINAR
    public void eliminarPedido(String numPedido) throws SQLException {
        String sql = "DELETE FROM pedido WHERE num_pedido = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, numPedido);
            pstmt.executeUpdate();
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
                String numPedido = rs.getString("num_pedido");
                int cantidad = rs.getInt("cantidad");

                // Convertimos el Timestamp de SQL de vuelta a LocalDateTime de Java
                java.time.LocalDateTime fecha = rs.getTimestamp("fecha").toLocalDateTime();

                String codArticulo = rs.getString("articulo_codigo");
                String emailCliente = rs.getString("cliente_email");

                poobdgroup.modelo.Pedido pedido = new poobdgroup.modelo.Pedido(numPedido, cantidad, fecha);

                // Buscamos el artículo y el cliente correspondientes usando Streams
                poobdgroup.modelo.Articulo art = catalogoArticulos.stream()
                        .filter(a -> a.getCodigo().equals(codArticulo))
                        .findFirst().orElse(null);

                poobdgroup.modelo.Cliente cli = listaClientes.stream()
                        .filter(c -> c.getEmail().equals(emailCliente))
                        .findFirst().orElse(null);

                pedido.setArticulo(art);
                pedido.setCliente(cli);

                listaPedidos.add(pedido);
            }
        }
        return listaPedidos;
    }
}
