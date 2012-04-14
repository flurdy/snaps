# --- First database schema

# --- !Ups

CREATE TABLE snapevent (
    eventid               SERIAL PRIMARY KEY,
    eventname             VARCHAR(255) NOT NULL ,
    eventDate             VARCHAR(255) NOT NULL
);


CREATE TABLE snapalbum (
    albumid               SERIAL PRIMARY KEY,
    publisher             VARCHAR(100) NOT NULL,
    url                   VARCHAR(255) NOT NULL,
    eventid               BIGINT NOT NULL,
    foreign key(eventid) references snapevent(eventid) on delete cascade
);



# --- !Downs


SET REFERENTIAL_INTEGRITY FALSE;

DROP TABLE IF EXISTS snapalbum;

DROP TABLE IF EXISTS snapevent;

SET REFERENTIAL_INTEGRITY TRUE;


