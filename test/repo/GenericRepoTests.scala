package repo

import mock.{FilledCollection, MockModel, MockRepo, WithMongoSetup}
import model.{Page, Pagination}
import org.junit.runner.RunWith
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import play.api.inject.guice.GuiceApplicationBuilder
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.Await
import scala.concurrent.duration.Duration

@RunWith(classOf[JUnitRunner])
class GenericRepoTests extends Specification {
  sequential

  "Generic repo" should {
    val appBuilder = new GuiceApplicationBuilder().build
    val reactiveMongoApi = appBuilder.injector.instanceOf[ReactiveMongoApi]
    val mockRepo = new MockRepo()(reactiveMongoApi)

    "return empty list on findAll empty collection" in new WithMongoSetup {
      Await.result(mockRepo.findAll(), Duration(1, "seconds")) must beEqualTo(List())
    }

    "save an object" in new WithMongoSetup {
      val mock = MockModel("123","Koen", 29)
      Await.result(mockRepo.create(mock), Duration(1, "seconds")) must beAnInstanceOf[MockModel]
      Await.result(mockRepo.findById("123"), Duration(1, "seconds")) must beSome(mock)
    }

    "update an object" in new WithMongoSetup {
      val mock = MockModel("123","Koen", 29)
      Await.result(mockRepo.create(MockModel("123", "Koen", 29)), Duration(1, "seconds")) must beAnInstanceOf[MockModel]
      Await.result(mockRepo.update(MockModel("123", "Jef", 30)), Duration(1, "seconds")) must beAnInstanceOf[MockModel]
      Await.result(mockRepo.findById("123"), Duration(1, "seconds")) must beSome(mock.copy(name = "Jef", age=30))
    }

    "return a List of all elements in the collection on findAll" in new FilledCollection {
      Await.result(mockRepo.findAll(), Duration(1, "seconds")) must beEqualTo(List(
        MockModel("a", "Zoe", 23),
        MockModel("b", "Luke", 28),
        MockModel("c", "Charles", 44),
        MockModel("d", "Robert", 60),
        MockModel("e", "John", 35),
        MockModel("f", "Sarah", 39),
        MockModel("g", "Kim", 18)
      ))
    }

    "return a List of all elements in the collection on findAllOrdered by name desc returns a sorted list" in new FilledCollection {
      Await.result(mockRepo.findAllOrdered("-name"), Duration(1, "seconds")) must beEqualTo(List(
        MockModel("a", "Zoe", 23),
        MockModel("f", "Sarah", 39),
        MockModel("d", "Robert", 60),
        MockModel("b", "Luke", 28),
        MockModel("g", "Kim", 18),
        MockModel("e", "John", 35),
        MockModel("c", "Charles", 44)
      ))
    }

    "return a Page of 3 elements in the collection on findAllOrderedAndPaginated sorted by lastname desc, page 2 size 3" in new FilledCollection {
      Await.result(mockRepo.findAllOrderedAndPaginated("-name", Pagination(2, 3)), Duration(1, "seconds")) must beEqualTo(
        Page(2, 3, 7, List(
          MockModel("b", "Luke", 28),
          MockModel("g", "Kim", 18),
          MockModel("e", "John", 35))
        ))
    }

    "return a List of the single matching element on findAllByField without pagination" in new FilledCollection {
      Await.result(mockRepo.findAllByField("name", "Luke"), Duration(1, "seconds")) must beEqualTo(
        List(MockModel("b", "Luke", 28)))
    }

    "return a Page with the single matching element on findAllByField with pagination" in new FilledCollection {
      Await.result(mockRepo.findAllByField("name", "Luke", Pagination(1, 5)), Duration(1, "seconds")) must
        beEqualTo(Page(1, 5, 1, List(MockModel("b", "Luke", 28))))
    }

  }
}
