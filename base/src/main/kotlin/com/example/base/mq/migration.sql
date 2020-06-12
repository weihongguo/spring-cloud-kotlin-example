
# mq

drop table if exists mq_log;

create table if not exists mq_log (
    id bigint not null auto_increment,
    create_time timestamp null,
    update_time timestamp null,
    delete_time timestamp null,
    queue varchar(64) null,
    correlation_id varchar(255) null,
    message varchar(255) null,
    operate char(16) not null,
    result char(16) not null,
    result_info char(255) not null,
    primary key (id),
    index idx_queue (queue)
);