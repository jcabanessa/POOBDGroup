package poobdgroup.excepciones;

// Excepción genérica para la aplicación
public class TiendaException extends Exception {
    public TiendaException(String mensaje) {
        super(mensaje);
    }
}