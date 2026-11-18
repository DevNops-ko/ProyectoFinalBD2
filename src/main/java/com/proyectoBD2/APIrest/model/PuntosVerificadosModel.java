package com.proyectoBD2.APIrest.model;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data 
@AllArgsConstructor 
@NoArgsConstructor
public class PuntosVerificadosModel {
    private String resultado;
    private Boolean aprobada;
    private String recomendaciones;
}
