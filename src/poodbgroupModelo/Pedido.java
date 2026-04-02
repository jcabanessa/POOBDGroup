package poodbgroupModelo;

import java.time.LocalDateTime;

public class Pedido {

    /*Pedidos

Para simplificar la aplicación se considerará que cada pedido
 solo puede contener un artículo, aunque se puedan pedir varias unidades de este.

Los datos necesarios para cada pedido son el número de pedido,
 cliente, el artículo, la cantidad de unidades del artículo
  y la fecha y hora del pedido.

Un pedido no se puede ser cancelado una vez transcurrido
 el tiempo de preparación para el envío del articulo a
 partir de la fecha del pedido.

Para calcular el precio del pedido hay que tener en cuenta
 el precio de venta, las unidades pedidas,
  el coste del envío y si el cliente que lo ha realizado es premium.*/

    private int numPedido;
    private Cliente cliente;
    private Articulo articulo;
    private int cantidad;
    private LocalDateTime fecha;

    //Constructor
    public Pedido(int numPedido, int cantidad, LocalDateTime fecha) {
        this.numPedido = numPedido;
        this.cantidad = cantidad;
        this.fecha = fecha;

    }

    //Getters y Setters
    public int getNumPedido() {
        return numPedido;
    }
    public int getCantidad(){ return cantidad; }
    public LocalDateTime getFecha(){ return fecha; }
    public Cliente getCliente() { return cliente; }
    public Articulo getArticulo() { return articulo; }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    //Métodos
    public double precioEnvio() {
        if(articulo == null) return 0.0;
        float costeBase = (float) articulo.getGastoEnvio();
        return costeBase * (1 - cliente.descuentoEnvio());
    }

    public double calcularPrecioTotal() {
        if (articulo == null) return 0.0;
        return (cantidad * articulo.getPrecioVenta()) + precioEnvio();
    }

    public boolean pedidoEnviado() {
        // Lógica: si han pasado más de 'n' minutos desde la preparación, se considera enviado
        if (articulo == null || fecha == null) return false;
        return LocalDateTime.now().isAfter(fecha.plusMinutes(articulo.getTiempoPreparacion()));

    }

    @Override
    public String toString() {
        String art = (articulo == null) ? "sin-articulo" : articulo.getCodigo();
        String cli = (cliente == null) ? "sin-cliente" : cliente.getEmail();
        String tipo = getCliente().tipoCliente();
        double gEnvio = precioEnvio();
        return "Pedido{numPedido='%s', cantidad=%d, fechaHora=%s, articulo=%s, cliente=%s, tipoCliente=%s, enviado=%s, GastosEnvio=%.2f€, total=%.2f€}".formatted(numPedido, cantidad, fecha, art, cli, tipo, pedidoEnviado(), gEnvio, calcularPrecioTotal());
    }
}
