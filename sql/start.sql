create database otdb;

CREATE USER otusr;
SET PASSWORD FOR otusr=PASSWORD('********');
GRANT ALL PRIVILEGES ON `otdb`.* TO 'otusr';

use otdb;

create table codes(
    code varchar(256),
    created_at timestamp not null default current_timestamp
);

SHOW GRANTS FOR otusr;
show tables;
desc table_name;
show databases;