import scala.annotation.tailrec
import scala.languageFeature.implicitConversions

// ACE
trait IdentifiableElement {
  protected def identifier: String
}

trait Action
trait CompositeAction extends Action {
  def refine(): Seq[Action]
}


// Graph manipulation
sealed trait AtomicAction extends Action
final case class createVertex(identifier: String) extends AtomicAction
final case class setVertexProperty[T](identifier: String, property: Property[T], value: T) extends AtomicAction


trait NamedElement {
  def _name: String
}
trait Concept extends NamedElement
trait Property[T] extends NamedElement

object Kind extends Property[String] { val _name = "Kind" }

// Praxis interface
sealed trait PraxisAction extends CompositeAction
final case class create(identifier: String, concept: Concept) extends PraxisAction {
  override def refine: Seq[AtomicAction] = Seq(
    createVertex(identifier),
    setVertexProperty(identifier, Kind, concept._name)
  )
}
case class setProperty[T](identifier: String, property: Property[T], value: T) extends PraxisAction {
  override def refine: Seq[AtomicAction] = Seq(setVertexProperty(identifier, property, value))
}


trait Query[T]


class Engine {

  @tailrec
  final def execute(actions: Seq[Action], universe: Backend): Unit = actions match {
    case Nil =>
    case a :: as => a match {
        case atom: AtomicAction     => { universe.execute(atom); execute(as, universe) }
        case compo: CompositeAction => { execute(compo.refine() ++ as, universe) }
      }
    }

}

class QueryManager {
  final def execute[T](q: Query[T]) = ???
}

trait Backend {

  def execute(a: Action)
}


class Neo4JBackend extends Backend {

  override def execute(a: Action) = { println(a) }

}

trait AbstractCompositionEngine {

  protected val universe: Backend

  protected val engine: Engine = new Engine()

  protected val queryManager: QueryManager = new QueryManager()

}


trait ACE extends AbstractCompositionEngine {
  override protected val universe = new Neo4JBackend()
}


// Arduino Domain

object Concepts {
  object  ArduinoApp extends Concept {
    override val _name: String = "ArduinoApp"
    object name extends Property[String] { override val _name = "name" }
  }
}



trait ArduinoQuery[T] extends Query[T]


trait ArduinoAppInterface {
  def name: String
  def name_=(n: String)
}

class ArduinoAppIml private() extends ArduinoAppInterface with IdentifiableElement with ACE {

  protected var identifier: String = ""

  def _this(identifier: String): Seq[Action] = Seq(create(identifier,Concepts.ArduinoApp))
  def this(identifier: String) = {
    this()
    this.identifier = identifier
    engine.execute(_this(identifier), universe)
  }

  def _getName: Query[String] = ???
  def _setName(n: String): Seq[Action] = Seq(setProperty(identifier, Concepts.ArduinoApp.name, n))

  override def name: String = queryManager.execute(_getName)
  override def name_=(n: String): Unit = engine.execute(_setName(n), universe)

}

class ArduinoApp(identifier: String) extends ArduinoAppIml(identifier)


// Main Code

object Main extends App {

  val app = new ArduinoApp(identifier = "app")
  app.name = "My Pretty Little App"
  //println(app.name)

  // Intercept also this() ?

  //import fsm._

  //val m1 = factory.app("m1")
  //m1.name = "My pretty model"
  //println(m1.name)

  //val m2 = factory.app("m2")
  //m2.name = "Another model"

  //val m = m1 + m2
  //println(m)

}

// verification de propriété et rollback de transaction si non resoectées?