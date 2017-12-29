# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table status (
  id                            bigint auto_increment not null,
  created_at                    timestamp,
  update_at                     timestamp,
  status_name                   varchar(255) not null,
  constraint uq_status_status_name unique (status_name),
  constraint pk_status primary key (id)
);


# --- !Downs

drop table if exists status;

