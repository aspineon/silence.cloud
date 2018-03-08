# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table company (
  id                            bigint auto_increment not null,
  created_at                    timestamp,
  updated_at                    timestamp,
  company_name                  varchar(255) not null,
  company_phone                 varchar(255) not null,
  company_email                 varchar(255) not null,
  company_address               varchar(255) not null,
  company_city                  varchar(255) not null,
  company_postal_code           varchar(255) not null,
  company_country               varchar(255) not null,
  tax_number                    varchar(255) not null,
  user_id                       bigint,
  is_primary                    boolean default false not null,
  constraint uq_company_company_name unique (company_name),
  constraint uq_company_company_phone unique (company_phone),
  constraint uq_company_company_email unique (company_email),
  constraint uq_company_tax_number unique (tax_number),
  constraint pk_company primary key (id)
);

create table module (
  id                            bigint auto_increment not null,
  created_at                    timestamp,
  updated_at                    timestamp,
  status_id                     bigint,
  module_name                   varchar(255),
  constraint pk_module primary key (id)
);

create table role (
  id                            bigint auto_increment not null,
  created_at                    timestamp,
  updated_at                    timestamp,
  status_id                     bigint,
  role_name                     varchar(255) not null,
  constraint uq_role_role_name unique (role_name),
  constraint pk_role primary key (id)
);

create table status (
  id                            bigint auto_increment not null,
  created_at                    timestamp,
  updated_at                    timestamp,
  status_name                   varchar(255) not null,
  constraint uq_status_status_name unique (status_name),
  constraint pk_status primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  created_at                    timestamp,
  updated_at                    timestamp,
  uuid                          uuid not null,
  token                         varchar(255) not null,
  username                      varchar(255) not null,
  email                         varchar(255) not null,
  phone                         varchar(255) not null,
  is_admin                      boolean default false not null,
  sha_password                  varbinary(64) not null,
  constraint uq_user_uuid unique (uuid),
  constraint uq_user_token unique (token),
  constraint uq_user_email unique (email),
  constraint uq_user_phone unique (phone),
  constraint pk_user primary key (id)
);

alter table company add constraint fk_company_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_company_user_id on company (user_id);

alter table module add constraint fk_module_status_id foreign key (status_id) references status (id) on delete restrict on update restrict;
create index ix_module_status_id on module (status_id);

alter table role add constraint fk_role_status_id foreign key (status_id) references status (id) on delete restrict on update restrict;
create index ix_role_status_id on role (status_id);


# --- !Downs

alter table company drop constraint if exists fk_company_user_id;
drop index if exists ix_company_user_id;

alter table module drop constraint if exists fk_module_status_id;
drop index if exists ix_module_status_id;

alter table role drop constraint if exists fk_role_status_id;
drop index if exists ix_role_status_id;

drop table if exists company;

drop table if exists module;

drop table if exists role;

drop table if exists status;

drop table if exists user;

