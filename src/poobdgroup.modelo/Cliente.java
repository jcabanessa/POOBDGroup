package poobdgroup.modelo;

public abstract class Cliente {
    private int id; //Se añade id para que coincida con BD
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public String getNif() {
        return nif;
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
