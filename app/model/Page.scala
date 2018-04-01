package model

case class Page[T](pageNumber: Int, elementsPerPage: Int, totalSize: Int, data: List[T])
