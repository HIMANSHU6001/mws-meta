package controllers

import javax.inject.Inject

import jp.t2v.lab.play2.auth.AuthElement
import play.api.mvc._
import repo.{AccountRepository, BookRepository}
import views.html

class Application @Inject()(val database: AccountRepository,
                            implicit val webJarAssets: WebJarAssets)
  extends Controller with AuthConfigTrait with AuthElement {

  def index = StackAction { implicit request =>
    Ok(html.index(loggedIn))
  }

}
