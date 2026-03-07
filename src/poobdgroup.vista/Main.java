package poobdgroup.vista;


// [MermaidChart: 05d3e7bd-4e9f-42f9-b47c-a7cc5e8fc8cf]

import poobdgroup.controlador.OnlineStore;
import poobdgroup.excepciones.TiendaException;
import poobdgroup.modelo.Articulo;
import poobdgroup.modelo.ClienteEstandar;
import poobdgroup.modelo.ClientePremium;
import poobdgroup.modelo.Datos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private OnlineStore controlador;
    private Scanner sc = new Scanner(System.in);

    public Main() {
        Datos datos = new Datos(new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
        controlador = new OnlineStore();
    }

    public static void main(String[] args) throws TiendaException {
        Main prg = new Main();
        prg.inicio();
    }

    void inicio() throws TiendaException {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN TIENDA ONLINE ---");
            System.out.println("1. Gestión Artículos\n2. Gestión Clientes\n3. Gestión Pedidos\n0. Salir");
            opcion = sc.nextInt();
            sc.nextLine(); // Limpiar buffer

            switch (opcion) {
                case 1 -> gestionArticulos();
                case 2 -> gestionClientes();
                case 3 -> gestionPedidos();
            }
        } while (opcion != 0);
    }

    void gestionArticulos() throws TiendaException {
        System.out.println("\n1. Añadir Artículo\n2. Mostrar Todos los Artículos\n0. Volver");
        int op = sc.nextInt();
        sc.nextLine(); // Limpiar buffer

        switch (op) {
            case 1 -> {
                System.out.print("Código: "); String cod = sc.nextLine();
                System.out.print("Descripción: "); String des = sc.nextLine();
                System.out.print("Precio: "); float pre = sc.nextFloat();
                System.out.print("Envío: "); float env = sc.nextFloat();
                System.out.print("Tiempo preparación (min): "); int t = sc.nextInt();
                controlador.addArticulo(new Articulo(cod, des, pre, env, t));
            }
            case 2 -> controlador.mostrarArticulos();
        }
    }

    void gestionClientes() throws TiendaException {
        System.out.println("\n1. Añadir Cliente\n2. Mostrar Todos los Clientes\n3. Mostrar Clientes Estandar\n4. Mostrar Clientes Premium\n0. Volver");
        int op = sc.nextInt();
        sc.nextLine(); // Limpiar buffer

        switch (op) {
            case 1 -> {
                System.out.print("Nombre: "); String nom = sc.nextLine();
                System.out.print("Email: "); String em = sc.nextLine();
                System.out.print("¿Es Premium? (s/n): ");
                if (sc.nextLine().equalsIgnoreCase("s"))
                    controlador.addCliente(new ClientePremium(nom, "", "", em));
                else
                    controlador.addCliente(new ClienteEstandar(nom, "", "", em));
            }
            case 2 -> controlador.mostrarClientes("Todos");
            case 3 -> controlador.mostrarClientes("Estandar");
            case 4 -> controlador.mostrarClientes("Premium");
        }
    }

    void gestionPedidos() throws TiendaException {
        System.out.println("\n1. Añadir Pedido\n2. Eliminar Pedido\n3. Mostrar Pedidos Pendientes de envío\n4. Mostrar Pedidos Enviados\n0. Volver");
        int op = sc.nextInt();
        sc.nextLine(); // Limpiar buffer

        switch (op) {
            case 1 -> {
                System.out.print("Número pedido: "); String num = sc.nextLine();
                System.out.print("Cantidad del artículo: "); int cant = sc.nextInt();
                System.out.print("Fecha: "); LocalDateTime fec = LocalDateTime.parse(sc.nextLine());
                System.out.print("Código de Artículo:"); String art = sc.nextLine();
                System.out.print("Email cliente: "); String mail = sc.nextLine();
                // Aquí deberías buscar si el cliente existe en el controlador antes de seguir

                controlador.addPedido(num, cant, LocalDateTime.now(), art, mail);
                System.out.println("Pedido registrado.");
            }
            case 2 -> {
                System.out.print("Número de pedido a eliminar: ");
                String num = sc.nextLine();
                if (controlador.eliminarPedido(num)) System.out.println("Eliminado.");
                else System.out.println("No se pudo eliminar (no existe o ya enviado).");
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
    }
}