package poobdgroup.modelo;


import java.time.LocalDateTime;

public class Pedido {
    private String numPedido;
    private int cantidad;
    private LocalDateTime fecha;
    private Articulo articulo;
    private Cliente cliente;


    //Constructor
    public Pedido(String numPedido, int cantidad, LocalDateTime fecha) {
        this.numPedido = numPedido;
        this.cantidad = cantidad;
        this.fecha = fecha;

    }

    //Getters y Setters
    public String getNumPedido() {
        return numPedido;
    }
    public Cliente getCliente() { return cliente; }

    public int getCantidad() {
        return cantidad;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    //Métodos
    public double precioEnvio() {
        if(articulo == null) return 0.0;
        float costeBase = (float) articulo.getGastosEnvio();
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



