package test;

import org.junit.jupiter.api.Test;
import poobdgroup.modelo.Datos;

import static org.junit.jupiter.api.Assertions.*;

class DatosTest {

    @Test
    void listasInicialesVacias() {
        Datos d = new Datos();
        assertNotNull(d.getArticulos());
        assertNotNull(d.getClientes());
        assertNotNull(d.getPedidos());

        assertTrue(d.getArticulos().isEmpty());
        assertTrue(d.getClientes().isEmpty());
        assertTrue(d.getPedidos().isEmpty());
    }
}