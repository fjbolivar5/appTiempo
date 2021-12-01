create table provincias(
    codprov varchar(2) primary key not null,
    nombre varchar,
    codauton varchar,
    comunidad varchar,
    capital varchar
);

create table municipios(
    codmuni varchar primary key not null,
    nombre varchar,
    codprov varchar(2),
    foreign key(codprov) references provincias(codprov)
);

create table tiempoMunicipio(
    codmuni varchar primary key not null,
    fecha date,
    minima int,
    maxima int,
    lluvia varchar,
    foreign key(codmuni) references municipios(codmuni)
);

create table tiempoProvincia(
    codprov varchar(2) primary key not null,
    hoy varchar,
    manana varchar,
    fecha date,
    foreign key (codprov) references provincias(codprov)
);

