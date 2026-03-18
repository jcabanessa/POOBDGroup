package poobdgroup.dao;

import poobdgroup.modelo.Articulo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ArticuloDAO {

    public void guardarArticulo(Articulo articulo) throws SQLException {
        // La consulta SQL con signos de interrogación para evitar inyecciones SQL
        String sql = "INSERT INTO articulo (codigo, descripcion, precio_venta, gastos_envio, tiempo_preparacion) VALUES (?, ?, ?, ?, ?)";

        // El bloque try-with-resources cierra la conexión automáticamente al terminar
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Sustituimos los '?' por los datos reales del objeto Articulo
            pstmt.setString(1, articulo.getCodigo());
            pstmt.setString(2, articulo.getDescripcion());
            pstmt.setDouble(3, articulo.getPrecioVenta());
            pstmt.setDouble(4, articulo.getGastosEnvio());
            pstmt.setInt(5, articulo.getTiempoPreparacion());

            // Ejecutamos la orden en la base de datos
            pstmt.executeUpdate();
            System.out.println("Artículo guardado correctamente en la base de datos.");
        }
    }

    // Importaciones extra necesarias arriba:
    // import java.sql.ResultSet;
    // import java.util.ArrayList;

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
                        rs.getDouble("precio_venta"),
                        rs.getDouble("gastos_envio"),
                        rs.getInt("tiempo_preparacion")
                );
                listaArticulos.add(articulo);
            }
        }
        return listaArticulos;
    }
}