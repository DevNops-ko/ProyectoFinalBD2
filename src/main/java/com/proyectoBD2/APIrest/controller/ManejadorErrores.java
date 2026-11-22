package com.proyectoBD2.APIrest.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> manejarErrorDeMongo(DataAccessException e) {
        String causa = e.getMostSpecificCause().getMessage();
        // Mongo rechaza el documento cuando no cumple el esquema (validationAction: error, codigo 121)
        if (causa != null && (causa.contains("Document failed validation") || causa.contains("code=121"))) {
            return new ResponseEntity<String>(
                "El documento no cumple el esquema de validacion de MongoDB: " + causa, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("Error de base de datos: " + causa, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
