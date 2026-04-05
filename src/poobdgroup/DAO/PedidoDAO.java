package poobdgroup.DAO;

/*import poobdgroup.modelo.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PedidoDAO {

    // Metodo para guardar pedidos con antiinyección SQL y llamada a Stored Procedure
    public void guardarPedido(Pedido pedido) throws SQLException {
        String sql = "{CALL insertar_pedido(?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionDB.obtenerConexion();
             java.sql.CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, pedido.getNumPedido());
            cs.setInt(2, pedido.getCantidad());


            // Sin Timestamp: evita desfase de zona horaria
            cs.setObject(3, pedido.getFecha());

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


                // Leer LocalDateTime directamente
                LocalDateTime fecha = rs.getObject("fecha", LocalDateTime.class);

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


    public boolean existePedido(String numPedido) {
        try {
            String sql = "SELECT 1 FROM pedido WHERE numPedido = ?";
            try (Connection conn = ConexionDB.obtenerConexion();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, numPedido);
                return ps.executeQuery().next();
            }
        } catch (SQLException e) {
            return false;
        }
    }
}*/

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import poobdgroup.modelo.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class PedidoDAO {

    public void guardarPedido(Pedido pedido) throws SQLException {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Aseguramos que las relaciones queden asociadas a entidades gestionadas por JPA
            Articulo articuloRef = em.getReference(Articulo.class, pedido.getArticulo().getId());
            Cliente clienteRef = em.getReference(Cliente.class, pedido.getCliente().getId());

            pedido.setArticulo(articuloRef);
            pedido.setCliente(clienteRef);

            em.persist(pedido);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw new SQLException("Error al guardar pedido con JPA", e);
        } finally {
            em.close();
        }
    }

    public void eliminarPedido(String numPedido) throws SQLException {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Pedido pedido = em.createQuery(
                            "SELECT p FROM Pedido p WHERE p.numPedido = :numPedido",
                            Pedido.class)
                    .setParameter("numPedido", numPedido)
                    .setMaxResults(1)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (pedido != null) {
                em.remove(pedido);
            }

            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw new SQLException("Error al eliminar pedido con JPA", e);
        } finally {
            em.close();
        }
    }

    public ArrayList<Pedido> obtenerPedidos(Repositorio<Articulo> catalogoArticulos,
                                            Repositorio<Cliente> listaClientes) throws SQLException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return new ArrayList<>(
                    em.createQuery(
                                    "SELECT p FROM Pedido p JOIN FETCH p.articulo JOIN FETCH p.cliente ORDER BY p.id",
                                    Pedido.class)
                            .getResultList()
            );
        } finally {
            em.close();
        }
    }

    public boolean existePedido(String numPedido) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(p) FROM Pedido p WHERE p.numPedido = :numPedido",
                            Long.class)
                    .setParameter("numPedido", numPedido)
                    .getSingleResult();

            return count != null && count > 0;
        } finally {
            em.close();
        }
    }

    public void enviarPedido(String numPedido) throws SQLException {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            int updated = em.createQuery(
                            "UPDATE Pedido p SET p.enviado = true WHERE p.numPedido = :numPedido")
                    .setParameter("numPedido", numPedido)
                    .executeUpdate();

            tx.commit();

            if (updated == 0) {
                throw new SQLException("No se encontró el pedido " + numPedido);
            }
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw new SQLException("Error al marcar pedido como enviado con JPA", e);
        } finally {
            em.close();
        }
    }
}
