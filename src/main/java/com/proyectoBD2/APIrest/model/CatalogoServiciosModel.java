package com.proyectoBD2.APIrest.model;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document 
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class CatalogoServiciosModel {
    @Id
    private ObjectId id;
    private String nombre;
    private String categoria;
    private String descripcionCS;
    private Integer duracionEstimada;
    private Integer precioReferencia;
    private List<String> tareasHabituales;
    private List<String> recomendacionesGenerales;
}
