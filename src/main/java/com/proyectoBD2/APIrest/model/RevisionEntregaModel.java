package com.proyectoBD2.APIrest.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor
public class RevisionEntregaModel {
    private List<String> puntosVerificados;
    private String resultado;
    private Boolean aprobada;
    private String recomendaciones;
}
