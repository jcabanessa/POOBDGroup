/*package poobdgroup.controlador;

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

    //Constructor
    public OnlineStore(Datos datos) {
        this.datos = (datos != null) ? datos : new Datos();
        this.clienteDAO = DAOFactory.getClienteDAO();
        this.articuloDAO = DAOFactory.getArticuloDAO();
        this.pedidoDAO = DAOFactory.getPedidoDAO();
    }

    public OnlineStore() {
        this(new Datos());

        // --- CARGA INICIAL DESDE LA BASE DE DATOS ---
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

    //Añadir cliente en memoria y a la base de datos
    public void addCliente(Cliente cli) throws TiendaException {
        // 1. Validaciones originales
        if (cli == null) throw new TiendaException("Cliente nulo.");
        if (datos.getClientes().getAll().stream().anyMatch(c -> c.getEmail().equals(cli.getEmail()))) {
            throw new TiendaException("Error: El email ya está registrado.");
        }

        // 2. Guardar en la Base de Datos
        try {
            // Intentamos guardar en MySQL
            clienteDAO.guardarCliente(cli);

            // 3. Si se guardó con éxito en la BD, lo añadimos también a la lista en memoria
            datos.getClientes().add(cli);

        } catch (java.sql.SQLException e) {
            // Si la base de datos da un error (ej. se cae la conexión), lanzamos tu excepción
            throw new TiendaException("Error al guardar el cliente en la base de datos: " + e.getMessage());
        }
    }


    //Muestra los clientes de la base de datos
    public void mostrarClientes(String tipo) throws TiendaException {
        try {
            // 1. Pedimos todos los clientes a BD
            java.util.ArrayList<Cliente> listaDesdeBD = clienteDAO.obtenerClientes();

            // 2. Comprobamos si la tabla está vacía
            if (listaDesdeBD.isEmpty()) {
                throw new TiendaException("Error: No hay clientes registrados en la base de datos.");
            } else {
                System.out.println("--- LISTADO DE CLIENTES ---");

                // 3. Recorremos la lista real traída de la base de datos
                for (Cliente c : listaDesdeBD) {
                    // Mantenemos tu lógica original para filtrar por tipo
                    if (tipo.equalsIgnoreCase("Todos") || c.tipoCliente().equalsIgnoreCase(tipo)) {
                        System.out.println(c + " | Tipo:" + c.tipoCliente());
                    }
                }
            }
        } catch (java.sql.SQLException e) {
            // Capturamos cualquier error de conexión
            throw new TiendaException("Error al consultar la base de datos: " + e.getMessage());
        }
    }



    //Añadir articulo a la base de datos
    public void addArticulo(Articulo art) throws TiendaException {
        if (art == null) throw new TiendaException("Artículo nulo.");


        if (datos.getArticulos().getAll().stream().anyMatch(a -> a.getCodigo().equals(art.getCodigo()))) {
            throw new TiendaException("Error: Ya existe un artículo con ese código.");
        }


        try {
            articuloDAO.guardarArticulo(art);
            datos.getArticulos().add(art);

        } catch (java.sql.SQLException e) {

            throw new TiendaException("Error al guardar en la base de datos: " + e.getMessage());
        }
    }

    //Muestra los articulos de la base de datos
    public void mostrarArticulos() throws TiendaException {
        try {
            // Pedimos los datos a DB
            java.util.ArrayList<Articulo> listaDesdeBD = articuloDAO.obtenerArticulos();

            if (listaDesdeBD.isEmpty()) {
                throw new TiendaException("Error: No hay artículos que mostrar en la base de datos.");
            } else {
                System.out.println("--- LISTADO DE ARTÍCULOS ---");
                for (Articulo a : listaDesdeBD) {
                    System.out.println(a.toString());
                }
            }
        } catch (java.sql.SQLException e) {
            throw new TiendaException("Error al consultar la base de datos: " + e.getMessage());
        }
    }

    //Añadir pedido
    public void addPedido(String numPedido, int cantidad, LocalDateTime fecha,
                          String codArticulo, String emailCliente) throws TiendaException {

        if (numPedido == null || numPedido.isBlank() ||
                codArticulo == null || codArticulo.isBlank())
            throw new TiendaException("Parámetros inválidos.");

        if (emailCliente == null || emailCliente.isBlank()) {
            throw new TiendaException("CLIENTE_NO_EXISTE");
        }

        if (datos.getPedidos().getAll().stream()
                .anyMatch(p -> p.getNumPedido().equals(numPedido)))
            throw new TiendaException("El pedido ya existe");

        try {
            // ARTICULO (memoria → BD)
            Articulo articulo = datos.getArticulos().getAll().stream()
                    .filter(a -> a.getCodigo().equalsIgnoreCase(codArticulo))
                    .findFirst()
                    .orElse(null);

            if (articulo == null) {
                articulo = articuloDAO.obtenerArticulos().stream()
                        .filter(a -> a.getCodigo().equalsIgnoreCase(codArticulo))
                        .findFirst()
                        .orElseThrow(() -> new TiendaException("Artículo no encontrado"));
            }

            // CLIENTE
            Cliente cliente = datos.getClientes().getAll().stream()
                    .filter(c -> c.getEmail().equalsIgnoreCase(emailCliente))
                    .findFirst()
                    .orElse(null);

            if (cliente == null) {
                cliente = clienteDAO.obtenerClientes().stream()
                        .filter(c -> c.getEmail().equalsIgnoreCase(emailCliente))
                        .findFirst()
                        .orElse(null);
            }

            // SI NO EXISTE → LANZAR EXCEPCIÓN
            if (cliente == null) {
                throw new TiendaException("CLIENTE_NO_EXISTE");
            }

            // CREAR PEDIDO
            Pedido p = new Pedido(numPedido, cantidad, fecha);
            p.setArticulo(articulo);
            p.setCliente(cliente);
            p.setEnviado(false); // Aun no se envia

            // GUARDAR EN BD
            pedidoDAO.guardarPedido(p);

            // GUARDAR EN MEMORIA
            datos.getPedidos().add(p);

        } catch (java.sql.SQLException e) {
            throw new TiendaException("Error al guardar el pedido: " + e.getMessage());
        }
    }

    //Elimina pedido si aun no ha sido enviado
    public boolean eliminarPedido(String numPedido) throws TiendaException {
        Pedido p = datos.getPedidos().getAll().stream()
                .filter(pedido -> pedido.getNumPedido().equals(numPedido))
                .findFirst()
                .orElseThrow(() -> new TiendaException("Error: El pedido no existe."));

        if (p.pedidoEnviado()) {
            throw new TiendaException("Error: No se puede eliminar un pedido que ya ha sido enviado.");
        }

        // --- CONEXIÓN A BASE DE DATOS ---
        try {
            pedidoDAO.eliminarPedido(numPedido);

            // Si se borró de la BD sin problemas, lo quitamos de la memoria
            return datos.getPedidos().remove(p);

        } catch (java.sql.SQLException e) {
            throw new TiendaException("Error al eliminar el pedido de la base de datos: " + e.getMessage());
        }
    }

    //Mostrar pedidos pendientes (Se mantiene la lógica solo busca en memoria no en BD)
    public void mostrarPedidosPendientes (String mailOTipo) throws TiendaException {
        System.out.println("=== PEDIDOS PENDIENTES ===");
        boolean found = false;
        for (Pedido p : datos.getPedidos()) {
            if (!p.pedidoEnviado() &&
                    (p.getCliente().getEmail().equalsIgnoreCase(mailOTipo))) {
                if(p.getCliente().tipoCliente().equalsIgnoreCase("Premium")){
                    System.out.println(p + " |CuotaAnual para descuento 20% en envíos= " + p.getCliente().calcAnual());
                }else
                    System.out.println(p);
                found = true;
            }
            else if (!p.pedidoEnviado() &&
                    (mailOTipo.equalsIgnoreCase("Todos") || p.getCliente().tipoCliente().equalsIgnoreCase(mailOTipo))) {
                if(p.getCliente().tipoCliente().equalsIgnoreCase("Premium")){
                    System.out.println(p + " |CuotaAnual para descuento 20% en envíos= " + p.getCliente().calcAnual());
                }else
                    System.out.println(p);
                found = true;
            }
        }
        if (!found) throw new TiendaException("Error: No hay pedidos pendientes.");
    }

    //Muestra pedidos enviados buscando en memoria y en BD
    public void mostrarPedidosEnviados(String filtro) throws TiendaException {
        System.out.println("=== PEDIDOS ENVIADOS ===");

        for (Pedido p : datos.getPedidos()) {

            int minutos = p.getArticulo().getTiempoPreparacion();

            if (!p.pedidoEnviado() &&
                    p.getFecha().plusMinutes(minutos).isBefore(LocalDateTime.now())) {

                p.setEnviado(true);

                try {
                    pedidoDAO.enviarPedido(p.getNumPedido());
                } catch (Exception ignored) {}
            }
        }
        boolean found = false;

        try {
            ArrayList<Pedido> lista = pedidoDAO.obtenerPedidos(datos.getArticulos(),
                    datos.getClientes());

            for (Pedido p : lista) {

                if (p.pedidoEnviado() && coincideFiltro(p, filtro)) {

                    imprimirPedido(p);
                    found = true;
                }
            }

        } catch (Exception e) {
            throw new TiendaException("Error al consultar pedidos: " + e.getMessage());
        }

        if (!found)
            throw new TiendaException("No hay pedidos enviados.");
    }

    //Metodo de apoyo para mostrar pedidos enviados y filtrar
    private boolean coincideFiltro(Pedido p, String filtro) {

        if (filtro == null || filtro.isBlank())
            return true;

        if (filtro.equalsIgnoreCase("todos"))
            return true;

        if (p.getCliente().getEmail().equalsIgnoreCase(filtro))
            return true;

        return p.getCliente().tipoCliente().equalsIgnoreCase(filtro);
    }

    //Metodo de apoyo para imprimir el pedido desde memoria y filtrar
    private void imprimirPedido(Pedido p) {

        if (p.getCliente().tipoCliente().equalsIgnoreCase("Premium")) {

            System.out.println(p +
                    " | Cuota anual (desc 20% envío): " +
                    p.getCliente().calcAnual());

        } else {
            System.out.println(p);
        }
    }

    //Metodos de apoyo para crear Articulos, Clientes y Pedidos en el Main

    public void crearArticulo(String cod, String des, double pre, double env, int t) throws TiendaException {
        addArticulo(new Articulo(cod, des, pre, env, t));
    }

    public void crearCliente(String nom, String dom, String nif, String email, boolean premium) throws TiendaException {

        Cliente c;

        if (premium) {
            c = new ClientePremium(nom, dom, nif, email);
        } else {
            c = new ClienteEstandar(nom, dom, nif, email);
        }

        addCliente(c);
    }

    public void crearPedido(String num, int cant, String codArt, String email) throws TiendaException {

        LocalDateTime fecha = LocalDateTime.now();

        addPedido(num, cant, fecha, codArt, email);
    }


    //toString


    @Override
    public String toString() {
        return "OnlineStore{" +
                "articulos=" + datos.getArticulos() +
                ", clientes=" + datos.getClientes() +
                ", pedidos=" + datos.getPedidos() +
                '}';
    }
}*/

