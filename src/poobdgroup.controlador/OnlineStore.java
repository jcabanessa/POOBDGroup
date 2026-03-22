package poobdgroup.controlador;

import poobdgroup.excepciones.TiendaException;
import poobdgroup.modelo.*;
import poobdgroup.DAO.*;

import java.time.LocalDateTime;
import java.util.Scanner;

import static java.lang.Integer.parseInt;


public class OnlineStore {
    private Datos datos;
    private final ClienteDAO clienteDAO;
    private final ArticuloDAO articuloDAO;
    private final PedidoDAO pedidoDAO;
    private Scanner sc;

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
            // Intentamos guardar en MySQL
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
            pedidoDAO.guardarPedido(p);

            // Si se guardó en BD sin problemas, lo añadimos a la lista en memoria
            datos.getPedidos().add(p);

        } catch (java.sql.SQLException e) {
            throw new TiendaException("Error al guardar el pedido en la base de datos: " + e.getMessage());
        }
    }

    public boolean existeCliente(String email) {
        return datos.getClientes().getAll().stream()
                .anyMatch(c -> c.getEmail().equalsIgnoreCase(email));
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


    /*//Las 3 funciones de Main gestionClientes(), gestionPedidos() y gestionArticulos las pasamos al controlador
    public void gestionArticulos() {
        int op = -1;
        while (op != 0) {

            try {
                System.out.println("\n1. Añadir Artículo\n2. Mostrar Todos los Artículos\n0. Volver\nOpción: ");
                op = parseInt(sc.nextLine());


                switch (op) {
                    case 1:
                        System.out.print("Código: ");
                        String cod = sc.nextLine();
                        System.out.print("Descripción: ");
                        String des = sc.nextLine();
                        System.out.print("Precio: ");
                        double pre = Double.parseDouble(sc.nextLine());
                        System.out.print("Gastos de Envío: ");
                        double env = Double.parseDouble(sc.nextLine());
                        System.out.print("Tiempo preparación (min): ");
                        int t = parseInt(sc.nextLine());
                        addArticulo(new Articulo(cod, des, pre, env, t));
                        System.out.println("Artículo añadido");
                        break;
                    case 2:
                        mostrarArticulos();
                        break;
                    case 0:
                        System.out.println("Volviendo");
                        break;
                    default:
                        System.out.println("Opción incorrecta");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

    }




    public void gestionClientes() throws TiendaException {
        int op = -1;
        while(op != 0) {
            try {
                System.out.println("\n1. Añadir Cliente\n2. Mostrar Todos los Clientes\n3. Mostrar Clientes Estandar\n4. Mostrar Clientes Premium\n0. Volver\nOpción: ");
                op = parseInt(sc.nextLine());

                switch (op) {
                    case 1:
                        System.out.print("Nombre: ");
                        String nom = sc.nextLine();
                        System.out.print("Domicilio: ");
                        String dom = sc.nextLine();
                        System.out.print("NIF: ");
                        String nif = sc.nextLine();
                        System.out.print("Email: ");
                        String em = sc.nextLine();
                        System.out.print("¿Es Premium? (s/n): ");
                        if (sc.nextLine().equalsIgnoreCase("s"))
                            addCliente(new ClientePremium(nom, dom, nif, em));
                        else
                            addCliente(new ClienteEstandar(nom, dom, nif, em));

                        System.out.println("Cliente añadido");
                        break;
                    case 2:
                        mostrarClientes("Todos");
                        break;
                    case 3:
                        mostrarClientes("Estandar");
                        break;
                    case 4:
                        mostrarClientes("Premium");
                        break;
                    case 0:
                        System.out.println("Volviendo");
                        break;
                    default:
                        System.out.println("Opción incorrecta");
                }
            }catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public void gestionPedidos() throws TiendaException {
        int op = -1;
        while(op != 0) {
            try {
                System.out.println("\n1. Añadir Pedido\n2. Eliminar Pedido\n3. Mostrar Pedidos Pendientes (filtrar por emailCliente/Todos)\n4. Mostrar Pedidos Enviados (filtrar por emailCliente/Todos)\n0. Volver\nOpción: ");
                op = parseInt(sc.nextLine());

                switch (op) {
                    case 1 -> {
                        System.out.print("Número pedido: ");
                        String num = sc.nextLine();
                        System.out.print("Cantidad del artículo: ");
                        int cant = parseInt(sc.nextLine());
                        System.out.print("Fecha y hora de creación: ");
                        LocalDateTime fecha = LocalDateTime.now();
                        System.out.println(fecha);
                        System.out.print("Código de Artículo:");
                        String art = sc.nextLine();
                        System.out.print("Email cliente: ");
                        String mail = sc.nextLine();
                        addPedido(num, cant, fecha, art, mail);
                        System.out.println("Pedido registrado.");
                    }
                    case 2 -> {
                        System.out.print("Número de pedido a eliminar: ");
                        String num = sc.nextLine();
                        if (eliminarPedido(num)) System.out.println("Eliminado.");
                        else throw new TiendaException("Erro: No se pudo eliminar (no existe o ya enviado).");
                    }
                    case 3 -> {
                        System.out.print("Introduce Email del cliente o Todos: ");
                        String emTip = sc.nextLine();
                        mostrarPedidosPendientes(emTip);
                    }
                    case 4 -> {
                        System.out.print("Introduce Email del cliente o Todos: ");
                        String emTip = sc.nextLine();
                        mostrarPedidosEnviados(emTip);
                    }
                }
            }catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }*/

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
