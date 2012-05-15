# --- Third database schema

# --- !Ups



DELETE FROM snapevent;



INSERT INTO snapevent (eventid,eventname,organiserid,eventdate) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
         'Sue''s birthday party',
         (SELECT MAX(participantid) FROM participant WHERE username = 'johnsmith'),
         '2010-12-13' );

INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,publicevent) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
         'Lucy Ann''s birthday party',
         (SELECT MAX(participantid) FROM participant WHERE username = 'anotheruser'),
          '2010-12-13' , FALSE);

INSERT INTO snapevent (eventid,eventname,eventdate,publicevent) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
         'New Year', '2008-12-31', TRUE );

INSERT INTO snapevent (eventid,eventname,eventdate) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
         'New Year', '2010-12-31' );


INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,publicevent) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
         'Adams at Barbados',
         (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
          'Summer 2010',FALSE );


INSERT INTO snapevent (eventid,eventname,organiserid,eventdate) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Skiing in Meribel',
        (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
         '2011-02-20' );


INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths',
        (SELECT MAX(participantid) FROM participant WHERE username = 'johnsmith'),
        '2011-10-25', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.' );


INSERT INTO snapevent (eventid,eventname,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas', 'Christmas 2011','Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.' );





# --- !Downs


DELETE FROM snapevent;
