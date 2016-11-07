package models

case class Book(title: String, author: String, year: Int, id: Option[Int] = None)
