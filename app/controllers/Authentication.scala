package controllers

import javax.inject.{Inject, Singleton}

import jp.t2v.lab.play2.auth._
import models.FormData
import models.Account
import utils.Constants

import play.api.Logger
import play.api.libs.json.Json._
import play.api.libs.json.JsValue
import play.api.mvc.Results.Redirect
import play.api.mvc.{Action, Controller, RequestHeader, Result}
import repo.AccountRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.{ClassTag, classTag}


/**
  * Handles all requests related to authentication
  */
@Singleton
class Authentication @Inject()(val database: AccountRepository, implicit val webJarAssets: WebJarAssets)
  extends Controller with AuthConfigTrait with OptionalAuthElement with LoginLogout {

  import Constants._

  private def successResponse(data: JsValue, message: String) = {
    obj("status" -> SUCCESS, "data" -> data, "msg" -> message)
  }

  def prepareLogin() = StackAction { implicit request =>
    if (loggedIn.isDefined)
      Redirect(routes.Application.index())
    else
      Ok(views.html.authentication.login(FormData.loginForm))
  }

  def logout = Action.async { implicit request => gotoLogoutSucceeded }

  def login() = Action.async { implicit request =>
    FormData.loginForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.authentication.login(formWithErrors))),
      account => {
        database.getByName(account.name).flatMap {
          case None =>
            Logger.warn(s"Wrong user")
            val form = FormData.loginForm.fill(account).withError("email", "Invalid user")
            Future.successful(BadRequest(views.html.authentication.login(form)))
          case Some(user) =>
            Logger.info(s"Pass: ${user.password}")

            if (account.password == user.password) {
              Logger.info(s"Login by ${account.name}")
              gotoLoginSucceeded(user.name)
            }
            else {
              Logger.warn(s"Wrong login credentials!")
              val form = FormData.loginForm.fill(account).withError("password", "Invalid password")
              Future.successful(BadRequest(views.html.authentication.login(form)))
            }
        }
      }
    )
  }

}

private[controllers] trait AuthConfigTrait extends AuthConfig {

  def database: AccountRepository

  type Id = String

  type User = Account

  type Authority = Int

  /**
    * A `ClassTag` is used to retrieve an id from the Cache API.
    * Use something like this:
    */
  val idTag: ClassTag[Id] = classTag[Id]

  /**
    * The session timeout in seconds
    */
  val sessionTimeoutInSeconds: Int = 3600

  /**
    * A function that returns a `User` object from an `Id`.
    * You can alter the procedure to suit your application.
    */
  def resolveUser(id: Id)(implicit ctx: ExecutionContext): Future[Option[User]] =
    database.getByName(id)

  /**
    * Where to redirect the user after a successful login.
    */
  def loginSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] =
    // TODO Hide modal
    Future.successful(Redirect(routes.Application.index()))

  /**
    * Where to redirect the user after logging out
    */
  def logoutSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] =
    // TODO Hide modal
    Future.successful(Redirect(routes.Authentication.login()))

  /**
    * If the user is not logged in and tries to access a protected resource then redirect them as follows:
    */
  def authenticationFailed(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] =
    Future.successful(Redirect(routes.Authentication.login()))

  /**
    * If authorization failed (usually incorrect password) redirect the user as follows:
    */
  override def authorizationFailed(request: RequestHeader, user: User, authority: Option[Authority])
                                  (implicit context: ExecutionContext): Future[Result] =
    Future.successful(Redirect(routes.Authentication.login()))


  /**
    * A function that determines what `Authority` a user has.
    * You should alter this procedure to suit your application.
    */
  def authorize(user: User, authority: Authority)(implicit ctx: ExecutionContext): Future[Boolean] =
    Future.successful(true)

}

