create table provincias(
    codprov int primary key not null,
    nombre varchar,
    codauton varchar,
    comunidad varchar,
    capital varchar
);

create table municipios(
    codmuni int primary key not null,
    nombre varchar,
    codprov int,
    foreign key(codprov) references provincias(codprov)
);

create table tiempoMunicipio(
    codmuni int primary key not null,
    fecha date,
    minima int,
    maxima int,
    lluvia varchar,
    foreign key(codmuni) references municipios(codmuni)
);

create table tiempoProvincia(
    codprov int primary key not null,
    hoy varchar,
    manana varchar,
    fecha date,
    foreign key (codprov) references provincias(codprov)
);

