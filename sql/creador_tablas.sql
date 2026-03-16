create database if not exists tienda_online;
use tienda_online;

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

show tables;

describe cliente;

describe pedido;

describe articulo;