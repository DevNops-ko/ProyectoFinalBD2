package com.proyectoBD2.APIrest.model;
import java.util.List;


import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document 
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class CitaClienteModel {
    private String problemasObservados;
    private List<ServicioModel> serviciosInteres;
}
