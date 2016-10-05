package controllers

import com.google.inject.Inject
import models._
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.libs.json.Json._
import play.api.mvc._
import repo.BookRepository
import utils.Constants
import utils.JsonFormat._

import scala.concurrent.Future

/**
  * Handles all requests related to book
  */
class BookController @Inject()(bookRepo: BookRepository) extends Controller {

  import Constants._

  val logger = Logger(this.getClass)

  private def successResponse(data: JsValue, message: String) = {
    obj("status" -> SUCCESS, "data" -> data, "msg" -> message)
  }

  /**
    * Handles request for getting all book from the database
    */
  def list() = Action.async {
    bookRepo.getAll().map { res =>
      logger.info("Book list: " + res)
      Ok(successResponse(Json.toJson(res), "Getting Book list successfully"))
    }
  }

  /**
    * Handles request for creation of new book
    */
  def create() = Action.async(parse.json) { request =>
    logger.info("Book Json ===> " + request.body)
    request.body.validate[Book].fold(error => Future.successful(BadRequest(JsError.toJson(error))), { book =>
      bookRepo.insert(book).map { createdBookId =>
        Ok(successResponse(Json.toJson(Map("id" ->createdBookId)), "Book has been created successfully."))
      }
    })
  }

  /**
    * Handles request for deletion of existing book by book_id
    */
  def delete(bookId: Int) = Action.async { request =>
    bookRepo.delete(bookId).map { _ =>
      Ok(successResponse(Json.toJson("{}"), "Book has been deleted successfully."))
    }
  }

  /**
    * Handles request for get book details for editing
    */
  def edit(bookId: Int): Action[AnyContent] = Action.async { request =>
    bookRepo.getById(bookId).map { bookOpt =>
      bookOpt.fold(Ok(obj("status" -> ERROR, "data" -> "{}", "msg" -> "Book does not exist.")))(book => Ok(
        successResponse(Json.toJson(book), "Getting Book successfully")))
    }
  }

  /**
    * Handles request for update existing book
    */
  def update = Action.async(parse.json) { request =>
    logger.info("Book Json ===> " + request.body)
    request.body.validate[Book].fold(error => Future.successful(BadRequest(JsError.toJson(error))), { book =>
      bookRepo.update(book).map { res => Ok(successResponse(Json.toJson("{}"), "Book has been updated successfully.")) }
    })
  }

}



