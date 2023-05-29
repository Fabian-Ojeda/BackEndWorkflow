package com.exampleneo4j.neo4jwithspringboot.dto;

import lombok.Data;

import java.util.List;
@Data
public class InfoPersonaNodosDTO {
    public PersonaDTO infoPersona;
    public List<InfoNodoDTO> nodos;
}
