/*package poobdgroup.vista;


// [MermaidChart: 05d3e7bd-4e9f-42f9-b47c-a7cc5e8fc8cf]
import poobdgroup.controlador.OnlineStore;
import poobdgroup.excepciones.TiendaException;

import java.time.LocalDateTime;

import java.util.Scanner;

import static java.lang.Integer.parseInt;


public class Main {
    private final OnlineStore controlador;
    private final Scanner sc = new Scanner(System.in);

    public Main() {
        controlador = new OnlineStore();
    }

    public static void main(String[] args) {
        Main prg = new Main();
        prg.inicio();


    }

    void inicio() {
        int opcion = -1;

        while (opcion != 0) {

            try {

                System.out.println("\n--- GESTIÓN TIENDA ONLINE ---");
                System.out.println("1. Gestión Artículos");
                System.out.println("2. Gestión Clientes");
                System.out.println("3. Gestión Pedidos");
                System.out.println("0. Salir");
                System.out.print("Opción: ");

                opcion = parseInt(sc.nextLine());

                switch (opcion) {

                    case 1:
                        gestionArticulos();
                        break;

                    case 2:
                        gestionClientes();
                        break;

                    case 3:
                        gestionPedidos();
                        break;

                    case 0:
                        System.out.println("Saliendo del programa...");
                        break;

                    default:
                        System.out.println("Opción no válida.");

                }

            } catch (Exception e) {
                System.out.println("Error de formato. Introduce datos válidos.");
            }
        }
    }

    void gestionArticulos() {
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
                        controlador.crearArticulo(cod, des, pre, env, t);
                        System.out.println("Artículo añadido");
                        break;
                    case 2:
                        controlador.mostrarArticulos();
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

    void gestionClientes() {
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
                        boolean premium = sc.nextLine().equalsIgnoreCase("s");
                        controlador.crearCliente(nom, dom, nif, em, premium);

                        System.out.println("Cliente añadido");
                        break;
                    case 2:
                        controlador.mostrarClientes("Todos");
                        break;
                    case 3:
                        controlador.mostrarClientes("Estandar");
                        break;
                    case 4:
                        controlador.mostrarClientes("Premium");
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

    void gestionPedidos() {
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
                        System.out.print("Email cliente o dejar vacío para crear nuevo cliente: ");
                        String mail = sc.nextLine();
                        try {
                            controlador.crearPedido(num, cant, art, mail);
                            System.out.println("✔ Pedido creado");

                        } catch (TiendaException e) {

                            // Si se deja en balco el email se crea uno nuevo
                            if (mail.isBlank()) {

                                System.out.println("Cliente no existe. Vamos a crearlo.");

                                // Pedir datos del cliente
                                System.out.print("Nombre: ");
                                String nom = sc.nextLine();

                                System.out.print("Domicilio: ");
                                String dom = sc.nextLine();

                                System.out.print("NIF: ");
                                String nif = sc.nextLine();

                                System.out.print("Email cliente: ");
                                String newmail = sc.nextLine();

                                System.out.print("¿Premium? (s/n): ");
                                boolean premium = sc.nextLine().equalsIgnoreCase("s");

                                try {
                                    // Crear cliente
                                    controlador.crearCliente(nom, dom, nif, newmail, premium);

                                    // Reintentar pedido
                                    controlador.crearPedido(num, cant, art, newmail);

                                    System.out.println("Cliente creado y pedido registrado");

                                } catch (Exception ex) {
                                    System.out.println("Error: " + ex.getMessage());
                                }

                            } else {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }
                    }
                    case 2 -> {
                        System.out.print("Número de pedido a eliminar: ");
                        String num = sc.nextLine();
                        if (controlador.eliminarPedido(num)) System.out.println("Eliminado.");
                        else throw new TiendaException("Error: No se pudo eliminar (no existe o ya enviado).");
                    }
                    case 3 -> {
                        System.out.print("Introduce Email del cliente o Todos: ");
                        String emTip = sc.nextLine();
                        controlador.mostrarPedidosPendientes(emTip);
                    }
                    case 4 -> {
                        System.out.print("Introduce Email del cliente o Todos: ");
                        String emTip = sc.nextLine();
                        controlador.mostrarPedidosEnviados(emTip);
                    }
                }
            }catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}*/