/*package poobdgroup.controlador;

import poobdgroup.DAO.ArticuloDAO;
import poobdgroup.DAO.ClienteDAO;
import poobdgroup.DAO.DAOFactory;
import poobdgroup.DAO.PedidoDAO;
import poobdgroup.excepciones.TiendaException;
import poobdgroup.modelo.Articulo;
import poobdgroup.modelo.Cliente;
import poobdgroup.modelo.ClienteEstandar;
import poobdgroup.modelo.ClientePremium;
import poobdgroup.modelo.Datos;
import poobdgroup.modelo.Pedido;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class OnlineStore {

    private final Datos datos;
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
    }

    private void recargarCacheDesdeBD() throws SQLException {
        datos.getArticulos().getAll().clear();
        datos.getClientes().getAll().clear();
        datos.getPedidos().getAll().clear();

        datos.getArticulos().addAll(articuloDAO.obtenerArticulos());
        datos.getClientes().addAll(clienteDAO.obtenerClientes());
        datos.getPedidos().addAll(
                pedidoDAO.obtenerPedidos(datos.getArticulos(), datos.getClientes())
        );
    }

    public void addCliente(Cliente cli) throws TiendaException {
        if (cli == null) throw new TiendaException("Cliente nulo.");

        try {
            if (clienteDAO.buscarPorEmail(cli.getEmail()) != null) {
                throw new TiendaException("Error: El email ya está registrado.");
            }

            clienteDAO.guardarCliente(cli);
            datos.getClientes().add(cli);

        } catch (SQLException e) {
            throw new TiendaException("Error al guardar el cliente en la base de datos: " + e.getMessage());
        }
    }

    public ArrayList<Cliente> mostrarClientes(String tipo) throws TiendaException {
        try {
            ArrayList<Cliente> listaDesdeBD = clienteDAO.obtenerClientes();
            ArrayList<Cliente> resultado = new ArrayList<>();

            for (Cliente c : listaDesdeBD) {
                if (tipo.equalsIgnoreCase("Todos") || c.tipoCliente().equalsIgnoreCase(tipo)) {
                    resultado.add(c);
                }
            }

            return resultado;
        } catch (SQLException e) {
            throw new TiendaException("Error al consultar la base de datos: " + e.getMessage());
        }
    }

    public void addArticulo(Articulo art) throws TiendaException {
        if (art == null) throw new TiendaException("Artículo nulo.");

        try {
            if (articuloDAO.buscarPorCodigo(art.getCodigo()) != null) {
                throw new TiendaException("Error: Ya existe un artículo con ese código.");
            }

            articuloDAO.guardarArticulo(art);
            datos.getArticulos().add(art);

        } catch (SQLException e) {
            throw new TiendaException("Error al guardar en la base de datos: " + e.getMessage());
        }
    }

    public ArrayList<Articulo> mostrarArticulos() throws TiendaException {
        try {
            return articuloDAO.obtenerArticulos();
        } catch (SQLException e) {
            throw new TiendaException("Error al consultar la base de datos: " + e.getMessage());
        }
    }

    public void addPedido(String numPedido, int cantidad, LocalDateTime fecha,
                          String codArticulo, String emailCliente) throws TiendaException {

        if (numPedido == null || numPedido.isBlank() ||
                codArticulo == null || codArticulo.isBlank()) {
            throw new TiendaException("Parámetros inválidos.");
        }

        if (emailCliente == null || emailCliente.isBlank()) {
            throw new TiendaException("CLIENTE_NO_EXISTE");
        }

        try {
            if (pedidoDAO.existePedido(numPedido)) {
                throw new TiendaException("El pedido ya existe");
            }

            Articulo articulo = articuloDAO.buscarPorCodigo(codArticulo);
            if (articulo == null) {
                throw new TiendaException("Artículo no encontrado");
            }

            Cliente cliente = clienteDAO.buscarPorEmail(emailCliente);
            if (cliente == null) {
                throw new TiendaException("CLIENTE_NO_EXISTE");
            }

            Pedido p = new Pedido(numPedido, cantidad, fecha);
            p.setArticulo(articulo);
            p.setCliente(cliente);
            p.setEnviado(false);

            pedidoDAO.guardarPedido(p);
            datos.getPedidos().add(p);

        } catch (SQLException e) {
            throw new TiendaException("Error al guardar el pedido en la base de datos: " + e.getMessage());
        }
    }

    public boolean eliminarPedido(String numPedido) throws TiendaException {
        try {
            recargarCacheDesdeBD();
        } catch (SQLException e) {
            throw new TiendaException("Error al consultar pedidos: " + e.getMessage());
        }

        Pedido p = datos.getPedidos().getAll().stream()
                .filter(pedido -> pedido.getNumPedido().equalsIgnoreCase(numPedido))
                .findFirst()
                .orElseThrow(() -> new TiendaException("Error: El pedido no existe."));

        if (p.pedidoEnviado()) {
            throw new TiendaException("Error: No se puede eliminar un pedido que ya ha sido enviado.");
        }

        try {
            pedidoDAO.eliminarPedido(numPedido);
            return datos.getPedidos().remove(p);
        } catch (SQLException e) {
            throw new TiendaException("Error al eliminar el pedido de la base de datos: " + e.getMessage());
        }
    }

    public ArrayList<Pedido> mostrarPedidosPendientes(String filtro) throws TiendaException {
        try {
            recargarCacheDesdeBD();
        } catch (SQLException e) {
            throw new TiendaException("Error al consultar pedidos: " + e.getMessage());
        }

        ArrayList<Pedido> resultado = new ArrayList<>();

        for (Pedido p : datos.getPedidos()) {
            if (!p.pedidoEnviado() && coincideFiltro(p, filtro)) {
                resultado.add(p);
            }
        }

        return resultado;
    }

    public ArrayList<Pedido> mostrarPedidosEnviados(String filtro) throws TiendaException {
        try {
            recargarCacheDesdeBD();
        } catch (SQLException e) {
            throw new TiendaException("Error al consultar pedidos: " + e.getMessage());
        }

        for (Pedido p : datos.getPedidos()) {
            if (p.pedidoEnviado()) {
                try {
                    pedidoDAO.enviarPedido(p.getNumPedido());
                } catch (Exception ignored) {
                }
            }
        }

        try {
            recargarCacheDesdeBD();
        } catch (SQLException e) {
            throw new TiendaException("Error al consultar pedidos: " + e.getMessage());
        }

        ArrayList<Pedido> resultado = new ArrayList<>();

        for (Pedido p : datos.getPedidos()) {
            if (p.pedidoEnviado() && coincideFiltro(p, filtro)) {
                resultado.add(p);
            }
        }

        return resultado;
    }

    private boolean coincideFiltro(Pedido p, String filtro) {
        if (filtro == null || filtro.isBlank()) return true;
        if (filtro.equalsIgnoreCase("todos")) return true;
        if (p.getCliente() == null) return false;
        if (p.getCliente().getEmail().equalsIgnoreCase(filtro)) return true;
        return p.getCliente().tipoCliente().equalsIgnoreCase(filtro);
    }

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

    public String imprimirArticulos() throws TiendaException {
        try {
            ArrayList<Articulo> articulos = articuloDAO.obtenerArticulos();

            if (articulos.isEmpty()) {
                return "No hay artículos que mostrar.";
            }

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

    public String imprimirClientes(String tipo) throws TiendaException {
        try {
            ArrayList<Cliente> clientes = clienteDAO.obtenerClientes();

            if (clientes.isEmpty()) {
                return "No hay clientes que mostrar.";
            }

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

    public String imprimirPedidos(ArrayList<Pedido> pedidos) {

        if (pedidos.isEmpty()) {
            return "No hay pedidos que mostrar.";
        }

        StringBuilder sb = new StringBuilder();

        for (Pedido p : pedidos) {
            sb.append(p).append("\n");
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return "OnlineStore{" +
                "articulos=" + datos.getArticulos() +
                ", clientes=" + datos.getClientes() +
                ", pedidos=" + datos.getPedidos() +
                '}';
    }
}*/

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
            datos.getArticulos().addAll(articuloDAO.obtenerArticulos());
            datos.getClientes().addAll(clienteDAO.obtenerClientes());
            datos.getPedidos().addAll(
                    pedidoDAO.obtenerPedidos(datos.getArticulos(), datos.getClientes())
            );
        } catch (Exception ignored) {}
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
            pedidoDAO.eliminarPedido(numPedido);
            return datos.getPedidos().getAll()
                    .removeIf(p -> p.getNumPedido().equals(numPedido));
        } catch (Exception e) {
            throw new TiendaException("Error al eliminar pedido: " + e.getMessage());
        }
    }

    // ================= PEDIDOS PENDIENTES =================

    public String obtenerPedidosPendientes(String filtro) throws TiendaException {
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

    public String obtenerPedidosEnviados(String filtro) throws TiendaException {
        try {
            // Actualizar estado
            for (Pedido p : datos.getPedidos()) {
                if (!p.pedidoEnviado() &&
                        p.getFecha().plusMinutes(p.getArticulo().getTiempoPreparacion())
                                .isBefore(LocalDateTime.now())) {

                    p.setEnviado(true);
                    pedidoDAO.enviarPedido(p.getNumPedido());
                }
            }

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