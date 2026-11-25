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

import com.proyectoBD2.APIrest.model.BicicletasModel;
import com.proyectoBD2.APIrest.service.IBicicletasService;

@RestController
@RequestMapping ("/UAO/apirest/bicicletas")
public class BicicletasController {
    @Autowired IBicicletasService bicicletaService;

    @PostMapping("/")
    public ResponseEntity<BicicletasModel> crearBicicleta(@RequestBody BicicletasModel bicicleta){
        return new ResponseEntity<BicicletasModel>(bicicletaService.crearBicicleta(bicicleta), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<BicicletasModel>> listarBicicletas(){
        return new ResponseEntity<List<BicicletasModel>>(bicicletaService.listarBicicletas(), HttpStatus.OK);
    }

    @GetMapping("/{bicicletaId}")
    public ResponseEntity<BicicletasModel> buscarBicicletasPorId(@PathVariable ObjectId bicicletaId){
        BicicletasModel bicicleta = bicicletaService.buscarBicicletasPorId(bicicletaId);
        if (bicicleta == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + bicicletaId + " en bicicletas.");
        }
        return new ResponseEntity<BicicletasModel>(bicicleta, HttpStatus.OK);
    }

    @PutMapping("/{bicicletaId}")
    public ResponseEntity<BicicletasModel> actualizarBicicleta(@PathVariable ObjectId bicicletaId, @RequestBody BicicletasModel bicicleta){
        return new ResponseEntity<BicicletasModel>(bicicletaService.actualizarBicicleta(bicicletaId, bicicleta), HttpStatus.OK);
    }

    @DeleteMapping("/{bicicletaId}")
    public ResponseEntity<String> eliminarBicicleta(@PathVariable ObjectId bicicletaId){
        bicicletaService.eliminarBicicleta(bicicletaId);
        return new ResponseEntity<String>("Bicicleta con ID " + bicicletaId + " eliminada correctamente.", HttpStatus.OK);
    }

}
