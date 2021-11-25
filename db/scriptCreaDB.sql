create table provincias(
    codprov int primary key,
    nombre varchar,
    codauton varchar,
    comunidad varchar,
    capital varchar
);

create table municipios(
    codgeo int primary key,
    nombre varchar,
    longitud varchar,
    latitud varchar,
    altitud int,
    codprov int,
    foreign key(codprov) references provincias(codprov)
);

create table temperaturas(
    fecha date primary key,
    minima int,
    maxima int,
    codgeo int,
    foreign key(codgeo) references municipios(codgeo)
);

create table descripcion_cielo(
    id int primary key,
    descripcion varchar
);

create table estado_cielo(
    fecha date primary key,
    id int,
    foreign key(id) references descripcion_cielo(id)
);