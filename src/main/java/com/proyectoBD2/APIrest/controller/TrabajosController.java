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

import com.proyectoBD2.APIrest.model.TrabajosModel;
import com.proyectoBD2.APIrest.service.ITrabajosService;

@RestController
@RequestMapping ("/UAO/apirest/trabajos")
public class TrabajosController {
    @Autowired ITrabajosService trabajoService;

    @PostMapping("/")
    public ResponseEntity<TrabajosModel> crearTrabajo(@RequestBody TrabajosModel trabajo){
        return new ResponseEntity<TrabajosModel>(trabajoService.crearTrabajo(trabajo), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<TrabajosModel>> listarTrabajos(){
        return new ResponseEntity<List<TrabajosModel>>(trabajoService.listarTrabajos(), HttpStatus.OK);
    }

    @GetMapping("/{trabajoId}")
    public ResponseEntity<TrabajosModel> buscarTrabajosPorId(@PathVariable ObjectId trabajoId){
        TrabajosModel trabajo = trabajoService.buscarTrabajosPorId(trabajoId);
        if (trabajo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + trabajoId + " en trabajos.");
        }
        return new ResponseEntity<TrabajosModel>(trabajo, HttpStatus.OK);
    }

    @PutMapping("/{trabajoId}")
    public ResponseEntity<TrabajosModel> actualizarTrabajo(@PathVariable ObjectId trabajoId, @RequestBody TrabajosModel trabajo){
        return new ResponseEntity<TrabajosModel>(trabajoService.actualizarTrabajo(trabajoId, trabajo), HttpStatus.OK);
    }

    @DeleteMapping("/{trabajoId}")
    public ResponseEntity<String> eliminarTrabajo(@PathVariable ObjectId trabajoId){
        trabajoService.eliminarTrabajo(trabajoId);
        return new ResponseEntity<String>("Trabajo con ID " + trabajoId + " eliminado correctamente.", HttpStatus.OK);
    }

}
