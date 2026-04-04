package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import poobdgroup.controlador.OnlineStore;
import poobdgroup.excepciones.TiendaException;
import poobdgroup.modelo.Articulo;
import poobdgroup.modelo.ClienteEstandar;
import poobdgroup.modelo.ClientePremium;
import poobdgroup.modelo.Datos;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OnlineStoreTest {

    private OnlineStore controlador;
    private Datos datos;

    @BeforeEach
    void setUp() {
        datos = new Datos();
        controlador = new OnlineStore(datos);
    }

    @Test
    void addMostrarArticulosYClientesYPedidos() {
        try {
            Articulo a = new Articulo("A001", "Silla", 50.5, 5.0, 1);
            controlador.addArticulo(a);
            assertEquals(1, datos.getArticulos().size());

            ClienteEstandar c = new ClienteEstandar("Ana", "C/Mayor", "123", "ana@x.com");
            controlador.addCliente(c);
            assertEquals(1, datos.getClientes().size());

            // agregar pedido (artículo y cliente existen)
            controlador.addPedido("P001", 2, LocalDateTime.now(), "A001", "ana@x.com");
            assertEquals(1, datos.getPedidos().size());

            // eliminar pedido pendiente -> no lanza excepción y devuelve true
            boolean eliminado = controlador.eliminarPedido("P001");
            assertTrue(eliminado);
            assertTrue(datos.getPedidos().isEmpty());

        } catch (TiendaException e) {
            fail("No debería lanzarse excepción: " + e.getMessage());
        }
    }

    @Test
    void addPedidoErrorPorArticuloNoExistente() {
        ClienteEstandar c = new ClienteEstandar("Andres", "C/Andrade 3", "1234", "andres@x.com");
        try {
            controlador.addCliente(c);
        } catch (TiendaException e) {
            fail(e.getMessage());
        }

        // artículo A999 no existe -> debe lanzar TiendaException
        assertThrows(TiendaException.class, () ->
                controlador.addPedido("P100", 1, LocalDateTime.now(), "A999", "andres@x.com")
        );
    }

    @Test
    void eliminarPedidoEnviadoLanzaExcepcion() {
        try {
            Articulo a = new Articulo("A002", "Mesa", 10.0, 2.0, 1);
            controlador.addArticulo(a);
            ClienteEstandar c = new ClienteEstandar("Pablo", "C/1", "999", "p@x.com");
            controlador.addCliente(c);

            // crear pedido antiguo (enviado)
            LocalDateTime old = LocalDateTime.now().minusHours(5);
            controlador.addPedido("P200", 1, old, "A002", "p@x.com");

            // ahora intentar eliminar -> debe lanzar TiendaException porque ya enviado
            assertThrows(TiendaException.class, () -> controlador.eliminarPedido("P200"));

        } catch (TiendaException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void mostrarPedidosFiltradosNoLanzaExcepcion() {
        try {
            Articulo a = new Articulo("A003", "Lampara", 20.0, 3.0, 48);
            controlador.addArticulo(a);
            ClienteEstandar ce = new ClienteEstandar("Jon", "D", "11", "jon@x.com");
            ClientePremium cp = new ClientePremium("Eva", "D2", "22", "eva@x.com");
            controlador.addCliente(ce);
            controlador.addCliente(cp);

            controlador.addPedido("PF1", 1, LocalDateTime.now(), "A003", "jon@x.com");
            controlador.addPedido("PF2", 1, LocalDateTime.now().minusMinutes(50), "A003", "eva@x.com");

            // filtrar pendientes por email (jon) y por Todos
            assertDoesNotThrow(() -> controlador.mostrarPedidosPendientes("jon@x.com"));
            assertDoesNotThrow(() -> controlador.mostrarPedidosPendientes("Todos"));

            // filtrar enviados (should not throw)
            assertDoesNotThrow(() -> controlador.mostrarPedidosEnviados("eva@x.com"));
            assertDoesNotThrow(() -> controlador.mostrarPedidosEnviados("Todos"));

        } catch (TiendaException e) {
            fail(e.getMessage());
        }
    }
}