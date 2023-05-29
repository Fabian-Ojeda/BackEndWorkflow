package com.exampleneo4j.neo4jwithspringboot.nodos;

import com.exampleneo4j.neo4jwithspringboot.relaciones.ContinuaRelationship;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;
import java.util.List;

@Data
@Node(labels = {"Tarea"})
public class TareaNode {
    @Id @GeneratedValue
    private Long id;
    private String nombre;
    private String estado;
    private LocalDate fecha_creacion;
    private LocalDate fecha_modificacion;
    @Relationship(type = "CONTINUA", direction = Relationship.Direction.OUTGOING)
    private List<TareaNode> tareasContigua;
}
