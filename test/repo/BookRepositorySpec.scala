package repo


import org.specs2.execute.{AsResult, Result}
import org.specs2.specification.{AroundEach, BeforeEach}
import play.api.Application
import play.api.test.{PlaySpecification, WithApplication}

import scala.concurrent.duration.Duration
import scala.concurrent.Await
import scala.concurrent.Future


class BookRepositorySpec extends PlaySpecification {

  import models._

  "Book repository" should {

    def bookRepo(implicit app: Application) = Application.instanceCache[BookRepository].apply(app)


    "get all rows" in new WithApplication()  {
      val result = await(bookRepo.getAll())
      result.length === 1
      result.head.title === "My first book"
    }

    "get single rows" in new WithApplication() {
      val result = await(bookRepo.getById(1))
      result.isDefined === true
      result.get.title === "My first book"
    }

    "insert a row" in new WithApplication()  {
      val knolId = await(bookRepo.insert(Book("My second book", "Orestis Melkonian", 2016)))
      knolId === 2
    }

    "insert multiple rows" in new  WithApplication()  {
      val result = bookRepo.insertAll(List(
        Book("My third book", "Orestis Melkonian", 2016),
        Book("My fourth book", "Orestis Melkonian", 2016)
      ))
      val knolIds = await(result)
      knolIds === Seq(2, 3)
    }

    "update a row" in new  WithApplication()  {
      val result = await(
        bookRepo.update(Book("My first book ever", "Orestis Melkonian", 2016, Some(1)))
      )
      result === 1
    }

    "delete a row" in new  WithApplication()  {
      val result = await(bookRepo.delete(1))
      result === 1
    }
  }

  def await[T](v: Future[T]): T = Await.result(v, Duration.Inf)

}
