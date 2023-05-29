package com.exampleneo4j.neo4jwithspringboot.dto;

import lombok.Data;

import java.util.List;
@Data
public class InfoGrafoDTO {
    public List<InfoNodoDTO> nodes;
    public List<InfoRelacionDTO> relations;
}
