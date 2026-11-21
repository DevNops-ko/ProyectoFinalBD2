package com.proyectoBD2.APIrest.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyectoBD2.APIrest.model.UsuarioModel;
import com.proyectoBD2.APIrest.repository.IUsuarioRepository;

@Service
public class UsuarioServiceImp implements IUsuarioService {
    @Autowired IUsuarioRepository usuarioRepository;

    @Override
    public UsuarioModel crearUsuario(UsuarioModel usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public List<UsuarioModel> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public UsuarioModel buscarUsuariosPorId(ObjectId usuarioId) {
        if (usuarioId == null) {
            return null;
        }
        Optional<UsuarioModel> usuarioExistente = usuarioRepository.findById(usuarioId);
        return usuarioExistente.orElse(null);
    }
}
