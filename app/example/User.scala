package example

import model.Model
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

case class User(id: String, firstname: String, lastname: String) extends Model[String]


object UserEvidence {

  implicit object mongoFormat {
    def writes(o: User): JsObject = Json.obj(
      "_id" -> o.id,
      "firstname" -> o.firstname,
      "lastname" -> o.lastname)

    def reads(json: JsValue): JsResult[User] = (
      (__ \ "_id").read[String] and
        (__ \ "firstname").read[String] and
        (__ \ "lastname").read[String]
      ) (User.apply(_, _, _)).reads(json)
  }


  implicit object restFormat {
    def writes(o: User): JsObject = Json.obj(
      "id" -> o.id,
      "firstname" -> o.firstname,
      "lastname" -> o.lastname)

    def reads(json: JsValue): JsResult[User] = (
      (__ \ "id").read[String] and
        (__ \ "firstname").read[String] and
        (__ \ "lastname").read[String]
      ) (User.apply(_, _, _)).reads(json)
  }

}


