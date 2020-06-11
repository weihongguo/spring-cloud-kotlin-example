
# consumer

drop table if exists consumer;

create table if not exists consumer (
    id bigint not null auto_increment,
    create_time timestamp null,
    update_time timestamp null,
    delete_time timestamp null,
    name varchar(16) not null,
    producer_id bigint not null,
    primary key (id),
    unique key uk_consumer (name),
    index idx_producer (producer_id)
);
