package com.exampleneo4j.neo4jwithspringboot.data;

import com.exampleneo4j.neo4jwithspringboot.nodos.PersonaNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonaRepository extends Neo4jRepository<PersonaNode, Long> {
    //@Query("MATCH (p:Person) RETURN p")
    List<PersonaNode> findAll();
    PersonaNode findByNombres(String nombres);
    @Query("MATCH (p:Person)-[r]-() WHERE ID(p) = $id DELETE p, r")
    void borrarPersonaConRelacionesPorId(@Param("id") Long id);

}
