package utils

import models._
import play.api.libs.json.Json

object JsonFormat {

  implicit val bookFormat = Json.format[Book]
  implicit val accountFormat = Json.format[Account]

}


