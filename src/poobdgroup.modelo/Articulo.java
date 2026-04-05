package poobdgroup.modelo;

/*public class Articulo {
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


}*/

import jakarta.persistence.*;

@Entity
@Table(name = "articulo")
public class Articulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "codigo", nullable = false, unique = true, length = 30)
    private String codigo;

    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;

    @Column(name = "precioVenta", nullable = false)
    private double precioVenta;

    @Column(name = "gastosEnvio", nullable = false)
    private double gastosEnvio;

    @Column(name = "tiempoPreparacion", nullable = false)
    private int tiempoPreparacion;

    public Articulo() {
    }

    public Articulo(String codigo, String descripcion, double precioVenta, double gastosEnvio, int tiempoPreparacion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precioVenta = precioVenta;
        this.gastosEnvio = gastosEnvio;
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public double getGastosEnvio() {
        return gastosEnvio;
    }

    public void setGastosEnvio(double gastosEnvio) {
        this.gastosEnvio = gastosEnvio;
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public void setTiempoPreparacion(int tiempoPreparacion) {
        this.tiempoPreparacion = tiempoPreparacion;
    }

    @Override
    public String toString() {
        return "Articulo{Codigo='%s', Descripcion='%s', Precio de Venta=%.2f€, Gastos de Envio=%.2f€, Tiempo de Preparacion=%d}"
                .formatted(codigo, descripcion, precioVenta, gastosEnvio, tiempoPreparacion);
    }
}
