create sequence message_seq start with 1 increment by 50;

create sequence usr_seq start with 1 increment by 50;

create table message (
                         id int8 not null,
                         filename varchar(255),
                         name varchar(255),
                         email varchar(2048) not null,
                         user_id int8,
                         primary key (id)
);

create table user_role (
                           user_id int8 not null,
                           roles varchar(255)
);

create table usr (
                     id int8 not null,
                     activation_code varchar(255),
                     active boolean not null,
                     email varchar(255),
                     password varchar(255) not null,
                     username varchar(255) not null,
                     primary key (id)
);

alter table if exists message
    add constraint message_user_fk
    foreign key (user_id) references usr;

alter table if exists user_role
    add constraint user_role_user_fk
    foreign key (user_id) references usr;