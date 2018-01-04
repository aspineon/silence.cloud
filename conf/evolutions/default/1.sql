# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table module (
  id                            bigint auto_increment not null,
  created_at                    timestamp,
  update_at                     timestamp,
  status_id                     bigint,
  module_name                   varchar(255),
  constraint pk_module primary key (id)
);

create table role (
  id                            bigint auto_increment not null,
  created_at                    timestamp,
  update_at                     timestamp,
  status_id                     bigint,
  role_name                     varchar(255) not null,
  constraint uq_role_role_name unique (role_name),
  constraint pk_role primary key (id)
);

create table status (
  id                            bigint auto_increment not null,
  created_at                    timestamp,
  update_at                     timestamp,
  status_name                   varchar(255) not null,
  constraint uq_status_status_name unique (status_name),
  constraint pk_status primary key (id)
);

alter table module add constraint fk_module_status_id foreign key (status_id) references status (id) on delete restrict on update restrict;
create index ix_module_status_id on module (status_id);

alter table role add constraint fk_role_status_id foreign key (status_id) references status (id) on delete restrict on update restrict;
create index ix_role_status_id on role (status_id);


# --- !Downs

alter table module drop constraint if exists fk_module_status_id;
drop index if exists ix_module_status_id;

alter table role drop constraint if exists fk_role_status_id;
drop index if exists ix_role_status_id;

drop table if exists module;

drop table if exists role;

drop table if exists status;

