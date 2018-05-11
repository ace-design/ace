package social_network.impl.plain

import social_network.{Person, PersonComposer}

case class PersonImpl(override val name: String, override val friends: Set[Person])
  extends Person {
  override protected val unit: PersonComposer = PersonCompositionUnit
}


