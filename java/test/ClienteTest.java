package test;

import org.junit.jupiter.api.Test;
import poobdgroup.modelo.ClienteEstandar;
import poobdgroup.modelo.ClientePremium;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void tiposYToString() {
        ClienteEstandar est = new ClienteEstandar("Ana", "C/Mayor 1", "1234A", "ana@example.com");
        ClientePremium prem = new ClientePremium("Luis", "Av Sol 2", "5678B", "luis@example.com");

        assertEquals("Estandar", est.tipoCliente());
        assertEquals("Premium", prem.tipoCliente());

        assertTrue(est.toString().contains("ana@example.com"));
        assertTrue(prem.toString().contains("luis@example.com"));
    }
}