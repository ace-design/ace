package ace

import scala.annotation.tailrec

trait ModelElement {

  val _identifier: String

}

trait Action[M <: ModelElement] {
  def execute(before: M): M
}

trait Engine[M <: ModelElement] {
  @tailrec final def execute(m: M, actions: Seq[Action[M]]): M = actions match {
    case Nil => m
    case a :: as => execute(a.execute(m), as)
  }
}

trait Query[T] {
  def run[M <: ModelElement](m: M): T
}

trait QueryManager[M <: ModelElement] {
  def execute[T](m: M, q: Query[T]): T = q.run(m)
}


trait GraphAction

