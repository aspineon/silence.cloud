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

create table page (
  id                            bigint auto_increment not null,
  created_at                    timestamp,
  update_at                     timestamp,
  name                          varchar(255) not null,
  title                         varchar(71) not null,
  description                   varchar(155),
  keywords                      varchar(1000),
  ogtitle                       varchar(96) not null,
  ogtype                        varchar(255) not null,
  ogimage                       varchar(1000) not null,
  ogurl                         varchar(1000) not null,
  ogdescription                 varchar(300) not null,
  twittercard                   varchar(1000) not null,
  twitterurl                    varchar(1000) not null,
  twittertitle                  varchar(71) not null,
  constraint uq_page_name unique (name),
  constraint uq_page_title unique (title),
  constraint uq_page_ogtitle unique (ogtitle),
  constraint uq_page_ogimage unique (ogimage),
  constraint uq_page_ogurl unique (ogurl),
  constraint uq_page_twittercard unique (twittercard),
  constraint uq_page_twitterurl unique (twitterurl),
  constraint uq_page_twittertitle unique (twittertitle),
  constraint pk_page primary key (id)
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

drop table if exists page;

drop table if exists role;

drop table if exists status;

