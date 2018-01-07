#!/usr/bin/env bash

docker run --publish=7474:7474 --publish=7687:7687 neo4j:3.0
#    --volume=$HOME/neo4j/data:/data \
#    --volume=$HOME/neo4j/logs:/logs \
