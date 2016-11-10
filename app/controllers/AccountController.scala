package controllers

import com.google.inject.Inject
import models._
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json._
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc._
import repo.AccountRepository
import utils.Constants
import utils.JsonFormat._

import scala.concurrent.Future

/**
  * Handles all requests related to accounts
  */
class AccountController @Inject()(accountRepo: AccountRepository) extends Controller {

  import Constants._

  val logger = Logger(this.getClass)

  private def successResponse(data: JsValue, message: String) = {
    obj("status" -> SUCCESS, "data" -> data, "msg" -> message)
  }

  /**
    * Handles request for getting all accounts from the database
    */
  def list() = Action.async {
    accountRepo.getAll.map { res =>
      logger.info("Account list: " + res)
      Ok(successResponse(Json.toJson(res), "Getting Account list successfully"))
    }
  }

  /**
    * Handles request for creation of new account
    */
  def signUp() = Action.async(parse.json) { request =>
    logger.info("Account Json ===> " + request.body)
    request.body.validate[Account].fold(
      error => Future.successful(BadRequest(JsError.toJson(error))),
      { account =>
          accountRepo.signUp(account).map { createdAccountId =>
            Ok(successResponse(Json.toJson(
              Map(
                "id" ->createdAccountId)),
                "Account has been created successfully."
            ))
          }
      })
  }

}



