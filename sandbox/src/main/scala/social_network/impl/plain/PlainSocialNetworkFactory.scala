package social_network.impl.plain

import social_network.{Organization, Person, SocialNetworkFactory}

object PlainSocialNetworkFactory extends SocialNetworkFactory {

  override def createPerson(name: String): Person =
    PersonImpl(name, Set())

  override def createOrganization(persons: Set[Person]): Organization =
    OrganizationImpl(persons)

}
