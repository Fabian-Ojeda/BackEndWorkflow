package com.exampleneo4j.neo4jwithspringboot.relaciones;

import com.exampleneo4j.neo4jwithspringboot.nodos.TareaNode;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;
@Data
@RelationshipProperties
public class RealizaRelationship {
    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private TareaNode tarea;
}
