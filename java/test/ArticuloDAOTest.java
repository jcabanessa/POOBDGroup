package test;

import org.junit.jupiter.api.*;
import poobdgroup.DAO.*;
import poobdgroup.modelo.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ArticuloDAOTest {

    private ArticuloDAO articuloDAO;

    @BeforeEach
    void setUp() {
        articuloDAO = DAOFactory.getArticuloDAO();
    }

    @Test
    void testGuardarYObtenerArticulo() throws Exception {

        Articulo art = new Articulo(
                "CODTEST",
                "Articulo Test",
                10.0,
                2.0,
                5
        );

        articuloDAO.guardarArticulo(art);

        ArrayList<Articulo> lista = articuloDAO.obtenerArticulos();

        boolean encontrado = lista.stream()
                .anyMatch(a -> a.getCodigo().equals("CODTEST"));

        assertTrue(encontrado);
    }
}