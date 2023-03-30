create table if not exists lancamentos (
    id bigint not null primary key auto_increment,
    descricao varchar(255) not null,
    valor decimal(12,2),
    data date
)