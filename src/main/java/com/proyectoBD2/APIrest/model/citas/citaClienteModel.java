package com.proyectoBD2.APIrest.model.citas;
import java.util.List;


import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document 
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class citaClienteModel {
    private String problemasObservados;
    private List<serviciosInteresModel> serviciosInteres;
}
