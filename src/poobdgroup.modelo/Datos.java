package poobdgroup.modelo;

import java.util.ArrayList;

public class Datos {
    private ArrayList<Articulo> articulos = new ArrayList<Articulo>();
    private ArrayList<Cliente> clientes = new ArrayList<Cliente>();
    private ArrayList<Pedido> pedidos = new ArrayList<Pedido>();


    public Datos(ArrayList<Articulo> articulos, ArrayList<Cliente> clientes, ArrayList<Pedido> pedidos) {
        this.articulos = articulos;
        this.clientes = clientes;
        this.pedidos = pedidos;
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
}









