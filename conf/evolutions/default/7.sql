# --- Seventh database schema

# --- !Ups


ALTER TABLE snapevent
ADD searchable BOOLEAN DEFAULT TRUE;

UPDATE snapevent
SET searchable = TRUE;

UPDATE snapevent
SET searchable = FALSE
WHERE eventname = 'Adams at Barbados';



INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'moreusers-1', 'Test User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'moreusers-2', 'Test User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'moreusers-3', 'Test User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'moreusers-4', 'Test User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'moreusers-5', 'Test User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'moreusers-6', 'Test User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'moreusers-7', 'Test User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'moreusers-8', 'Test User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );


# --- !Downs

ALTER TABLE snapevent
DROP COLUMN searchable;

DELETE FROM participant
WHERE username like 'moreusers%';