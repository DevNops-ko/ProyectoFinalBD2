package com.proyectoBD2.APIrest.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<String> manejarDuplicado(DuplicateKeyException e) {
        return new ResponseEntity<String>(
            "MongoDB rechazo el documento por un indice unico: " + e.getMostSpecificCause().getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> manejarErrorDeMongo(DataAccessException e) {
        String causa = e.getMostSpecificCause().getMessage();
        if (causa != null && (causa.contains("Document failed validation") || causa.contains("code=121"))) {
            return new ResponseEntity<String>(
                "El documento no cumple el esquema de validacion de MongoDB: " + causa, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("Error de base de datos: " + causa, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
