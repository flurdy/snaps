# --- Second database schema

# --- !Ups


DELETE FROM snapalbum;

DELETE FROM snapevent;


INSERT INTO snapevent (eventid,eventname,eventdate) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
         'Phil''s birthday party',  '2010-12-13' );

INSERT INTO snapevent (eventid,eventname,organiser,eventdate) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
         'Sue''s birthday party', 'John', '2010-12-13' );

INSERT INTO snapevent (eventid,eventname,organiser,eventdate) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
         'Dieter''s birthday party', '', '2010-12-13' );

INSERT INTO snapevent (eventid,eventname,organiser,eventdate) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
         'Henriette''s birthday party', '', '2010-12-13' );

INSERT INTO snapevent (eventid,eventname,organiser,eventdate) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
         'Lucy Ann''s birthday party', 'Shawn', '2010-12-13' );

INSERT INTO snapevent (eventid,eventname,organiser,eventdate) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
         'New Year', '', '2008-12-31' );

INSERT INTO snapevent (eventid,eventname,organiser,eventdate) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
         'New Year', '', '2010-12-31' );


INSERT INTO snapevent (eventid,eventname,organiser,eventdate) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
         'Adams at Barbados', 'Barbara Adams', 'Summer 2010' );


INSERT INTO snapevent (eventid,eventname,organiser,eventdate) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Skiing in Meribel', 'DJ', '2011-02-20' );


INSERT INTO snapevent (eventid,eventname,organiser,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths', 'John and Sue Smith', '2011-10-25', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.' );


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


