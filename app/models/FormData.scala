package models

import play.api.data.Form
import play.api.data.Forms._

object FormData {

  val loginForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "password" -> nonEmptyText
    )(Account.apply)(Account.unapply)
  )

}