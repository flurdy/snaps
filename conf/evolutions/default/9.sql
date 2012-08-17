# --- Ninth database schema

# --- !Ups


ALTER TABLE snapalbum ADD COLUMN notes VARCHAR(2000);
--
--UPDATE snapalbum set notes = 'Album username is matilda and password is terry'
--WHERE eventid = (SELECT MAX(eventid) FROM snapevent WHERE eventname = 'Christmas');

# --- !Downs

ALTER TABLE snapalbum DROP COLUMN notes;



