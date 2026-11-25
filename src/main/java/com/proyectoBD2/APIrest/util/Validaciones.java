package com.proyectoBD2.APIrest.util;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** Validaciones reutilizables de valores que en MongoDB son "enum". */
public final class Validaciones {

    private Validaciones() {
    }

    public static void validarEnum(String campo, String valor, List<String> permitidos) {
        if (valor == null || !permitidos.contains(valor)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Valor invalido para '" + campo + "': " + valor + ". Valores permitidos: " + permitidos + ".");
        }
    }

    public static String normalizarTipoBicicleta(String tipo) {
        if (tipo != null) {
            String buscado = tipo.trim().toLowerCase().replace("\u00e9", "e");
            for (String permitido : Constantes.TIPOS_BICICLETA) {
                if (permitido.equals(buscado)) {
                    return permitido;
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Valor invalido para 'tipo': " + tipo + ". Valores permitidos: " + Constantes.TIPOS_BICICLETA + ".");
    }
}
