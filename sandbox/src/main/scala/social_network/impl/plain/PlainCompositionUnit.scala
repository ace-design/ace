package social_network.impl.plain

import social_network._

object PersonCompositionUnit extends PersonComposer {

  private val factory: SocialNetworkFactory = PlainSocialNetworkFactory

  override def merge(o1: Person, o2: Person): Person = {
    require(o1 === o2, "Cannot merge non equivalent persons")
    (factory.createPerson(o1.name) /: (o1.friends ++ o2.friends)) {
      (result, friend) => result += friend
    }
  }

  override def absorb(o1: Person, o2: Person): Person = {
    require(o1 =!= o2, "Cannot absorb myself")
    PersonImpl(o1.name, o1.friends + o2)
  }

  override def equivalent(o1: Person, o2: Person): Boolean =
    o1.name == o2.name
}


object OrganizationCompositionUnit extends OrganizationComposer {

  override def merge(o1: Organization, o2: Organization): Organization = ???

}


