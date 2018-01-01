# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

CREATE TABLE module (
  id          BIGINT AUTO_INCREMENT NOT NULL,
  created_at  TIMESTAMP,
  update_at   TIMESTAMP,
  status_id   BIGINT,
  module_name VARCHAR(255),
  CONSTRAINT pk_module PRIMARY KEY (id)
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

ALTER TABLE module
  ADD CONSTRAINT fk_module_status_id FOREIGN KEY (status_id) REFERENCES status (id)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;
CREATE INDEX ix_module_status_id
  ON module (status_id);

alter table role add constraint fk_role_status_id foreign key (status_id) references status (id) on delete restrict on update restrict;
create index ix_role_status_id on role (status_id);


# --- !Downs

ALTER TABLE module
  DROP constraint IF EXISTS fk_module_status_id;
DROP INDEX if EXISTS ix_module_status_id;

alter table role drop constraint if exists fk_role_status_id;
drop index if exists ix_role_status_id;

DROP TABLE IF EXISTS module;

drop table if exists role;

drop table if exists status;

