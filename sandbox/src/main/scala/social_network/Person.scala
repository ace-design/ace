package social_network

trait Person extends ComposablePerson {

  def name: String
  def friends: Set[Person]

}



