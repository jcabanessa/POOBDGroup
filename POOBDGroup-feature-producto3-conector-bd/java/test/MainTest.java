package test;

import org.junit.jupiter.api.Test;
import poobdgroup.vista.Main;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void constructorNoLanzaExcepcion() {
        assertDoesNotThrow(() -> new Main());
    }
}