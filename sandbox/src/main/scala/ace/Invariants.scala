package ace

class InvariantTable() {

  def update(name: String, predicate: => Boolean) = ???

}

trait Invariants {
  val invariant = new InvariantTable()
}