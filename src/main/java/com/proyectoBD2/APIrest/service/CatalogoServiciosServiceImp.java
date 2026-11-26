package com.proyectoBD2.APIrest.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyectoBD2.APIrest.model.CatalogoServiciosModel;
import com.proyectoBD2.APIrest.repository.ICatalogoServiciosRepository;
import com.proyectoBD2.APIrest.util.Constantes;
import com.proyectoBD2.APIrest.util.Validaciones;

@Service
public class CatalogoServiciosServiceImp implements ICatalogoServiciosService {
    @Autowired ICatalogoServiciosRepository catalogoServiciosRepository;

    @Override
    public CatalogoServiciosModel crearServicio(CatalogoServiciosModel servicio) {
        Validaciones.validarEnum("categoria", servicio.getCategoria(), Constantes.CATEGORIAS_SERVICIO);
        return catalogoServiciosRepository.save(servicio);
    }

    @Override
    public List<CatalogoServiciosModel> listarServicios() {
        return catalogoServiciosRepository.findAll();
    }

    @Override
    public CatalogoServiciosModel buscarServiciosPorId(ObjectId idServicio) {
        if (idServicio == null) {
            return null;
        }
        Optional<CatalogoServiciosModel> existente = catalogoServiciosRepository.findById(idServicio);
        return existente.orElse(null);
    }
    
    @Override
    public CatalogoServiciosModel actualizarServicio(ObjectId idServicio, CatalogoServiciosModel servicio) {
        if (buscarServiciosPorId(idServicio) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + idServicio + " en catalogoServicios.");
        }
        Validaciones.validarEnum("categoria", servicio.getCategoria(), Constantes.CATEGORIAS_SERVICIO);
        servicio.setId(idServicio);
        return catalogoServiciosRepository.save(servicio);
    }

    @Override
    public void eliminarServicio(ObjectId idServicio) {
        if (buscarServiciosPorId(idServicio) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + idServicio + " en catalogoServicios.");
        }
        catalogoServiciosRepository.deleteById(idServicio);
    }

}