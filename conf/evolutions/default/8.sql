# --- Eight database schema

# --- !Ups

DROP TABLE IF EXISTS emailverification;

DROP SEQUENCE IF EXISTS emailverification_seq;

CREATE TABLE emailverification (
    verificationid          BIGINT NOT NULL,
    participantid          BIGINT NOT NULL,
    email                  VARCHAR(100) NOT NULL,
    verified               BOOLEAN DEFAULT FALSE,
    verificationhash       VARCHAR(128),
    foreign key(participantid) references participant(participantid) on delete cascade
);



CREATE SEQUENCE emailverification_seq START WITH 1000;


# --- !Downs

DROP TABLE IF EXISTS emailverification;

DROP SEQUENCE IF EXISTS emailverification_seq;