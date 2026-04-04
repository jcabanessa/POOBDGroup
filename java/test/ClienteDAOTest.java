package test;

import org.junit.jupiter.api.*;
import poobdgroup.DAO.*;
import poobdgroup.modelo.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ClienteDAOTest {

    private ClienteDAO clienteDAO;

    @BeforeEach
    void setUp() {
        clienteDAO = DAOFactory.getClienteDAO();
    }

    @Test
    void testGuardarYObtenerCliente() throws Exception {

        Cliente cliente = new ClienteEstandar(
                "Test",
                "Calle Test",
                "12345678A",
                "test@email.com"
        );

        clienteDAO.guardarCliente(cliente);

        ArrayList<Cliente> lista = clienteDAO.obtenerClientes();

        boolean encontrado = lista.stream()
                .anyMatch(c -> c.getEmail().equals("test@email.com"));

        assertTrue(encontrado);
    }
}