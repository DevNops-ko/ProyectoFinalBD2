package com.proyectoBD2.APIrest.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyectoBD2.APIrest.model.HallazgosModel;
import com.proyectoBD2.APIrest.model.HistorialCambiosModel;
import com.proyectoBD2.APIrest.model.OrdenServicioModel;
import com.proyectoBD2.APIrest.repository.IOrdenServicioRepository;
import com.proyectoBD2.APIrest.util.Constantes;
import com.proyectoBD2.APIrest.util.Validaciones;

@Service
public class OrdenServiciosServiceImp implements IOrdenServicioService {
    @Autowired IOrdenServicioRepository ordenServicioRepository;
    @Autowired IBicicletasService bicicletaService;
    @Autowired IUsuarioService usuarioService;
    @Autowired ICitasService citaService;

    private void validarOrden(OrdenServicioModel ordenServicio) {
        ObjectId bicicletaId = ordenServicio.getBicicletaId();
        if (bicicletaService.buscarBicicletasPorId(bicicletaId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La bicicleta con ID " + bicicletaId + " no existe o es nulo.");
        }

        usuarioService.validarRol(ordenServicio.getUsuarioId(), Constantes.ROL_CLIENTE);
        usuarioService.validarRol(ordenServicio.getCoordinadorResponsable(), Constantes.ROL_COORDINADOR);

        if (ordenServicio.getTecnicosAsignados() != null) {
            for (ObjectId tecnicoId : ordenServicio.getTecnicosAsignados()) {
                usuarioService.validarRol(tecnicoId, Constantes.ROL_TECNICO);
            }
        }

        ObjectId citaId = ordenServicio.getCitaId();
        if (citaId != null && citaService.buscarCitasPorId(citaId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La cita con ID " + citaId + " no existe o es nulo.");
        }

        Validaciones.validarEnum("estado", ordenServicio.getEstado(), Constantes.ESTADOS_ORDEN);

        if (ordenServicio.getHistorialCambios() != null) {
            for (HistorialCambiosModel cambio : ordenServicio.getHistorialCambios()) {
                Validaciones.validarEnum("historialCambios.estado", cambio.getEstado(), Constantes.ESTADOS_ORDEN);
            }
        }

        if (ordenServicio.getDiagnostico() != null && ordenServicio.getDiagnostico().getHallazgos() != null) {
            for (HallazgosModel hallazgo : ordenServicio.getDiagnostico().getHallazgos()) {
                usuarioService.validarRol(hallazgo.getTecnicoId(), Constantes.ROL_TECNICO);
            }
        }
    }

    @Override
    public OrdenServicioModel crearOrdenServicio(OrdenServicioModel ordenServicio) {
        ObjectId bicicletaId = ordenServicio.getBicicletaId();
        if (bicicletaService.buscarBicicletasPorId(bicicletaId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La bicicleta con ID " + bicicletaId + " no existe o es nulo.");
        }
        ObjectId usuarioId = ordenServicio.getUsuarioId();
        if (usuarioService.buscarUsuariosPorId(usuarioId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con ID " + usuarioId + " no existe o es nulo.");
        }
        ObjectId coordinadorResponsable = ordenServicio.getCoordinadorResponsable();
        if (usuarioService.buscarUsuariosPorId(coordinadorResponsable) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El coordinador con ID " + coordinadorResponsable + " no existe o es nulo.");
        }
        if (ordenServicio.getTecnicosAsignados() != null) {
            for (ObjectId idItem : ordenServicio.getTecnicosAsignados()) {
                if (usuarioService.buscarUsuariosPorId(idItem) == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con ID " + idItem + " no existe o es nulo.");
                }
            }
        }
        ObjectId citaId = ordenServicio.getCitaId();
        if (citaId != null && citaService.buscarCitasPorId(citaId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La cita con ID " + citaId + " no existe o es nulo.");
        }
        
        return ordenServicioRepository.save(ordenServicio);
    }

    @Override
    public List<OrdenServicioModel> listarOrdenesServicio() {
        return ordenServicioRepository.findAll();
    }

    @Override
    public OrdenServicioModel buscarOrdenesServicioPorId(ObjectId idOrdenServicio) {
        if (idOrdenServicio == null) {
            return null;
        }
        Optional<OrdenServicioModel> existente = ordenServicioRepository.findById(idOrdenServicio);
        return existente.orElse(null);
    }
    
    @Override
    public OrdenServicioModel actualizarOrdenServicio(ObjectId idOrdenServicio, OrdenServicioModel ordenServicio) {
        if (buscarOrdenesServicioPorId(idOrdenServicio) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + idOrdenServicio + " en ordenesServicio.");
        }
        ObjectId bicicletaId = ordenServicio.getBicicletaId();
        if (bicicletaService.buscarBicicletasPorId(bicicletaId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La bicicleta con ID " + bicicletaId + " no existe o es nulo.");
        }
        ObjectId usuarioId = ordenServicio.getUsuarioId();
        if (usuarioService.buscarUsuariosPorId(usuarioId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con ID " + usuarioId + " no existe o es nulo.");
        }
        ObjectId coordinadorResponsable = ordenServicio.getCoordinadorResponsable();
        if (usuarioService.buscarUsuariosPorId(coordinadorResponsable) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El coordinador con ID " + coordinadorResponsable + " no existe o es nulo.");
        }
        if (ordenServicio.getTecnicosAsignados() != null) {
            for (ObjectId idItem : ordenServicio.getTecnicosAsignados()) {
                if (usuarioService.buscarUsuariosPorId(idItem) == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con ID " + idItem + " no existe o es nulo.");
                }
            }
        }
        ObjectId citaId = ordenServicio.getCitaId();
        if (citaId != null && citaService.buscarCitasPorId(citaId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La cita con ID " + citaId + " no existe o es nulo.");
        }
        ordenServicio.setId(idOrdenServicio);
        return ordenServicioRepository.save(ordenServicio);
    }

    @Override
    public void eliminarOrdenServicio(ObjectId idOrdenServicio) {
        if (buscarOrdenesServicioPorId(idOrdenServicio) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + idOrdenServicio + " en ordenesServicio.");
        }
        ordenServicioRepository.deleteById(idOrdenServicio);
    }

}
