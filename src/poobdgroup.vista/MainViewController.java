package poobdgroup.vista;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import poobdgroup.controlador.OnlineStore;
import poobdgroup.excepciones.TiendaException;

public class MainViewController {

    private OnlineStore store;

    @FXML private TextArea outputArea;

    @FXML private TextField txtEmailTipo;

    @FXML private TextField txtEmailTipoEnv;

    @FXML private TextField txtCodPed;

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
            txtCodArt.clear(); txtDescArt.clear(); txtPrecio.clear(); txtEnvio.clear(); txtTiempo.clear();

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
            txtNombre.clear(); txtDom.clear(); txtNif.clear(); txtEmail.clear(); txtTipo.clear();

        } catch (TiendaException e) {
            mostrar(e.getMessage());
        }
    }

    @FXML
    private void listarClientes() {
        try {
            mostrar(store.imprimirClientes("Todos"));
        } catch (TiendaException e) {
            mostrar(e.getMessage());
        }
    }

    @FXML
    private void listarEstandar() {
        try {
            mostrar(store.imprimirClientes("Estandar"));
        } catch (TiendaException e) {
            mostrar(e.getMessage());
        }
    }

    @FXML
    private void listarPremium() {
        try {
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
            txtPedido.clear(); txtCantidad.clear(); txtCodPedido.clear(); txtEmailPedido.clear();

        } catch (Exception e) {
            mostrar("Error: " + e.getMessage());
        }
    }

    /*@FXML
    private void crearPedido() {
        try {
            String num = txtPedido.getText().trim();
            int cant = Integer.parseInt(txtCantidad.getText().trim());
            String codArt = txtCodPedido.getText().trim();
            String emailPedido = txtEmailPedido.getText().trim();

            // Si el email está vacío, crear cliente nuevo sin salir de la pestaña
            if (emailPedido.isBlank()) {
                Cliente nuevo = pedirClienteNuevo();
                if (nuevo == null) {
                    mostrar("Alta de cliente cancelada.");
                    return;
                }

                store.addCliente(nuevo);
                emailPedido = nuevo.getEmail();
            }

            store.crearPedido(num, cant, codArt, emailPedido);
            mostrar("Pedido creado correctamente");

            txtPedido.clear();
            txtCantidad.clear();
            txtCodPedido.clear();
            txtEmailPedido.clear();

        } catch (Exception e) {
            mostrar("Error: " + e.getMessage());
        }
    }*/

    @FXML
    private void verPendientes() {
        try {
            String emailTipo = txtEmailTipo.getText();
            mostrar(store.mostrarPedidosPendientes(emailTipo));
            txtEmailTipo.clear();
        } catch (TiendaException e) {
            mostrar(e.getMessage());
        }
    }

    @FXML
    private void delPedido() {
        try {
            String codPed = txtCodPed.getText();
            store.eliminarPedido(codPed);
            mostrar("Pedido eliminado correctamente");
            txtCodPed.clear();
        }catch (TiendaException e) {
            mostrar(e.getMessage());
        }
    }

    @FXML
    private void verEnviados() {
        try {
            String emailTipoEnv = txtEmailTipoEnv.getText();
            mostrar(store.mostrarPedidosEnviados(emailTipoEnv));
            txtEmailTipoEnv.clear();
        } catch (TiendaException e) {
            mostrar(e.getMessage());
        }
    }

    // ================= AUX =================

    private void mostrar(String texto) {
        outputArea.setText(texto);
    }
}
