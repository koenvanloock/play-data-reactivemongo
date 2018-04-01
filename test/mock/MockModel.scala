package mock

import model.Model
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class MockModel(id: String, name: String, age: Int) extends Model[String]


object ModelEvidence {

  private val format = new Format[MockModel]{
    override def writes(o: MockModel): JsValue = Json.obj(
      "_id" -> o.id,
      "name" -> o.name,
      "age" -> o.age)

    override def reads(json: JsValue): JsResult[MockModel] = (
      (__ \ "_id").read[String] and
        (__ \ "name").read[String] and
        (__ \ "age").read[Int]
      ) (MockModel).reads(json)
  }


  def writes(o: MockModel): JsObject = format.writes(o).asInstanceOf[JsObject]

  def reads(json: JsValue): JsResult[MockModel] = format.reads(json)
}