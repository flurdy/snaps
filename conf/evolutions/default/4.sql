# --- Second database schema

# --- !Ups


DELETE FROM snapalbum;



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
