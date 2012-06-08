# --- Second database schema

# --- !Ups

DELETE FROM participant;


INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'testuser', 'Test User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser', 'Another User', 'another@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'johnsmith', 'John And Sue Smith', 'another@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'dj', 'DJ', 'another@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

/*

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'longlonglonglonglonglonglongmoreusers-1', 'Test User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'longlonglonglonglonglonglongmoreusers-2', 'Test User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

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
*/


# --- !Downs



DELETE FROM participant;

