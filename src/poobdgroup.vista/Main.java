package poobdgroup.vista;


// [MermaidChart: 05d3e7bd-4e9f-42f9-b47c-a7cc5e8fc8cf]

import poobdgroup.controlador.OnlineStore;
import poobdgroup.modelo.*;
import poobdgroup.excepciones.TiendaException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Main prg = new Main();
        prg.inicio();
    }

    void inicio(){
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

    void gestionArticulos(){
        System.out.println("\n1. Añadir Artículo\n2. Mostrar Todos los Artículos\n0. Volver");
        int op = sc.nextInt();
        sc.nextLine(); // Limpiar buffer

        switch (op) {
            case 1 -> controlador.addArticulo();
            case 2 -> controlador.mostrarClientes();
        }
    }

    void gestionClientes(){
        System.out.println("\n1. Añadir Cliente\n2. Mostrar Todos los Clientes\n3. Mostrar Clientes Estandar\n4. Mostrar Clientes Premium\n0. Volver");
        int op = sc.nextInt();
        sc.nextLine(); // Limpiar buffer

        switch (op) {
            case 1 -> controlador.addCliente();
            case 2 -> controlador.mostrarClientes();
            case 3 -> controlador.mostrarClientes().tipoCliente("Estandar");
            case 4 -> controlador.mostrarClientes().tipoCliente("Premium");
        }
    }

    void gestionPedidos(){

    }
}