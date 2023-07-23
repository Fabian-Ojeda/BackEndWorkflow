package com.exampleneo4j.neo4jwithspringboot.dto;

import lombok.Data;

@Data
public class ResponseDTO <T>{
    public boolean error;
    public T contenido;
    public String description;
}
