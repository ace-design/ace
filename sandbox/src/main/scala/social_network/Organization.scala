package social_network

trait Organization extends ComposableOrganization {

  def persons: Set[Person]

}
