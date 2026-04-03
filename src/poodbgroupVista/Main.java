package poodbgroupVista;

import poodbgroupControlador.OnlineStore;
import poodbgroupExcepciones.TiendaException;
import poodbgroupModelo.Articulo;
import poodbgroupModelo.ClienteEstandar;
import poodbgroupModelo.ClientePremium;
import poodbgroupModelo.Cliente;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Main {
    private final OnlineStore controlador;
    private final Scanner sc = new Scanner(System.in);
    DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public Main() {
        // Cambio: ya no necesita Datos, el controlador usa DAOs directamente
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
                System.out.println("\n**gestion tienda online**");
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
            } catch (NumberFormatException e) {
                System.out.println("Error de formato. Introduce un número válido.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    void gestionArticulos() {
        int op = -1;
        while (op != 0) {
            try {
                System.out.println("\n*gestion articulos*");
                System.out.println("1. Añadir Artículo");
                System.out.println("2. Mostrar Todos los Artículos");
                System.out.println("0. Volver");
                System.out.print("Opción: ");
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
                        System.out.println("Artículo añadido correctamente.");
                        break;

                    case 2:
                        try {
                            String resultado = controlador.mostrarArticulo();
                            System.out.println(resultado);
                        } catch (TiendaException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 0:
                        System.out.println("Volviendo al menú principal...");
                        break;

                    default:
                        System.out.println("Opción incorrecta.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Introduce un valor numérico válido.");
            } catch (TiendaException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
            }
        }
    }

    void gestionClientes() {
        int op = -1;
        while (op != 0) {
            try {
                System.out.println("\n**gestion clientes*");
                System.out.println("1. Añadir Cliente");
                System.out.println("2. Mostrar Todos los Clientes");
                System.out.println("3. Mostrar Clientes Estándar");
                System.out.println("4. Mostrar Clientes Premium");
                System.out.println("0. Volver");
                System.out.print("Opción: ");
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
                        String respuesta = sc.nextLine();

                        if (respuesta.equalsIgnoreCase("s")) {
                            controlador.addCliente(new ClientePremium(nom, dom, nif, em));
                            System.out.println("Cliente Premium añadido correctamente.");
                        } else {
                            controlador.addCliente(new ClienteEstandar(nom, dom, nif, em));
                            System.out.println("Cliente Estándar añadido correctamente.");
                        }
                        break;

                    case 2:
                        try {
                            String resultado = controlador.mostrarClientes("todos");
                            System.out.println(resultado);
                        } catch (TiendaException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 3:
                        try {
                            String resultado = controlador.mostrarClientes("estandar");
                            System.out.println(resultado);
                        } catch (TiendaException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 4:
                        try {
                            String resultado = controlador.mostrarClientes("premium");
                            System.out.println(resultado);
                        } catch (TiendaException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 0:
                        System.out.println("Volviendo al menú principal...");
                        break;

                    default:
                        System.out.println("Opción incorrecta.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Introduce un valor numérico válido.");
            } catch (TiendaException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
            }
        }
    }

    void gestionPedidos() {
        int op = -1;
        while (op != 0) {
            try {
                System.out.println("\n**gestion pedidos**");
                System.out.println("1. Añadir Pedido");
                System.out.println("2. Eliminar Pedido");
                System.out.println("3. Mostrar Pedidos Pendientes (filtrar por email cliente/Todos)");
                System.out.println("4. Mostrar Pedidos Enviados (filtrar por email cliente/Todos)");
                System.out.println("0. Volver");
                System.out.print("Opción: ");
                op = parseInt(sc.nextLine());

                switch (op) {
                    case 1:
                        anyadirPedido();
                        break;

                    case 2:
                        System.out.print("Número de pedido a eliminar: ");
                        int numPedido = parseInt(sc.nextLine());
                        try {
                            boolean eliminado = controlador.eliminarPedido(numPedido);
                            if (eliminado) {
                                System.out.println("Pedido eliminado correctamente.");
                            }
                        } catch (TiendaException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 3:
                        System.out.print("Introduce email del cliente o 'todos': ");
                        String filtroPendiente = sc.nextLine();
                        try {
                            String resultado = controlador.mostrarPedidosPendientes(filtroPendiente);
                            System.out.println(resultado);
                        } catch (TiendaException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 4:
                        System.out.print("Introduce email del cliente o 'todos': ");
                        String filtroEnviado = sc.nextLine();
                        try {
                            String resultado = controlador.mostrarPedidosEnviados(filtroEnviado);
                            System.out.println(resultado);
                        } catch (TiendaException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 0:
                        System.out.println("Volviendo al menú principal...");
                        break;

                    default:
                        System.out.println("Opción incorrecta.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Introduce un valor numérico válido.");
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
            }
        }
    }

    private void anyadirPedido() {
        try {
            System.out.print("Cantidad de unidades: ");
            int cantidad = parseInt(sc.nextLine());

            System.out.print("Código del artículo: ");
            String codArticulo = sc.nextLine();

            System.out.print("Email del cliente: ");
            String emailCliente = sc.nextLine();

            LocalDateTime fecha = LocalDateTime.now();
            System.out.println("Fecha y hora del pedido: " + fecha.format(df));

            try {
                controlador.addPedido(cantidad, fecha, codArticulo, emailCliente);
                System.out.println("Pedido registrado correctamente.");

            } catch (TiendaException e) {
                if (e.getMessage().equals("Cliente no encontrado")) {
                    System.out.println("\n*** El cliente no existe en el sistema ***");
                    System.out.println("Vamos a crear un nuevo cliente con el email: " + emailCliente);

                    System.out.print("Nombre del cliente: ");
                    String nombre = sc.nextLine();

                    System.out.print("Domicilio: ");
                    String domicilio = sc.nextLine();

                    System.out.print("NIF: ");
                    String nif = sc.nextLine();

                    System.out.print("¿Es cliente Premium? (s/n): ");
                    String esPremium = sc.nextLine();

                    Cliente nuevoCliente;
                    if (esPremium.equalsIgnoreCase("s")) {
                        nuevoCliente = new ClientePremium(nombre, domicilio, nif, emailCliente);
                        System.out.println("Cliente Premium creado.");
                    } else {
                        nuevoCliente = new ClienteEstandar(nombre, domicilio, nif, emailCliente);
                        System.out.println("Cliente Estándar creado.");
                    }

                    controlador.addCliente(nuevoCliente);
                    System.out.println("Cliente añadido correctamente.");

                    controlador.addPedido(cantidad, fecha, codArticulo, emailCliente);
                    System.out.println("Pedido registrado correctamente con el nuevo cliente.");

                } else {
                    throw e;
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: Introduce valores numéricos válidos.");
        } catch (TiendaException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado al añadir pedido: " + e.getMessage());
        }
    }
}