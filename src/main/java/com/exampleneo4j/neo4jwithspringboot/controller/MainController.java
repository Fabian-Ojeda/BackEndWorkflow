package com.exampleneo4j.neo4jwithspringboot.controller;

import com.exampleneo4j.neo4jwithspringboot.dto.*;
import com.exampleneo4j.neo4jwithspringboot.service.ObjetivosService;
import com.exampleneo4j.neo4jwithspringboot.service.PersonaService;
import com.exampleneo4j.neo4jwithspringboot.service.TareasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class MainController {

    @Autowired
    PersonaService personaService;

    @Autowired
    ObjetivosService objetivosService;
    @Autowired
    TareasService tareasService;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saludo() {
        return new ResponseEntity<>(personaService.saludin(), HttpStatus.OK);
    }

    @PostMapping("/infoPersonaByNombre")
    public ResponseEntity<PersonaDTO> getPersonaByNombres(@RequestBody NombresPersonaDTO nombresPersonaDTO){
        return new ResponseEntity<>(personaService.personaByNombre(nombresPersonaDTO.getNombres()), HttpStatus.OK);
    }

    @PostMapping("/crearPersona")
    public ResponseEntity<String> crearPersona(@RequestBody PersonaDTO personaDTO){
        return new ResponseEntity<>(personaService.crearPersona(personaDTO), HttpStatus.OK);
    }
    @PostMapping("/asociarTareaPersona")
    public ResponseEntity<String> asociarTareaPersona(@RequestBody TareaCreacionDTO tareaCreacionDTO){
        return new ResponseEntity<>(personaService.asociarTareaPersona(tareaCreacionDTO), HttpStatus.OK);
    }

    @PostMapping("/eliminarPersona")
    public ResponseEntity<String> eliminarPersonaPorNombre(@RequestBody NombresPersonaDTO nombresPersonaDTO){
        return new ResponseEntity<>(personaService.eliminarPersona(nombresPersonaDTO.getNombres()), HttpStatus.OK);
    }

    @GetMapping(value="/obtenerObjetivosPorCreador", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InfoPersonaNodosDTO> obtenerObjetivosPorCreador(@RequestParam("personaId") Long personaId){
        return new ResponseEntity<>(objetivosService.listaObjetivosPorCreador(personaId), HttpStatus.OK);
    }
    @PostMapping(value="/obtenerTareasPorObjetivo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InfoGrafoDTO> obtenerTareasPorObjetivo(@RequestBody IdObjetivoDTO idObjetivoDTO){
        return new ResponseEntity<>(tareasService.obtenerTaresByObjetivoId(idObjetivoDTO.idObjetivo), HttpStatus.OK);
    }
    @PostMapping(value="/guardarTareasPorObjetivo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> guardarTareasPorObjetivo(@RequestBody TareasByObjetivoDTO tareasByObjetivoDTO){
        return new ResponseEntity<>(tareasService.guardarTareasPorObjetivo(tareasByObjetivoDTO), HttpStatus.OK);
    }
}
