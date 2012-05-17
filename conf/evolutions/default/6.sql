# --- Fifth database schema

# --- !Ups

DROP TABLE IF EXISTS eventrequest;

CREATE TABLE eventrequests (
    eventid               BIGINT NOT NULL,
    participantid          BIGINT NOT NULL,
    requestdate            TIMESTAMP,
    foreign key(eventid) references snapevent(eventid) on delete cascade,
    foreign key(participantid) references participant(participantid) on delete cascade
);

INSERT INTO eventrequests (eventid,participantid,requestdate) VALUES (
         (SELECT MAX(eventid) FROM snapevent WHERE eventname = 'Adams at Barbados'),
          (SELECT MAX(participantid) FROM participant WHERE username = 'anotheruser'),
          CURRENT_TIMESTAMP
        );



# --- !Downs


DROP TABLE IF EXISTS eventrequest;
