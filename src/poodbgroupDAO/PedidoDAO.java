package poodbgroupDAO;

import poodbgroupModelo.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PedidoDAO {

    public void guardarPedido(Pedido pedido) throws SQLException{

        String sql = "{call insertar_pedido(?,?,?,?)}";

        try(Connection conn = ConexionDB.obtenerConexion();
            CallableStatement cs = conn.prepareCall(sql)){

            cs.setInt(1, pedido.getCantidad());
            cs.setTimestamp(2, Timestamp.valueOf(pedido.getFecha()));
            cs.setInt(3, pedido.getArticulo().getId());
            cs.setInt(4, pedido.getCliente().getId());

            cs.executeUpdate();
        }
    }

    public void eliminarPedido(int numPedido) throws SQLException{

        String sql = "{call eliminar_pedido(?)}";

        try (Connection conn = ConexionDB.obtenerConexion();
            CallableStatement cs = conn.prepareCall(sql)){

            cs.setInt(1, numPedido);
            cs.executeUpdate();

        }

    }

    public ArrayList<Pedido> obtenerPedidos() throws SQLException {
        ArrayList<Pedido> listaPedidos = new ArrayList<>();

        String sql = """
        SELECT p.*, a.codigo, a.descripcion, a.precioventa, a.gastosenvio, a.tiempopreparacion,
               c.nombre, c.domicilio, c.nif, c.email, c.tipo
        FROM pedido p
        JOIN articulo a ON p.id_articulo = a.id
        JOIN cliente c ON p.id_cliente = c.id
    """;

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int numPedido = rs.getInt("numpedido");
                int cantidad = rs.getInt("cantidad");
                LocalDateTime fecha = rs.getTimestamp("fecha").toLocalDateTime();

                Pedido pedido = new Pedido(numPedido, cantidad, fecha);

                Articulo art = new Articulo(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precioventa"),
                        rs.getDouble("gastosenvio"),
                        rs.getInt("tiempopreparacion")
                );
                art.setId(rs.getInt("id_articulo"));

                Cliente cli;
                if ("premium".equalsIgnoreCase(rs.getString("tipo"))) {
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

                listaPedidos.add(pedido);
            }
        }
        return listaPedidos;
    }
}
