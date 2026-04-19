package poobdgroup.DAO;

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
