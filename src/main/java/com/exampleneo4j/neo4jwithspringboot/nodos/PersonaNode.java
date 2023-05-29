package com.exampleneo4j.neo4jwithspringboot.nodos;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;
@Data
@Node(labels = {"Person"})
public class PersonaNode {
    @Id @GeneratedValue
    private Long id;
    private String nombres;
    private String apellidos;
    private String cargo;
    private String email;
    @Relationship(type = "REALIZA", direction = Relationship.Direction.OUTGOING)
    private List<TareaNode> tareasAsociadas;
    @Relationship(type = "CREA", direction = Relationship.Direction.OUTGOING)
    private List<TareaNode> tareasCreadas;
    @Relationship(type = "CREA", direction = Relationship.Direction.OUTGOING)
    private List<ObjetivoNode> objetivosCreados;
//    private List<Realiza> tareasAsociadas;
}
