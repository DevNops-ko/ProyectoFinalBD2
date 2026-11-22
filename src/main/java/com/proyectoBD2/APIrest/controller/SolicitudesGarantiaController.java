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

import com.proyectoBD2.APIrest.model.SolicitudesGarantiaModel;
import com.proyectoBD2.APIrest.service.ISolicitudesGarantiaService;

@RestController
@RequestMapping ("/UAO/apirest/solicitudesGarantia")
public class SolicitudesGarantiaController {
    @Autowired ISolicitudesGarantiaService solicitudGarantiaService;

    @PostMapping("/")
    public ResponseEntity<SolicitudesGarantiaModel> crearSolicitudGarantia(@RequestBody SolicitudesGarantiaModel solicitudGarantia){
        return new ResponseEntity<SolicitudesGarantiaModel>(solicitudGarantiaService.crearSolicitudGarantia(solicitudGarantia), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<SolicitudesGarantiaModel>> listarSolicitudesGarantia(){
        return new ResponseEntity<List<SolicitudesGarantiaModel>>(solicitudGarantiaService.listarSolicitudesGarantia(), HttpStatus.OK);
    }

    @GetMapping("/{solicitudGarantiaId}")
    public ResponseEntity<SolicitudesGarantiaModel> buscarSolicitudesGarantiaPorId(@PathVariable ObjectId solicitudGarantiaId){
        SolicitudesGarantiaModel solicitudGarantia = solicitudGarantiaService.buscarSolicitudesGarantiaPorId(solicitudGarantiaId);
        if (solicitudGarantia == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + solicitudGarantiaId + " en solicitudesGarantia.");
        }
        return new ResponseEntity<SolicitudesGarantiaModel>(solicitudGarantia, HttpStatus.OK);
    }
}
