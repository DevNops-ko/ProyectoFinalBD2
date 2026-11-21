package com.proyectoBD2.APIrest.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class DiagnosticoModel{
    private List<HallazgosModel> hallazgos;
} 