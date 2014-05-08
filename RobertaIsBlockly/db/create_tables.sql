PRAGMA foreign_keys=ON;

create table PROJECT (
  ID INTEGER not null,
  NAME varchar(255) not null,
  EMAIL varchar(255),
  URL varchar(255),
  TEXT varchar(1024),
  primary key (ID)
);

create unique index prvNameIdx on PROJECT(NAME);

create table PROGRAM (
  ID INTEGER not null,
  NAME varchar(255) not null,
  PROJECT_ID INTEGER not null,
  TEXT varchar(1024),
  PROGRAM_TEXT varchar(1024),
  STATE varchar(30) not null,
  primary key (ID),
  foreign key (PROJECT_ID) references PROJECT(ID)
);

create unique index ingNameIdx on PROGRAM(PROJECT_ID, NAME);

insert into PROJECT values(1,'RobertaLabTest','reinhard.budde@iais.fraunhofer.de','http://iais.fraunhofer.de','dies ist ein RobertaLab-Projekt');
insert into PROJECT values(2,'LegoTest','reinhard.budde@iais.fraunhofer.de','http://iais.fraunhofer.de','dies ist ein Lego-Projekt');