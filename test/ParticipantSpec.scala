package models

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import models._
import play.Logger

class ParticipantSpec extends Specification {


  "A Participant" should {

    "be able to be created and persisted" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val participant = Participant(0,"tester",Some("Test User"), Some("test@example.com"), Some("testpassword"));
        participant.participantId.toInt must beEqualTo(0)
        Participant.save(participant).participantId.toInt must beGreaterThan(0)
      }
    }


    "cannot have the same username" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val participant1 = Participant(0,"tester",Some("Test User"), Some("test@example.com"), Some("testpassword"));
        Participant.save(participant1)
        val participant2 = Participant(0,"tester",Some("Test User"), Some("test@example.com"), Some("testpassword"))
        Participant.save(participant2) must throwAn[IllegalArgumentException];
      }
    }


    "have the password encrypted" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val participant1 = Participant(0,"tester",Some("Test User"), Some("test@example.com"), Some("testpassword"));
        participant1.encryptedPassword must beSome[String]
      }
    }

    "no bother encrypting empty passwords" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val participant1 = Participant(0,"tester",Some("Test User"), Some("test@example.com"), None);
        participant1.encryptedPassword must beNone
      }
    }




    "be able to authenticate" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val participant1 = Participant(0,"tester",Some("Test User"), Some("test@example.com"), Some("testpassword"));
        Participant.save(participant1)
        Participant.authenticate("tester","testpassword") must beSome[Participant]
      }
    }



    "not be able to authenticate with wrong password" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val participant1 = Participant(0,"tester",Some("Test User"), Some("test@example.com"), Some("testpassword"));
        Participant.save(participant1)
        Participant.authenticate("tester","wrongpassword") must beNone
      }
    }



    "be able to delete himself" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val participant1 = Participant(0,"tester",Some("Test User"), Some("test@example.com"), Some("testpassword"));
        val participantSaved = Participant.save(participant1)
        val participantId = participantSaved.participantId
        Participant.findById(participantId) must beSome[Participant]
        Participant.deleteAccount(participantId)
        Participant.findById(participantId) must beNone
      }
    }


    "delete all events when deleted" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val participant1 = Participant(0,"tester",Some("Test User"), Some("test@example.com"), Some("testpassword"));
        val participantId = Participant.save(participant1).participantId
        participantId.toInt must beGreaterThan(0)
        Participant.findById(participantId) must beSome[Participant]
        val eventId = Event.createAndSaveEvent("Christmas",participantId).eventId

        Event.findAllEventsAsParticipantOrOrganiser(participantId) must have size(1)

        Participant.deleteAccount(participantId)

        Participant.findById(participantId) must beNone
        Event.findEvent(eventId) must beNone
        Event.findAllEventsAsParticipantOrOrganiser(participantId) must have size(0)

      }
    }


    "delete all requests when deleted" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val participant1 = Participant(0,"tester1",Some("Test User1"), Some("test@example.com"), Some("testpassword"));
        val participant2 = Participant(0,"tester2",Some("Test User2"), Some("test@example.com"), Some("testpassword"));
        val participant3 = Participant(0,"tester3",Some("Test User3"), Some("test@example.com"), Some("testpassword"));
        val participantId1 = Participant.save(participant1).participantId
        val participantId2 = Participant.save(participant2).participantId
        val participantId3 = Participant.save(participant3).participantId
        val eventId1 = Event.createAndSaveEvent("Christmas 2",participantId2).eventId
        val eventId2 = Event.createAndSaveEvent("Christmas 3",participantId3).eventId

        Event.addJoinRequest(eventId1,participantId1)
        Event.addJoinRequest(eventId2,participantId2)

        Event.findAllEventsAsParticipantOrOrganiser(participantId1) must have size(0)
        Event.findAllEventsAsParticipantOrOrganiser(participantId2) must have size(1)
        Event.findAllEventsAsParticipantOrOrganiser(participantId3) must have size(1)

        Event.isAlreadyJoinRequested(eventId1,participantId1) must beTrue
        Event.isAlreadyJoinRequested(eventId1,participantId2) must beFalse
        Event.isAlreadyJoinRequested(eventId1,participantId3) must beFalse
        Event.isAlreadyJoinRequested(eventId2,participantId1) must beFalse
        Event.isAlreadyJoinRequested(eventId2,participantId2) must beTrue
        Event.isAlreadyJoinRequested(eventId2,participantId3) must beFalse

        Participant.deleteAccount(participantId1)

        Participant.findById(participantId1) must beNone
        Participant.findById(participantId2) must beSome[Participant]
        Participant.findById(participantId3) must beSome[Participant]

        Event.findAllEventsAsParticipantOrOrganiser(participantId1) must have size(0)
        Event.findAllEventsAsParticipantOrOrganiser(participantId2) must have size(1)
        Event.findAllEventsAsParticipantOrOrganiser(participantId3) must have size(1)

        Event.isAlreadyJoinRequested(eventId1,participantId1) must beFalse
        Event.isAlreadyJoinRequested(eventId1,participantId2) must beFalse
        Event.isAlreadyJoinRequested(eventId1,participantId3) must beFalse
        Event.isAlreadyJoinRequested(eventId2,participantId1) must beFalse
        Event.isAlreadyJoinRequested(eventId2,participantId2) must beTrue
        Event.isAlreadyJoinRequested(eventId2,participantId3) must beFalse
      }
    }


    "delete all participations when deleted" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val participant1 = Participant(0,"tester1",Some("Test User1"), Some("test@example.com"), Some("testpassword"));
        val participant2 = Participant(0,"tester2",Some("Test User2"), Some("test@example.com"), Some("testpassword"));
        val participant3 = Participant(0,"tester3",Some("Test User3"), Some("test@example.com"), Some("testpassword"));
        val participantId1 = Participant.save(participant1).participantId
        val participantId2 = Participant.save(participant2).participantId
        val eventId1 = Event.createAndSaveEvent("Christmas 2",participantId2).eventId
        val eventId2 = Event.createAndSaveEvent("Christmas 3",participantId2).eventId

        Event.addParticipant(eventId1,participantId1)
        Event.addParticipant(eventId2,participantId1)

        Event.findAllEventsAsParticipantOrOrganiser(participantId1) must have size(2)

        Participant.deleteAccount(participantId1)

        Participant.findById(participantId1) must beNone
        Participant.findById(participantId2) must beSome[Participant]

        Event.findAllEventsAsParticipantOrOrganiser(participantId1) must have size(0)
      }
    }


    "be able to reset password" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val participant = Participant(0,"tester",Some("Test User"), Some("test@example.com"), Some("testpassword"));
        val participantId = Participant.save(participant).participantId
        Participant.authenticate("tester","testpassword") must beSome[Participant]
        val newPassword = Participant.resetPassword(participantId)
        Participant.authenticate("tester","testpassword") must beNone
        Participant.authenticate("tester",newPassword) must beSome[Participant]
      }
    }



  }

}
