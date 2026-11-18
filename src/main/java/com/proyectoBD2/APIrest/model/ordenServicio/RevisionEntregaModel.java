package com.proyectoBD2.APIrest.model.ordenServicio;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data 
@AllArgsConstructor 
@NoArgsConstructor
public class RevisionEntregaModel {
    private List<PuntosVerificadosModel> puntosVerificados;
}
