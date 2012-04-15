# --- Second database schema

# --- !Ups


DELETE FROM snapalbum;

DELETE FROM snapevent;


INSERT INTO snapevent (eventid,eventname,eventdate) VALUES (
        DEFAULT,
         'Adams at Barbados', '2010-10-14' );


INSERT INTO snapevent (eventid,eventname,eventdate) VALUES (
        DEFAULT,
        'Skiing in Meribel', '2011-02-20' );


INSERT INTO snapevent (eventid,eventname,eventdate) VALUES (
        DEFAULT,
        'Christmas at Smiths', '2011-10-25' );


INSERT INTO snapevent (eventid,eventname,eventdate) VALUES (
        DEFAULT,
        'Christmas', '2011-12-24' );

INSERT INTO snapalbum (albumid,publisher,url,eventid) VALUES (
        DEFAULT,
         'John Smith', 'http://flickr.com/photos/flurdy/set/12121eqweewqwqe',
        (SELECT MAX(eventid) FROM snapevent WHERE eventname = 'Christmas') );

INSERT INTO snapalbum (albumid,publisher,url,eventid) VALUES (
        DEFAULT,
         'Sue Smith', 'http://picasaweb.com/flurdy/12121eqweewqwqe',
        (SELECT MAX(eventid) FROM snapevent WHERE eventname = 'Christmas') );



# --- !Downs


DELETE FROM snapalbum;

DELETE FROM snapevent;

