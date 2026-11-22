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

@Service
public class BicicletasServiceImp implements IBicicletasService {
    @Autowired IBicicletasRepository bicicletaRepository;
    @Autowired IUsuarioService usuarioService;

    @Override
    public BicicletasModel crearBicicleta(BicicletasModel bicicleta) {
        ObjectId usuarioId = bicicleta.getUsuarioId();
        if (usuarioService.buscarUsuariosPorId(usuarioId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con ID " + usuarioId + " no existe o es nulo.");
        }
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
}
