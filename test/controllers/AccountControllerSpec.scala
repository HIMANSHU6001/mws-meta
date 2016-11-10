package controllers

import models.Account
import org.specs2.mock.Mockito
import play.api.libs.json.Json
import play.api.mvc._
import play.api.test._
import repo.AccountRepository
import utils.JsonFormat._

import scala.concurrent.Future

class AccountControllerSpec extends PlaySpecification with Mockito with Results {

  val mockedRepo = mock[AccountRepository]
  val accountController = new AccountController(mockedRepo)

  "AccountController " should {

    "signUp a account" in {
        val account = Account("Orestis", "1234")
        mockedRepo.signUp(account) returns Future.successful(1)

        val result = accountController.signUp().apply(FakeRequest().withBody(Json.toJson(account)))
        val resultAsString = contentAsString(result)
        resultAsString === """{"status":"success","data":{"id":1},"msg":"Account has been created successfully."}"""
    }

    "get all list" in {
      val account = Account("Orestis", "1234")
      mockedRepo.getAll returns Future.successful(List(account))
      val result = accountController.list().apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString === """{"status":"success","data":[{"name":"Orestis","password":"1234"}],"msg":"Getting Account list successfully"}"""
    }

  }

}
