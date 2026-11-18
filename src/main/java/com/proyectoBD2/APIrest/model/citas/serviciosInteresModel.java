package com.proyectoBD2.APIrest.model.citas;

import org.springframework.data.annotation.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document 
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class serviciosInteresModel {
    @Id
    private ObjectId usuarioId;

}
