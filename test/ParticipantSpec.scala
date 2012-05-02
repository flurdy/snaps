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



  }

}
