# --- Second database schema

# --- !Ups

DELETE FROM participant;

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'testuser', 'Test User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );





# --- !Downs



DELETE FROM participant;

