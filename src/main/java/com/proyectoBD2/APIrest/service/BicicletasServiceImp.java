package com.proyectoBD2.APIrest.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyectoBD2.APIrest.model.BicicletasModel;
import com.proyectoBD2.APIrest.repository.IBicicletasRepository;
import com.proyectoBD2.APIrest.util.Constantes;
import com.proyectoBD2.APIrest.util.Validaciones;

@Service
public class BicicletasServiceImp implements IBicicletasService {

    @Autowired IBicicletasRepository bicicletaRepository;
    @Autowired IUsuarioService usuarioService;

    
    private void validarBicicleta(BicicletasModel bicicleta) {
        usuarioService.validarRol(bicicleta.getUsuarioId(), Constantes.ROL_CLIENTE);
        bicicleta.setTipo(Validaciones.normalizarTipoBicicleta(bicicleta.getTipo()));

        String tipo = bicicleta.getTipo();
        if (!Constantes.TIPO_MONTANA.equals(tipo) && (bicicleta.getSuspension() != null || bicicleta.getRecorrido() != null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Los campos 'suspension' y 'recorrido' solo aplican a bicicletas de tipo '" + Constantes.TIPO_MONTANA + "'.");
        }
        if (!Constantes.TIPO_ELECTRICA.equals(tipo) && (bicicleta.getBateria() != null || bicicleta.getMotor() != null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Los campos 'bateria' y 'motor' solo aplican a bicicletas de tipo '" + Constantes.TIPO_ELECTRICA + "'.");
        }
    }

    @Override
    public BicicletasModel crearBicicleta(BicicletasModel bicicleta) {
        validarBicicleta(bicicleta);
        return bicicletaRepository.save(bicicleta);
    }

    @Override
    public List<BicicletasModel> listarBicicletas() {
        return bicicletaRepository.findAll();
    }

    @Override
    public BicicletasModel buscarBicicletasPorId(ObjectId bicicletaId) {
        if (bicicletaId == null) {
            return null;
        }
        Optional<BicicletasModel> existente = bicicletaRepository.findById(bicicletaId);
        return existente.orElse(null);
    }

    @Override
    public BicicletasModel actualizarBicicleta(ObjectId bicicletaId, BicicletasModel bicicleta) {
        if (buscarBicicletasPorId(bicicletaId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + bicicletaId + " en bicicletas.");
        }
        validarBicicleta(bicicleta);
        bicicleta.setId(bicicletaId);
        return bicicletaRepository.save(bicicleta);
    }

    @Override
    public void eliminarBicicleta(ObjectId bicicletaId) {
        if (buscarBicicletasPorId(bicicletaId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + bicicletaId + " en bicicletas.");
        }
        bicicletaRepository.deleteById(bicicletaId);
    }
}
