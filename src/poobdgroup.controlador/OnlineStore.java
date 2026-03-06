package poobdgroup.controlador;

import poobdgroup.excepciones.TiendaException;
import poobdgroup.modelo.Articulo;
import poobdgroup.modelo.Cliente;
import poobdgroup.modelo.Datos;
import poobdgroup.modelo.Pedido;

import java.util.ArrayList;

public class OnlineStore {
    private Datos datos;

    //Constructor


    public OnlineStore(Datos datos) {
        this.datos = datos;
    }

    //Métodos
    public void addCliente(Cliente cli) throws TiendaException {
        if (datos.getClientes().stream().anyMatch(c -> c.getEmail().equals(cli.getEmail()))) {
            throw new TiendaException("Error: El email ya está registrado.");
        }
        datos.getClientes().add(cli);
    }

    public void mostrarClientes(String tipo) throws TiendaException {
        if (datos.getClientes().isEmpty()) {
            throw new TiendaException("Error: No hay clientes registrados en el sistema.");
        } else {
            System.out.println("--- LISTADO DE CLIENTES ---");
            for (Cliente c : datos.getClientes()) {
                // Si tipo es "Todos" o coincide con el tipo del cliente, lo muestra
                if (tipo.equals("Todos") || c.tipoCliente().equalsIgnoreCase(tipo)) {
                    System.out.println(c);

                }
            }
        }
    }
        public void addArticulo (Articulo art) throws TiendaException {
            if (datos.getArticulos().stream().anyMatch(a -> a.getCodigo().equals(art.getCodigo()))) {
                throw new TiendaException("Error: Ya existe un artículo con ese código.");
            }
            datos.getArticulos().add(art);
        }

        public void mostrarArticulos () throws TiendaException {
            if (datos.getArticulos().isEmpty()) {
                throw new TiendaException("Error: No hay articulos que mostrar.");
            } else {
                System.out.println("--- LISTADO DE ARTICULOS ---");
                for (Articulo a : datos.getArticulos()) {
                    System.out.println(a.toString());
                }
            }
        }

        public void addPedido (Pedido ped) throws TiendaException {
            if (datos.getPedidos().stream().anyMatch(p -> p.getNumPedido().equals(ped.getNumPedido()))) {
                throw new TiendaException("Error: Ya existe un pedido con ese número.");
            }
            datos.getPedidos().add(ped);

        }

        public void eliminarPedido (String numPedido) throws TiendaException {
            Pedido p = datos.getPedidos().stream()
                    .filter(pedido -> pedido.getNumPedido().equals(numPedido))
                    .findFirst()
                    .orElseThrow(() -> new TiendaException("Error: El pedido no existe."));

            if (p.pedidoEnviado()) {
                throw new TiendaException("Error: No se puede eliminar un pedido que ya ha sido enviado.");
            }
            datos.getPedidos().remove(p);
        }

        public void mostrarPedidosPendientes () throws TiendaException {
            System.out.println("=== PEDIDOS PENDIENTES ===");
            boolean found = false;
            for (Pedido p : datos.getPedidos()) {
                if (!p.pedidoEnviado()) {
                    System.out.println(p);
                    found = true;
                }
            }
            if (!found) throw new TiendaException("Error: No hay pedidos pendientes.");
        }

        public void mostrarPedidosEnviados () throws TiendaException {
            System.out.println("=== PEDIDOS ENVIADOS ===");
            boolean found = false;
            for (Pedido p : datos.getPedidos()) {
                if (p.pedidoEnviado())
                    System.out.println(p);
                found = true;
            }
            if (!found) throw new TiendaException("Error: No hay pedidos enviados");
        }


        //toString
        @Override
        public String toString () {
            return "OnlineStore{" +
                    "articulos=" + datos.getArticulos() +
                    ", clientes=" + datos.getClientes() +
                    ", pedidos=" + datos.getPedidos() +
                    '}';
        }
    }
