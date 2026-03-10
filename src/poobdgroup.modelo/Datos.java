package poobdgroup.modelo;

import java.util.ArrayList;
import java.util.HashMap;

public class Datos {
    private ArrayList<Articulo> articulos = new ArrayList<Articulo>();
    private HashMap<String, Cliente> clientes = new HashMap<>();
    private ArrayList<Pedido> pedidos = new ArrayList<Pedido>();


    public Datos() {}


    public Datos(ArrayList<Articulo> articulos, HashMap<String, Cliente> clientes, ArrayList<Pedido> pedidos) {
        this.articulos = (articulos != null) ? articulos : new ArrayList<>();
        this.clientes = (clientes != null) ? clientes : new HashMap<>();
        this.pedidos = (pedidos != null) ? pedidos : new ArrayList<>();
    }

    //Getters y Setters
    public ArrayList<Articulo> getArticulos() {
        return articulos;
    }

    public HashMap<String, Cliente> getClientes() {
        return clientes;
    }

    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }
}









