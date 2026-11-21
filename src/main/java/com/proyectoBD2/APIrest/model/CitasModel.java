package com.proyectoBD2.APIrest.model;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("citas")
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class CitasModel {
    @Id 
    private ObjectId id;
    private ObjectId usuarioId;
    private ObjectId biciletaId;
    private Date fechaHora;
    private String motivoVisita;
    private String estado;
    private String observacionesCoordinador;
    private List<citaClienteModel> citaCliente;
}
