package poodbgroupControlador;

import poodbgroupExcepciones.TiendaException;
import poodbgroupModelo.Articulo;
import poodbgroupModelo.Cliente;
import poodbgroupModelo.Datos;
import poodbgroupModelo.Pedido;

import java.time.LocalDateTime;

public class OnlineStore {

    private Datos datos;

    public OnlineStore(Datos datos){

        this.datos = (datos != null) ? datos : new Datos();

    }

    public void addCliente(Cliente cli) throws TiendaException{

        if (cli==null) throw new TiendaException("Cliente nulo.");
        if(datos.getClientes().stream().anyMatch(c -> c.getEmail().equals(cli.getEmail()))){

            throw new TiendaException("Error: El mail ya esta registrado.");

        }

        datos.getClientes().add(cli);

    }

    public String mostrarClientes(String tipo) throws TiendaException{

        String mensaje;

        if(datos.getClientes().isEmpty()) throw new TiendaException("No hay clientes en el sistema");
        else {

            mensaje = "**Listado de clientes**";

        }

        for (Cliente c : datos.getClientes()){

            if(tipo.equalsIgnoreCase("todos") || c.tipoCliente().equalsIgnoreCase(tipo)){

                mensaje += "Nombre: " + c.getNombre() + " | Email: " + c.getEmail() + " | Tipo: " + c.tipoCliente() + "\n";

            }

        }
        return mensaje;
    }

    public void addArticulo(Articulo art) throws TiendaException{

        if (art==null) throw new TiendaException("Articulo nulo.");
        if(datos.getArticulos().stream().anyMatch(c -> c.getCodigo().equals(art.getCodigo()))){

            throw new TiendaException("Error: El producto ya existe.");

        }

        datos.getArticulos().add(art);

    }

    public String mostrarArticulo() throws TiendaException {
        String mensaje;
        if(datos.getArticulos().isEmpty()) throw new TiendaException("No hay articulos que mostrar");
        else{

            mensaje = "**Lista de articulos disponibles";

            for (Articulo a : datos.getArticulos()){

                mensaje += a.getCodigo() + " - " + a.getDescripcion() + "\n";

            }

        }

        return mensaje;
    }

    public void addPedido(int numPedido, int cantidad, LocalDateTime fecha, String codArticulo, String emailCliente) throws TiendaException{

        if (numPedido <= 0 || codArticulo ==null || emailCliente == null) throw new TiendaException("Parametros invalidos");

        if(datos.getPedidos().stream().anyMatch(p-> p.getNumPedido() == numPedido)) throw new TiendaException("El pedido ya existe");

        Articulo articulo = datos.getArticulos().stream().filter(a -> a.getCodigo().equals(codArticulo)).findFirst().orElseThrow( () -> new TiendaException("Articulo no encontrado"));
        Cliente cliente = datos.getClientes().stream().filter(a -> a.getEmail().equals(emailCliente)).findFirst().orElseThrow( () -> new TiendaException("Cliente no encontrado"));

        Pedido p = new Pedido(numPedido, cantidad, fecha);

        p.setArticulo(articulo);
        p.setCliente(cliente);

        datos.getPedidos().add(p);

    }

    public boolean eliminarPedido(int numPedido) throws TiendaException{

        Pedido p = datos.getPedidos().stream().filter(ped -> ped.getNumPedido() == numPedido).findFirst().orElseThrow(()-> new TiendaException("el pedido no existe"));

        if(p.pedidoEnviado()){

            throw new TiendaException("No se puede eliminar porque ya ha sido enviado");

        }

        return datos.getPedidos().remove(p);

    }

    public String mostrarPedidosPendientes(String mailOTipo) throws TiendaException {
        String mensaje = "** Pedidos pendientes **\n";
        boolean found = false;

        for (Pedido p : datos.getPedidos()) {
            if (!p.pedidoEnviado()) {
                // Verificar si coincide con el filtro
                boolean coincide = false;

                if (mailOTipo.equalsIgnoreCase("todos")) {
                    coincide = true;
                } else if (p.getCliente().getEmail().equalsIgnoreCase(mailOTipo)) {
                    coincide = true;
                } else if (p.getCliente().tipoCliente().equalsIgnoreCase(mailOTipo)) {
                    coincide = true;
                }

                if (coincide) {
                    found = true;
                    mensaje += "Pedido Nº: " + p.getNumPedido() + " | Cliente: " + p.getCliente().getNombre() +
                            " | Email: " + p.getCliente().getEmail();

                    if (p.getCliente().tipoCliente().equalsIgnoreCase("premium")) {
                        mensaje += " | Cuota anual: " + p.getCliente().calcAnual() + "€";
                    }
                    mensaje += "\n";
                }
            }
        }

        if (!found) {
            throw new TiendaException("No hay pedidos pendientes");
        }
        return mensaje;
    }

    public String mostrarPedidosEnviados(String mailOTipo) throws TiendaException {
        String mensaje = "** Pedidos enviados **\n";
        boolean found = false;

        for (Pedido p : datos.getPedidos()) {
            if (p.pedidoEnviado()) {
                // Verificar si coincide con el filtro
                boolean coincide = false;

                if (mailOTipo.equalsIgnoreCase("todos")) {
                    coincide = true;
                } else if (p.getCliente().getEmail().equalsIgnoreCase(mailOTipo)) {
                    coincide = true;
                } else if (p.getCliente().tipoCliente().equalsIgnoreCase(mailOTipo)) {
                    coincide = true;
                }

                if (coincide) {
                    found = true;
                    mensaje += "Pedido Nº: " + p.getNumPedido() + " | Cliente: " + p.getCliente().getNombre() +
                            " | Email: " + p.getCliente().getEmail();

                    if (p.getCliente().tipoCliente().equalsIgnoreCase("premium")) {
                        mensaje += " | Cuota anual: " + p.getCliente().calcAnual() + "€";
                    }
                    mensaje += "\n";
                }
            }
        }

        if (!found) {
            throw new TiendaException("No hay pedidos enviados");
        }
        return mensaje;
    }

}