/*package poobdgroup.vista;

import poobdgroup.controlador.OnlineStore;
import poobdgroup.excepciones.TiendaException;
import poobdgroup.modelo.Articulo;
import poobdgroup.modelo.Cliente;
import poobdgroup.modelo.Pedido;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Main {

    private final OnlineStore controlador;
    private final Scanner sc = new Scanner(System.in);

    public Main() {
        controlador = new OnlineStore();
    }

    public static void main(String[] args) {
        new Main().inicio();
    }

    void inicio() {
        int opcion = -1;

        while (opcion != 0) {
            try {
                System.out.println("\n--- GESTIÓN TIENDA ONLINE ---");
                System.out.println("1. Gestión Artículos");
                System.out.println("2. Gestión Clientes");
                System.out.println("3. Gestión Pedidos");
                System.out.println("0. Salir");
                System.out.print("Opción: ");

                opcion = parseInt(sc.nextLine());

                switch (opcion) {
                    case 1 -> gestionArticulos();
                    case 2 -> gestionClientes();
                    case 3 -> gestionPedidos();
                    case 0 -> System.out.println("Saliendo del programa...");
                    default -> System.out.println("Opción no válida.");
                }

            } catch (Exception e) {
                System.out.println("Error de formato. Introduce datos válidos.");
            }
        }
    }

    void gestionArticulos() {
        int op = -1;
        while (op != 0) {
            try {
                System.out.println("\n1. Añadir Artículo\n2. Mostrar Todos los Artículos\n0. Volver\nOpción: ");
                op = parseInt(sc.nextLine());

                switch (op) {
                    case 1 -> {
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

                        controlador.crearArticulo(cod, des, pre, env, t);
                        System.out.println("Artículo añadido");
                    }
                    case 2 -> imprimirArticulos(controlador.mostrarArticulos());
                    case 0 -> System.out.println("Volviendo");
                    default -> System.out.println("Opción incorrecta");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    void gestionClientes() {
        int op = -1;
        while (op != 0) {
            try {
                System.out.println("\n1. Añadir Cliente\n2. Mostrar Todos los Clientes\n3. Mostrar Clientes Estandar\n4. Mostrar Clientes Premium\n0. Volver\nOpción: ");
                op = parseInt(sc.nextLine());

                switch (op) {
                    case 1 -> {
                        System.out.print("Nombre: ");
                        String nom = sc.nextLine();
                        System.out.print("Domicilio: ");
                        String dom = sc.nextLine();
                        System.out.print("NIF: ");
                        String nif = sc.nextLine();
                        System.out.print("Email: ");
                        String em = sc.nextLine();
                        System.out.print("¿Es Premium? (s/n): ");
                        boolean premium = sc.nextLine().equalsIgnoreCase("s");

                        controlador.crearCliente(nom, dom, nif, em, premium);
                        System.out.println("Cliente añadido");
                    }
                    case 2 -> imprimirClientes(controlador.mostrarClientes("Todos"));
                    case 3 -> imprimirClientes(controlador.mostrarClientes("Estandar"));
                    case 4 -> imprimirClientes(controlador.mostrarClientes("Premium"));
                    case 0 -> System.out.println("Volviendo");
                    default -> System.out.println("Opción incorrecta");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    void gestionPedidos() {
        int op = -1;
        while (op != 0) {
            try {
                System.out.println("\n1. Añadir Pedido\n2. Eliminar Pedido\n3. Mostrar Pedidos Pendientes (filtrar por emailCliente/Todos)\n4. Mostrar Pedidos Enviados (filtrar por emailCliente/Todos)\n0. Volver\nOpción: ");
                op = parseInt(sc.nextLine());

                switch (op) {
                    case 1 -> {
                        System.out.print("Número pedido: ");
                        String num = sc.nextLine();
                        System.out.print("Cantidad del artículo: ");
                        int cant = parseInt(sc.nextLine());
                        System.out.print("Código de Artículo: ");
                        String art = sc.nextLine();
                        System.out.print("Email cliente o dejar vacío para crear nuevo cliente: ");
                        String mail = sc.nextLine();

                        try {
                            if (mail.isBlank()) {
                                System.out.println("Cliente no existe. Vamos a crearlo.");

                                System.out.print("Nombre: ");
                                String nom = sc.nextLine();
                                System.out.print("Domicilio: ");
                                String dom = sc.nextLine();
                                System.out.print("NIF: ");
                                String nif = sc.nextLine();
                                System.out.print("Email cliente: ");
                                String newmail = sc.nextLine();
                                if (newmail.isBlank()) {
                                    throw new TiendaException("El email no puede quedar vacío.");
                                }
                                System.out.print("¿Premium? (s/n): ");
                                boolean premium = sc.nextLine().equalsIgnoreCase("s");

                                controlador.crearCliente(nom, dom, nif, newmail, premium);
                                controlador.crearPedido(num, cant, art, newmail);
                            } else {
                                controlador.crearPedido(num, cant, art, mail);
                            }

                            System.out.println("✔ Pedido creado");

                        } catch (TiendaException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                    case 2 -> {
                        System.out.print("Número de pedido a eliminar: ");
                        String num = sc.nextLine();
                        if (controlador.eliminarPedido(num)) {
                            System.out.println("Eliminado.");
                        } else {
                            System.out.println("Error: No se pudo eliminar.");
                        }
                    }
                    case 3 -> {
                        System.out.print("Introduce Email del cliente o Todos: ");
                        String emTip = sc.nextLine();
                        imprimirPedidos(controlador.mostrarPedidosPendientes(emTip));
                    }
                    case 4 -> {
                        System.out.print("Introduce Email del cliente o Todos: ");
                        String emTip = sc.nextLine();
                        imprimirPedidos(controlador.mostrarPedidosEnviados(emTip));
                    }
                    case 0 -> System.out.println("Volviendo");
                    default -> System.out.println("Opción incorrecta");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /*private void imprimirArticulos(ArrayList<Articulo> articulos) {
        if (articulos.isEmpty()) {
            System.out.println("No hay artículos que mostrar.");
            return;
        }

        System.out.println("--- LISTADO DE ARTÍCULOS ---");
        for (Articulo a : articulos) {
            System.out.println(a);
        }
    }

    private void imprimirClientes(ArrayList<Cliente> clientes) {
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes que mostrar.");
            return;
        }

        System.out.println("--- LISTADO DE CLIENTES ---");
        for (Cliente c : clientes) {
            System.out.println(c + " | Tipo:" + c.tipoCliente());
        }
    }

    private void imprimirPedidos(ArrayList<Pedido> pedidos) {
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos que mostrar.");
            return;
        }

        for (Pedido p : pedidos) {
            System.out.println(p);
        }
    }*/
