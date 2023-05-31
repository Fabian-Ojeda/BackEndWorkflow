package com.exampleneo4j.neo4jwithspringboot.service.implementation;

import com.exampleneo4j.neo4jwithspringboot.data.ObjetivoRepository;
import com.exampleneo4j.neo4jwithspringboot.data.TareaRepository;
import com.exampleneo4j.neo4jwithspringboot.dto.*;
import com.exampleneo4j.neo4jwithspringboot.nodos.ObjetivoNode;
import com.exampleneo4j.neo4jwithspringboot.nodos.TareaNode;
import com.exampleneo4j.neo4jwithspringboot.service.TareasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TareasServiceImpl implements TareasService {
    @Autowired
    TareaRepository tareaRepository;

    @Autowired
    ObjetivoRepository objetivoRepository;

    @Override
    public InfoGrafoDTO obtenerTaresByObjetivoId(Long objetivoId) {
        InfoGrafoDTO infoGrafoDTO = new InfoGrafoDTO();
        List<InfoNodoDTO> nodes = new ArrayList<>();
        List<InfoRelacionDTO> relations = new ArrayList<>();

        Optional<ObjetivoNode> objetivo = objetivoRepository.findById(objetivoId);
        nodes.add(new InfoNodoDTO(""+objetivo.get().getId(), objetivo.get().getNombre(), "yellow", null));
        TareaNode tareaInicial = tareaRepository.tareaInicialByObjetivoId(objetivoId);
        Optional<TareaNode> tareaInico = tareaRepository.findById(tareaInicial.getId());
        TareaNode tareaActual = tareaInico.get();

        int indicator = 0;
        while (indicator!=1){
            nodes.add(new InfoNodoDTO(""+tareaActual.getId(), tareaActual.getNombre(), "lightgreen", tareaActual.getEstado()));
            if(tareaActual.getTareasContigua().size()!=0){
                relations.add(new InfoRelacionDTO(tareaActual.getId(), ""+tareaActual.getId(), ""+tareaActual.getTareasContigua().get(0).getId(), "b", "t"));
                tareaActual= tareaActual.getTareasContigua().get(0);
            }else{
                indicator = 1;
            }
        }
        infoGrafoDTO.setNodes(nodes);
        infoGrafoDTO.setRelations(relations);
        //Optional<TareaNode> tareaCompleta = tareaRepository.findById(tareaInicial.getId());
        return infoGrafoDTO;
    }

    @Override
    public ResponseDTO guardarTareasPorObjetivo(TareasByObjetivoDTO tareasByObjetivoDTO) {
        ResponseDTO responseDTO = new ResponseDTO();

        //-----------------------------Tareas a eliminar---------------------------------------//
        List<TareaNode> tareasExistentesPorObjetivo = tareaRepository.tareasByObjetivoId(tareasByObjetivoDTO.getIdObjetivo());
        List<TareaNode> tareasAEliminar = tareasExistentesPorObjetivo.stream()
                .filter(tarea -> tareasByObjetivoDTO.getNodes().stream()
                        .noneMatch(node -> Long.parseLong(node.getId())==(tarea.getId()))).collect(Collectors.toList());
        for (TareaNode tareaIterator: tareasAEliminar) {
            tareaRepository.borrarTarea(tareaIterator.getId());
        }
        //-----------------------------------------------------------------------------------//

        //-----recorro las tareas entrantes para el proceso de actualización, si alguna no se encuantra sera creada despues----//
        List<InfoNodoDTO> tareasNuevas = new ArrayList<>();
        for (InfoNodoDTO infoIterator: tareasByObjetivoDTO.getNodes()) {
            Optional<TareaNode> tareaTemp = tareaRepository.actualizarTarea(Long.valueOf(infoIterator.getId()), infoIterator.getText(), "sin finalizar");
            if( tareaTemp.isEmpty() ){
                tareasNuevas.add(infoIterator);
            }
        }
        //-----------------------------------------------------------------------------------------------//

        //----------------------------guardamos las tareas nuevas----------------------------------------//
        for (InfoNodoDTO infoIterator: tareasNuevas) {
            if(!infoIterator.getColor().equals("yellow")) {
                saveTarea(infoIterator, tareasByObjetivoDTO.getRelations(), tareasByObjetivoDTO.getIdObjetivo());
            }
        }
        //-----------------------------------------------------------------------------------------------//

        //-----------------------------------Analizar las relaciones-------------------------------------//
        for (InfoRelacionDTO infoRelationIterator : tareasByObjetivoDTO.getRelations()) {
            tareaRepository.vincularTareas(Long.parseLong(infoRelationIterator.getFrom()), Long.parseLong(infoRelationIterator.getTo()));
        }
        //-----------------------------------------------------------------------------------------------//

        responseDTO.setError(false);
        responseDTO.setDescription("Guardado correcto de data");
        return responseDTO;
    }

    private void saveTarea(InfoNodoDTO nuevaTarea, List<InfoRelacionDTO> relations, Long idObjetivo){
        TareaNode tareaNode = new TareaNode();
        tareaNode.setNombre(nuevaTarea.getText());
        tareaNode.setEstado("sin finalizar");
        tareaNode.setFecha_creacion(LocalDate.now());
        TareaNode tareaCreada = tareaRepository.save(tareaNode);
        objetivoRepository.vincularObjetivoATareas(idObjetivo, tareaCreada.getId());
        for (InfoRelacionDTO relationIterator : relations) {
            if(Long.parseLong(relationIterator.getFrom())==Long.parseLong(nuevaTarea.getId())){
                relationIterator.setFrom(""+tareaNode.getId());
            }
            if(Long.parseLong(relationIterator.getTo())==Long.parseLong(nuevaTarea.getId())){
                relationIterator.setTo(""+tareaNode.getId());
            }
        }
    }


}
