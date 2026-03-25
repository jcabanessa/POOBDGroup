package poobdgroup.DAO;

import poobdgroup.modelo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class PedidoDAO {

    // Metodo para guardar pedidos con antiinyección SQL y llamada a Stored Procedure
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

    // Metodo para eliminar pedidos con antiinyección SQL y llamada a Stored Procedure
    public void eliminarPedido(String numPedido) throws SQLException {
        String sql = "{CALL eliminar_pedido(?)}";

        try (Connection conn = ConexionDB.obtenerConexion();
             java.sql.CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, numPedido);
            cs.executeUpdate();
        }
    }

    //Metodos para obtener pedidos de la BD con prepareStatements
    public ArrayList<Pedido> obtenerPedidos(
            Repositorio<Articulo> catalogoArticulos,
            Repositorio<Cliente> listaClientes) throws SQLException {

        ArrayList<Pedido> listaPedidos = new ArrayList<>();

        String sql = """
        SELECT p.*, a.codigo, a.descripcion, a.precioVenta, a.gastosEnvio, a.tiempoPreparacion,
               c.nombre, c.domicilio, c.nif, c.email, c.tipo
        FROM pedido p
        JOIN articulo a ON p.id_articulo = a.id
        JOIN cliente c ON p.id_cliente = c.id
    """;

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String numPedido = rs.getString("numPedido");
                int cantidad = rs.getInt("cantidad");

                // Convertimos el Timestamp de SQL de vuelta a LocalDateTime de Java
                java.time.LocalDateTime fecha = rs.getTimestamp("fecha").toLocalDateTime();

                Pedido pedido = new Pedido(numPedido, cantidad, fecha);


                Articulo art = new Articulo(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precioVenta"),
                        rs.getDouble("gastosEnvio"),
                        rs.getInt("tiempoPreparacion")
                );
                art.setId(rs.getInt("id_articulo"));

                Cliente cli;
                if ("Premium".equalsIgnoreCase(rs.getString("tipo"))) {
                    cli = new ClientePremium(
                            rs.getString("nombre"),
                            rs.getString("domicilio"),
                            rs.getString("nif"),
                            rs.getString("email")
                    );
                } else {
                    cli = new ClienteEstandar(
                            rs.getString("nombre"),
                            rs.getString("domicilio"),
                            rs.getString("nif"),
                            rs.getString("email")
                    );
                }
                cli.setId(rs.getInt("id_cliente"));


                pedido.setArticulo(art);
                pedido.setCliente(cli);
                pedido.setEnviado(rs.getBoolean("enviado"));

                listaPedidos.add(pedido);
            }
        }
        return listaPedidos;
    }



    //Metodo para cambiar el estado del pedido a True y que quede guardado en BD
    public void enviarPedido(String numPedido) throws SQLException {
        String sql = "{CALL enviar_pedido(?)}";

        try (Connection conn = ConexionDB.obtenerConexion();
             java.sql.CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, numPedido);
            cs.executeUpdate();
        }
    }
}
