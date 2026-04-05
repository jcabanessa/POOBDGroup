package poobdgroup.DAO;

/*import poobdgroup.modelo.Cliente;
import poobdgroup.modelo.ClienteEstandar;
import poobdgroup.modelo.ClientePremium;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClienteDAO {

    // Metodo para guardar clientes con antiinyección SQL y llamada a Stored Procedure
    public void guardarCliente(Cliente cliente) throws SQLException {
        String sql = "{CALL insertar_cliente(?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionDB.obtenerConexion();
             java.sql.CallableStatement cs = conn.prepareCall(sql)) {


            cs.setString(1, cliente.getNombre());
            cs.setString(2, cliente.getDomicilio());
            cs.setString(3, cliente.getNif());
            cs.setString(4, cliente.getEmail());
            cs.setString(5, cliente.tipoCliente()); // Aquí guardamos 'Estandar' o 'Premium'

            cs.executeUpdate();
        }
        Cliente guardado = buscarPorEmail(cliente.getEmail());
        if (guardado != null) {
            cliente.setId(guardado.getId());
        }
    }

    // Metodo para obtener los clinetes de la BD con prepareStatements
    public ArrayList<Cliente> obtenerClientes() throws SQLException {
        ArrayList<Cliente> listaClientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String domicilio = rs.getString("domicilio");
                String nif = rs.getString("nif");
                String email = rs.getString("email");
                String tipo = rs.getString("tipo");


                Cliente cliente;
                if (tipo.equalsIgnoreCase("Premium")) {
                    cliente = new ClientePremium(nombre, domicilio, nif, email);
                } else {
                    cliente = new ClienteEstandar(nombre, domicilio, nif, email);
                }
                cliente.setId(rs.getInt("id"));
                listaClientes.add(cliente);
            }
        }
        return listaClientes;
    }

    //Metodo para buscar cliente por email en BD con prepareStatements
    public Cliente buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE email = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String tipo = rs.getString("tipo");
                Cliente cliente;

                if (tipo.equalsIgnoreCase("Premium"))
                    cliente = new ClientePremium(
                            rs.getString("nombre"),
                            rs.getString("domicilio"),
                            rs.getString("nif"),
                            email
                    );
                else
                    cliente = new ClienteEstandar(
                            rs.getString("nombre"),
                            rs.getString("domicilio"),
                            rs.getString("nif"),
                            email
                    );
                cliente.setId(rs.getInt("id"));
                return cliente;
            }
        }
        return null;
    }
}*/

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import poobdgroup.modelo.Cliente;
import poobdgroup.modelo.ClienteEstandar;
import poobdgroup.modelo.ClientePremium;

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
