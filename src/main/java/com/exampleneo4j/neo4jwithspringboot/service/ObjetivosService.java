package com.exampleneo4j.neo4jwithspringboot.service;
import com.exampleneo4j.neo4jwithspringboot.dto.InfoNodoDTO;
import com.exampleneo4j.neo4jwithspringboot.dto.InfoPersonaNodosDTO;
import com.exampleneo4j.neo4jwithspringboot.dto.ResponseDTO;
import com.exampleneo4j.neo4jwithspringboot.exception.NotFoundException;

import java.util.List;

public interface ObjetivosService {
    public ResponseDTO<InfoPersonaNodosDTO> listaObjetivosPorCreador(Long personaID) throws NotFoundException;
}
