package models

case class Book(name: String, author: String, year: Int, id: Option[Int] = None)
