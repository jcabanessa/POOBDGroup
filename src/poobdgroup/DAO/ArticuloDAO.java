package poobdgroup.DAO;

/*import poobdgroup.modelo.Articulo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ArticuloDAO {

    //Metodo para guardar Articulo en BD con antiinyección SQL
    public void guardarArticulo(Articulo articulo) throws SQLException {
        // La consulta SQL con signos de interrogación para evitar inyecciones SQL
        String sql = "{CALL insertar_articulo(?, ?, ?, ?, ?)}";

        // El bloque try-with-resources cierra la conexión automáticamente al terminar
        try (Connection conn = ConexionDB.obtenerConexion();
             java.sql.CallableStatement cs = conn.prepareCall(sql)) {

            // Sustituimos los '?' por los datos reales del objeto Articulo
            cs.setString(1, articulo.getCodigo());
            cs.setString(2, articulo.getDescripcion());
            cs.setDouble(3, articulo.getPrecioVenta());
            cs.setDouble(4, articulo.getGastosEnvio());
            cs.setInt(5, articulo.getTiempoPreparacion());

            // Ejecutamos la orden en la base de datos
            cs.executeUpdate();
            System.out.println("Artículo guardado correctamente en la base de datos.");
        }
        Articulo guardado = buscarPorCodigo(articulo.getCodigo());
        if (guardado != null) {
            articulo.setId(guardado.getId());
        }
    }



    //Metodo para obtener los articulos de la BD con prepareStatements
    public ArrayList<Articulo> obtenerArticulos() throws SQLException {
        ArrayList<Articulo> listaArticulos = new ArrayList<>();
        String sql = "SELECT * FROM articulo";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Mientras haya filas en la base de datos, las vamos leyendo
            while (rs.next()) {
                Articulo articulo = new Articulo(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precioVenta"),
                        rs.getDouble("gastosEnvio"),
                        rs.getInt("tiempoPreparacion")
                );
                articulo.setId(rs.getInt("id"));
                listaArticulos.add(articulo);
            }
        }
        return listaArticulos;
    }

    //Metodo que busca por codigo en BD con prepareStatements
    public Articulo buscarPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT * FROM articulo WHERE codigo = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Articulo a = new Articulo(
                        codigo,
                        rs.getString("descripcion"),
                        rs.getDouble("precioVenta"),
                        rs.getDouble("gastosEnvio"),
                        rs.getInt("tiempoPreparacion")
                );
                a.setId(rs.getInt("id"));
                return a;
            }
        }
        return null;
    }
}*/

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
