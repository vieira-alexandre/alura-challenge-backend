alter table if exists despesa
    add column categoria varchar(255) not null DEFAULT 'OUTRAS';