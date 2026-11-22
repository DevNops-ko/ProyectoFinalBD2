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

import com.proyectoBD2.APIrest.model.RepuestosModel;
import com.proyectoBD2.APIrest.service.IRepuestosService;

@RestController
@RequestMapping ("/UAO/apirest/repuestos")
public class RepuestosController {
    @Autowired IRepuestosService repuestosService;

    @PostMapping("/")
    public ResponseEntity<RepuestosModel> crearRepuesto(@RequestBody RepuestosModel repuesto){
        return new ResponseEntity<RepuestosModel>(repuestosService.crearRepuesto(repuesto), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<RepuestosModel>> listarRepuestos(){
        return new ResponseEntity<List<RepuestosModel>>(repuestosService.listarRepuestos(), HttpStatus.OK);
    }

    @GetMapping("/{repuestoId}")
    public ResponseEntity<RepuestosModel> buscarRepuestosPorId(@PathVariable ObjectId repuestoId){
        RepuestosModel repuesto = repuestosService.buscarRepuestosPorId(repuestoId);
        if (repuesto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + repuestoId + " en repuestos.");
        }
        return new ResponseEntity<RepuestosModel>(repuesto, HttpStatus.OK);
    }
}
