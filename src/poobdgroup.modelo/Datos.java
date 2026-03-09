package poobdgroup.modelo;

import java.util.ArrayList;

public class Datos {
    /*private ArrayList<Articulo> articulos = new ArrayList<Articulo>();
    private ArrayList<Cliente> clientes = new ArrayList<Cliente>();
    private ArrayList<Pedido> pedidos = new ArrayList<Pedido>();


    public Datos() {}


    public Datos(ArrayList<Articulo> articulos, ArrayList<Cliente> clientes, ArrayList<Pedido> pedidos) {
        this.articulos = (articulos != null) ? articulos : new ArrayList<>();
        this.clientes = (clientes != null) ? clientes : new ArrayList<>();
        this.pedidos = (pedidos != null) ? pedidos : new ArrayList<>();
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
    }*/
    private Repositorio<Articulo> articulos;
    private Repositorio<Cliente> clientes;
    private Repositorio<Pedido> pedidos;


    public Datos(Repositorio<Pedido> pedidos, Repositorio<Cliente> clientes, Repositorio<Articulo> articulos) {
        this.articulos = (articulos != null) ? articulos : new Repositorio<>();
        this.clientes = (clientes != null) ? clientes : new Repositorio<>();
        this.pedidos = (pedidos != null) ? pedidos : new Repositorio<>();
    }

    public Datos() {
        clientes = new Repositorio<>();
        pedidos = new Repositorio<>();
        articulos = new Repositorio<>();
    }

    public Repositorio<Articulo> getArticulos() {
        return articulos;
    }

    public Repositorio<Cliente> getClientes() {
        return clientes;
    }

    public Repositorio<Pedido> getPedidos() {
        return pedidos;
    }
}









