package poodbgroupControlador;

import poodbgroupDAO.DAOFactory;
import poodbgroupExcepciones.TiendaException;
import poodbgroupModelo.Articulo;
import poodbgroupModelo.Cliente;
import poodbgroupModelo.Pedido;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class OnlineStore {

    public OnlineStore() {
    }

    public void addCliente(Cliente cli) throws TiendaException {
        if (cli == null) throw new TiendaException("Cliente nulo.");

        try {
            Cliente existente = DAOFactory.getClienteDAO().buscarPorEmail(cli.getEmail());
            if (existente != null) {
                throw new TiendaException("Error: El mail ya esta registrado.");
            }
            DAOFactory.getClienteDAO().guardarCliente(cli);
        } catch (SQLException e) {
            throw new TiendaException("Error al guardar cliente: " + e.getMessage());
        }
    }

    public String mostrarClientes(String tipo) throws TiendaException {
        try {
            ArrayList<Cliente> clientes = DAOFactory.getClienteDAO().obtenerClientes();

            if (clientes.isEmpty()) {
                throw new TiendaException("No hay clientes en el sistema");
            }

            StringBuilder mensaje = new StringBuilder("**Listado de clientes**\n");

            for (Cliente c : clientes) {
                if (tipo.equalsIgnoreCase("todos") || c.tipoCliente().equalsIgnoreCase(tipo)) {
                    mensaje.append("Nombre: ").append(c.getNombre())
                            .append(" | Email: ").append(c.getEmail())
                            .append(" | Tipo: ").append(c.tipoCliente()).append("\n");
                }
            }
            return mensaje.toString();
        } catch (SQLException e) {
            throw new TiendaException("Error al obtener clientes: " + e.getMessage());
        }
    }

    public void addArticulo(Articulo art) throws TiendaException {
        if (art == null) throw new TiendaException("Articulo nulo.");

        try {
            Articulo existente = DAOFactory.getArticuloDAO().buscarPorCodigo(art.getCodigo());
            if (existente != null) {
                throw new TiendaException("Error: El producto ya existe.");
            }
            DAOFactory.getArticuloDAO().guardarArticulo(art);
        } catch (SQLException e) {
            throw new TiendaException("Error al guardar artículo: " + e.getMessage());
        }
    }

    public String mostrarArticulo() throws TiendaException {
        try {
            ArrayList<Articulo> articulos = DAOFactory.getArticuloDAO().obtenerArticulos();

            if (articulos.isEmpty()) {
                throw new TiendaException("No hay articulos que mostrar");
            }

            StringBuilder mensaje = new StringBuilder("**Lista de articulos disponibles**\n");
            for (Articulo a : articulos) {
                mensaje.append(a.getCodigo()).append(" - ").append(a.getDescripcion()).append("\n");
            }
            return mensaje.toString();
        } catch (SQLException e) {
            throw new TiendaException("Error al obtener artículos: " + e.getMessage());
        }
    }

    public void addPedido(int cantidad, LocalDateTime fecha, String codArticulo, String emailCliente) throws TiendaException {
        if (codArticulo == null || emailCliente == null) {
            throw new TiendaException("Parametros invalidos");
        }

        try {
            Articulo articulo = DAOFactory.getArticuloDAO().buscarPorCodigo(codArticulo);
            if (articulo == null) {
                throw new TiendaException("Articulo no encontrado");
            }

            Cliente cliente = DAOFactory.getClienteDAO().buscarPorEmail(emailCliente);
            if (cliente == null) {
                throw new TiendaException("Cliente no encontrado");
            }

            Pedido p = new Pedido(cantidad, fecha);
            p.setArticulo(articulo);
            p.setCliente(cliente);

            DAOFactory.getPedidoDAO().guardarPedido(p);
        } catch (SQLException e) {
            throw new TiendaException("Error al guardar pedido: " + e.getMessage());
        }
    }

    public boolean eliminarPedido(int numPedido) throws TiendaException {
        try {
            ArrayList<Pedido> pedidos = DAOFactory.getPedidoDAO().obtenerPedidos();
            Pedido p = pedidos.stream()
                    .filter(ped -> ped.getNumPedido() == numPedido)
                    .findFirst()
                    .orElseThrow(() -> new TiendaException("El pedido no existe"));

            if (p.pedidoEnviado()) {
                throw new TiendaException("No se puede eliminar porque ya ha sido enviado");
            }

            DAOFactory.getPedidoDAO().eliminarPedido(numPedido);
            return true;
        } catch (SQLException e) {
            throw new TiendaException("Error al eliminar pedido: " + e.getMessage());
        }
    }

    public String mostrarPedidosPendientes(String mailOTipo) throws TiendaException {
        try {
            ArrayList<Pedido> pedidos = DAOFactory.getPedidoDAO().obtenerPedidos();
            StringBuilder mensaje = new StringBuilder("** Pedidos pendientes **\n");
            boolean found = false;

            for (Pedido p : pedidos) {
                if (!p.pedidoEnviado()) {
                    boolean coincide = mailOTipo.equalsIgnoreCase("todos") ||
                            p.getCliente().getEmail().equalsIgnoreCase(mailOTipo) ||
                            p.getCliente().tipoCliente().equalsIgnoreCase(mailOTipo);

                    if (coincide) {
                        found = true;
                        mensaje.append("Pedido Nº: ").append(p.getNumPedido())
                                .append(" | Cliente: ").append(p.getCliente().getNombre())
                                .append(" | Email: ").append(p.getCliente().getEmail());

                        if (p.getCliente().tipoCliente().equalsIgnoreCase("premium")) {
                            mensaje.append(" | Cuota anual: ").append(p.getCliente().calcAnual()).append("€");
                        }
                        mensaje.append("\n");
                    }
                }
            }

            if (!found) {
                throw new TiendaException("No hay pedidos pendientes");
            }
            return mensaje.toString();
        } catch (SQLException e) {
            throw new TiendaException("Error al obtener pedidos: " + e.getMessage());
        }
    }

    public String mostrarPedidosEnviados(String mailOTipo) throws TiendaException {
        try {
            ArrayList<Pedido> pedidos = DAOFactory.getPedidoDAO().obtenerPedidos();
            StringBuilder mensaje = new StringBuilder("** Pedidos enviados **\n");
            boolean found = false;

            for (Pedido p : pedidos) {
                if (p.pedidoEnviado()) {
                    boolean coincide = mailOTipo.equalsIgnoreCase("todos") ||
                            p.getCliente().getEmail().equalsIgnoreCase(mailOTipo) ||
                            p.getCliente().tipoCliente().equalsIgnoreCase(mailOTipo);

                    if (coincide) {
                        found = true;
                        mensaje.append("Pedido Nº: ").append(p.getNumPedido())
                                .append(" | Cliente: ").append(p.getCliente().getNombre())
                                .append(" | Email: ").append(p.getCliente().getEmail());

                        if (p.getCliente().tipoCliente().equalsIgnoreCase("premium")) {
                            mensaje.append(" | Cuota anual: ").append(p.getCliente().calcAnual()).append("€");
                        }
                        mensaje.append("\n");
                    }
                }
            }

            if (!found) {
                throw new TiendaException("No hay pedidos enviados");
            }
            return mensaje.toString();
        } catch (SQLException e) {
            throw new TiendaException("Error al obtener pedidos: " + e.getMessage());
        }
    }
}