package com.proyectoBD2.APIrest.util;

import java.util.List;

public final class Constantes {

    private Constantes() {
    }

    public static final String ROL_CLIENTE = "cliente";
    public static final String ROL_TECNICO = "tecnico";
    public static final String ROL_COORDINADOR = "coordinador";

    public static final String DECISION_PENDIENTE = "Pendiente";
    public static final List<String> DECISIONES_TRABAJO = List.of("Pendiente", "Autorizado", "Rechazado");

    public static final List<String> ESTADOS_CITA = List.of("Solicitada", "Confirmada", "Atendida", "Cancelada");

    public static final List<String> ESTADOS_ORDEN = List.of("Recibida", "En Diagn\u00f3stico",
            "Pendiente de Autorizaci\u00f3n", "En Ejecuci\u00f3n", "Lista para Entrega", "Entregado", "Cancelado");

    public static final List<String> TIPOS_BICICLETA = List.of("urbana", "ruta", "monta\u00f1a", "electrica");
}
