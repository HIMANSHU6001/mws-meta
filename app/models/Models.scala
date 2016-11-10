package models

case class Book(title: String, author: String, year: Int, accountId: String, id: Option[Int] = None)
case class Account(name: String, password: String)
