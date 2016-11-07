package repo

import javax.inject.{ Inject, Singleton }
import models.Book
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile
import scala.concurrent.Future

@Singleton()
class BookRepository @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends BookTable with HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def insert(book: Book): Future[Int] = db.run { booksInc += book }

  def insertAll(books: List[Book]): Future[Seq[Int]] = db.run { booksInc ++= books }

  def update(book: Book): Future[Int] = db.run { books.filter(_.id === book.id).update(book) }

  def delete(id: Int): Future[Int] = db.run { books.filter(_.id === id).delete }

  def getAll(): Future[List[Book]] = db.run { books.to[List].result }

  def getById(empId: Int): Future[Option[Book]] = db.run { books.filter(_.id === empId).result.headOption }

  def ddl = books.schema

}

private[repo] trait BookTable  { self: HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  class BookTable(tag: Tag) extends Table[Book](tag, "book") {
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    val title: Rep[String] = column[String]("title", O.SqlType("VARCHAR(200)"))
    val author: Rep[String] = column[String]("author", O.SqlType("VARCHAR(200)"))
    val year: Rep[Int] = column[Int]("year")
    def titleUnique = index("title_unique", title, unique = true)
    def * = (title, author, year, id.?) <> (Book.tupled, Book.unapply)
  }

  lazy protected val books = TableQuery[BookTable]

  lazy protected val booksInc = books returning books.map(_.id)

}

