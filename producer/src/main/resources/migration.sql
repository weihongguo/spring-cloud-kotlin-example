
# producer

drop table if exists producer;

create table if not exists producer (
    id bigint not null auto_increment,
    create_time timestamp null,
    update_time timestamp null,
    delete_time timestamp null,
    name varchar(16) not null,
    primary key (id),
    unique key uk_producer (name)
);
