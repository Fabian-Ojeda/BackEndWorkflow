package com.exampleneo4j.neo4jwithspringboot.service.implementation;

import com.exampleneo4j.neo4jwithspringboot.dto.PersonaDTO;
import com.exampleneo4j.neo4jwithspringboot.dto.TareaCreacionDTO;
import com.exampleneo4j.neo4jwithspringboot.nodos.ObjetivoNode;
import com.exampleneo4j.neo4jwithspringboot.nodos.PersonaNode;
import com.exampleneo4j.neo4jwithspringboot.data.ObjetivoRepository;
import com.exampleneo4j.neo4jwithspringboot.data.PersonaRepository;
import com.exampleneo4j.neo4jwithspringboot.data.TareaRepository;
import com.exampleneo4j.neo4jwithspringboot.nodos.TareaNode;
import com.exampleneo4j.neo4jwithspringboot.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PersonaServiceImpl implements PersonaService {
    @Autowired
    PersonaRepository personaRepository;

    @Autowired
    TareaRepository tareaRepository;

    @Autowired
    ObjetivoRepository objetivoRepository;
    @Override
    public String saludin() {
        List<PersonaNode> listaPersonas = personaRepository.findAll();
        List<ObjetivoNode> listaObjetivos = objetivoRepository.findAll();
        //List<TareaNode> tareasAsociadas = listaPersonas.get(2).getTareasAsociadas();
        String respuesta = listaObjetivos.get(0).getNombre();
        return respuesta;
    }
    @Override
    public PersonaDTO personaByNombre(String nombre){
        PersonaDTO personaDTO = new PersonaDTO();
        PersonaNode personaFinded = personaRepository.findByNombres(nombre);
        personaDTO.setNombres(personaFinded.getNombres());
        personaDTO.setApellidos(personaFinded.getApellidos());
        personaDTO.setCargo(personaFinded.getCargo());
        personaDTO.setEmail(personaFinded.getEmail());
        return  personaDTO;
    }

    @Override
    public String crearPersona(PersonaDTO personaDTO) {
        PersonaNode persona = new PersonaNode();
        persona.setNombres(personaDTO.getNombres());
        persona.setApellidos(personaDTO.getApellidos());
        persona.setCargo(personaDTO.getCargo());
        persona.setEmail(personaDTO.getEmail());
        personaRepository.save(persona);
        return "creada :)";
    }

    @Override
    public String asociarTareaPersona(TareaCreacionDTO tareaCreacionDTO) {
        PersonaNode personaFinded = personaRepository.findByNombres("Fabián Andres");
        TareaNode tarea = new TareaNode();
        tarea.setNombre(tareaCreacionDTO.getNombre());
        tarea.setEstado(tareaCreacionDTO.getEstado());
        tarea.setFecha_creacion(LocalDate.now());
        tareaRepository.save(tarea);
        personaFinded.getTareasAsociadas().add(tarea);
        personaRepository.save(personaFinded);
        return "Asignada :)";
    }

    @Override
    public String eliminarPersona(String nombre) {
        PersonaNode personaFinded = personaRepository.findByNombres(nombre);
        personaRepository.borrarPersonaConRelacionesPorId(personaFinded.getId());
        return "Borrada :)";
    }
}
