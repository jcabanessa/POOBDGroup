package poobdgroup.controlador;

import poobdgroup.excepciones.TiendaException;
import poobdgroup.modelo.Articulo;
import poobdgroup.modelo.Cliente;
import poobdgroup.modelo.Datos;
import poobdgroup.modelo.Pedido;

import java.time.LocalDateTime;


public class OnlineStore {
    private Datos datos;

    //Constructor
    public OnlineStore(Datos datos) {
        this.datos = (datos != null) ? datos : new Datos();
    }

    public OnlineStore() {
        this(new Datos());

        // --- CARGA INICIAL DESDE LA BASE DE DATOS ---
        try {
            poobdgroup.DAO.ArticuloDAO aDAO = new poobdgroup.DAO.ArticuloDAO();
            poobdgroup.DAO.ClienteDAO cDAO = new poobdgroup.DAO.ClienteDAO();
            poobdgroup.DAO.PedidoDAO pDAO = new poobdgroup.DAO.PedidoDAO();

            // 1. Cargamos Artículos y Clientes primero
            this.datos.getArticulos().addAll(aDAO.obtenerArticulos());
            this.datos.getClientes().addAll(cDAO.obtenerClientes());

            // 2. Ahora cargamos los Pedidos pasándole las listas para que los enlace
            this.datos.getPedidos().addAll(pDAO.obtenerPedidos(this.datos.getArticulos(), this.datos.getClientes()));

            System.out.println("✅ Base de datos cargada correctamente al iniciar.");
        } catch (Exception e) {
            System.err.println("❌ No se pudo sincronizar con la base de datos: " + e.getMessage());
        }
    }

    //Métodos
    /*public void addCliente(Cliente cli) throws TiendaException {
        if (cli == null) throw new TiendaException("Cliente nulo.");
        if (datos.getClientes().stream().anyMatch(c -> c.getEmail().equals(cli.getEmail()))) {
            throw new TiendaException("Error: El email ya está registrado.");
        }
        datos.getClientes().add(cli);
    }*/

    public void addCliente(Cliente cli) throws TiendaException {
        // 1. Validaciones originales
        if (cli == null) throw new TiendaException("Cliente nulo.");
        if (datos.getClientes().getAll().stream().anyMatch(c -> c.getEmail().equals(cli.getEmail()))) {
            throw new TiendaException("Error: El email ya está registrado.");
        }

        // 2. Guardar en la Base de Datos
        try {
            // Instanciamos el DAO que creaste
            poobdgroup.DAO.ClienteDAO clienteDAO = new poobdgroup.DAO.ClienteDAO();

            // Intentamos guardar en MariaDB
            clienteDAO.guardarCliente(cli);

            // 3. Si se guardó con éxito en la BD, lo añadimos también a la lista en memoria
            datos.getClientes().add(cli);

        } catch (java.sql.SQLException e) {
            // Si la base de datos da un error (ej. se cae la conexión), lanzamos tu excepción
            throw new TiendaException("Error al guardar el cliente en la base de datos: " + e.getMessage());
        }
    }

    /*public void mostrarClientes(String tipo) throws TiendaException {
        if (datos.getClientes().isEmpty()) {
            throw new TiendaException("Error: No hay clientes registrados en el sistema.");
        } else {
            System.out.println("--- LISTADO DE CLIENTES ---");
            for (Cliente c : datos.getClientes()) {
                // Si tipo es "Todos" o coincide con el tipo del cliente, lo muestra
                if (tipo.equalsIgnoreCase("Todos") || c.tipoCliente().equalsIgnoreCase(tipo)) {
                    System.out.println(c + " | Tipo:" + c.tipoCliente());

                }
            }
        }
    }*/

