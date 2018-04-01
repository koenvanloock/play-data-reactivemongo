package model

case class Pagination(pageNumber: Int, pageSize: Int) {
  def skip: Int = (pageNumber - 1) * pageSize
}
