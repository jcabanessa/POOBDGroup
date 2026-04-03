package poodbgroupModelo;

public class Articulo {

    /*Artículos

Los datos básicos de los artículos son un código alfanumérico,
 una descripción y el precio de venta, gastos de envío
  y un tiempo de preparación para el envío representado en minutos.*/

    private String codigo;
    private String descripcion;
    private double precioVenta;
    private double gastoEnvio;
    private int tiempoPreparacion;
    private int id;

    public Articulo(String codigo, String descripcion, double precioVenta, double gastoEnvio, int tiempoPreparacion){

        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precioVenta = precioVenta;
        this.gastoEnvio = gastoEnvio;
        this.tiempoPreparacion = tiempoPreparacion;

    }

    public String getCodigo(){

        return codigo;

    }

    public void setCodigo(String codigo){

        this.codigo = codigo;

    }

    public String getDescripcion(){

        return descripcion;

    }

    public void setDescripcion(String descripcion){

        this.descripcion = descripcion;

    }

    public double getPrecioVenta(){

        return precioVenta;

    }

    public void setPrecioVenta(double precioVenta){

        this.precioVenta = precioVenta;

    }

    public double getGastoEnvio(){

        return gastoEnvio;

    }

    public void setGastoEnvio(double gastoEnvio){

        this.gastoEnvio = gastoEnvio;

    }

    public int getTiempoPreparacion(){

        return tiempoPreparacion;

    }

    public void setTiempoPreparacion(int tiempoPreparacion){

        this.tiempoPreparacion = tiempoPreparacion;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Articulo{" +
                "codigo='" + codigo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precioVenta=" + precioVenta +
                ", gastoEnvio=" + gastoEnvio +
                ", tiempoPreparacion=" + tiempoPreparacion +
                '}';
    }
}
