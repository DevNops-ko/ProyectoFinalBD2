package com.proyectoBD2.APIrest.model;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("bicicletas")
@Data
@AllArgsConstructor 
@NoArgsConstructor 
public class BicicletasModel {
    @Id
    private ObjectId id;
    private ObjectId usuarioId;
    private String marca;
    private Integer modelo;
    private String color;
    private String numeroSerie;
    private List<CaracteristicasTecnicasModel> caracteristicasTecnicas;
    private String suspension; 
    private String recorrido; 
    private String bateria; 
    private String motor; 
}
