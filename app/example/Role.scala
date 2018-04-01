package example

import model.Model
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Role(id: Int, name: String) extends Model[Int]


object RoleFormat {
  val reads: Reads[Role] = Json.reads[Role]
  val writes: OWrites[Role] = Json.writes[Role]
}

object RoleMongoFormat {
  def reads: Reads[Role] = (
    (__ \"_id").read[Int] and
      (__ \ "name").read[String]
  ) (Role.apply(_, _))

  def writes(role: Role): JsObject = Json.obj(
    "_id" -> role.id,
    "name" -> role.name
    )
}
