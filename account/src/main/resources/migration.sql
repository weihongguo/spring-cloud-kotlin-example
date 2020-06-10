
drop table if exists user;

create table if not exists user (
    id bigint not null auto_increment,
    create_time timestamp null,
    update_time timestamp null,
    delete_time timestamp null,
    mobile char(11) not null,
    password varchar(255) not null,
    primary key (id),
    unique key uk_user (mobile)
);

drop table if exists user_role;

create table if not exists user_role (
    id bigint not null auto_increment,
    create_time timestamp null,
    update_time timestamp null,
    delete_time timestamp null,
    user_id bigint not null,
    role_id bigint not null,
    primary key (id),
    unique key uk_user_role (user_id, role_id)
);

drop table if exists role;

create table if not exists role (
    id bigint not null auto_increment,
    create_time timestamp null,
    update_time timestamp null,
    delete_time timestamp null,
    type varchar(16) not null,
    name varchar(16) not null,
    primary key (id),
    unique key uk_role (type, name)
);

drop table if exists role_permission;

create table if not exists role_permission (
    id bigint not null auto_increment,
    create_time timestamp null,
    update_time timestamp null,
    delete_time timestamp null,
    role_id bigint not null,
    permission_id bigint not null,
    primary key (id),
    unique key uk_role_permission (role_id, permission_id)
);

drop table if exists permission;

create table if not exists permission (
    id bigint not null auto_increment,
    create_time timestamp null,
    update_time timestamp null,
    delete_time timestamp null,
    name varchar(16) not null,
    module varchar(16) not null,
    method varchar(8) not null,
    path_pattern varchar(128) not null,
    primary key (id),
    unique key uk_permission_name (name),
    unique key uk_permission (module, method, path_pattern)
);

# schedule

drop table if exists schedule_job;

create table if not exists schedule_job (
    id bigint not null auto_increment,
    create_time timestamp null,
    update_time timestamp null,
    delete_time timestamp null,
    module varchar(16) not null,
    name varchar(16) not null,
    lock_time timestamp null,
    lock_util timestamp null,
    lock_by varchar(16) null,
    primary key (id),
    unique key uk_schedule_job (module, name)
);

drop table if exists schedule_log;

create table if not exists schedule_log (
    id bigint not null auto_increment,
    create_time timestamp null,
    update_time timestamp null,
    delete_time timestamp null,
    schedule_job_id bigint not null,
    process_by varchar(16) not null,
    start_time timestamp null,
    end_time timestamp null,
    result varchar(16) not null,
    result_info varchar(255) null,
    primary key (id),
    index idx_schedule_job (schedule_job_id)
);

# mq_fail_log

drop table if exists mq_fail_log;

create table if not exists mq_fail_log (
    id bigint not null auto_increment,
    create_time timestamp null,
    update_time timestamp null,
    delete_time timestamp null,
    queue varchar(64) null,
    correlation_id varchar(255) null,
    message varchar(255) null,
    operate char(16) not null,
    reason char(255) not null,
    primary key (id),
    index idx_queue (queue)
);
