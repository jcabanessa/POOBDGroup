package poodbgroupModelo;

public abstract class Cliente {
    /*Clientes

Los datos que se almacenarán de cada cliente
serán el nombre, domicilio, nif y email,
siendo el email el identificador del cliente.
Existen dos tipos de clientes:

Estándar. No paga ninguna cuota.
Premium. Paga una cuota anual de 30 euros
 y se le aplica un 20% de descuento en los
 gastos de envío de cada pedido.*/

    private String nombre;
    private String domicilio;
    private String nif;
    private String email;
    private int id;

    //Constructor
    public Cliente(String nombre, String domicilio, String nif, String email) {
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.nif = nif;
        this.email = email;
    }

    //Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public String getNif() { return nif;}

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract String tipoCliente();
    public abstract double calcAnual();
    public abstract double descuentoEnvio();

    @Override
    public String toString() {
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", domicilio='" + domicilio + '\'' +
                ", nif='" + nif + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
