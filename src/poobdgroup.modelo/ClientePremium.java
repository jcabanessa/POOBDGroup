package poobdgroup.modelo;

public class ClientePremium extends Cliente{

    public ClientePremium(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
    }

    @Override public String tipoCliente() { return "Premium"; }
    @Override public float calcAnual() { return 30.0f; }
    @Override public float descuentoEnvio() { return 0.20f; } // 20%
}
