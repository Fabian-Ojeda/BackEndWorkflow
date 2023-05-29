package com.exampleneo4j.neo4jwithspringboot.service;

import com.exampleneo4j.neo4jwithspringboot.dto.PersonaDTO;
import com.exampleneo4j.neo4jwithspringboot.dto.TareaCreacionDTO;

public interface PersonaService {
    String saludin();
    public PersonaDTO personaByNombre(String nombre);
    String crearPersona(PersonaDTO personaDTO);
    String asociarTareaPersona(TareaCreacionDTO tareaCreacionDTO);
    String eliminarPersona(String nombre);
}
