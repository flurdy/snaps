# --- Fifth database schema

# --- !Ups


UPDATE participant SET admin = true WHERE username = 'testuser';

# --- !Downs

UPDATE participant SET admin = false WHERE username = 'testuser';

