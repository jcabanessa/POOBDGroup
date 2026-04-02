package poodbgroupModelo;

import java.util.ArrayList;

public class Datos {

    private ArrayList<Articulo> articulos= new ArrayList<>();
    private ArrayList<Cliente> clientes = new ArrayList<>();
    private ArrayList<Pedido> pedidos = new ArrayList<>();

    public Datos(ArrayList<Articulo> articulos, ArrayList<Cliente> clientes, ArrayList<Pedido> pedidos){

        this.articulos = (articulos != null) ? articulos : new ArrayList<>();
        this.clientes = (clientes != null) ? clientes : new ArrayList<>();
        this.pedidos = (pedidos != null) ? pedidos : new ArrayList<>();

    }

    public Datos() {

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
