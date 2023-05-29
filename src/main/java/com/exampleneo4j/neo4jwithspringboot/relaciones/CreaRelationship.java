package com.exampleneo4j.neo4jwithspringboot.relaciones;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;


@Data
@RelationshipProperties
public class CreaRelationship {
    @Id
    @GeneratedValue
    private Long id;
}
