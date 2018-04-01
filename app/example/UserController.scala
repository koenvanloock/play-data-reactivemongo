package example

import java.util.UUID
import javax.inject.Inject

import model.PageWrites
import play.api.libs.functional.syntax._
import play.api.libs.json.{Json, __}
import play.api.mvc.InjectedController

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import PageWrites._

class UserController @Inject()(userRepo: UserRepo, roleRepo: RoleRepo) extends InjectedController {

  import model.JsonUtils._
  implicit val userReads =  (
    (__ \ "firstname").read[String] and
    (__ \ "lastname").read[String]
  ) (User(UUID.randomUUID.toString,_,_))

  implicit val roleReads =  (
    (__ \ "id").read[Int] and
    (__ \ "name").read[String]
  ) (Role(_,_))

  def getUsers(pageNr: Option[Int], pageSize: Option[Int]) = {
    val page = pageNr.getOrElse(1)
    val size = pageSize.getOrElse(5)
    Action.async(userRepo.findAll(page, size).map(users => Ok(Json.toJson(users)(PageWrites.searchResultsWrites(Json.writes[User])))))
  }

  def insertUser() = Action.async(request =>
    request.body.asJson.flatMap(json => Json.fromJson(json)(userReads).asOpt.map(user => userRepo.create(user))) match {
      case Some(userfut: Future[User]) =>  userfut.map(user => Ok(Json.toJson(user)(UserEvidence.restFormat.writes)))
      case _ => Future.successful(BadRequest("No valid user content"))
    })

  def getUser(id: String) = Action.async(userRepo.findById(id).map{
    case Some(user) => Ok(Json.toJson(user)(UserEvidence.restFormat.writes))
    case _ => NotFound
  })

  def getByLastName() = Action.async(userRepo.sortedByLastName.map(users => Ok(Json.toJson(users)(searchResultsWrites(UserEvidence.restFormat.writes)))))

  def drop = Action.async(userRepo.deleteAll().map( if(_) NoContent else NotFound))


  def insertRole() = Action.async{ request =>
    request.body.asJson.flatMap( roleJson => Json.fromJson(roleJson)(roleReads).asOpt.map(role => roleRepo.create(role))) match {
      case Some(roleFut: Future[Role]) =>  roleFut.map(role => Ok(Json.toJson(role)(RoleFormat.writes)))
      case _ => Future.successful(BadRequest("No valid user content"))
    }
  }
}
