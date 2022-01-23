create table despesa
(
    id        bigserial      not null,
    descricao varchar(255)   not null,
    mes       date           not null,
    valor     numeric(12, 2) not null,
    primary key (id)
);

create table receita
(
    id        bigserial      not null,
    descricao varchar(255)   not null,
    mes       date           not null,
    valor     numeric(12, 2) not null,
    primary key (id)
);

alter table if exists despesa
    add constraint uk_despesa_descricao_mes unique (descricao, mes);

alter table if exists receita
    add constraint uk_receita_descricao_mes unique (descricao, mes);

