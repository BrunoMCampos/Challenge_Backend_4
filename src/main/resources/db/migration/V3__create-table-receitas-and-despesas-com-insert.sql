create table if not exists receitas (
    id bigint not null primary key auto_increment,
    descricao varchar(255) not null,
    valor decimal(12,2) not null,
    data date not null
);

insert into receitas (descricao,valor,data) select descricao,valor,data from lancamentos where tipo = "RECEITA";

create table if not exists despesas (
    id bigint not null primary key auto_increment,
    descricao varchar(255) not null,
    valor decimal(12,2) not null,
    data date not null
);

insert into despesas (descricao,valor,data) select descricao,valor,data from lancamentos where tipo = "DESPESA";