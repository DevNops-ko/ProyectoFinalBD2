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

import com.proyectoBD2.APIrest.model.CitasModel;
import com.proyectoBD2.APIrest.service.ICitasService;

@RestController
@RequestMapping ("/UAO/apirest/citas")
public class CitasController {
    @Autowired ICitasService citaService;

    @PostMapping("/")
    public ResponseEntity<CitasModel> crearCita(@RequestBody CitasModel cita){
        return new ResponseEntity<CitasModel>(citaService.crearCita(cita), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<CitasModel>> listarCitas(){
        return new ResponseEntity<List<CitasModel>>(citaService.listarCitas(), HttpStatus.OK);
    }

    @GetMapping("/{citaId}")
    public ResponseEntity<CitasModel> buscarCitasPorId(@PathVariable ObjectId citaId){
        CitasModel cita = citaService.buscarCitasPorId(citaId);
        if (cita == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + citaId + " en citas.");
        }
        return new ResponseEntity<CitasModel>(cita, HttpStatus.OK);
    }
}
