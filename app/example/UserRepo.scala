package example

import javax.inject.Inject

import model.Pagination
import repo.GenericMongoRepo
import play.modules.reactivemongo.ReactiveMongoApi
import example.UserEvidence.mongoFormat
import play.api.libs.json.JsObject

import scala.concurrent.Future

class UserRepo @Inject()(implicit val reactiveMongoApi: ReactiveMongoApi)
  extends GenericMongoRepo[User, String]("user", mongoFormat.reads, mongoFormat.writes){

  def getByFirstname(firstname: String): Future[List[User]] = findAllByField("firstname", firstname)
  def sortedByLastName = retrieveAllByFieldsOrderedAndPaginated(None, "-lastname", Pagination(2, 5))
}
