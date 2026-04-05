package poobdgroup.modelo;


/*import java.time.LocalDateTime;

public class Pedido {
    private String numPedido;
    private int cantidad;
    private LocalDateTime fecha;
    private Articulo articulo;
    private Cliente cliente;
    private boolean enviado = false;


    //Constructor
    public Pedido(String numPedido, int cantidad, LocalDateTime fecha) {
        this.numPedido = numPedido;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.enviado = false;
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

    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
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

    //Metodo para cambiar el estado del pedido enviado en memoria a True
    public boolean pedidoEnviado() {
        if (articulo == null || fecha == null) return false;

        boolean porTiempo = fecha.plusMinutes(articulo.getTiempoPreparacion())
                .isBefore(LocalDateTime.now());

        if (porTiempo) {
            enviado = true; // se actualiza en memoria
        }
        return enviado || porTiempo;
    }
    public boolean isEnviado() {
        return enviado;
    }

    @Override
    public String toString() {
        String art = (articulo == null) ? "sin-articulo" : articulo.getCodigo();
        String cli = (cliente == null) ? "sin-cliente" : cliente.getEmail();
        String tipo = (cliente == null) ? "sin-tipo" : cliente.tipoCliente();
        double gEnvio = precioEnvio();
        return "Pedido{numPedido='%s', cantidad=%d, fechaHora=%s, articulo=%s, cliente=%s, tipoCliente=%s, enviado=%s, GastosEnvio=%.2f€, total=%.2f€}".formatted(numPedido, cantidad, fecha, art, cli, tipo, pedidoEnviado(), gEnvio, calcularPrecioTotal());
    }


}*/

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "numPedido", nullable = false, unique = true, length = 30)
    private String numPedido;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_articulo", nullable = false)
    private Articulo articulo;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @Column(name = "enviado", nullable = false)
    private boolean enviado = false;

    public Pedido() {
    }

    public Pedido(String numPedido, int cantidad, LocalDateTime fecha) {
        this.numPedido = numPedido;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.enviado = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(String numPedido) {
        this.numPedido = numPedido;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public boolean isEnviado() {
        return enviado;
    }

    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }

    public double precioEnvio() {
        if (articulo == null || cliente == null) return 0.0;
        return articulo.getGastosEnvio() * (1 - cliente.descuentoEnvio());
    }

    public double calcularPrecioTotal() {
        if (articulo == null) return 0.0;
        return (cantidad * articulo.getPrecioVenta()) + precioEnvio();
    }

    public boolean pedidoEnviado() {
        if (enviado) return true;
        if (articulo == null || fecha == null) return false;
        return fecha.plusMinutes(articulo.getTiempoPreparacion()).isBefore(LocalDateTime.now());
    }

    @Override
    public String toString() {
        String art = (articulo == null) ? "sin-articulo" : articulo.getCodigo();
        String cli = (cliente == null) ? "sin-cliente" : cliente.getEmail();
        String tipo = (cliente == null) ? "sin-tipo" : cliente.tipoCliente();

        return "Pedido{numPedido='%s', cantidad=%d, fechaHora=%s, articulo=%s, cliente=%s, tipoCliente=%s, enviado=%s, GastosEnvio=%.2f€, total=%.2f€}"
                .formatted(numPedido, cantidad, fecha, art, cli, tipo, isEnviado(), precioEnvio(), calcularPrecioTotal());
    }
}



