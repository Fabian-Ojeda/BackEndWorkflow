package com.exampleneo4j.neo4jwithspringboot.service.implementation;

import com.exampleneo4j.neo4jwithspringboot.data.ObjetivoRepository;
import com.exampleneo4j.neo4jwithspringboot.data.PersonaRepository;
import com.exampleneo4j.neo4jwithspringboot.data.TareaRepository;
import com.exampleneo4j.neo4jwithspringboot.dto.*;
import com.exampleneo4j.neo4jwithspringboot.nodos.ObjetivoNode;
import com.exampleneo4j.neo4jwithspringboot.nodos.PersonaNode;
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
    PersonaRepository personaRepository;

    @Autowired
    ObjetivoRepository objetivoRepository;

    @Override
    public InfoGrafoDTO obtenerTaresByObjetivoId(Long objetivoId) {
        InfoGrafoDTO infoGrafoDTO = new InfoGrafoDTO();
        List<InfoNodoDTO> nodes = new ArrayList<>();
        List<InfoRelacionDTO> relations = new ArrayList<>();

        //----------------------------------Se consultan los empleados-------------------------//

        //primero se busca el jefe que creo el objetivo
        PersonaNode jefe = personaRepository.consultarCreadorObjetivo(objetivoId);
        List<PersonaNode> subordinados = personaRepository.consultarSubordinadosByJefeId(jefe.getId());
        Long iterator = 100L;
        for (PersonaNode subordinado:subordinados) {
            nodes.add(new InfoNodoDTO(""+subordinado.getId(), subordinado.getNombres()+subordinado.getApellidos()
                    , "lightpink", null));
            //consultamos las tareas que hace la persona
            List<TareaNode> tareasAsociadas = tareaRepository.tareasByRealizadorIdAndObjetivoID(subordinado.getId(), objetivoId);
            for (TareaNode tareaSubordinado: tareasAsociadas) {
                relations.add(new InfoRelacionDTO(iterator, ""+subordinado.getId(),
                        ""+tareaSubordinado.getId(), "b", "t", "realiza"));
                iterator++;
            }
        }

        TareaNode tareaInicial = tareaRepository.tareaInicialByObjetivoId(objetivoId);
        Optional<TareaNode> tareaInico = tareaRepository.findById(tareaInicial.getId());
        TareaNode tareaActual = tareaInico.get();

        int indicator = 0;
        while (indicator!=1){
            nodes.add(new InfoNodoDTO(""+tareaActual.getId(), tareaActual.getNombre(), "lightgreen", tareaActual.getEstado()));
            if(tareaActual.getTareasContigua().size()!=0){
                relations.add(new InfoRelacionDTO(tareaActual.getId(), ""+tareaActual.getId(),
                        ""+tareaActual.getTareasContigua().get(0).getId(), "b", "t", "continua"));
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
        //------Primero se separan las tareas de las personas en la información que llega----------//
        List<InfoNodoDTO> tareas = tareasByObjetivoDTO.getNodes().stream().filter(nodo -> nodo.getColor().equals("lightgreen")).collect(Collectors.toList());

        //-----------------------------Tareas a eliminar---------------------------------------//
        List<TareaNode> tareasExistentesPorObjetivo = tareaRepository.tareasByObjetivoId(tareasByObjetivoDTO.getIdObjetivo());
        List<TareaNode> tareasAEliminar = tareasExistentesPorObjetivo.stream()
                .filter(tarea -> tareas.stream()
                        .noneMatch(node -> Long.parseLong(node.getId())==(tarea.getId()))).collect(Collectors.toList());
        for (TareaNode tareaIterator: tareasAEliminar) {
            tareaRepository.borrarTarea(tareaIterator.getId());
        }

        //-----recorro las tareas entrantes para el proceso de actualización, si alguna no se encuantra sera creada despues----//
        List<InfoNodoDTO> tareasNuevas = new ArrayList<>();
        for (InfoNodoDTO infoIterator: tareas) {
            Optional<TareaNode> tareaTemp = tareaRepository.actualizarTarea(Long.valueOf(infoIterator.getId()), infoIterator.getText(), "sin finalizar");
            if( tareaTemp.isEmpty() ){
                tareasNuevas.add(infoIterator);
            }
        }

        //----------------------------guardamos las tareas nuevas----------------------------------------//
        for (InfoNodoDTO infoIterator: tareasNuevas) {
            saveTarea(infoIterator, tareasByObjetivoDTO.getRelations(), tareasByObjetivoDTO.getIdObjetivo());
        }

        /*----------------------------Se deben extraer unicamente las relaciones de "continua" para que
        ------------------------------------sean analizadas con las tareas-------------------------------*/
        List<InfoRelacionDTO> relationsTareas = tareasByObjetivoDTO.getRelations().stream().filter(relation -> relation.getText().equals("continua")).collect(Collectors.toList());

        //-----------------------------------vincular las relaciones de tareas-------------------------------------//
        for (InfoRelacionDTO infoRelationIterator : relationsTareas) {
            tareaRepository.vincularTareas(Long.parseLong(infoRelationIterator.getFrom()), Long.parseLong(infoRelationIterator.getTo()));
        }
        //--------Se toman las relaciones de vincular las personas con las tareas que deben hacer --------------------//
        List<InfoRelacionDTO> relationsPersonaTarea = tareasByObjetivoDTO.getRelations().stream().filter(relation -> relation.getText().equals("realiza")).collect(Collectors.toList());

        for (InfoRelacionDTO infoRelationIterator : relationsPersonaTarea) {
            tareaRepository.vincularPersonaConTareas(Long.parseLong(infoRelationIterator.getFrom()), Long.parseLong(infoRelationIterator.getTo()));
        }

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
