package poobdgroup.dao;

// Factory: centraliza la creación de los DAOs
public class DAOFactory {

    public static ArticuloDAO getArticuloDAO() {
        return new ArticuloDAO();
    }

    public static ClienteDAO getClienteDAO() {
        return new ClienteDAO();
    }

    public static PedidoDAO getPedidoDAO() {
        return new PedidoDAO();
    }
}
