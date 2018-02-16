import scala.util.Random

// Context
case class Event(data: Map[String, Int])

// Generate a random history of given size
def history(size: Int): Seq[Event] = {
  for (i <- 1 to size)
    yield Event((1 to Random.nextInt(10) map {
      _ => Random.nextInt(10).toString -> Random.nextInt(100)
    }).toMap)
}

// alternative  #1
def alt1(history: Seq[Event]): Map[String,Int]= {
  val keys = history.flatMap(_.data.keySet).distinct
  val tmp = keys.map { k => k -> history.flatMap(_.data.get(k)).sum }
  tmp.toMap
}

// alternative  #2
def alt2(history: Seq[Event]): Map[String,Int] =
  (Map[String, Int]() /: history) { (acc, elem) =>
    acc ++ elem.data.map { case (k, v) => k -> (acc.getOrElse(k, 0) + v) }
  }

def alt3(history: Seq[Event]): Map[String,Int]= {
  val result = new scala.collection.mutable.HashMap[String, Int]().withDefaultValue(0)
  for (elem <- history; (k,v) <- elem.data ) {
    result(k) += v
  }
  result.toMap
}

// Benchmark
def timer[T,U](setup: => T)(first: T => U, second: T => U, third: T => U): (U, Long, Long, Long) = {
  val ctx = setup
  val t1S = System.nanoTime()
  val r1 = first(ctx)
  val t1E = System.nanoTime()
  val r2 = second(ctx)
  val t2E = System.nanoTime()
  val r3 = third(ctx)
  val t3E = System.nanoTime()
  assert(r1 == r2)
  assert(r2 == r3)
  (r1, t1E-t1S, t2E-t1E, t3E-t2E)
}

def benchmark(howMany: Int): Map[String, Double] = {
  val times: Seq[(Long,Long, Long)] = 1 to howMany map { _ =>
    val (_, t1, t2, t3) = timer(history(10000))(h => alt1(h), h => alt2(h), h => alt3(h))
    (t1,t2, t3)
  }
  Map(
    "alt1" -> times.map {_._1}.sum / times.length,
    "alt2" -> times.map {_._2}.sum / times.length,
    "alt3" -> times.map {_._3}.sum / times.length
  )
}

benchmark(100)

