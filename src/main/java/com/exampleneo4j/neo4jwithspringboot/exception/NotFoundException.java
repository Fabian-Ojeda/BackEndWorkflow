package com.exampleneo4j.neo4jwithspringboot.exception;

public class NotFoundException extends Exception{

    public NotFoundException() {
        super();
    }

    public NotFoundException(String mensaje) {
        super(mensaje);
    }
}
