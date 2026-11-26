package com.proyectoBD2.APIrest.controller;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyectoBD2.APIrest.service.IEstadisticasService;

@RestController
@RequestMapping ("/UAO/apirest/estadisticas")
public class EstadisticasController {
    @Autowired IEstadisticasService estadisticasService;

    @GetMapping("/serviciosMasSolicitados")
    public ResponseEntity<List<Document>> serviciosMasSolicitados(@RequestParam(required = false) ObjectId coordinadorId){
        return new ResponseEntity<List<Document>>(estadisticasService.serviciosMasSolicitados(coordinadorId), HttpStatus.OK);
    }

    @GetMapping("/ordenesPorEstado")
    public ResponseEntity<List<Document>> ordenesPorEstado(@RequestParam(required = false) ObjectId coordinadorId){
        return new ResponseEntity<List<Document>>(estadisticasService.ordenesPorEstado(coordinadorId), HttpStatus.OK);
    }

    @GetMapping("/tiemposAtencion")
    public ResponseEntity<Document> tiemposAtencion(@RequestParam(required = false) ObjectId coordinadorId){
        return new ResponseEntity<Document>(estadisticasService.tiemposAtencion(coordinadorId), HttpStatus.OK);
    }

    @GetMapping("/valoraciones")
    public ResponseEntity<Document> valoracionesRecibidas(@RequestParam(required = false) ObjectId coordinadorId){
        return new ResponseEntity<Document>(estadisticasService.valoracionesRecibidas(coordinadorId), HttpStatus.OK);
    }
}
//http://localhost:8080/UAO/apirest/estadisticas/serviciosMasSolicitados?coordinadorId=ID_DEL_COORDINADOR