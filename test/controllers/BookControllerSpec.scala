package controllers


import models.Book
import org.specs2.mock.Mockito
import play.api.libs.json.Json
import play.api.mvc._
import play.api.test._
import repo.BookRepository
import utils.JsonFormat._

import scala.concurrent.Future

class BookControllerSpec extends PlaySpecification with Mockito with Results {

  val mockedRepo = mock[BookRepository]
  val bookController = new BookController(mockedRepo)

  "BookController " should {

    "create a book" in {
        val book = Book("First book", "Orestiss Melkoniann", 2016)
        mockedRepo.insert(book) returns Future.successful(1)

        val result = bookController.create().apply(FakeRequest().withBody(Json.toJson(book)))
        val resultAsString = contentAsString(result)
        resultAsString === """{"status":"success","data":{"id":1},"msg":"Book has been created successfully."}"""
      }

    "update a book" in {
      val updatedBook = Book("First book", "Orestis Melkonian", 2016, Some(1))
      mockedRepo.update(updatedBook) returns Future.successful(1)

      val result = bookController.update().apply(FakeRequest().withBody(Json.toJson(updatedBook)))
      val resultAsString = contentAsString(result)
      resultAsString === """{"status":"success","data":"{}","msg":"Book has been updated successfully."}"""
    }

    "edit a book" in {
      val book = Book("First book", "Orestis Melkonian", 2016, Some(1))
      mockedRepo.getById(1) returns Future.successful(Some(book))
      val result = bookController.edit(1).apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString === """{"status":"success","data":{"name":"First book","author":"Orestis Melkonian","year":2016,"id":1},"msg":"Getting Book successfully"}"""
    }

    "delete a book" in {
      mockedRepo.delete(1) returns Future.successful(1)
      val result = bookController.delete(1).apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString === """{"status":"success","data":"{}","msg":"Book has been deleted successfully."}"""
    }
    "get all list" in {
      val book = Book("First book", "Orestis Melkonian", 2016, Some(1))
      mockedRepo.getAll() returns Future.successful(List(book))
      val result = bookController.list().apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString === """{"status":"success","data":[{"name":"First book","author":"Orestis Melkonian","year":2016,"id":1}],"msg":"Getting Book list successfully"}"""
    }

  }

}
