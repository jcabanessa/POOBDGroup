package poobdgroup.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import poobdgroup.modelo.Cliente;

import java.sql.SQLException;
import java.util.ArrayList;

public class ClienteDAO {

    public void guardarCliente(Cliente cliente) throws SQLException {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(cliente);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw new SQLException("Error al guardar cliente con JPA", e);
        } finally {
            em.close();
        }
    }

    public ArrayList<Cliente> obtenerClientes() throws SQLException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return new ArrayList<>(
                    em.createQuery("SELECT c FROM Cliente c ORDER BY c.id", Cliente.class)
                            .getResultList()
            );
        } finally {
            em.close();
        }
    }

    public Cliente buscarPorEmail(String email) throws SQLException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Cliente c WHERE c.email = :email",
                            Cliente.class)
                    .setParameter("email", email)
                    .setMaxResults(1)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }
}
