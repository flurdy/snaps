package models

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import models._

class EventModelSpec extends Specification {

  "An Event" should {

    "be able to be created and persisted" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val event = Event.createEvent("asasasa")
        event.eventId.toInt must beGreaterThan(0)
        Event.findEvent(event.eventId).get.eventName must beEqualTo("asasasa")
      }
    }

    "not be able to be created if null name" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        Event.createEvent(null) must throwAn[NullPointerException]
      }
    }

    "not be able to be created if no name" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        Event.createEvent("") must throwAn[IllegalArgumentException]
      }
    }

    "not be able to be created if space name" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        Event.createEvent(" ") must throwAn[IllegalArgumentException]
      }
    }

    "be able to be found" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val event = Event.createEvent("asasasa")
        Event.findEvent(event.eventId) must beSome
      }
    }

    "not be able to be find non existant" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val event = Event.createEvent("asasasa")
        Event.findEvent(event.eventId) must beSome
        Event.findEvent(event.eventId+1) must beNone
      }
    }

    "be able to be update its name" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val event1 = Event.createEvent("Christmas")
        Event.findEvent(event1.eventId).get.eventName must beEqualTo("Christmas")
        val event2 = event1.copy(eventName="Easter")
        Event.updateEvent(event2)
        Event.findEvent(event1.eventId).get.eventName must beEqualTo("Easter")
      }
    }

    "not be able to be update its name to nothing" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val event1 = Event.createEvent("Christmas")
        event1.copy(eventName="") must throwAn[IllegalArgumentException]
      }
    }

    "be able to be deleted" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val event = Event.createEvent("Christmas")
        Event.findEvent(event.eventId) must beSome
        Event.deleteEvent(event.eventId)
        Event.findEvent(event.eventId) must beNone
      }
    }

  }

  "An Album" should {

    "be able to be added to event" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val event = Event.createEvent("asasasa")
        event.addAlbum(new Album("John","http://bbb.com"))
        val event2 = Event.findEventWithAlbums(event.eventId).get
        event2.albums must have size(1)
        val albumId = event2.albums.head.albumId
        Album.findAlbum(event.eventId,albumId).get.publisher must beEqualTo("John")
      }
    }

    "not be able to be add with invalid data" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val event = Event.createEvent("asasasa")
        event.addAlbum(new Album("","http://bbb.com")) must throwAn[IllegalArgumentException]
        event.addAlbum(new Album("John","http://")) must throwAn[IllegalArgumentException]
      }
    }

    "be able to be find event with albums" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val event = Event.createEvent("asasasa")
        event.addAlbum(new Album("John","http://bbb.com"))
        event.addAlbum(new Album("Sue","http://aaa.com"))
        Event.findEventWithAlbums(event.eventId).get.albums must have size(2)
      }
    }


    "be able to be find album" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val event1 = Event.createEvent("asasasa")
        event1.addAlbum(new Album("John","http://bbb.com"))
        val event2 = Event.findEventWithAlbums(event1.eventId).get
        event1.findAlbum(event2.albums.head.albumId) must beSome
        Album.findAlbum(event1.eventId,event2.albums.head.albumId) must beSome
      }
    }


    "be able to be update album" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val event1 = Event.createEvent("asasasa")
        event1.addAlbum(new Album("John","http://bbb.com"))
        val event2 = Event.findEventWithAlbums(event1.eventId).get
        var album = event2.albums.head
        Album.findAlbum(event1.eventId,album.albumId).get.publisher must beEqualTo("John")
        album = event2.albums.head.copy(publisher="Oliver")
        Album.updateAlbum(album)
        Event.findEventWithAlbums(event1.eventId).get.albums must have size(1)
        Album.findAlbum(event1.eventId,album.albumId).get.publisher must beEqualTo("Oliver")
      }
    }

    "not be able to be update with invalid data" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val event = Event.createEvent("asasasa")
        event.addAlbum(new Album("John","http://bbb.com"))
        val album = Event.findEventWithAlbums(event.eventId).get.albums.head
        album.copy(publisher = "") must throwAn[IllegalArgumentException]
        album.copy(url = "http") must throwAn[IllegalArgumentException]
      }
    }


    "be able to be remove album" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val event1 = Event.createEvent("asasasa")
        event1.addAlbum(new Album("John","http://bbb.com"))
        Event.findEventWithAlbums(event1.eventId).get.albums must have size(1)
        val event2 = Event.findEventWithAlbums(event1.eventId).get
        val album = event2.albums.head
        event2.removeAlbum(album)
        Event.findEventWithAlbums(event1.eventId).get.albums must have size(0)
        Album.findAlbum(event1.eventId,album.albumId) must beNone
      }
    }


  }

}
