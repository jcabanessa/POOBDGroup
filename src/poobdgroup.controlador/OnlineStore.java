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

    //Metdodo de apoyo para imprimir el pedido desde memoria y filtrar
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
}
