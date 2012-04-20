# --- First database schema

# --- !Ups
SET REFERENTIAL_INTEGRITY FALSE;

DROP TABLE IF EXISTS participant;

DROP TABLE IF EXISTS snapalbum;

DROP TABLE IF EXISTS snapevent;

DROP SEQUENCE IF EXISTS snapevent_seq;

DROP SEQUENCE IF EXISTS snapalbum_seq;

DROP SEQUENCE IF EXISTS participant_seq;

SET REFERENTIAL_INTEGRITY TRUE;

CREATE TABLE snapevent (
    eventid               SERIAL PRIMARY KEY,
    eventname             VARCHAR(128) NOT NULL,
    organiser             VARCHAR(128),
    eventDate             VARCHAR(128),
    description           VARCHAR(4000),
    publicevent           BOOLEAN DEFAULT TRUE
);


CREATE TABLE snapalbum (
    albumid               SERIAL PRIMARY KEY,
    publisher             VARCHAR(100) NOT NULL,
    url                   VARCHAR(255) NOT NULL,
    eventid               BIGINT NOT NULL,
    foreign key(eventid) references snapevent(eventid) on delete cascade
);

CREATE TABLE PARTICIPANT (
    participantid         SERIAL PRIMARY KEY,
    username              VARCHAR(100) UNIQUE,
    fullname              VARCHAR(100) NOT NULL,
    email                 VARCHAR(100) NOT NULL,
    password              VARCHAR(100) NOT NULL
);

CREATE SEQUENCE snapevent_seq START WITH 1000;

CREATE SEQUENCE snapalbum_seq START WITH 1000;

CREATE SEQUENCE participant_seq START WITH 1000;




# --- !Downs


SET REFERENTIAL_INTEGRITY FALSE;

DROP TABLE IF EXISTS participant;

DROP TABLE IF EXISTS snapalbum;

DROP TABLE IF EXISTS snapevent;

DROP SEQUENCE IF EXISTS snapevent_seq;

DROP SEQUENCE IF EXISTS snapalbum_seq;

DROP SEQUENCE IF EXISTS participant_seq;

SET REFERENTIAL_INTEGRITY TRUE;


