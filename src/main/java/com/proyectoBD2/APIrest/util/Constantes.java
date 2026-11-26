package com.proyectoBD2.APIrest.util;

import java.util.List;

public final class Constantes {

    private Constantes() {
    }


    public static final String ROL_CLIENTE = "cliente";
    public static final String ROL_TECNICO = "tecnico";
    public static final String ROL_COORDINADOR = "coordinador";

    public static final String DECISION_PENDIENTE = "Pendiente";
    public static final String DECISION_AUTORIZADO = "Autorizado";
    public static final List<String> DECISIONES_TRABAJO = List.of("Pendiente", "Autorizado", "Rechazado");


    public static final List<String> ESTADOS_CITA = List.of("Solicitada", "Confirmada", "Atendida", "Cancelada");
    
    public static final List<String> ESTADOS_CITA_ACTIVOS = List.of("Solicitada", "Confirmada");


    public static final String ESTADO_LISTA_ENTREGA = "Lista para Entrega";
    public static final String ESTADO_ENTREGADO = "Entregado";
    public static final List<String> ESTADOS_ORDEN = List.of("Recibida", "En Diagnóstico",
            "Pendiente de Autorización", "En Ejecución", "Lista para Entrega", "Entregado", "Cancelado");
    
    public static final List<String> ESTADOS_ORDEN_ACTIVOS = List.of("Recibida", "En Diagnóstico",
            "Pendiente de Autorización", "En Ejecución", "Lista para Entrega");


    public static final String TIPO_MONTANA = "montaña";
    public static final String TIPO_ELECTRICA = "electrica";
    public static final List<String> TIPOS_BICICLETA = List.of("urbana", "ruta", "montaña", "electrica");
}