    public void mostrarClientes(String tipo) throws TiendaException {
        try {
            // 1. Instanciamos el DAO y pedimos todos los clientes a MariaDB
            poobdgroup.DAO.ClienteDAO clienteDAO = new poobdgroup.DAO.ClienteDAO();
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

   /* public void addArticulo (Articulo art) throws TiendaException {
        if (art == null) throw new TiendaException("Artículo nulo.");
        if (datos.getArticulos().stream().anyMatch(a -> a.getCodigo().equals(art.getCodigo()))) {
            throw new TiendaException("Error: Ya existe un artículo con ese código.");
        }
        datos.getArticulos().add(art);
    }*/

    public void addArticulo(Articulo art) throws TiendaException {
        if (art == null) throw new TiendaException("Artículo nulo.");


        if (datos.getArticulos().getAll().stream().anyMatch(a -> a.getCodigo().equals(art.getCodigo()))) {
            throw new TiendaException("Error: Ya existe un artículo con ese código.");
        }


        try {
            poobdgroup.DAO.ArticuloDAO articuloDAO = new poobdgroup.DAO.ArticuloDAO();
            articuloDAO.guardarArticulo(art);


            datos.getArticulos().add(art);

        } catch (java.sql.SQLException e) {

            throw new TiendaException("Error al guardar en la base de datos: " + e.getMessage());
        }
    }

    /*public void mostrarArticulos () throws TiendaException {
        if (datos.getArticulos().isEmpty()) {
            throw new TiendaException("Error: No hay articulos que mostrar.");
        } else {
            System.out.println("--- LISTADO DE ARTICULOS ---");
            for (Articulo a : datos.getArticulos()) {
                System.out.println(a.toString());
            }
        }
    }*/

    public void mostrarArticulos() throws TiendaException {
        try {
            // Instanciamos el DAO y pedimos los datos a MariaDB
            poobdgroup.DAO.ArticuloDAO articuloDAO = new poobdgroup.DAO.ArticuloDAO();
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

    /*public void addPedido (String numPedido,
                           int cantidad,
                           LocalDateTime fecha,
                           String codArticulo,
                           String emailCliente) throws TiendaException {
        if (numPedido == null || codArticulo == null || emailCliente == null) throw new TiendaException("Parámetros inválidos.");
        if (datos.getPedidos().stream()
                .anyMatch(p -> p.getNumPedido().equals(numPedido)))
            throw new TiendaException("El pedido ya existe");

        Articulo articulo = datos.getArticulos().stream()
                .filter(a -> a.getCodigo().equals(codArticulo))
                .findFirst()
                .orElseThrow(() -> new TiendaException("Artículo no encontrado"));

        Cliente cliente = datos.getClientes().stream()
                .filter(c -> c.getEmail().equals(emailCliente))
                .findFirst()
                .orElseThrow(() -> new TiendaException("Cliente no encontrado"));

        Pedido p = new Pedido(numPedido, cantidad, fecha);

        p.setArticulo(articulo);
        p.setCliente(cliente);

        datos.getPedidos().add(p);

    }*/

    public void addPedido(String numPedido, int cantidad, LocalDateTime fecha, String codArticulo, String emailCliente) throws TiendaException {
        if (numPedido == null || codArticulo == null || emailCliente == null) throw new TiendaException("Parámetros inválidos.");
        if (datos.getPedidos().getAll().stream().anyMatch(p -> p.getNumPedido().equals(numPedido)))
            throw new TiendaException("El pedido ya existe");

        Articulo articulo = datos.getArticulos().getAll().stream()
                .filter(a -> a.getCodigo().equals(codArticulo))
                .findFirst()
                .orElseThrow(() -> new TiendaException("Artículo no encontrado"));

        Cliente cliente = datos.getClientes().getAll().stream()
                .filter(c -> c.getEmail().equals(emailCliente))
                .findFirst()
                .orElseThrow(() -> new TiendaException("Cliente no encontrado"));

        Pedido p = new Pedido(numPedido, cantidad, fecha);
        p.setArticulo(articulo);
        p.setCliente(cliente);

        // --- CONEXIÓN A BASE DE DATOS ---
        try {
            poobdgroup.DAO.PedidoDAO pedidoDAO = new poobdgroup.DAO.PedidoDAO();
            pedidoDAO.guardarPedido(p);

            // Si se guardó en BD sin problemas, lo añadimos a la lista en memoria
            datos.getPedidos().add(p);

        } catch (java.sql.SQLException e) {
            throw new TiendaException("Error al guardar el pedido en la base de datos: " + e.getMessage());
        }
    }

    /*public boolean eliminarPedido (String numPedido) throws TiendaException {
        Pedido p = datos.getPedidos().stream()
                .filter(pedido -> pedido.getNumPedido().equals(numPedido))
                .findFirst()
                .orElseThrow(() -> new TiendaException("Error: El pedido no existe."));

        if (p.pedidoEnviado()) {
            throw new TiendaException("Error: No se puede eliminar un pedido que ya ha sido enviado.");
        }
        return datos.getPedidos().remove(p);
    }*/

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
            poobdgroup.DAO.PedidoDAO pedidoDAO = new poobdgroup.DAO.PedidoDAO();
            pedidoDAO.eliminarPedido(numPedido);

            // Si se borró de la BD sin problemas, lo quitamos de la memoria
            return datos.getPedidos().remove(p);

        } catch (java.sql.SQLException e) {
            throw new TiendaException("Error al eliminar el pedido de la base de datos: " + e.getMessage());
        }
    }

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

    public void mostrarPedidosEnviados (String mailOTipo) throws TiendaException {
        System.out.println("=== PEDIDOS ENVIADOS ===");
        boolean found = false;
        for (Pedido p : datos.getPedidos()) {
            if (p.pedidoEnviado() &&
                    (p.getCliente().getEmail().equalsIgnoreCase(mailOTipo))) {
                if(p.getCliente().tipoCliente().equalsIgnoreCase("Premium")){
                    System.out.println(p + " |CuotaAnual para descuento 20% en envíos= " + p.getCliente().calcAnual());
                }else
                    System.out.println(p);
                found = true;
            }
            else if(p.pedidoEnviado() &&
                    (mailOTipo.equalsIgnoreCase("Todos") || p.getCliente().tipoCliente().equalsIgnoreCase(mailOTipo))) {
                if(p.getCliente().tipoCliente().equalsIgnoreCase("Premium")){
                    System.out.println(p + " |CuotaAnual para descuento 20% en envíos= " + p.getCliente().calcAnual());
                }else
                    System.out.println(p);
                found = true;
            }
        }
        if (!found) throw new TiendaException("Error: No hay pedidos enviados");
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
