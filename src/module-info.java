module poobdgroup {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    opens poobdgroup.vista to javafx.fxml;
    opens poobdgroup.modelo to org.hibernate.orm.core, javafx.base;

    exports poobdgroup.vista;
    exports poobdgroup.controlador;
    exports main;
}