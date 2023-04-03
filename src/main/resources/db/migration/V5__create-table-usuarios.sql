create table usuarios (
    id bigint not null primary key auto_increment,
    login varchar(100) not null,
    senha varchar(255) not null
);