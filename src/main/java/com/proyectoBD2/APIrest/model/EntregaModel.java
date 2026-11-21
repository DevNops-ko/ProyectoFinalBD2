package com.proyectoBD2.APIrest.model;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor
public class EntregaModel {
    private Date fecha;
    private Boolean confirmacionRecepcionCliente;
}
