# --- Fifth database schema

# --- !Ups


DELETE FROM eventrequests;


INSERT INTO eventrequests (eventid,participantid,requestdate) VALUES (
         (SELECT MAX(eventid) FROM snapevent WHERE eventname = 'Adams at Barbados'),
          (SELECT MAX(participantid) FROM participant WHERE username = 'anotheruser'),
          CURRENT_TIMESTAMP
        );



# --- !Downs


DELETE FROM eventrequests;
