# --- First database schema

# --- !Ups


DROP TABLE IF EXISTS eventrequest;

DROP TABLE IF EXISTS eventparticipant;

DROP TABLE IF EXISTS snapalbum;

DROP TABLE IF EXISTS snapevent;

DROP TABLE IF EXISTS participant;

DROP SEQUENCE IF EXISTS snapevent_seq;

DROP SEQUENCE IF EXISTS snapalbum_seq;

DROP SEQUENCE IF EXISTS participant_seq;


CREATE TABLE snapevent (
    eventid               SERIAL PRIMARY KEY,
    eventname             VARCHAR(128) NOT NULL,
    organiserid           BIGINT,
    eventdate             VARCHAR(128),
    description           VARCHAR(4000),
    publicevent           BOOLEAN DEFAULT TRUE,
    searchable            BOOLEAN DEFAULT TRUE
);


CREATE TABLE snapalbum (
    albumid               SERIAL PRIMARY KEY,
    publisher             VARCHAR(100) NOT NULL,
    url                   VARCHAR(255) NOT NULL,
    eventid               BIGINT NOT NULL,
    notes                 VARCHAR(2000),
    foreign key(eventid) references snapevent(eventid) on delete cascade
);

CREATE TABLE participant (
    participantid         SERIAL PRIMARY KEY,
    username              VARCHAR(100) UNIQUE,
    fullname              VARCHAR(100),
    email                 VARCHAR(100),
    password              VARCHAR(100) NOT NULL,
    admin                 BOOLEAN DEFAULT FALSE,
    superuser             BOOLEAN DEFAULT FALSE
);

CREATE TABLE eventparticipant (
    eventid               BIGINT NOT NULL,
    participantid          BIGINT NOT NULL,
    foreign key(eventid) references snapevent(eventid) on delete cascade,
    foreign key(participantid) references participant(participantid) on delete cascade
);

CREATE TABLE eventrequest (
    eventid               BIGINT NOT NULL,
    participantid          BIGINT NOT NULL,
    requestdate            TIMESTAMP,
    foreign key(eventid) references snapevent(eventid) on delete cascade,
    foreign key(participantid) references participant(participantid) on delete cascade
);

CREATE TABLE emailverification (
    verificationid          BIGINT NOT NULL,
    participantid          BIGINT NOT NULL,
    email                  VARCHAR(100) NOT NULL,
    verified               BOOLEAN DEFAULT FALSE,
    verificationhash       VARCHAR(128),
    foreign key(participantid) references participant(participantid) on delete cascade
);


CREATE SEQUENCE emailverification_seq START WITH 1000;

CREATE SEQUENCE snapevent_seq START WITH 1000;

CREATE SEQUENCE snapalbum_seq START WITH 1000;

CREATE SEQUENCE participant_seq START WITH 1000;




# --- !Downs

DROP TABLE IF EXISTS eventrequest;

DROP TABLE IF EXISTS eventparticipant;

DROP TABLE IF EXISTS participant;

DROP TABLE IF EXISTS snapalbum;

DROP TABLE IF EXISTS snapevent;

DROP TABLE IF EXISTS emailverification;

DROP SEQUENCE IF EXISTS snapevent_seq;

DROP SEQUENCE IF EXISTS snapalbum_seq;

DROP SEQUENCE IF EXISTS participant_seq;

DROP SEQUENCE IF EXISTS emailverification_seq;
