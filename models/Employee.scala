package models
import org.joda.time.DateTime
import play.api.libs.json.{Format, Json}
import reactivemongo.play.json._
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson._
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._

case class Employee(
                  _id:Option[BSONObjectID],
                  _creationDate: Option[DateTime],
                  _updateDate: Option[DateTime],
                  name:String,
                  gender: String,
                  position: String,
                  salary: Int
                )
object Employee{
  implicit val fmt : Format[Employee] = Json.format[Employee]
  implicit object EmployeeBSONReader extends BSONDocumentReader[Employee] {
    def read(doc: BSONDocument): Employee = {
      Employee(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[BSONDateTime]("_creationDate").map(dt => new DateTime(dt.value)),
        doc.getAs[BSONDateTime]("_updateDate").map(dt => new DateTime(dt.value)),
        doc.getAs[String]("name").get,
        doc.getAs[String]("gender").get,
        doc.getAs[String]("position").get,
        doc.getAs[Int]("salary").get )
    }
  }

  implicit object EmployeeBSONWriter extends BSONDocumentWriter[Employee] {
    def write(employee: Employee): BSONDocument = {
      BSONDocument(
        "_id" -> employee._id,
        "_creationDate" -> employee._creationDate.map(date => BSONDateTime(date.getMillis)),
        "_updateDate" -> employee._updateDate.map(date => BSONDateTime(date.getMillis)),
        "name" -> employee.name,
        "gender"->employee.gender,
        "position"->employee.position,
        "salary"->employee.salary
      )
    }
  }
}
