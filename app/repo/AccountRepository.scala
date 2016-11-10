package repo

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile
import scala.concurrent.Future
import models.Account


@Singleton()
class AccountRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends AccountTable with HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def signUp(account: Account): Future[Int] =
    db.run { accounts += account }

  def getByName(name: String): Future[Option[Account]] =
    db.run { accounts.filter(_.name === name).result.headOption }

  def getAll: Future[List[Account]] =
    db.run { accounts.to[List].result }

  def ddl = accounts.schema

}

private[repo] trait AccountTable  { self: HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  class AccountTable(tag: Tag) extends Table[Account](tag, "account") {
    val name: Rep[String] = column[String]("name", O.PrimaryKey, O.SqlType("VARCHAR(200)"))
    var password: Rep[String] = column[String]("password", O.SqlType("VARCHAR(200)"))
    def * = (name, password) <> (Account.tupled, Account.unapply)
  }

  lazy protected val accounts = TableQuery[AccountTable]

  lazy protected val accountsInc = accounts returning accounts.map(_.name)

}

