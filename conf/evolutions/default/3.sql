# --- Second database schema

# --- !Ups


DELETE FROM snapalbum;

DELETE FROM snapevent;


INSERT INTO snapevent (eventid,eventname,eventdate) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
         'Phil''s birthday party',  '2010-12-13' );

INSERT INTO snapevent (eventid,eventname,organiserid,eventdate) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
         'Sue''s birthday party',
         (SELECT MAX(participantid) FROM participant WHERE username = 'johnsmith'),
         '2010-12-13' );

INSERT INTO snapevent (eventid,eventname,eventdate,publicevent) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
         'Dieter''s birthday party', '2010-12-13', FALSE );

INSERT INTO snapevent (eventid,eventname, eventdate) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
         'Henriette''s birthday party', '2010-12-13' );

INSERT INTO snapevent (eventid,eventname,organiserid,eventdate) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
         'Lucy Ann''s birthday party',
         (SELECT MAX(participantid) FROM participant WHERE username = 'anotheruser'),
          '2010-12-13' );

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

INSERT INTO snapalbum (albumid,publisher,url,eventid) VALUES (
        (SELECT NEXTVAL('snapalbum_seq')),
         'John Smith', 'http://flickr.com/photos/flurdy/set/12121eqweewqwqe',
        (SELECT MAX(eventid) FROM snapevent WHERE eventname = 'Christmas') );

INSERT INTO snapalbum (albumid,publisher,url,eventid) VALUES (
        (SELECT NEXTVAL('snapalbum_seq')),
         'Sue Smith', 'http://picasaweb.com/flurdy/12121eqweewqwqe',
        (SELECT MAX(eventid) FROM snapevent WHERE eventname = 'Christmas') );



# --- !Downs


DELETE FROM snapalbum;

DELETE FROM snapevent;
