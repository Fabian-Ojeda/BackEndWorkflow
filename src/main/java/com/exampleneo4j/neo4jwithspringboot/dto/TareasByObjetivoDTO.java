package com.exampleneo4j.neo4jwithspringboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TareasByObjetivoDTO {
    @JsonProperty("idObjetivo")
    public Long idObjetivo;
    public List<InfoNodoDTO> nodes;
    public List<InfoRelacionDTO> relations;
}
