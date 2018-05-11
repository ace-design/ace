package eu.ace_design.kernel

import eu.ace_design.kernel.modules.query.{QueryEngine, QueryFactory}
import eu.ace_design.kernel.modules.storage.{StorageActionEngine, StorageActionFactory}

trait Universe[TargetLanguage] {

  val storageEngine: StorageActionEngine[TargetLanguage]

  val storage: StorageActionFactory[TargetLanguage]

  val queryEngine: QueryEngine[TargetLanguage]

  val query: QueryFactory[TargetLanguage]

}