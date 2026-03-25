package poobdgroup.modelo;

public class Articulo {
    private int id;//Se añade id para que coincida con la BD
    private String codigo;
    private String descripcion;
    private double precioVenta;
    private double gastosEnvio;
    private int tiempoPreparacion;

    //Constructor
    public Articulo(String codigo, String descripcion, double precioVenta, double gastosEnvio, int tiempoPreparacion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precioVenta = precioVenta;
        this.gastosEnvio = gastosEnvio;
        this.tiempoPreparacion = tiempoPreparacion;
    }

    //Getters y Setters

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public double getGastosEnvio() {
        return gastosEnvio;
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }



    //toString
    @Override
    public String toString() {
        return "Articulo{Codigo='%s', Descripcion='%s', Precio de Venta=%.2f€, Gastos de Envio=%.2f€, Tiempo de Preparacion=%d}".formatted(codigo, descripcion, precioVenta, gastosEnvio, tiempoPreparacion);
    }


}
