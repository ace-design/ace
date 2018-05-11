package social_network

trait SocialNetworkFactory {

  def createPerson(name: String): Person
  def createOrganization(persons: Set[Person]): Organization

}
