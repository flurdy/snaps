# --- First database schema

# --- !Ups

SET REFERENTIAL_INTEGRITY FALSE;

DROP TABLE IF EXISTS eventparticipant;

DROP TABLE IF EXISTS snapalbum;

DROP TABLE IF EXISTS snapevent;

DROP TABLE IF EXISTS participant;

DROP SEQUENCE IF EXISTS snapevent_seq;

DROP SEQUENCE IF EXISTS snapalbum_seq;

DROP SEQUENCE IF EXISTS participant_seq;

SET REFERENTIAL_INTEGRITY TRUE;

CREATE TABLE snapevent (
    eventid               SERIAL PRIMARY KEY,
    eventname             VARCHAR(128) NOT NULL,
    organiserid           BIGINT,
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

CREATE TABLE participant (
    participantid         SERIAL PRIMARY KEY,
    username              VARCHAR(100) UNIQUE,
    fullname              VARCHAR(100),
    email                 VARCHAR(100),
    password              VARCHAR(100) NOT NULL
);

CREATE TABLE eventparticipant (
    eventid               BIGINT NOT NULL,
    participantid          BIGINT NOT NULL,
    foreign key(eventid) references snapevent(eventid) on delete cascade,
    foreign key(participantid) references participant(participantid) on delete cascade
);

CREATE SEQUENCE snapevent_seq START WITH 1000;

CREATE SEQUENCE snapalbum_seq START WITH 1000;

CREATE SEQUENCE participant_seq START WITH 1000;




# --- !Downs


SET REFERENTIAL_INTEGRITY FALSE;

DROP TABLE IF EXISTS eventparticipant;

DROP TABLE IF EXISTS participant;

DROP TABLE IF EXISTS snapalbum;

DROP TABLE IF EXISTS snapevent;

DROP SEQUENCE IF EXISTS snapevent_seq;

DROP SEQUENCE IF EXISTS snapalbum_seq;

DROP SEQUENCE IF EXISTS participant_seq;

SET REFERENTIAL_INTEGRITY TRUE;