//}*/

package poobdgroup.vista;

import poobdgroup.controlador.OnlineStore;
import poobdgroup.excepciones.TiendaException;

import java.time.LocalDateTime;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Main {

    private final OnlineStore controlador;
    private final Scanner sc = new Scanner(System.in);

    public Main() {
        controlador = new OnlineStore();
    }

    public static void main(String[] args) {
        Main prg = new Main();
        prg.inicio();
    }

    void inicio() {
        int opcion = -1;

        while (opcion != 0) {
            try {
                System.out.println("\n--- GESTIÓN TIENDA ONLINE ---");
                System.out.println("1. Gestión Artículos");
                System.out.println("2. Gestión Clientes");
                System.out.println("3. Gestión Pedidos");
                System.out.println("0. Salir");
                System.out.print("Opción: ");

                opcion = parseInt(sc.nextLine());

                switch (opcion) {
                    case 1 -> gestionArticulos();
                    case 2 -> gestionClientes();
                    case 3 -> gestionPedidos();
                    case 0 -> System.out.println("Saliendo del programa...");
                    default -> System.out.println("Opción no válida.");
                }

            } catch (Exception e) {
                System.out.println("Error de formato. Introduce datos válidos.");
            }
        }
    }

    // ================= ARTÍCULOS =================

    void gestionArticulos() {
        int op = -1;

        while (op != 0) {
            try {
                System.out.println("\n1. Añadir Artículo");
                System.out.println("2. Mostrar Todos los Artículos");
                System.out.println("0. Volver");
                System.out.print("Opción: ");

                op = parseInt(sc.nextLine());

                switch (op) {
                    case 1 -> {
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

                        controlador.crearArticulo(cod, des, pre, env, t);
                        System.out.println("✔ Artículo añadido");
                    }

                    case 2 -> {
                        System.out.println(controlador.imprimirArticulos());
                    }

                    case 0 -> System.out.println("Volviendo...");
                    default -> System.out.println("Opción incorrecta");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ================= CLIENTES =================

    void gestionClientes() {
        int op = -1;

        while (op != 0) {
            try {
                System.out.println("\n1. Añadir Cliente");
                System.out.println("2. Mostrar Todos los Clientes");
                System.out.println("3. Mostrar Clientes Estandar");
                System.out.println("4. Mostrar Clientes Premium");
                System.out.println("0. Volver");
                System.out.print("Opción: ");

                op = parseInt(sc.nextLine());

                switch (op) {
                    case 1 -> {
                        System.out.print("Nombre: ");
                        String nom = sc.nextLine();

                        System.out.print("Domicilio: ");
                        String dom = sc.nextLine();

                        System.out.print("NIF: ");
                        String nif = sc.nextLine();

                        System.out.print("Email: ");
                        String em = sc.nextLine();

                        System.out.print("¿Es Premium? (s/n): ");
                        boolean premium = sc.nextLine().equalsIgnoreCase("s");

                        controlador.crearCliente(nom, dom, nif, em, premium);
                        System.out.println("✔ Cliente añadido");
                    }

                    case 2 -> System.out.println(controlador.imprimirClientes("Todos"));
                    case 3 -> System.out.println(controlador.imprimirClientes("Estandar"));
                    case 4 -> System.out.println(controlador.imprimirClientes("Premium"));

                    case 0 -> System.out.println("Volviendo...");
                    default -> System.out.println("Opción incorrecta");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ================= PEDIDOS =================

    void gestionPedidos() {
        int op = -1;

        while (op != 0) {
            try {
                System.out.println("\n1. Añadir Pedido");
                System.out.println("2. Eliminar Pedido");
                System.out.println("3. Mostrar Pedidos Pendientes");
                System.out.println("4. Mostrar Pedidos Enviados");
                System.out.println("0. Volver");
                System.out.print("Opción: ");

                op = parseInt(sc.nextLine());

                switch (op) {

                    case 1 -> {
                        System.out.print("Número pedido: ");
                        String num = sc.nextLine();

                        System.out.print("Cantidad: ");
                        int cant = parseInt(sc.nextLine());

                        System.out.print("Código artículo: ");
                        String art = sc.nextLine();

                        System.out.print("Email cliente (o vacío): ");
                        String mail = sc.nextLine();

                        try {
                            controlador.crearPedido(num, cant, art, mail);
                            System.out.println("✔ Pedido creado");

                        } catch (TiendaException e) {

                            if (mail.isBlank()) {
                                System.out.println("Cliente no existe. Vamos a crearlo.");

                                System.out.print("Nombre: ");
                                String nom = sc.nextLine();

                                System.out.print("Domicilio: ");
                                String dom = sc.nextLine();

                                System.out.print("NIF: ");
                                String nif = sc.nextLine();

                                System.out.print("Email: ");
                                String newmail = sc.nextLine();

                                System.out.print("¿Premium? (s/n): ");
                                boolean premium = sc.nextLine().equalsIgnoreCase("s");

                                controlador.crearCliente(nom, dom, nif, newmail, premium);
                                controlador.crearPedido(num, cant, art, newmail);

                                System.out.println("✔ Cliente y pedido creados");

                            } else {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }
                    }

                    case 2 -> {
                        System.out.print("Número pedido: ");
                        String num = sc.nextLine();

                        if (controlador.eliminarPedido(num)) {
                            System.out.println("✔ Pedido eliminado");
                        } else {
                            System.out.println("No se pudo eliminar");
                        }
                    }

                    case 3 -> {
                        System.out.print("Filtro (email / tipo / Todos): ");
                        String filtro = sc.nextLine();

                        System.out.println(controlador.obtenerPedidosPendientes(filtro));
                    }

                    case 4 -> {
                        System.out.print("Filtro (email / tipo / Todos): ");
                        String filtro = sc.nextLine();

                        System.out.println(controlador.obtenerPedidosEnviados(filtro));
                    }

                    case 0 -> System.out.println("Volviendo...");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}