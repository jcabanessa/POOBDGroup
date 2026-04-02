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

-- tabla pedido (numPedido como primary key)
create table if not exists pedido (
    numpedido int auto_increment primary key,
    cantidad int not null,
    fecha datetime not null,
    enviado boolean default false,
    id_articulo int not null,
    id_cliente int not null,
    foreign key (id_articulo) references articulo(id),
    foreign key (id_cliente) references cliente(id)
);

-- procedimiento: insertar cliente
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

-- procedimiento: insertar articulo
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

-- procedimiento: insertar pedido (con transaccion)
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

-- procedimiento: eliminar pedido (solo si no ha sido enviado)
delimiter //
create procedure eliminar_pedido(
    in p_numpedido int
)
begin

        delete from pedido where numpedido = p_numpedido;

end //
delimiter ;

-- procedimiento: obtener clientes por tipo
delimiter //
create procedure sp_obtener_clientes_por_tipo(
    in p_tipo varchar(20)
)
begin
    select id, nombre, domicilio, nif, email, tipo, cuota_anual
    from cliente
    where p_tipo = 'todos' or tipo = p_tipo;
end //
delimiter ;

-- procedimiento: obtener pedidos por estado (pendiente/enviado)
delimiter //
create procedure sp_obtener_pedidos_por_estado(
    in p_enviado boolean,
    in p_filtro varchar(100)
)
begin
    select p.numpedido, p.cantidad, p.fecha, p.enviado,
           p.id_articulo, a.codigo as articulo_codigo, a.descripcion as articulo_desc,
           p.id_cliente, c.nombre as cliente_nombre, c.email as cliente_email, c.tipo
    from pedido p
    join articulo a on p.id_articulo = a.id
    join cliente c on p.id_cliente = c.id
    where p.enviado = p_enviado
    and (p_filtro = 'todos'
         or c.email = p_filtro
         or c.tipo = p_filtro);
end //
delimiter ;

-- procedimiento: verificar si existe cliente por email
delimiter //
create procedure sp_existe_cliente(
    in p_email varchar(30),
    out p_existe boolean,
    out p_id int
)
begin
    select id into p_id from cliente where email = p_email limit 1;
    set p_existe = (p_id is not null);
end //
delimiter ;

-- procedimiento: verificar si existe articulo por codigo
delimiter //
create procedure sp_existe_articulo(
    in p_codigo varchar(30),
    out p_existe boolean,
    out p_id int
)
begin
    select id into p_id from articulo where codigo = p_codigo limit 1;
    set p_existe = (p_id is not null);
end //
delimiter ;

-- mostrar tablas
show tables;

-- describir tablas
describe cliente;
describe articulo;
describe pedido;