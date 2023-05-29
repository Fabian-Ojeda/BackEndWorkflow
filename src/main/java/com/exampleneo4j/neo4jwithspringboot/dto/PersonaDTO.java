package com.exampleneo4j.neo4jwithspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonaDTO {
    private String nombres;
    private String apellidos;
    private String email;
    private String cargo;
}
