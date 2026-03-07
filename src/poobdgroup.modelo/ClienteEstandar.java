package poobdgroup.modelo;

public class ClienteEstandar extends Cliente{

    public ClienteEstandar(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
    }


    @Override public String tipoCliente() { return "Estandar"; }
    @Override public double calcAnual() { return 0; }
    @Override public double descuentoEnvio() { return 0; }
}
