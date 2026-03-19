package poobdgroup.controlador;

import poobdgroup.dao.DAOFactory;
import poobdgroup.excepciones.TiendaException;
import poobdgroup.modelo.Articulo;
import poobdgroup.modelo.Cliente;
import poobdgroup.modelo.Datos;
import poobdgroup.modelo.Pedido;

import java.time.LocalDateTime;

public class OnlineStore {
    private Datos datos;

    public OnlineStore(Datos datos) {
        this.datos = (datos != null) ? datos : new Datos();
    }

    public OnlineStore() {
        this(new Datos());

        // Carga inicial desde la base de datos
        try {
            this.datos.getArticulos().addAll(DAOFactory.getArticuloDAO().obtenerArticulos());
            this.datos.getClientes().addAll(DAOFactory.getClienteDAO().obtenerClientes());
            this.datos.getPedidos().addAll(DAOFactory.getPedidoDAO().obtenerPedidos(
                    this.datos.getArticulos(), this.datos.getClientes()));
            System.out.println("Base de datos cargada correctamente.");
        } catch (Exception e) {
            System.err.println("No se pudo cargar la base de datos: " + e.getMessage());
        }
    }

    public void addCliente(Cliente cli) throws TiendaException {
        if (cli == null)
            throw new TiendaException("Cliente nulo.");
        if (datos.getClientes().stream().anyMatch(c -> c.getEmail().equals(cli.getEmail())))
            throw new TiendaException("Error: El email ya está registrado.");

        try {
            DAOFactory.getClienteDAO().guardarCliente(cli);
            datos.getClientes().add(cli);
        } catch (java.sql.SQLException e) {
            throw new TiendaException("Error al guardar el cliente: " + e.getMessage());
        }
    }

    public void mostrarClientes(String tipo) throws TiendaException {
        try {
            java.util.ArrayList<Cliente> lista = DAOFactory.getClienteDAO().obtenerClientes();
            if (lista.isEmpty())
                throw new TiendaException("No hay clientes registrados.");
            System.out.println("--- LISTADO DE CLIENTES ---");
            for (Cliente c : lista) {
                if (tipo.equalsIgnoreCase("Todos") || c.tipoCliente().equalsIgnoreCase(tipo))
                    System.out.println(c + " | Tipo:" + c.tipoCliente());
            }
        } catch (java.sql.SQLException e) {
            throw new TiendaException("Error al consultar la base de datos: " + e.getMessage());
        }
    }

    public void addArticulo(Articulo art) throws TiendaException {
        if (art == null)
            throw new TiendaException("Artículo nulo.");
        if (datos.getArticulos().stream().anyMatch(a -> a.getCodigo().equals(art.getCodigo())))
            throw new TiendaException("Error: Ya existe un artículo con ese código.");

        try {
            DAOFactory.getArticuloDAO().guardarArticulo(art);
            datos.getArticulos().add(art);
        } catch (java.sql.SQLException e) {
            throw new TiendaException("Error al guardar el artículo: " + e.getMessage());
        }
    }

    public void mostrarArticulos() throws TiendaException {
        try {
            java.util.ArrayList<Articulo> lista = DAOFactory.getArticuloDAO().obtenerArticulos();
            if (lista.isEmpty())
                throw new TiendaException("No hay artículos que mostrar.");
            System.out.println("--- LISTADO DE ARTÍCULOS ---");
            for (Articulo a : lista)
                System.out.println(a);
        } catch (java.sql.SQLException e) {
            throw new TiendaException("Error al consultar la base de datos: " + e.getMessage());
        }
    }

    public void addPedido(String numPedido, int cantidad, LocalDateTime fecha, String codArticulo, String emailCliente)
            throws TiendaException {
        if (numPedido == null || codArticulo == null || emailCliente == null)
            throw new TiendaException("Parámetros inválidos.");
        if (datos.getPedidos().stream().anyMatch(p -> p.getNumPedido().equals(numPedido)))
            throw new TiendaException("El pedido ya existe.");

        Articulo articulo = datos.getArticulos().stream()
                .filter(a -> a.getCodigo().equals(codArticulo))
                .findFirst().orElseThrow(() -> new TiendaException("Artículo no encontrado."));

        Cliente cliente = datos.getClientes().stream()
                .filter(c -> c.getEmail().equals(emailCliente))
                .findFirst().orElseThrow(() -> new TiendaException("Cliente no encontrado."));

        Pedido p = new Pedido(numPedido, cantidad, fecha);
        p.setArticulo(articulo);
        p.setCliente(cliente);

        try {
            DAOFactory.getPedidoDAO().guardarPedido(p);
            datos.getPedidos().add(p);
        } catch (java.sql.SQLException e) {
            throw new TiendaException("Error al guardar el pedido: " + e.getMessage());
        }
    }

    public boolean eliminarPedido(String numPedido) throws TiendaException {
        Pedido p = datos.getPedidos().stream()
                .filter(pedido -> pedido.getNumPedido().equals(numPedido))
                .findFirst().orElseThrow(() -> new TiendaException("El pedido no existe."));

        if (p.pedidoEnviado())
            throw new TiendaException("No se puede eliminar un pedido ya enviado.");

        try {
            DAOFactory.getPedidoDAO().eliminarPedido(numPedido);
            return datos.getPedidos().remove(p);
        } catch (java.sql.SQLException e) {
            throw new TiendaException("Error al eliminar el pedido: " + e.getMessage());
        }
    }

    public void mostrarPedidosPendientes(String mailOTipo) throws TiendaException {
        System.out.println("=== PEDIDOS PENDIENTES ===");
        boolean found = false;
        for (Pedido p : datos.getPedidos()) {
            if (!p.pedidoEnviado() &&
                    (p.getCliente().getEmail().equalsIgnoreCase(mailOTipo) ||
                            mailOTipo.equalsIgnoreCase("Todos") ||
                            p.getCliente().tipoCliente().equalsIgnoreCase(mailOTipo))) {
                System.out.println(p);
                found = true;
            }
        }
        if (!found)
            throw new TiendaException("No hay pedidos pendientes.");
    }

    public void mostrarPedidosEnviados(String mailOTipo) throws TiendaException {
        System.out.println("=== PEDIDOS ENVIADOS ===");
        boolean found = false;
        for (Pedido p : datos.getPedidos()) {
            if (p.pedidoEnviado() &&
                    (p.getCliente().getEmail().equalsIgnoreCase(mailOTipo) ||
                            mailOTipo.equalsIgnoreCase("Todos") ||
                            p.getCliente().tipoCliente().equalsIgnoreCase(mailOTipo))) {
                System.out.println(p);
                found = true;
            }
        }
        if (!found)
            throw new TiendaException("No hay pedidos enviados.");
    }

    @Override
    public String toString() {
        return "OnlineStore{articulos=" + datos.getArticulos() +
                ", clientes=" + datos.getClientes() +
                ", pedidos=" + datos.getPedidos() + '}';
    }
}