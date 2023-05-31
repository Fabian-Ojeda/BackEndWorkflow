package com.exampleneo4j.neo4jwithspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class InfoNodoDTO {
    public String id;
    public String text;
    public String color;
    public String estado;
}
