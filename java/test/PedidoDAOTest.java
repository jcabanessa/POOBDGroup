package test;

import org.junit.jupiter.api.*;
import poobdgroup.DAO.*;
import poobdgroup.modelo.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PedidoDAOTest {

    private PedidoDAO pedidoDAO;
    private ClienteDAO clienteDAO;
    private ArticuloDAO articuloDAO;

    @BeforeEach
    void setUp() {
        pedidoDAO = DAOFactory.getPedidoDAO();
        clienteDAO = DAOFactory.getClienteDAO();
        articuloDAO = DAOFactory.getArticuloDAO();
    }

    @Test
    void testGuardarPedido() throws Exception {

        // Crear cliente
        Cliente cli = new ClienteEstandar(
                "PedidoTest",
                "Calle",
                "99999999Z",
                "pedido@test.com"
        );
        clienteDAO.guardarCliente(cli);

        // Obtener cliente con ID
        cli = clienteDAO.obtenerClientes().stream()
                .filter(c -> c.getEmail().equals("pedido@test.com"))
                .findFirst().get();

        // Crear articulo
        Articulo art = new Articulo("ARTTEST", "Articulo", 5, 1, 1);
        articuloDAO.guardarArticulo(art);

        art = articuloDAO.obtenerArticulos().stream()
                .filter(a -> a.getCodigo().equals("ARTTEST"))
                .findFirst().get();

        // Crear pedido
        Pedido p = new Pedido("PEDTEST", 2, LocalDateTime.now());
        p.setCliente(cli);
        p.setArticulo(art);

        pedidoDAO.guardarPedido(p);

        ArrayList<Pedido> lista = pedidoDAO.obtenerPedidos(null, null);

        boolean encontrado = lista.stream()
                .anyMatch(pe -> pe.getNumPedido().equals("PEDTEST"));

        assertTrue(encontrado);
    }
}