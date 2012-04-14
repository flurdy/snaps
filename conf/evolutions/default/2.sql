# --- Second database schema

# --- !Ups


DELETE FROM snapalbum;

DELETE FROM snapevent;


INSERT INTO snapevent (eventname,eventdate) VALUES (
        'Christmas', '2011-12-24' );

INSERT INTO snapalbum (publisher,url,eventid) VALUES (
         'John Smith', 'http://flickr.com/photos/flurdy/set/12121eqweewqwqe',
        (select max(eventid) from snapevent) );

INSERT INTO snapalbum (publisher,url,eventid) VALUES (
         'Sue Smith', 'http://picasaweb.com/flurdy/12121eqweewqwqe',1),
        (select max(eventid) from snapevent) );


INSERT INTO snapevent (eventname,eventdate) VALUES (
         'Adams at Barbados', '2010-10-14' );


INSERT INTO snapevent (eventname,eventdate) VALUES (
        'Skiing in Meribel', '2011-02-20' );


INSERT INTO snapevent (eventname,eventdate) VALUES (
        'Christmas at Smiths', '2011-10-25' );



# --- !Downs


DELETE FROM snapalbum;

DELETE FROM snapevent;


