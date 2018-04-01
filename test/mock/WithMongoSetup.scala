package mock

import org.specs2.mutable.Before
import play.api.inject.guice.GuiceApplicationBuilder
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

class WithMongoSetup extends Before {
  override def before: Any = {
    val appBuilder = new GuiceApplicationBuilder().build
    val reactiveMongoApi = appBuilder.injector.instanceOf[ReactiveMongoApi]
    val mockRepo = new MockRepo()(reactiveMongoApi)
    Await.result(mockRepo.deleteAll(), Duration(2, "seconds"))
  }
}


class FilledCollection extends Before {
  override def before: Any = {
    val appBuilder = new GuiceApplicationBuilder().build
    val reactiveMongoApi = appBuilder.injector.instanceOf[ReactiveMongoApi]
    val mockRepo = new MockRepo()(reactiveMongoApi)
    Await.result(
      mockRepo.deleteAll()
        .flatMap( _ => mockRepo.create(MockModel("a", "Zoe", 23)))
        .flatMap( _ => mockRepo.create(MockModel("b", "Luke", 28)))
        .flatMap( _ => mockRepo.create(MockModel("c", "Charles", 44)))
        .flatMap( _ => mockRepo.create(MockModel("d", "Robert", 60)))
        .flatMap( _ => mockRepo.create(MockModel("e", "John", 35)))
        .flatMap( _ => mockRepo.create(MockModel("f", "Sarah", 39)))
        .flatMap( _ => mockRepo.create(MockModel("g", "Kim", 18)))
      , Duration(2, "seconds"))
  }
}
