# --- Fifth database schema

# --- !Ups


DELETE FROM eventparticipant;
--
--
--INSERT INTO eventparticipant (eventid,participantid) VALUES (
--         (SELECT MAX(eventid) FROM snapevent WHERE eventname = 'Christmas at Smiths'),
--          (SELECT MAX(participantid) FROM participant WHERE username = 'anotheruser')
--        );
--
--
--INSERT INTO eventparticipant (eventid,participantid) VALUES (
--         (SELECT MAX(eventid) FROM snapevent WHERE eventname = 'Skiing in Meribel'),
--          (SELECT MAX(participantid) FROM participant WHERE username = 'anotheruser')
--        );
--
--INSERT INTO eventparticipant (eventid,participantid) VALUES (
--         (SELECT MAX(eventid) FROM snapevent WHERE eventname = 'Christmas at Smiths'),
--          (SELECT MAX(participantid) FROM participant WHERE username = 'testuser')
--        );



# --- !Downs


DELETE FROM eventparticipant;
