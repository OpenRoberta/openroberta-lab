create cached table USER (
  ID INTEGER not null,
  USERGROUP_ID INTEGER,
  ACCOUNT varchar(255) not null,
  PASSWORD  varchar(255) not null,
  EMAIL varchar(255),
  ROLE varchar(32) not null,
  CREATED timestamp not null,
  LAST_LOGIN timestamp not null,
  TAGS varchar(16M), -- e.g. HERDER-GYMNASIUM KÖLN Q1 ED_SHEERAN
  USER_NAME varchar(255),
  YOUNGER_THAN_14 boolean default false not null,
  ACTIVATED boolean default false not null,
  primary key (ID)
);

create cached table LOST_PASSWORD (
    ID INTEGER not null,
    USER_ID INTEGER not null,
    URL_POSTFIX varchar(255),
    CREATED timestamp not null,
    primary key (ID)
);

create cached table PENDING_EMAIL_CONFIRMATIONS (
    ID INTEGER not null,
    USER_ID INTEGER not null,
    URL_POSTFIX varchar(255),
    CREATED timestamp not null,
    primary key (ID)
);

create cached table ROBOT (
  ID INTEGER not null,
  NAME varchar(255) not null,
  CREATED timestamp not null,
  TAGS varchar(16M), 
  ICON_NUMBER integer not null,
  primary key (ID),
);

create cached table PROGRAM (
  ID INTEGER not null,
  NAME varchar(255) not null,
  OWNER_ID INTEGER not null,
  AUTHOR_ID INTEGER not null,
  ROBOT_ID INTEGER not null,
  PROGRAM_TEXT varchar(16M),
  CONFIG_NAME varchar(255),
  CONFIG_HASH varchar(255),
  CREATED timestamp not null,
  LAST_CHANGED timestamp not null,
  LAST_CHECKED timestamp,
  LAST_ERRORFREE timestamp,
  VIEWED INTEGER,
  TAGS varchar(16M), -- e.g. CAR AUTONOMOUS COOL 3WHEELS
  ICON_NUMBER integer not null,
  primary key (ID)
);

create cached table USER_PROGRAM (
  ID INTEGER not null,
  USER_ID INTEGER not null,
  PROGRAM_ID INTEGER not null,
  RELATION varchar(32) not null -- 1 READ access, 2 WRITE access, 4 DELETE right, (really? not yet used) 8 PROMOTE_READ right, 16 PROMOTE_WRITE right
);

create cached table USER_PROGRAM_LIKE (
  ID INTEGER not null,
  USER_ID INTEGER not null,
  PROGRAM_ID INTEGER not null,
  CREATED timestamp not null,
  MARK varchar(16M),
  COMMENT varchar(16M)
);

create cached table CONFIGURATION (
  ID INTEGER not null,
  NAME varchar(255) not null,
  OWNER_ID INTEGER,
  ROBOT_ID INTEGER not null,
  CONFIGURATION_HASH varchar(255),
  CREATED timestamp not null,
  LAST_CHANGED timestamp not null,
  LAST_CHECKED timestamp,
  LAST_ERRORFREE timestamp,
  TAGS varchar(16M), -- e.g. CAR AUTONOMOUS COOL 3WHEELS
  ICON_NUMBER integer not null,
  primary key (ID)
);

create cached table CONFIGURATION_DATA (
  CONFIGURATION_HASH varchar(255) not null,
  CONFIGURATION_TEXT varchar(16M) not null,
  primary key (CONFIGURATION_HASH)
);

create cached table USERGROUP (
  ID INTEGER not null,
  NAME varchar(255) not null,
  OWNER_ID INTEGER not null,
  ACCESS_RIGHT varchar(32) not null,
  CREATED timestamp not null,
  primary key (ID)
);

create cached table USERGROUP_PROGRAM (
  ID INTEGER not null,
  USERGROUP_ID INTEGER not null,
  PROGRAM_ID INTEGER not null,
  RELATION varchar(32) not null, -- [READ | WRITE]
  primary key (ID)
);

commit;