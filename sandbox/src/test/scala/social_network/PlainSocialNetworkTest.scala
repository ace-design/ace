package social_network

import social_network.impl.plain.PlainSocialNetworkFactory

class PlainSocialNetworkTest extends SocialNetworkTest {
  override protected val factory: SocialNetworkFactory = PlainSocialNetworkFactory
}
