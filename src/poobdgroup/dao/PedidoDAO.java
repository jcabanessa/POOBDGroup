package poobdgroup.dao;

import poobdgroup.modelo.Pedido;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PedidoDAO {

    // Guarda un pedido usando transacción
    public void guardarPedido(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedido (num_pedido, cantidad, fecha, articulo_codigo, cliente_email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.obtenerConexion()) {
            conn.setAutoCommit(false); // inicio transacción
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, pedido.getNumPedido());
                pstmt.setInt(2, pedido.getCantidad());
                pstmt.setTimestamp(3, Timestamp.valueOf(pedido.getFecha()));
                pstmt.setString(4, pedido.getArticulo().getCodigo());
                pstmt.setString(5, pedido.getCliente().getEmail());
                pstmt.executeUpdate();
                conn.commit(); // confirmamos
            } catch (SQLException e) {
                conn.rollback(); // si falla, deshacemos
                throw e;
            }
        }
    }

    // Elimina un pedido por número
    public void eliminarPedido(String numPedido) throws SQLException {
        String sql = "DELETE FROM pedido WHERE num_pedido = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, numPedido);
            pstmt.executeUpdate();
        }
    }

    // Obtiene todos los pedidos enlazando artículo y cliente
    public ArrayList<Pedido> obtenerPedidos(
            ArrayList<poobdgroup.modelo.Articulo> catalogoArticulos,
            ArrayList<poobdgroup.modelo.Cliente> listaClientes) throws SQLException {

        ArrayList<Pedido> listaPedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String numPedido = rs.getString("num_pedido");
                int cantidad = rs.getInt("cantidad");
                LocalDateTime fecha = rs.getTimestamp("fecha").toLocalDateTime();
                String codArticulo = rs.getString("articulo_codigo");
                String emailCliente = rs.getString("cliente_email");

                Pedido pedido = new Pedido(numPedido, cantidad, fecha);

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

    // Llama al procedimiento almacenado para calcular el total de un pedido
    public double calcularTotalPedido(String numPedido) throws SQLException {
        String sql = "{CALL calcularTotalPedido(?, ?)}";

        try (Connection conn = ConexionDB.obtenerConexion();
                CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, numPedido);
            cs.registerOutParameter(2, Types.DECIMAL);
            cs.execute();
            return cs.getDouble(2);
        }
    }
}