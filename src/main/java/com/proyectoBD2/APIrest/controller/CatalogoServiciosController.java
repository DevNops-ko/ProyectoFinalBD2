package com.proyectoBD2.APIrest.controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.proyectoBD2.APIrest.model.CatalogoServiciosModel;
import com.proyectoBD2.APIrest.service.ICatalogoServiciosService;

@RestController
@RequestMapping ("/UAO/apirest/catalogoServicios")
public class CatalogoServiciosController {
    @Autowired ICatalogoServiciosService catalogoServiciosService;

    @PostMapping("/")
    public ResponseEntity<CatalogoServiciosModel> crearServicio(@RequestBody CatalogoServiciosModel servicio){
        return new ResponseEntity<CatalogoServiciosModel>(catalogoServiciosService.crearServicio(servicio), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<CatalogoServiciosModel>> listarServicios(){
        return new ResponseEntity<List<CatalogoServiciosModel>>(catalogoServiciosService.listarServicios(), HttpStatus.OK);
    }

    @GetMapping("/{servicioId}")
    public ResponseEntity<CatalogoServiciosModel> buscarServiciosPorId(@PathVariable ObjectId servicioId){
        CatalogoServiciosModel servicio = catalogoServiciosService.buscarServiciosPorId(servicioId);
        if (servicio == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + servicioId + " en catalogoServicios.");
        }
        return new ResponseEntity<CatalogoServiciosModel>(servicio, HttpStatus.OK);
    }

    @PutMapping("/{servicioId}")
    public ResponseEntity<CatalogoServiciosModel> actualizarServicio(@PathVariable ObjectId servicioId, @RequestBody CatalogoServiciosModel servicio){
        return new ResponseEntity<CatalogoServiciosModel>(catalogoServiciosService.actualizarServicio(servicioId, servicio), HttpStatus.OK);
    }

    @DeleteMapping("/{servicioId}")
    public ResponseEntity<String> eliminarServicio(@PathVariable ObjectId servicioId){
        catalogoServiciosService.eliminarServicio(servicioId);
        return new ResponseEntity<String>("Servicio con ID " + servicioId + " eliminado correctamente.", HttpStatus.OK);
    }

}
