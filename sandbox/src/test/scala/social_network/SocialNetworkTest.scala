package social_network

import org.specs2.mutable.SpecificationWithJUnit

trait SocialNetworkTest extends SpecificationWithJUnit {

  "Social Network Specification".title

  protected val factory: SocialNetworkFactory


  "A person" should {

    val john: Person     = factory.createPerson("John Doe")
    val jim:  Person     = factory.createPerson("Jim Doe")
    val james:  Person   = factory.createPerson("James Doe")
    val jane: Person     = factory.createPerson("Jane Doe")
    val jessica:  Person = factory.createPerson("Jessica Doe")
    val julia:  Person   = factory.createPerson("Julia Doe")

    "have a name" in {
      john.name must_== "John Doe"
      jane.name must_== "Jane Doe"
    }

    "have no friends at the beginning" in {
      john.friends must beEmpty
      jane.friends must beEmpty
    }

    "make friend during their lives" in {
      val p = john += jessica += jim
      p.friends must haveSize(2)
      p.friends must contain(jessica)
      p.friends must contain(jim)
    }

    "not be affected when absorbing a new friend" in {
      val p = john += jessica += jim
      p.friends       must haveSize(2)
      john.friends    must beEmpty
      jessica.friends must beEmpty
      jim.friends     must beEmpty
    }

    "support an equivalence relation based on naming" in {
      (john === john) must beTrue
      (john ==  john) must beTrue
      (john === jim) must beFalse
      (john ==  jim) must beFalse
      (john === (john += jim)) must beTrue
      (john ==  (john += jim)) must beFalse
    }

    "not be composed when not equivalent" in {
      (john U jim)  must throwAn[Exception]
      (john U john) must not(throwAn[Exception])
    }

    "be merged in a commutative way" in {
      val p1 = john += jim += james
      val p2 = john += julia
      (p1 U p2) must_== (p2 U p1)
    }

  }

}