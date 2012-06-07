package notifiers

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import models._
import play.Logger

class EmailSpec extends Specification {

  val testUser = Participant(0,"tester",Some("Test User"), Some("test@example.com"), Some("testpassword"))

  "Notifications" should {
    "be send on registration" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        EmailNotifier.registrationNotification(testUser)
        1 must beGreaterThan(0)
      }
    }
  }




}
