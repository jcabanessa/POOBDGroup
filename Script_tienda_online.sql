SET GLOBAL validate_password.policy=LOW;
SET GLOBAL validate_password.length=7; 
ALTER USER 'root'@'localhost' IDENTIFIED BY 'Adm1234';
FLUSH PRIVILEGES;


create table if not exists cliente(
id int auto_increment primary key, -- se han creado IDs siguiendo la rubrica de la evaluación 
nombre varchar(50) not null, -- todos los campos son not null porque no tenemos constructores con sobrecarga
domicilio varchar(100) not null,
nif varchar(10) not null,
email varchar(30) unique not null, -- tenemos la restriccion para la unicidad de los mails
tipo enum('Estandar', 'Premium') not null -- tenemos los dos tipos de cliente como una opcion de la misma tabla por eficiencia
); 

create table if not exists articulo(
id int auto_increment primary key,
codigo varchar(30) unique not null,
descripcion varchar(100) not null,
precioVenta double(10,2) not null,
gastosEnvio double(10,2) not null,
tiempoPreparacion int not null
);

create table if not exists pedido(
id int auto_increment primary key,
numPedido varchar(30) unique not null,
cantidad int not null,
fecha datetime not null,
id_articulo int not null,
id_cliente int not null,
foreign key (id_articulo) references articulo(id),
foreign key (id_cliente) references cliente(id)
);

/*Stored Procedures*/
DELIMITER $$

CREATE PROCEDURE insertar_pedido(
    IN p_numPedido VARCHAR(20),
    IN p_cantidad INT,
    IN p_fecha DATETIME,
    IN p_id_articulo INT,
    IN p_id_cliente INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
    END;

    START TRANSACTION;

    INSERT INTO pedido (numPedido, cantidad, fecha, id_articulo, id_cliente)
    VALUES (p_numPedido, p_cantidad, p_fecha, p_id_articulo, p_id_cliente);

    COMMIT;
END$$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE insertar_cliente(
    IN p_nombre VARCHAR(100),
    IN p_domicilio VARCHAR(150),
    IN p_nif VARCHAR(20),
    IN p_email VARCHAR(100),
    IN p_tipo VARCHAR(20)
)
BEGIN
    START TRANSACTION;

    INSERT INTO cliente (nombre, domicilio, nif, email, tipo)
    VALUES (p_nombre, p_domicilio, p_nif, p_email, p_tipo);

    COMMIT;
END$$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE insertar_articulo(
    IN p_codigo VARCHAR(20),
    IN p_descripcion VARCHAR(150),
    IN p_precio DOUBLE,
    IN p_envio DOUBLE,
    IN p_tiempo INT
)
BEGIN
    START TRANSACTION;

    INSERT INTO articulo (codigo, descripcion, precio, gastos_envio, tiempo_preparacion)
    VALUES (p_codigo, p_descripcion, p_precio, p_envio, p_tiempo);

    COMMIT;
END$$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE eliminar_pedido(
    IN p_numPedido VARCHAR(20)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
    END;

    START TRANSACTION;

    DELETE FROM pedido WHERE numPedido = p_numPedido;

    COMMIT;
END$$

DELIMITER ;

ALTER TABLE pedido ADD enviado BOOLEAN DEFAULT FALSE;

-- Stored Procedure Update pedidos enviados
DELIMITER $$

CREATE PROCEDURE enviar_pedido(IN p_numPedido VARCHAR(20))
BEGIN
    UPDATE pedido 
    SET enviado = TRUE
    WHERE numPedido = p_numPedido;
END $$

DELIMITER ;
