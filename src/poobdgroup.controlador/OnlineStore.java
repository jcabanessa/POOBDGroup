package poobdgroup.controlador;

import poobdgroup.modelo.*;
import poobdgroup.excepciones.TiendaException;
import java.util.ArrayList;

public class OnlineStore {
    private ArrayList<Articulo> articulos = new ArrayList<Articulo>();
    private ArrayList<Cliente> clientes = new ArrayList<Cliente>();
    private ArrayList<Pedido> pedidos = new ArrayList<Pedido>();

    //Constructor
    public OnlineStore(ArrayList<Articulo> articulos, ArrayList<Pedido> pedidos, ArrayList<Cliente> clientes) {
        this.articulos = articulos;
        this.pedidos = pedidos;
        this.clientes = clientes;
    }

    //Getters y Setters
    public ArrayList<Articulo> getArticulos() {
        return articulos;
    }

    public ArrayList<Cliente> getClientes() {
        return clientes;
    }

    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }


    //Métodos
    public void addCliente(Cliente cli) throws TiendaException {
        if (clientes.stream().anyMatch(c -> c.getEmail().equals(cli.getEmail()))) {
            throw new TiendaException("Error: El email ya está registrado.");
        }
        clientes.add(cli);
    }

    public void mostrarClientes() throws TiendaException {
        if (clientes.isEmpty()){
            throw new TiendaException("Error: No hay clientes registrados en el sistema.")
        } else {
            System.out.println("--- LISTADO DE CLIENTES ---")
            for (Cliente c: clientes){
                System.out.println(c.toString + " | Tipo: " + cliente.tipoCliente())
            }
        }
    }

    public void addArticulo(Articulo art) throws TiendaException {
        if (articulos.stream().anyMatch(a -> a.getCodigo().equals(art.getCodigo()))) {
            throw new TiendaException("Error: Ya existe un artículo con ese código.");
        }
        articulos.add(art);
    }

    public void mostrarArticulos() throws TiendaException {
        if (articulos.isEmpty()){
            throw new TiendaException("Error: No hay articulos que mostrar.");
        } else {
            System.out.println("--- LISTADO DE ARTICULOS ---")
            for (Articulo a: articulos){
                System.out.println(a.toString)
            }
        }
    }

    public void addPedido(Pedido ped) throws TiendaException{
        if (pedidos.stream().anyMatch(p -> p.getNumPedido().equals(ped.getNumPedido()))) {
            throw new TiendaException("Error: Ya existe un pedido con ese número.")
        }
        pedidos.add(ped);

    }

    public void eliminarPedido(String numPedido) throws TiendaException {
        Pedido p = pedidos.stream()
                .filter(pedido -> pedido.getNumPedido().equals(numPedido))
                .findFirst()
                .orElseThrow(() -> new TiendaException("Error: El pedido no existe."));

        if (p.pedidoEnviado()) {
            throw new TiendaException("Error: No se puede eliminar un pedido que ya ha sido enviado.");
        }
        pedidos.remove(p);
    }

    public void mostrarPedidosPendientes() throws TiendaException {
        System.out.println("=== PEDIDOS PENDIENTES ===");
        boolean found = false;
        for (Pedido p : pedidos){
            if (!p.pedidoEnviado()){
                System.out.println(p);
                found = true;
            }
        }
        if (!found) throw new TinedaException("Error: No hay pedidos pendientes.");
    }

    public void mostrarPedidosEnviados() throws TiendaException {
        System.out.println("=== PEDIDOS ENVIADOS ===");
        boolean found = false;
        for (Pedido p : pedidos){
            if(p.pedidoEnviado())
                System.out.println(p);
                found = true;
        }
        if (!found) throw new TinedaException("Error: No hay pedidos enviados");
    }


    //toString
    @Override
    public String toString() {
        return "OnlineStore{" +
                "articulos=" + articulos +
                ", clientes=" + clientes +
                ", pedidos=" + pedidos +
                '}';
    }
}
