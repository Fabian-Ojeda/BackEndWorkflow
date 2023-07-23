package com.exampleneo4j.neo4jwithspringboot.service.implementation;

import com.exampleneo4j.neo4jwithspringboot.data.ObjetivoRepository;
import com.exampleneo4j.neo4jwithspringboot.data.PersonaRepository;
import com.exampleneo4j.neo4jwithspringboot.dto.InfoNodoDTO;
import com.exampleneo4j.neo4jwithspringboot.dto.InfoPersonaNodosDTO;
import com.exampleneo4j.neo4jwithspringboot.dto.PersonaDTO;
import com.exampleneo4j.neo4jwithspringboot.dto.ResponseDTO;
import com.exampleneo4j.neo4jwithspringboot.exception.NotFoundException;
import com.exampleneo4j.neo4jwithspringboot.nodos.ObjetivoNode;
import com.exampleneo4j.neo4jwithspringboot.nodos.PersonaNode;
import com.exampleneo4j.neo4jwithspringboot.service.ObjetivosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ObjetivosServiceImpl implements ObjetivosService {

    @Autowired
    ObjetivoRepository objetivoRepository;

    @Autowired
    PersonaRepository personaRepository;

    @Override
    public ResponseDTO<InfoPersonaNodosDTO> listaObjetivosPorCreador(Long personaId) throws NotFoundException {
        ResponseDTO<InfoPersonaNodosDTO> responseDTO = new ResponseDTO<InfoPersonaNodosDTO>();
        InfoPersonaNodosDTO infoPersonaNodosDTO = new InfoPersonaNodosDTO();
        Optional<PersonaNode> persona = personaRepository.findById(personaId);
        if (persona.isEmpty()) {
            throw new NotFoundException("No se han encontrado la persona buscada");
        }
        infoPersonaNodosDTO.setInfoPersona(new PersonaDTO(persona.get().getNombres(), persona.get().getApellidos(),
                persona.get().getEmail(), persona.get().getCargo()));

        List<InfoNodoDTO> listNodos = new ArrayList<>();
        List<ObjetivoNode> objetivoNodes = objetivoRepository.objetivosPorCreador(personaId);
        if (objetivoNodes.isEmpty()){
            throw new NotFoundException("No se han encontrado objetivos para la persona buscada");
        }
        for (ObjetivoNode objetivoIterator : objetivoNodes) {
            listNodos.add(new InfoNodoDTO("" + objetivoIterator.getId(), objetivoIterator.getNombre(), "lightblue", objetivoIterator.getEstado()));
        }
        infoPersonaNodosDTO.setNodos(listNodos);
        responseDTO.setError(false);
        responseDTO.setContenido(infoPersonaNodosDTO);
        responseDTO.setDescription("Lista de objetivos creados de una persona");
        return responseDTO;
    }
}
