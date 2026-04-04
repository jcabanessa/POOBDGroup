package test;

import org.junit.jupiter.api.Test;
import poobdgroup.DAO.*;

import static org.junit.jupiter.api.Assertions.*;

public class DAOFactoryTest {

    @Test
    void testGetClienteDAO() {
        ClienteDAO dao = DAOFactory.getClienteDAO();
        assertNotNull(dao);
    }

    @Test
    void testGetArticuloDAO() {
        ArticuloDAO dao = DAOFactory.getArticuloDAO();
        assertNotNull(dao);
    }

    @Test
    void testGetPedidoDAO() {
        PedidoDAO dao = DAOFactory.getPedidoDAO();
        assertNotNull(dao);
    }
}