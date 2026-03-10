package poobdgroup.modelo;



public class Datos {

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









