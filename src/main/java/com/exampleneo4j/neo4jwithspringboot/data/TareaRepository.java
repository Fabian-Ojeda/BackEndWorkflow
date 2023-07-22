package com.exampleneo4j.neo4jwithspringboot.data;

import com.exampleneo4j.neo4jwithspringboot.nodos.TareaNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TareaRepository extends Neo4jRepository<TareaNode, Long> {
    @Query("MATCH (o:Objetivo)-[:INCLUYE]->(t:Tarea) WHERE ID(o) = $objetivoId AND NOT ()-[:CONTINUA]->(t) RETURN t")
    TareaNode tareaInicialByObjetivoId(@Param("objetivoId") Long objetivoId);
    @Query("MATCH (o:Objetivo)-[:INCLUYE]->(t:Tarea) WHERE ID(o) = $objetivoId RETURN t")
    List<TareaNode>  tareasByObjetivoId(@Param("objetivoId") Long objetivoId);

    @Query("MATCH (t:Tarea) WHERE ID(t) = $tareaId SET t.nombre = $nombreTarea, t.estado = $estadoTarea RETURN t")
    Optional<TareaNode> actualizarTarea(@Param("tareaId") Long tareaId, @Param("nombreTarea") String nombreTarea,
                                           @Param("estadoTarea") String estadoTarea);

    @Query("MATCH (t:Tarea) WHERE ID(t) = $from MATCH (s:Tarea) WHERE ID(s) = $to MERGE (t)-[:CONTINUA]->(s) RETURN t")
    Optional<TareaNode> vincularTareas(@Param("from") Long from, @Param("to") Long to);
    @Query("MATCH (p:Person) WHERE ID(p) = $from MATCH (t:Tarea) WHERE ID(t) = $to MERGE (p)-[:REALIZA]->(t) RETURN t")
    Optional<TareaNode> vincularPersonaConTareas(@Param("from") Long from, @Param("to") Long to);

    @Query("MATCH (t:Tarea) WHERE ID(t) = $tareaId DETACH DELETE t")
    void borrarTarea(@Param("tareaId") Long tareaId);

    @Query("MATCH (p:Person)-[:REALIZA]->(t:Tarea)"+
            " MATCH (o:Objetivo)-[:INCLUYE]->(t)"+
            " WHERE ID(p) = $realizadorId AND ID(o) = $objetivoId"+
            " RETURN t")
    List<TareaNode>  tareasByRealizadorIdAndObjetivoID(@Param("realizadorId") Long realizadorId,
                                                       @Param("objetivoId") Long objetivoId);
//    @Query("MATCH (o:Objetivo)-[:INCLUYE]->(t:Tarea) WHERE ID(o) = $objetivoId WITH t ORDER BY NOT EXISTS(()-[:CONTINUA]->(t)) DESC RETURN COLLECT(t)")
//    List<TareaNode>  tareaByObjetivoId(@Param("objetivoId") Long objetivoId);
}
