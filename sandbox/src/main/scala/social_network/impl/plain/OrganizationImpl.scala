package social_network.impl.plain

import social_network.{Organization, OrganizationComposer, Person}

case class OrganizationImpl(override val persons: Set[Person]) extends Organization {
  override protected val unit: OrganizationComposer = OrganizationCompositionUnit
}
