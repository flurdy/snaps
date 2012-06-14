# --- Seventh database schema

# --- !Ups


ALTER TABLE participant ADD COLUMN admin BOOLEAN DEFAULT false;
ALTER TABLE participant ADD COLUMN superuser BOOLEAN DEFAULT false;

UPDATE participant SET admin = true WHERE username = 'testuser';

# --- !Downs


ALTER TABLE participant DROP COLUMN admin;
ALTER TABLE participant DROP COLUMN superuser;



