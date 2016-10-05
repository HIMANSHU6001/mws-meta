package controllers


import models.Book
import org.specs2.mock.Mockito
import play.api.libs.json.Json
import play.api.mvc._
import play.api.test._
import repo.BookRepository
import utils.JsonFormat._

import scala.concurrent.Future

class BookControllerSpec  extends PlaySpecification with Mockito with Results {

  val mockedRepo = mock[BookRepository]
  val employeeController= new BookController(mockedRepo)

  "BookController " should {

    "create a employee" in {
        val emp = Book("sky", "sky@knoldus.com", "knoldus", "Senior Consultant")
        mockedRepo.insert(emp) returns Future.successful(1)

        val result = employeeController.create().apply(FakeRequest().withBody(Json.toJson(emp)))
        val resultAsString = contentAsString(result)
        resultAsString === """{"status":"success","data":{"id":1},"msg":"Book has been created successfully."}"""
      }

    "update a employee" in {
      val updatedEmp = Book("Satendra", "sky@knoldus.com", "knoldus", "Senior Consultant", Some(1))
      mockedRepo.update(updatedEmp) returns Future.successful(1)

      val result = employeeController.update().apply(FakeRequest().withBody(Json.toJson(updatedEmp)))
      val resultAsString = contentAsString(result)
      resultAsString === """{"status":"success","data":"{}","msg":"Book has been updated successfully."}"""
    }

    "edit a employee" in {
      val emp = Book("sky", "sky@knoldus.com", "knoldus", "Senior Consultant",Some(1))
      mockedRepo.getById(1) returns Future.successful(Some(emp))
      val result = employeeController.edit(1).apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString === """{"status":"success","data":{"name":"sky","email":"sky@knoldus.com","companyName":"knoldus","position":"Senior Consultant","id":1},"msg":"Getting Book successfully"}"""
    }

    "delete a employee" in {
      mockedRepo.delete(1) returns Future.successful(1)
      val result = employeeController.delete(1).apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString === """{"status":"success","data":"{}","msg":"Book has been deleted successfully."}"""
    }
    "get all list" in {
      val emp = Book("sky", "sky@knoldus.com", "knoldus", "Senior Consultant",Some(1))
      mockedRepo.getAll() returns Future.successful(List(emp))
      val result = employeeController.list().apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString === """{"status":"success","data":[{"name":"sky","email":"sky@knoldus.com","companyName":"knoldus","position":"Senior Consultant","id":1}],"msg":"Getting Book list successfully"}"""
    }

  }

}
