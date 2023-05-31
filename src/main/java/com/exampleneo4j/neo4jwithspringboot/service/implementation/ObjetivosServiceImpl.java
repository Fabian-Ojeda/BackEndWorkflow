package com.exampleneo4j.neo4jwithspringboot.service.implementation;

import com.exampleneo4j.neo4jwithspringboot.data.ObjetivoRepository;
import com.exampleneo4j.neo4jwithspringboot.data.PersonaRepository;
import com.exampleneo4j.neo4jwithspringboot.dto.InfoNodoDTO;
import com.exampleneo4j.neo4jwithspringboot.dto.InfoPersonaNodosDTO;
import com.exampleneo4j.neo4jwithspringboot.dto.PersonaDTO;
import com.exampleneo4j.neo4jwithspringboot.nodos.ObjetivoNode;
import com.exampleneo4j.neo4jwithspringboot.nodos.PersonaNode;
import com.exampleneo4j.neo4jwithspringboot.service.ObjetivosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ObjetivosServiceImpl implements ObjetivosService {

    @Autowired
    ObjetivoRepository objetivoRepository;

    @Autowired
    PersonaRepository personaRepository;
    @Override
    public InfoPersonaNodosDTO listaObjetivosPorCreador(Long personaId) {
        InfoPersonaNodosDTO infoPersonaNodosDTO = new InfoPersonaNodosDTO();
        Optional<PersonaNode> persona = personaRepository.findById(personaId);
        infoPersonaNodosDTO.setInfoPersona(new PersonaDTO(persona.get().getNombres(), persona.get().getApellidos(),
                persona.get().getEmail(), persona.get().getCargo()));
        List<InfoNodoDTO> listNodos = new ArrayList<>();
        List<ObjetivoNode> objetivoNodes = objetivoRepository.objetivosPorCreador(personaId);
        for (ObjetivoNode objetivoIterator : objetivoNodes) {
            listNodos.add(new InfoNodoDTO(""+objetivoIterator.getId(), objetivoIterator.getNombre(), "lightblue", objetivoIterator.getEstado()));
        }
        infoPersonaNodosDTO.setNodos(listNodos);
        return infoPersonaNodosDTO;
    }
}
