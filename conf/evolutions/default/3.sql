# --- Third database schema

# --- !Ups



 DELETE FROM snapevent;


INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths',
        (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
        '2011-10-25', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.' );



# --- !Downs


DELETE FROM snapevent;
