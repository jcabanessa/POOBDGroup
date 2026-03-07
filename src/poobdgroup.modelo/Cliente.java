package poobdgroup.modelo;

public abstract class Cliente {
    private String nombre;
    private String domicilio;
    private String nif;
    private String email;

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

    public String getEmail() {
        return email;
    }


    // Métodos abstractos que definen el comportamiento por tipo
    public abstract String tipoCliente();
    public abstract double calcAnual();
    public abstract double descuentoEnvio();

    //toString
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
