package com.exampleneo4j.neo4jwithspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class InfoRelacionDTO {
    public Long key;
    public String from;
    public String to;
    public String fromPort;
    public String toPort;
    public String text;
}
