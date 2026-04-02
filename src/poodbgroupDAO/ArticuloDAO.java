package poodbgroupDAO;

import poodbgroupModelo.Articulo;

import java.sql.*;
import java.util.ArrayList;

public class ArticuloDAO {

    public void guardarArticulo(Articulo articulo) throws SQLException{

        String sql = "{call insertar_articulo(?,?,?,?,?)}";

        try(Connection conn = ConexionDB.obtenerConexion();
            CallableStatement cs = conn.prepareCall(sql)){

            cs.setString(1,articulo.getCodigo());
            cs.setString(2,articulo.getDescripcion());
            cs.setDouble(3,articulo.getPrecioVenta());
            cs.setDouble(4,articulo.getGastoEnvio());
            cs.setInt(5,articulo.getTiempoPreparacion());

            cs.executeUpdate();

        }

        Articulo guardado = buscarPorCodigo(articulo.getCodigo());
        if (guardado != null) {
            articulo.setId(guardado.getId());
        }

    }

    public ArrayList<Articulo> obtenerArticulos() throws SQLException{

        ArrayList<Articulo> listaArticulos = new ArrayList<>();
        String sql = "select * from articulo";

        try(Connection conn = ConexionDB.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while (rs.next()){

                String codigo = rs.getString("codigo");
                String descripcion = rs.getString("descripcion");
                double precioVenta = rs.getDouble("precioventa");
                double gastoEnvio = rs.getDouble("gastoenvio");
                int tiempoPreparacion = rs.getInt("tiempopreparacion");

                Articulo articulo = new Articulo(codigo, descripcion, precioVenta, gastoEnvio, tiempoPreparacion);

                articulo.setId(rs.getInt("id"));
                listaArticulos.add(articulo);
            }

        }
        return listaArticulos;
    }

    public Articulo buscarPorCodigo(String codigoIn) throws SQLException{

        String sql = "select * from articulo where codigo = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1,codigoIn);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){


                String codigo = rs.getString("codigo");
                String descripcion = rs.getString("descripcion");
                double precioVenta = rs.getDouble("precioventa");
                double gastoEnvio = rs.getDouble("gastoenvio");
                int tiempoPreparacion = rs.getInt("tiempopreparacion");

                Articulo articulo = new Articulo(codigo, descripcion, precioVenta, gastoEnvio, tiempoPreparacion);
                articulo.setId(rs.getInt("id"));
                return articulo;
            }

        }
        return null;
    }
}
