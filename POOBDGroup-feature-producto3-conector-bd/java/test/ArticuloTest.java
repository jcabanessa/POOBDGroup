package test;

import org.junit.jupiter.api.Test;
import poobdgroup.modelo.Articulo;

import static org.junit.jupiter.api.Assertions.*;

class ArticuloTest {

    @Test
    void gettersYToString() {
        Articulo a = new Articulo("A001", "Silla", 50.5, 5.0, 24);

        assertEquals("A001", a.getCodigo());
        assertEquals("Silla", a.getDescripcion());
        assertEquals(50.5, a.getPrecioVenta(), 1e-6);
        assertEquals(5.0, a.getGastosEnvio(), 1e-6);
        assertEquals(24, a.getTiempoPreparacion());

        String s = a.toString();
        assertTrue(s.contains("A001"));
        assertTrue(s.contains("Silla"));
    }
}