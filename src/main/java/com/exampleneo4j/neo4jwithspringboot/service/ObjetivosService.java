package com.exampleneo4j.neo4jwithspringboot.service;
import com.exampleneo4j.neo4jwithspringboot.dto.InfoNodoDTO;
import com.exampleneo4j.neo4jwithspringboot.dto.InfoPersonaNodosDTO;

import java.util.List;

public interface ObjetivosService {
    public InfoPersonaNodosDTO listaObjetivosPorCreador(Long personaID);
}
