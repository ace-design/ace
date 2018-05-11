package social_network

// Abstract
trait Composable { }

trait EquivalenceRelation[T <: Composable] {
  def equivalent(o1: T, o2: T): Boolean
}

trait Merger[T <: Composable] { def merge(o1: T, o2: T): T }

trait Absorber[T <: Composable] { def absorb(o1: T, o2: T): T }

trait PersonComposer
  extends Merger[Person] with Absorber[Person] with EquivalenceRelation[Person]



trait Equivalence[T <: Composable] { self: T =>
  val relation: EquivalenceRelation[T]
  def ===(that: T): Boolean = relation.equivalent(this, that)
  final def =!=(that: T): Boolean = !(this === that)
}


trait Mergeable[T <: Composable] { self: T =>
  val merger: Merger[T]
  final def U(that: T): T = merger.merge(this, that)
}


trait Absorbable[T<: Composable] { self: T =>
  val absorber: Absorber[T]
  final def +=(that: T): T = absorber.absorb(this, that)
}

// Instance


trait ComposablePerson extends Composable
  with Mergeable[Person] with Absorbable[Person] with Equivalence[Person] { self: Person =>
  protected val unit: PersonComposer
  final lazy val relation = unit
  final lazy val merger   = unit
  final lazy val absorber = unit
}

trait OrganizationComposer extends Merger[Organization]

trait ComposableOrganization extends Composable with Mergeable[Organization] { self: Organization =>
  protected val unit: OrganizationComposer
  final lazy val merger   = unit
}













