package com.exampleneo4j.neo4jwithspringboot.service;

import com.exampleneo4j.neo4jwithspringboot.dto.InfoGrafoDTO;
import com.exampleneo4j.neo4jwithspringboot.dto.ResponseDTO;
import com.exampleneo4j.neo4jwithspringboot.dto.TareasByObjetivoDTO;

public interface TareasService {

    InfoGrafoDTO obtenerTaresByObjetivoId(Long objetivoId);
    ResponseDTO guardarTareasPorObjetivo(TareasByObjetivoDTO tareasByObjetivoDTO) ;
}
