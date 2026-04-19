package poobdgroup.modelo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Premium")
public class ClientePremium extends Cliente {

    public ClientePremium() {
    }

    public ClientePremium(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
    }

    @Override
    public String tipoCliente() {
        return "Premium";
    }

    @Override
    public double calcAnual() {
        return 30.0;
    }

    @Override
    public double descuentoEnvio() {
        return 0.20;
    }
}
