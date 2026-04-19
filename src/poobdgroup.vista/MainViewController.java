package poobdgroup.vista;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import poobdgroup.controlador.OnlineStore;
import poobdgroup.excepciones.TiendaException;

public class MainViewController {

    private OnlineStore store;

    @FXML private TextArea outputArea;

    @FXML private TextField txtCodArt, txtDescArt, txtPrecio, txtEnvio, txtTiempo;

    @FXML private TextField txtNombre, txtDom, txtNif, txtEmail, txtTipo;

    @FXML private TextField txtPedido, txtCantidad, txtCodPedido, txtEmailPedido;

    @FXML
    public void initialize() {
        store = new OnlineStore();
    }

    // ================= ARTÍCULOS =================

    @FXML
    private void crearArticulo() {
        try {
            store.crearArticulo(
                    txtCodArt.getText(),
                    txtDescArt.getText(),
                    Double.parseDouble(txtPrecio.getText()),
                    Double.parseDouble(txtEnvio.getText()),
                    Integer.parseInt(txtTiempo.getText())
            );
            mostrar("Artículo creado correctamente");

        } catch (Exception e) {
            mostrar("Error: " + e.getMessage());
        }
    }

    @FXML
    private void listarArticulos() {
        try {
            mostrar(store.imprimirArticulos());
        } catch (TiendaException e) {
            mostrar(e.getMessage());
        }
    }

    // ================= CLIENTES =================

    @FXML
    private void crearCliente() {
        try {
            store.crearCliente(
                    txtNombre.getText(),
                    txtDom.getText(),
                    txtNif.getText(),
                    txtEmail.getText(),
                    Boolean.parseBoolean(txtTipo.getText())
            );
            mostrar("Cliente creado correctamente");

        } catch (TiendaException e) {
            mostrar(e.getMessage());
        }
    }

    @FXML
    private void listarClientes() {
        try {
            mostrar(store.imprimirClientes("Todos"));
            mostrar(store.imprimirClientes("Estandar"));
            mostrar(store.imprimirClientes("Premium"));
        } catch (TiendaException e) {
            mostrar(e.getMessage());
        }
    }



    // ================= PEDIDOS =================

    @FXML
    private void crearPedido() {
        try {
            store.crearPedido(
                    txtPedido.getText(),
                    Integer.parseInt(txtCantidad.getText()),
                    txtCodPedido.getText(),
                    txtEmailPedido.getText()
            );
            mostrar("Pedido creado correctamente");

        } catch (Exception e) {
            mostrar("Error: " + e.getMessage());
        }
    }

    @FXML
    private void verPendientes() {
        try {
            mostrar(store.mostrarPedidosPendientes("Todos"));
        } catch (TiendaException e) {
            mostrar(e.getMessage());
        }
    }

    @FXML
    private void verEnviados() {
        try {
            mostrar(store.mostrarPedidosEnviados("Todos"));
        } catch (TiendaException e) {
            mostrar(e.getMessage());
        }
    }

    // ================= AUX =================

    private void mostrar(String texto) {
        outputArea.setText(texto);
    }
}
