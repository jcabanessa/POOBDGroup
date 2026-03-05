package poobdgroup.modelo;


import java.time.LocalDateTime;

public class Pedido {
    private String numPedido;
    private int cantidad;
    private LocalDateTime fecha;
    private Cliente cliente;
    private Articulo articulo;

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


    //Métodos
    public float precioEnvio() {
        float costeBase = (float) articulo.getGastosEnvio();
        return costeBase * (1 - cliente.descuentoEnvio());
    }

    public double calcularPrecioTotal() {
        return (cantidad * articulo.getPrecioVenta()) + precioEnvio();
    }

    public boolean pedidoEnviado() {
        // Lógica: si han pasado más de 'n' minutos desde la preparación, se considera enviado
        return LocalDateTime.now().isAfter(fecha.plusMinutes(articulo.getTiempoPreparacion()));

    //toString
    @Override
    public String toString() {
        return "Pedido{" +
                "numPedido='" + numPedido + '\'' +
                ", cantidad=" + cantidad +
                ", fecha=" + fecha +
                '}';
    }
}


