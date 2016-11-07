package bootstrap

import com.google.inject.Inject
import repo.BookRepository
import models.Book
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.Logger
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class InitialData @Inject() (bookRepo: BookRepository) {

  def insert = for {
    books <- bookRepo.getAll() if books.isEmpty
    _ <- bookRepo.insertAll(Data.employees)
  } yield {}

  try {
    Logger.info("DB initialization.................")
    Await.result(insert, Duration.Inf)
  } catch {
    case ex: Exception =>
      Logger.error("Error in database initialization ", ex)
  }

}

object Data {
  val employees = List(
    Book("The Rebel", "Albert Camus", 1951)
  )
}
