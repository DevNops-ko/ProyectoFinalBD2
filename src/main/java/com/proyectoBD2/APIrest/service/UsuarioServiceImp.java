package com.proyectoBD2.APIrest.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyectoBD2.APIrest.model.UsuarioModel;
import com.proyectoBD2.APIrest.repository.IUsuarioRepository;
import com.proyectoBD2.APIrest.util.Constantes;
import com.proyectoBD2.APIrest.util.Validaciones;

@Service
public class UsuarioServiceImp implements IUsuarioService {

    @Autowired IUsuarioRepository usuarioRepository;

    @Override
    public UsuarioModel crearUsuario(UsuarioModel usuario) {
        Validaciones.validarEnum("rol", usuario.getRol(), Constantes.ROLES);
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

    @Override
    public UsuarioModel actualizarUsuario(ObjectId usuarioId, UsuarioModel usuarioActualizado) {
        if (usuarioId == null || !usuarioRepository.existsById(usuarioId)) {
            return null;
        }
        Validaciones.validarEnum("rol", usuarioActualizado.getRol(), Constantes.ROLES);
        usuarioActualizado.setId(usuarioId);
        return usuarioRepository.save(usuarioActualizado);
    }

    @Override
    public void eliminarUsuario(ObjectId usuarioId) {
        if (usuarioId != null) {
            usuarioRepository.deleteById(usuarioId);
        }
    }

    @Override
    public UsuarioModel validarRol(ObjectId usuarioId, String rolEsperado) {
        UsuarioModel usuario = buscarUsuariosPorId(usuarioId);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "El usuario con ID " + usuarioId + " no existe o es nulo.");
        }
        if (usuario.getRol() == null || !usuario.getRol().equals(rolEsperado)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "El usuario con ID " + usuarioId + " tiene rol '" + usuario.getRol()
                            + "' y esta operacion exige rol '" + rolEsperado + "'.");
        }
        return usuario;
    }
}
