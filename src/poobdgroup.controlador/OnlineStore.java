package poobdgroup.controlador;

import poobdgroup.excepciones.TiendaException;
import poobdgroup.modelo.Articulo;
import poobdgroup.modelo.Cliente;
import poobdgroup.modelo.Datos;
import poobdgroup.modelo.Pedido;

import java.time.LocalDateTime;


public class OnlineStore {
    private Datos datos;

    //Constructor
    public OnlineStore(Datos datos) {
        this.datos = (datos != null) ? datos : new Datos();
    }

    public OnlineStore() {
        this(new Datos());
    }

    //Métodos
    public void addCliente(Cliente cli) throws TiendaException {
        if (cli == null) throw new TiendaException("Cliente nulo.");
        if (datos.getClientes().containsKey(cli.getEmail())) {
            throw new TiendaException("Error: El email ya está registrado.");
        }
        datos.getClientes().put(cli.getEmail(), cli);
    }

    public void mostrarClientes(String tipo) throws TiendaException {
        if (datos.getClientes().isEmpty()) {
            throw new TiendaException("Error: No hay clientes registrados en el sistema.");
        } else {
            System.out.println("--- LISTADO DE CLIENTES ---");
            for (Cliente c : datos.getClientes().values()) {
                // Si tipo es "Todos" o coincide con el tipo del cliente, lo muestra
                if (tipo.equalsIgnoreCase("Todos") || c.tipoCliente().equalsIgnoreCase(tipo)) {
                    System.out.println(c + " | Tipo:" + c.tipoCliente());

                }
            }
        }
    }
    public void addArticulo (Articulo art) throws TiendaException {
        if (art == null) throw new TiendaException("Artículo nulo.");
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

    public void addPedido (String numPedido,
                           int cantidad,
                           LocalDateTime fecha,
                           String codArticulo,
                           String emailCliente) throws TiendaException {
        if (numPedido == null || codArticulo == null || emailCliente == null) throw new TiendaException("Parámetros inválidos.");
        if (datos.getPedidos().stream()
                .anyMatch(p -> p.getNumPedido().equals(numPedido)))
            throw new TiendaException("El pedido ya existe");

        Articulo articulo = datos.getArticulos().stream()
                .filter(a -> a.getCodigo().equals(codArticulo))
                .findFirst()
                .orElseThrow(() -> new TiendaException("Artículo no encontrado"));

        Cliente cliente = datos.getClientes().get(emailCliente);
            if (cliente == null) {
                throw new TiendaException("Cliente no encontrado");
            }

        Pedido p = new Pedido(numPedido, cantidad, fecha);

        p.setArticulo(articulo);
        p.setCliente(cliente);

        datos.getPedidos().add(p);

    }

    public boolean eliminarPedido (String numPedido) throws TiendaException {
        Pedido p = datos.getPedidos().stream()
                .filter(pedido -> pedido.getNumPedido().equals(numPedido))
                .findFirst()
                .orElseThrow(() -> new TiendaException("Error: El pedido no existe."));

        if (p.pedidoEnviado()) {
            throw new TiendaException("Error: No se puede eliminar un pedido que ya ha sido enviado.");
        }
        return datos.getPedidos().remove(p);
    }

    public void mostrarPedidosPendientes (String mailOTipo) throws TiendaException {
        System.out.println("=== PEDIDOS PENDIENTES ===");
        boolean found = false;
        for (Pedido p : datos.getPedidos()) {
            if (!p.pedidoEnviado() &&
                    (p.getCliente().getEmail().equalsIgnoreCase(mailOTipo))) {
                if(p.getCliente().tipoCliente().equalsIgnoreCase("Premium")){
                    System.out.println(p + " |CuotaAnual para descuento 20% en envíos= " + p.getCliente().calcAnual());
                }else
                    System.out.println(p);
                found = true;
            }
            else if (!p.pedidoEnviado() &&
                    (mailOTipo.equalsIgnoreCase("Todos") || p.getCliente().tipoCliente().equalsIgnoreCase(mailOTipo))) {
                if(p.getCliente().tipoCliente().equalsIgnoreCase("Premium")){
                    System.out.println(p + " |CuotaAnual para descuento 20% en envíos= " + p.getCliente().calcAnual());
                }else
                    System.out.println(p);
                found = true;
            }
        }
        if (!found) throw new TiendaException("Error: No hay pedidos pendientes.");
    }

    public void mostrarPedidosEnviados (String mailOTipo) throws TiendaException {
        System.out.println("=== PEDIDOS ENVIADOS ===");
        boolean found = false;
        for (Pedido p : datos.getPedidos()) {
            if (p.pedidoEnviado() &&
                    (p.getCliente().getEmail().equalsIgnoreCase(mailOTipo))) {
                if(p.getCliente().tipoCliente().equalsIgnoreCase("Premium")){
                    System.out.println(p + " |CuotaAnual para descuento 20% en envíos= " + p.getCliente().calcAnual());
                }else
                    System.out.println(p);
                found = true;
            }
            else if(p.pedidoEnviado() &&
                    (mailOTipo.equalsIgnoreCase("Todos") || p.getCliente().tipoCliente().equalsIgnoreCase(mailOTipo))) {
                if(p.getCliente().tipoCliente().equalsIgnoreCase("Premium")){
                    System.out.println(p + " |CuotaAnual para descuento 20% en envíos= " + p.getCliente().calcAnual());
                }else
                    System.out.println(p);
                found = true;
            }
        }
        if (!found) throw new TiendaException("Error: No hay pedidos enviados");
    }


    //toString


    @Override
    public String toString() {
        return "OnlineStore{" +
                "articulos=" + datos.getArticulos() +
                ", clientes=" + datos.getClientes().values() +
                ", pedidos=" + datos.getPedidos() +
                '}';
    }
}
