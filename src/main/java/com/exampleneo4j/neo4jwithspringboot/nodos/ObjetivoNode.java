package com.exampleneo4j.neo4jwithspringboot.nodos;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Data
@Node(labels = {"Objetivo"})
public class ObjetivoNode {
    @Id @GeneratedValue
    private Long id;
    private String nombre;
    private String estado;
    private String area;
    @Relationship(type = "INCLUYE", direction = Relationship.Direction.OUTGOING)
    private List<TareaNode> tareasIncluidas;
}
