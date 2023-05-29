package com.exampleneo4j.neo4jwithspringboot.data;

import com.exampleneo4j.neo4jwithspringboot.nodos.ObjetivoNode;
import com.exampleneo4j.neo4jwithspringboot.nodos.TareaNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ObjetivoRepository extends Neo4jRepository<ObjetivoNode, Long> {
    @Query("MATCH (o:Objetivo) WHERE ID(o) = $objetivoId MATCH (t:Tarea) WHERE ID(t) = $idTarea MERGE (o)-[:INCLUYE]->(t) RETURN o")
    Optional<ObjetivoNode> vincularObjetivoATareas(@Param("objetivoId") Long objetivoId, @Param("idTarea") Long idTarea);

    @Query("MATCH (p:Person)-[:CREA]->(o:Objetivo) WHERE ID(p) = $personaId RETURN o")
    List<ObjetivoNode> objetivosPorCreador(@Param("personaId") Long personaId);

}
