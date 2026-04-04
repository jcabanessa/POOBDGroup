package poobdgroup.controlador;

import poobdgroup.excepciones.TiendaException;
import poobdgroup.modelo.*;
import poobdgroup.DAO.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class OnlineStore {

    private Datos datos;
    private final ClienteDAO clienteDAO;
    private final ArticuloDAO articuloDAO;
    private final PedidoDAO pedidoDAO;

    public OnlineStore(Datos datos) {
        this.datos = (datos != null) ? datos : new Datos();
        this.clienteDAO = DAOFactory.getClienteDAO();
        this.articuloDAO = DAOFactory.getArticuloDAO();
        this.pedidoDAO = DAOFactory.getPedidoDAO();
    }

    public OnlineStore() {
        this(new Datos());

        try {
            this.datos.getArticulos().addAll(DAOFactory.getArticuloDAO().obtenerArticulos());
            this.datos.getClientes().addAll(DAOFactory.getClienteDAO().obtenerClientes());
            this.datos.getPedidos().addAll(
                    DAOFactory.getPedidoDAO().obtenerPedidos(
                            this.datos.getArticulos(),
                            this.datos.getClientes()
                    )
            );


            System.out.println("✅ Base de datos cargada correctamente al iniciar.");
        } catch (Exception e) {
            System.err.println("❌ No se pudo sincronizar con la base de datos: " + e.getMessage());
        }
    }

    // ================= CLIENTES =================

    public void addCliente(Cliente cli) throws TiendaException {
        if (cli == null) throw new TiendaException("Cliente nulo.");

        try {
            clienteDAO.guardarCliente(cli);
            datos.getClientes().add(cli);
        } catch (Exception e) {
            throw new TiendaException("Error al guardar cliente: " + e.getMessage());
        }
    }

    public String imprimirClientes(String tipo) throws TiendaException {
        try {
            ArrayList<Cliente> clientes = clienteDAO.obtenerClientes();

            if (clientes.isEmpty()) return "No hay clientes.";

            StringBuilder sb = new StringBuilder();
            sb.append("--- LISTADO DE CLIENTES ---\n");

            for (Cliente c : clientes) {
                if (tipo.equalsIgnoreCase("Todos") || c.tipoCliente().equalsIgnoreCase(tipo)) {
                    sb.append(c)
                            .append(" | Tipo: ")
                            .append(c.tipoCliente())
                            .append("\n");
                }
            }

            return sb.toString();

        } catch (Exception e) {
            throw new TiendaException("Error al obtener clientes: " + e.getMessage());
        }
    }

    // ================= ARTÍCULOS =================

    public void addArticulo(Articulo art) throws TiendaException {
        if (art == null) throw new TiendaException("Artículo nulo.");

        try {
            articuloDAO.guardarArticulo(art);
            datos.getArticulos().add(art);
        } catch (Exception e) {
            throw new TiendaException("Error al guardar artículo: " + e.getMessage());
        }
    }

    public String imprimirArticulos() throws TiendaException {
        try {
            ArrayList<Articulo> articulos = articuloDAO.obtenerArticulos();

            if (articulos.isEmpty()) return "No hay artículos.";

            StringBuilder sb = new StringBuilder();
            sb.append("--- LISTADO DE ARTÍCULOS ---\n");

            for (Articulo a : articulos) {
                sb.append(a).append("\n");
            }

            return sb.toString();

        } catch (Exception e) {
            throw new TiendaException("Error al obtener artículos: " + e.getMessage());
        }
    }

    // ================= PEDIDOS =================

    public void addPedido(String numPedido, int cantidad, LocalDateTime fecha,
                          String codArticulo, String emailCliente) throws TiendaException {

        if (numPedido == null || numPedido.isBlank())
            throw new TiendaException("Número de pedido inválido.");

        try {
            // Evitar duplicados en BD
            if (pedidoDAO.existePedido(numPedido)) {
                throw new TiendaException("El pedido ya existe.");
            }

            // Buscar artículo
            Articulo articulo = articuloDAO.obtenerArticulos().stream()
                    .filter(a -> a.getCodigo().equalsIgnoreCase(codArticulo))
                    .findFirst()
                    .orElseThrow(() -> new TiendaException("Artículo no encontrado"));

            // Buscar cliente
            Cliente cliente = clienteDAO.obtenerClientes().stream()
                    .filter(c -> c.getEmail().equalsIgnoreCase(emailCliente))
                    .findFirst()
                    .orElseThrow(() -> new TiendaException("CLIENTE_NO_EXISTE"));

            Pedido p = new Pedido(numPedido, cantidad, fecha);
            p.setArticulo(articulo);
            p.setCliente(cliente);
            p.setEnviado(false);

            pedidoDAO.guardarPedido(p);
            datos.getPedidos().add(p);

        } catch (TiendaException e) {
            throw e;
        } catch (Exception e) {
            throw new TiendaException("Error al guardar pedido: " + e.getMessage());
        }
    }

    public boolean eliminarPedido(String numPedido) throws TiendaException {
        try {
            // 1. Buscar el pedido en BD (no en memoria)
            ArrayList<Pedido> lista = pedidoDAO.obtenerPedidos(
                    datos.getArticulos(), datos.getClientes());

            Pedido pedido = lista.stream()
                    .filter(p -> p.getNumPedido().equalsIgnoreCase(numPedido))
                    .findFirst()
                    .orElseThrow(() -> new TiendaException("El pedido no existe."));

            // 2. VALIDACIÓN CLAVE
            if (pedido.pedidoEnviado()) {
                throw new TiendaException("No se puede eliminar un pedido ya enviado.");
            }

            // 3. Eliminar en BD
            pedidoDAO.eliminarPedido(numPedido);

            // 4. Eliminar en memoria
            return datos.getPedidos().getAll()
                    .removeIf(p -> p.getNumPedido().equalsIgnoreCase(numPedido));

        } catch (TiendaException e) {
            throw e;
        } catch (Exception e) {
            throw new TiendaException("Error al eliminar pedido: " + e.getMessage());
        }
    }

    // ================= SINCRONIZACIÓN =================

    private void sincronizarPedidosEnviadosAutomaticamente() throws TiendaException {
        try {
            ArrayList<Pedido> listaBD = pedidoDAO.obtenerPedidos(
                    datos.getArticulos(), datos.getClientes());

            for (Pedido p : listaBD) {

                boolean yaEnviadoBD = p.isEnviado();

                boolean debeEnviarse =
                        !yaEnviadoBD &&
                                p.getFecha()
                                        .plusMinutes(p.getArticulo().getTiempoPreparacion())
                                        .isBefore(LocalDateTime.now());

                if (debeEnviarse) {
                    pedidoDAO.enviarPedido(p.getNumPedido());
                }
            }

        } catch (Exception e) {
            throw new TiendaException("Error al sincronizar pedidos: " + e.getMessage());
        }
    }

    // ================= PEDIDOS PENDIENTES =================

    public String mostrarPedidosPendientes(String filtro) throws TiendaException {

        sincronizarPedidosEnviadosAutomaticamente();

        try {
            ArrayList<Pedido> lista = pedidoDAO.obtenerPedidos(
                    datos.getArticulos(), datos.getClientes());

            StringBuilder sb = new StringBuilder();
            sb.append("=== PEDIDOS PENDIENTES ===\n");

            boolean found = false;

            for (Pedido p : lista) {
                if (!p.pedidoEnviado() && coincideFiltro(p, filtro)) {
                    sb.append(formatearPedido(p));
                    found = true;
                }
            }

            if (!found) return "No hay pedidos pendientes.";

            return sb.toString();

        } catch (Exception e) {
            throw new TiendaException("Error al obtener pedidos: " + e.getMessage());
        }
    }

    // ================= PEDIDOS ENVIADOS =================

    public String mostrarPedidosEnviados(String filtro) throws TiendaException {

        sincronizarPedidosEnviadosAutomaticamente();

        try {
            ArrayList<Pedido> lista = pedidoDAO.obtenerPedidos(
                    datos.getArticulos(), datos.getClientes());

            StringBuilder sb = new StringBuilder();
            sb.append("=== PEDIDOS ENVIADOS ===\n");

            boolean found = false;

            for (Pedido p : lista) {
                if (p.pedidoEnviado() && coincideFiltro(p, filtro)) {
                    sb.append(formatearPedido(p));
                    found = true;
                }
            }


            if (!found) return "No hay pedidos enviados.";

            return sb.toString();

        } catch (Exception e) {
            throw new TiendaException("Error al obtener pedidos enviados: " + e.getMessage());
        }
    }

    // ================= MÉTODOS AUXILIARES =================

    private boolean coincideFiltro(Pedido p, String filtro) {
        if (filtro == null || filtro.isBlank() || filtro.equalsIgnoreCase("Todos"))
            return true;

        return p.getCliente().getEmail().equalsIgnoreCase(filtro)
                || p.getCliente().tipoCliente().equalsIgnoreCase(filtro);
    }

    private String formatearPedido(Pedido p) {
        StringBuilder sb = new StringBuilder();

        sb.append(p);

        if (p.getCliente().tipoCliente().equalsIgnoreCase("Premium")) {
            sb.append(" | Cuota anual: ")
                    .append(p.getCliente().calcAnual());
        }

        sb.append("\n");
        return sb.toString();
    }

    // ================= CREACIÓN =================

    public void crearArticulo(String cod, String des, double pre, double env, int t) throws TiendaException {
        addArticulo(new Articulo(cod, des, pre, env, t));
    }

    public void crearCliente(String nom, String dom, String nif, String email, boolean premium) throws TiendaException {
        Cliente c = premium
                ? new ClientePremium(nom, dom, nif, email)
                : new ClienteEstandar(nom, dom, nif, email);

        addCliente(c);
    }

    public void crearPedido(String num, int cant, String codArt, String email) throws TiendaException {
        addPedido(num, cant, LocalDateTime.now(), codArt, email);
    }
}