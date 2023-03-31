alter table despesas add column categoria varchar(11) not null;

update despesas set categoria = "OUTRAS";