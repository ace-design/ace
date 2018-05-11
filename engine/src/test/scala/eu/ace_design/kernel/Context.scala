package eu.ace_design.kernel

import eu.ace_design.instances.neo4j.{Cypher, CypherQueryFactory, CypherStorageFactory, Neo4J}
import eu.ace_design.kernel.modules.query.{QueryEngine, QueryFactory}
import eu.ace_design.kernel.modules.storage.{StorageActionEngine, StorageActionFactory}

object Context extends Universe[Cypher] {

  override val storageEngine: StorageActionEngine[Cypher] = Neo4J
  override val storage: StorageActionFactory[Cypher] = CypherStorageFactory

  override val queryEngine: QueryEngine[Cypher] = Neo4J
  override val query: QueryFactory[Cypher] = CypherQueryFactory

}
