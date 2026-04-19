package poobdgroup.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import poobdgroup.modelo.Articulo;

import java.sql.SQLException;
import java.util.ArrayList;

public class ArticuloDAO {

    public void guardarArticulo(Articulo articulo) throws SQLException {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(articulo);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw new SQLException("Error al guardar artículo con JPA", e);
        } finally {
            em.close();
        }
    }

    public ArrayList<Articulo> obtenerArticulos() throws SQLException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return new ArrayList<>(
                    em.createQuery("SELECT a FROM Articulo a ORDER BY a.id", Articulo.class)
                            .getResultList()
            );
        } finally {
            em.close();
        }
    }

    public Articulo buscarPorCodigo(String codigo) throws SQLException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Articulo a WHERE a.codigo = :codigo",
                            Articulo.class)
                    .setParameter("codigo", codigo)
                    .setMaxResults(1)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }
}
