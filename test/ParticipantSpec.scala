package models

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import models._

class ParticipantSpec extends Specification {


  "A Participant" should {

    "be able to be created and persisted" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val participant = Participant(0,"testuser","Test User", "test@example.com", Some("testpassword"));
        Participant.save(participant)
        participant.participantId.toInt must beGreaterThan(0)
      }
    }



  }

}
