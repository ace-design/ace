package eu.ace_design.kernel.graph

import org.apache.logging.log4j.scala.Logging


/**
  * Define the transactional behavior of the engine, if available.
  * @tparam Transaction the underlying transaction model
  */
trait Transactional[Transaction] extends Logging { self: GraphStorage[_] =>

  /**
    * Execute a code block within a transaction
    * @param code the code block to protect
    * @tparam R return type of code (if any)
    * @return the result of code
    */
  final def withinTransaction[R](code: => R): R = {
    val tx = begin()
    logger.info(s"Beginning Transaction [${tx.hashCode()}]")
    try {
      val result = code
      success(tx)
      result
    } catch {
        case e: Exception => fail(tx); throw e;
    } finally {
      if (tx != null) { close(tx) }
      logger.info(s"Closing Transaction [${tx.hashCode()}]")
    }
  }

  /**
    * Starts a transaction
    * @return the started transaction
    */
  protected def begin(): Transaction

  /**
    * Flags a given transaction as successful
    * @param tx
    */
  protected def success(tx: Transaction)

  /**
    * Flags a given transaction as a failure
    * @param tx
    */
  protected def fail(tx: Transaction)

  /**
    * Closes a given transaction, after having called success or failure.
    * close a transaction
    * @param tx
    */
  protected def close(tx: Transaction)

}