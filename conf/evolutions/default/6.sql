# --- Sixth database schema

# --- !Ups

DELETE FROM eventrequest;

INSERT INTO eventrequest (eventid,participantid,requestdate) VALUES (
         (SELECT MAX(eventid) FROM snapevent WHERE eventname = 'Adams at Barbados'),
          (SELECT MAX(participantid) FROM participant WHERE username = 'anotheruser'),
          CURRENT_TIMESTAMP
        );



# --- !Downs


DELETE FROM eventrequest;
