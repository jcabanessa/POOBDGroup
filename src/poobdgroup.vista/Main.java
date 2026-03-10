package poobdgroup.vista;


// [MermaidChart: 05d3e7bd-4e9f-42f9-b47c-a7cc5e8fc8cf]

import poobdgroup.controlador.OnlineStore;
import poobdgroup.excepciones.TiendaException;
import poobdgroup.modelo.Articulo;
import poobdgroup.modelo.ClienteEstandar;
import poobdgroup.modelo.ClientePremium;
import poobdgroup.modelo.Datos;
import poobdgroup.modelo.Repositorio;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Scanner;

import static java.lang.Integer.parseInt;


public class Main {
    private final OnlineStore controlador;
    private final Scanner sc = new Scanner(System.in);
    DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd

    public Main() {
        Datos datos = new Datos(new Repositorio<>(),new Repositorio<>(), new Repositorio<>());
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
                        controlador.addArticulo(new Articulo(cod, des, pre, env, t));
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

    void gestionClientes() throws TiendaException {
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
                            controlador.addCliente(new ClientePremium(nom, dom, nif, em));
                        else
                            controlador.addCliente(new ClienteEstandar(nom, dom, nif, em));

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

    void gestionPedidos() throws TiendaException {
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
                        controlador.addPedido(num, cant, fecha, art, mail);
                        System.out.println("Pedido registrado.");
                    }
                    case 2 -> {
                        System.out.print("Número de pedido a eliminar: ");
                        String num = sc.nextLine();
                        if (controlador.eliminarPedido(num)) System.out.println("Eliminado.");
                        else throw new TiendaException("Erro: No se pudo eliminar (no existe o ya enviado).");
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
}