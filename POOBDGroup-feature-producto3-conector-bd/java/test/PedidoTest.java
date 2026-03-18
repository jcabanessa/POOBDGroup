package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import poobdgroup.modelo.Articulo;
import poobdgroup.modelo.ClienteEstandar;
import poobdgroup.modelo.Pedido;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    private Pedido pedido;
    private Articulo articulo;
    private ClienteEstandar cliente;

    @BeforeEach
    void setUp() {
        articulo = new Articulo("A001", "Silla", 50.0, 5.0, 2); // tiempoPreparacion 2 min
        cliente = new ClienteEstandar("Ana", "C/Mayor 1", "1234A", "ana@example.com");

        // pedido creado hace 3 horas -> debería considerarse enviado
        LocalDateTime fechaEnviado = LocalDateTime.now().minusMinutes(3);
        pedido = new Pedido("0100", 2, fechaEnviado);
        pedido.setArticulo(articulo);
        pedido.setCliente(cliente);
    }

    @Test
    void calcularYEnvio() {
        // calcularPrecioTotal = cantidad * precioVenta
        double esperadoArt = 2 * 50.0;
        assertEquals(esperadoArt + 5, pedido.calcularPrecioTotal(), 1e-6);

        // precioEnvio delega al artículo
        assertEquals(5.0, pedido.precioEnvio(), 1e-6);

        // totalPedido = artículos + envío
        assertEquals(esperadoArt + 5.0, pedido.calcularPrecioTotal(), 1e-6);

        // pedidoEnviado -> true porque lo creamos hace 3 horas y tiempoPreparacion=2
        assertTrue(pedido.pedidoEnviado());

        // toString no nulo y contiene número de pedido
        assertNotNull(pedido.toString());
        assertTrue(pedido.toString().contains("0100"));
    }

    @Test
    void pedidoNoEnviado() {
        // crear pedido reciente (ahora) con tiempo preparacion 24min -> no enviado
        Pedido reciente = new Pedido("0200", 1, LocalDateTime.now());
        Articulo art2 = new Articulo("A002", "Mesa", 100.0, 10.0, 24);
        reciente.setArticulo(art2);
        reciente.setCliente(cliente);

        assertFalse(reciente.pedidoEnviado());
    }
}