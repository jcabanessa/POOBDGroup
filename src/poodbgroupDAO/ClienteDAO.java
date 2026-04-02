package poodbgroupDAO;

import poodbgroupModelo.Cliente;
import poodbgroupModelo.ClienteEstandar;
import poodbgroupModelo.ClientePremium;

import java.sql.*;
import java.util.ArrayList;

public class ClienteDAO {

    public void guardarCliente(Cliente cliente) throws SQLException{
        String sql = "{call insertar_cliente(?,?,?,?,?)}";

        try (Connection conn = ConexionDB.obtenerConexion();
            CallableStatement cs = conn.prepareCall(sql)){

            cs.setString(1,cliente.getNombre());
            cs.setString(2,cliente.getDomicilio());
            cs.setString(3,cliente.getNif());
            cs.setString(4,cliente.getEmail());
            cs.setString(5,cliente.tipoCliente());

            cs.executeUpdate();

        }
        Cliente guardado = buscarPorEmail(cliente.getEmail());
        if (guardado != null) {
            cliente.setId(guardado.getId());
        }
    }

    public ArrayList<Cliente> obtenerClientes() throws SQLException{

        ArrayList<Cliente> listaClientes = new ArrayList<>();
        String sql = "select * from cliente";

        try (Connection conn = ConexionDB.obtenerConexion();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()){

            while (rs.next()){

                String nombre = rs.getString("nombre");
                String domicilio = rs.getString("domicilio");
                String nif = rs.getString("nif");
                String email = rs.getString("email");
                String tipo = rs.getString("tipo");

                Cliente cliente;
                if(tipo.equalsIgnoreCase("premium")){

                    cliente = new ClientePremium(nombre, domicilio, nif, email);

                } else{

                    cliente = new ClienteEstandar(nombre, domicilio, nif, email);

                }
                cliente.setId(rs.getInt("id"));
                listaClientes.add(cliente);
            }
        }
        return  listaClientes;
    }


    public Cliente buscarPorEmail(String email) throws SQLException{

        String sql = "select * from cliente where email = ?";

        try(Connection conn = ConexionDB.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                 String tipo = rs.getString("tipo");
                 Cliente cliente;

                 if(tipo.equalsIgnoreCase("premium")){

                     cliente = new ClientePremium(rs.getString("nombre"), rs.getString("domicilio"), rs.getString("nif"), email);

                 }else{

                     cliente = new ClienteEstandar(rs.getString("nombre"), rs.getString("domicilio"), rs.getString("nif"), email);

                 }
                cliente.setId(rs.getInt("id"));
                 return cliente;

            }

        }

        return null;

    }

}
