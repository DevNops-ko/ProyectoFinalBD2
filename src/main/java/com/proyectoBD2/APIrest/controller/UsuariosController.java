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

import com.proyectoBD2.APIrest.model.UsuarioModel;
import com.proyectoBD2.APIrest.service.IUsuarioService;

@RestController 
@RequestMapping ("/UAO/apirest/usuarios")
public class UsuariosController {
    @Autowired IUsuarioService usuarioService;

    @PostMapping ("/")
    public ResponseEntity<UsuarioModel> crearUsuario(@RequestBody UsuarioModel usuario){
        return new ResponseEntity<UsuarioModel>(usuarioService.crearUsuario(usuario), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<UsuarioModel>> listarUsuarios(){
        return new ResponseEntity<List<UsuarioModel>>(usuarioService.listarUsuarios(), HttpStatus.OK);
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<UsuarioModel> buscarUsuariosPorId(@PathVariable ObjectId usuarioId){
        UsuarioModel usuario = usuarioService.buscarUsuariosPorId(usuarioId);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con ID " + usuarioId + " no existe.");
        }
        return new ResponseEntity<UsuarioModel>(usuario, HttpStatus.OK);
    }

    @PutMapping("/{usuarioId}")
    public ResponseEntity<UsuarioModel> actualizarUsuario(@PathVariable ObjectId usuarioId, @RequestBody UsuarioModel usuario) {
        UsuarioModel actualizado = usuarioService.actualizarUsuario(usuarioId, usuario);
        if (actualizado == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con ID " + usuarioId + " no existe.");
        }
        return new ResponseEntity<UsuarioModel>(actualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable ObjectId usuarioId) {
        UsuarioModel usuario = usuarioService.buscarUsuariosPorId(usuarioId);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con ID " + usuarioId + " no existe.");
        }
        usuarioService.eliminarUsuario(usuarioId);
        return new ResponseEntity<String>("Usuario eliminado correctamente.", HttpStatus.OK);
    }
}