package poobdgroup.DAO;

public class DAOFactory {

    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final PedidoDAO pedidoDAO = new PedidoDAO();
    private static final ArticuloDAO articuloDAO = new ArticuloDAO();

    public static ClienteDAO getClienteDAO() {
        return clienteDAO;
    }

    public static PedidoDAO getPedidoDAO() {
        return pedidoDAO;
    }

    public static ArticuloDAO getArticuloDAO() {
        return articuloDAO;
    }
}
