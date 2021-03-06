package models

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import models._
import org.specs2.specification.BeforeExample

class EventSpec extends Specification {

  val testUser = Participant(0,"tester",Some("Test User"), "test@example.com", Some("testpassword"))

  "An Event" should {

    "be able to be created and persisted" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val event = Event.createAndSaveEvent("asasasa",1)
        event.eventId.toInt must beGreaterThan(0)
        Event.findEvent(event.eventId).get.eventName must beEqualTo("asasasa")
      }
    }

    "not be able to be created if null name" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val eventName : String = null
        val testerId = Participant.save( testUser ).participantId
        Event.createAndSaveEvent(eventName,testerId) must throwAn[NullPointerException]
      }
    }

    "not be able to be created if no name" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val testerId = Participant.save( testUser ).participantId
        Event.createAndSaveEvent("",testerId) must throwAn[IllegalArgumentException]
      }
    }

    "not be able to be created if space name" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val testerId = Participant.save( testUser ).participantId
        Event.createAndSaveEvent(" ",testerId ) must throwAn[IllegalArgumentException]
      }
    }

    "be able to be found" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val testerId = Participant.save( testUser ).participantId
        val event = Event.createAndSaveEvent("asasasa",testerId)
        Event.findEvent(event.eventId) must beSome
      }
    }

    "not be able to be find non existant" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val testerId = Participant.save( testUser ).participantId
        val event = Event.createAndSaveEvent("asasasa",testerId)
        Event.findEvent(event.eventId) must beSome
        Event.findEvent(event.eventId+1) must beNone
      }
    }

    "be able to be update its name" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val testerId = Participant.save( testUser ).participantId
        val event1 = Event.createAndSaveEvent("Christmas",testerId)
        Event.findEvent(event1.eventId).get.eventName must beEqualTo("Christmas")
        val event2 = event1.copy(eventName="Easter")
        Event.updateEvent(event2)
        Event.findEvent(event1.eventId).get.eventName must beEqualTo("Easter")
      }
    }

    "not be able to be update its name to nothing" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val testerId = Participant.save( testUser ).participantId
        val event1 = Event.createAndSaveEvent("Christmas",testerId)
        event1.copy(eventName="") must throwAn[IllegalArgumentException]
      }
    }

    "be able to be deleted" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val testerId = Participant.save( testUser ).participantId
        val event = Event.createAndSaveEvent("Christmas",testerId)
        Event.findEvent(event.eventId) must beSome
        Event.deleteEvent(event.eventId)
        Event.findEvent(event.eventId) must beNone
      }
    }
//    "not have a huge description" in {
//      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
//        val longDescription = new StringBuilder("")
//        for(i <- 0 until 4000 ){
//          longDescription.append(' ')
//        }
//        val event = new Event("abc").copy(description = Some(longDescription.toString()))
//        Event.createAndSaveEvent(event) must throwAn[IllegalArgumentException]
//      }
    }

  }

class AlbumSpec extends Specification {

  val testUser = Participant(0,"tester",Some("Test User"), "test@example.com", Some("testpassword"))


  "An Album" should {

    "be able to be added to event" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val testerId = Participant.save( testUser ).participantId
        val event = Event.createAndSaveEvent("asasasa",testerId)
        event.addAlbum(new Album("John","http://bbb.com"))
        val event2 = Event.findEvent(event.eventId).get
        event2.findAlbums must have size(1)
        val albumId = event2.findAlbums.head.albumId
        Album.findAlbum(event.eventId,albumId).get.publisher must beEqualTo("John")
      }
    }

    "not be able to be add with invalid data" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val testerId = Participant.save( testUser ).participantId
        val event = Event.createAndSaveEvent("asasasa",testerId)
        event.addAlbum(new Album("","http://bbb.com")) must throwAn[IllegalArgumentException]
        event.addAlbum(new Album("John","http://")) must throwAn[IllegalArgumentException]
      }
    }

    "be able to be find event with albums" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val testerId = Participant.save( testUser ).participantId
        val event = Event.createAndSaveEvent("asasasa",testerId)
        event.addAlbum(new Album("John","http://bbb.com"))
        event.addAlbum(new Album("Sue","http://aaa.com"))
        Event.findEvent(event.eventId).get.findAlbums must have size(2)
      }
    }


    "be able to be find album" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val testerId = Participant.save( testUser ).participantId
        val event1 = Event.createAndSaveEvent("asasasa",testerId)
        event1.addAlbum(new Album("John","http://bbb.com"))
        val event2 = Event.findEvent(event1.eventId).get
        event1.findAlbum(event2.findAlbums.head.albumId) must beSome
        Album.findAlbum(event1.eventId,event2.findAlbums.head.albumId) must beSome
      }
    }


    "be able to be update album" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val testerId = Participant.save( testUser ).participantId
        val event1 = Event.createAndSaveEvent("asasasa",testerId)
        event1.addAlbum(new Album("John","http://bbb.com"))
        val event2 = Event.findEvent(event1.eventId).get
        var album = event2.findAlbums.head
        Album.findAlbum(event1.eventId,album.albumId).get.publisher must beEqualTo("John")
        album = event2.findAlbums.head.copy(publisher="Oliver")
        Album.updateAlbum(album)
        Event.findEvent(event1.eventId).get.findAlbums must have size(1)
        Album.findAlbum(event1.eventId,album.albumId).get.publisher must beEqualTo("Oliver")
      }
    }

    "not be able to be update with invalid data" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val testerId = Participant.save( testUser ).participantId
        val event = Event.createAndSaveEvent("asasasa",testerId)
        event.addAlbum(new Album("John","http://bbb.com"))
        val album = Event.findEvent(event.eventId).get.findAlbums.head
        album.copy(publisher = "") must throwAn[IllegalArgumentException]
        album.copy(url = "http") must throwAn[IllegalArgumentException]
      }
    }


    "be able to be remove album" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val testerId = Participant.save( testUser ).participantId
        val event1 = Event.createAndSaveEvent("asasasa",testerId)
        event1.addAlbum(new Album("John","http://bbb.com"))
        Event.findEvent(event1.eventId).get.findAlbums must have size(1)
        val event2 = Event.findEvent(event1.eventId).get
        val album = event2.findAlbums.head
        event2.removeAlbum(album)
        Event.findEvent(event1.eventId).get.findAlbums must have size(0)
        Album.findAlbum(event1.eventId,album.albumId) must beNone
      }
    }


  }

}
