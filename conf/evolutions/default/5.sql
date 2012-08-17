# --- Fifth database schema

# --- !Ups



INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths',
        (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
        '2011-10-25', 'Lorem ipsum' );

INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths',
        (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
        '2011-10-25', 'Lorem ipsum' );

INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths',
        (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
        '2011-10-25', 'Lorem ipsum' );

INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths',
        (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
        '2011-10-25', 'Lorem ipsum' );

INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths',
        (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
        '2011-10-25', 'Lorem ipsum' );

INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths',
        (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
        '2011-10-25', 'Lorem ipsum' );

INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths',
        (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
        '2011-10-25', 'Lorem ipsum' );

INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths',
        (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
        '2011-10-25', 'Lorem ipsum' );

INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths',
        (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
        '2011-10-25', 'Lorem ipsum' );

INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths',
        (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
        '2011-10-25', 'Lorem ipsum' );

INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths',
        (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
        '2011-10-25', 'Lorem ipsum' );

INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths',
        (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
        '2011-10-25', 'Lorem ipsum' );

INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths',
        (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
        '2011-10-25', 'Lorem ipsum' );

INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths',
        (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
        '2011-10-25', 'Lorem ipsum' );

INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths',
        (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
        '2011-10-25', 'Lorem ipsum' );

INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths',
        (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
        '2011-10-25', 'Lorem ipsum' );

INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths',
        (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
        '2011-10-25', 'Lorem ipsum' );

INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths',
        (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
        '2011-10-25', 'Lorem ipsum' );

INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths',
        (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
        '2011-10-25', 'Lorem ipsum' );

INSERT INTO snapevent (eventid,eventname,organiserid,eventdate,description) VALUES (
        (SELECT NEXTVAL('snapevent_seq')),
        'Christmas at Smiths',
        (SELECT MAX(participantid) FROM participant WHERE username = 'testuser'),
        '2011-10-25', 'Lorem ipsum' );



INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser1', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );


INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser2', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );


INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser3', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser4blahblahblah', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser5', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser6', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser7', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser8', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser9', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser10blahblahblah', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser11', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser12', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser13', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser14', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser15', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser16', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser17blahblahblah', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser18', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser19blahblahblah', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser20', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser21blahblahblah', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser22', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser23', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser24blahblahblah', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser25', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser26blahblahblah', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser27', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser28', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser29', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser30', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser31', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser32', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser33', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser34', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser35', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser36', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser37', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser38', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

INSERT INTO participant (participantid,username,fullname,email,password) VALUES (
      (SELECT NEXTVAL('participant_seq')),
       'anotheruser39', 'Another User', 'test@example.com', '$2a$10$waIvJd.49bI.OhwgmKIO2uBhAP4KMoCGGdx/at2kjIE6IFdjmWV6e' );

# --- !Downs


DELETE FROM participant where username != 'testuser';
