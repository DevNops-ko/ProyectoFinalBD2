package com.proyectoBD2.APIrest.controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.proyectoBD2.APIrest.model.OrdenServicioModel;
import com.proyectoBD2.APIrest.service.IOrdenServicioService;

@RestController
@RequestMapping ("/UAO/apirest/ordenesServicio")
public class OrdenServicioController {
    @Autowired IOrdenServicioService ordenServicioService;

    @PostMapping("/")
    public ResponseEntity<OrdenServicioModel> crearOrdenServicio(@RequestBody OrdenServicioModel ordenServicio){
        return new ResponseEntity<OrdenServicioModel>(ordenServicioService.crearOrdenServicio(ordenServicio), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<OrdenServicioModel>> listarOrdenesServicio(){
        return new ResponseEntity<List<OrdenServicioModel>>(ordenServicioService.listarOrdenesServicio(), HttpStatus.OK);
    }

    @GetMapping("/{ordenServicioId}")
    public ResponseEntity<OrdenServicioModel> buscarOrdenesServicioPorId(@PathVariable ObjectId ordenServicioId){
        OrdenServicioModel ordenServicio = ordenServicioService.buscarOrdenesServicioPorId(ordenServicioId);
        if (ordenServicio == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + ordenServicioId + " en ordenesServicio.");
        }
        return new ResponseEntity<OrdenServicioModel>(ordenServicio, HttpStatus.OK);
    }
}
