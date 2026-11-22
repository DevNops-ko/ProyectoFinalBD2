package com.proyectoBD2.APIrest.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.proyectoBD2.APIrest.model.UsuarioModel;

public interface IUsuarioService {
    UsuarioModel crearUsuario(UsuarioModel usuario);
    List<UsuarioModel> listarUsuarios();
    UsuarioModel buscarUsuariosPorId(ObjectId idUsuario);
}
