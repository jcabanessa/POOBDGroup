package poobdgroup.modelo;

public class ClienteEstandar extends Cliente{

    public ClienteEstandar(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
    }


    @Override public String tipoCliente() { return "Estandar"; }
    @Override public float calcAnual() { return 0; }
    @Override public float descuentoEnvio() { return 0; }
}
