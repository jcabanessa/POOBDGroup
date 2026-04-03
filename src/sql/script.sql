-- crear base de datos
create database if not exists tienda_online;
use tienda_online;

-- tabla cliente
create table if not exists cliente (
    id int auto_increment primary key,
    nombre varchar(50) not null,
    domicilio varchar(100) not null,
    nif varchar(10) not null,
    email varchar(30) unique not null,
    tipo enum('estandar', 'premium') not null
);

-- tabla articulo
create table if not exists articulo (
    id int auto_increment primary key,
    codigo varchar(30) unique not null,
    descripcion varchar(100) not null,
    precioventa decimal(10,2) not null,
    gastosenvio decimal(10,2) not null,
    tiempopreparacion int not null
);

-- tabla pedido
create table if not exists pedido (
    numpedido int auto_increment primary key,
    cantidad int not null,
    fecha datetime not null,
    id_articulo int not null,
    id_cliente int not null,
    foreign key (id_articulo) references articulo(id),
    foreign key (id_cliente) references cliente(id)
);

-- procedimiento insertar cliente
delimiter //
create procedure insertar_cliente(
    in p_nombre varchar(50),
    in p_domicilio varchar(100),
    in p_nif varchar(10),
    in p_email varchar(30),
    in p_tipo varchar(20)
)
begin
    insert into cliente (nombre, domicilio, nif, email, tipo)
    values (p_nombre, p_domicilio, p_nif, p_email, p_tipo);
end //
delimiter ;

-- procedimiento insertar articulo
delimiter //
create procedure insertar_articulo(
    in p_codigo varchar(30),
    in p_descripcion varchar(100),
    in p_precioventa decimal(10,2),
    in p_gastosenvio decimal(10,2),
    in p_tiempopreparacion int
)
begin
    insert into articulo (codigo, descripcion, precioventa, gastosenvio, tiempopreparacion)
    values (p_codigo, p_descripcion, p_precioventa, p_gastosenvio, p_tiempopreparacion);
end //
delimiter ;

-- procedimiento insertar pedido
delimiter //
create procedure insertar_pedido(
    in p_cantidad int,
    in p_fecha datetime,
    in p_id_articulo int,
    in p_id_cliente int
)
begin
    insert into pedido (cantidad, fecha, id_articulo, id_cliente)
    values (p_cantidad, p_fecha, p_id_articulo, p_id_cliente);
end //
delimiter ;

-- procedimiento eliminar pedido
delimiter //
create procedure eliminar_pedido(
    in p_numpedido int
)
begin
    delete from pedido where numpedido = p_numpedido;
end //
delimiter ;