package com.proyectoBD2.APIrest.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyectoBD2.APIrest.model.BicicletasModel;
import com.proyectoBD2.APIrest.model.CitasModel;
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
        ObjectId usuarioId = ordenServicio.getUsuarioId();
        usuarioService.validarRol(usuarioId, Constantes.ROL_CLIENTE);

        ObjectId bicicletaId = ordenServicio.getBicicletaId();
        BicicletasModel bicicleta = bicicletaService.buscarBicicletasPorId(bicicletaId);
        if (bicicleta == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La bicicleta con ID " + bicicletaId + " no existe o es nulo.");
        }
        if (!usuarioId.equals(bicicleta.getUsuarioId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "La bicicleta con ID " + bicicletaId + " no pertenece al cliente con ID " + usuarioId + ".");
        }

        usuarioService.validarRol(ordenServicio.getCoordinadorResponsable(), Constantes.ROL_COORDINADOR);

        if (ordenServicio.getTecnicosAsignados() != null) {
            for (ObjectId tecnicoId : ordenServicio.getTecnicosAsignados()) {
                usuarioService.validarRol(tecnicoId, Constantes.ROL_TECNICO);
            }
        }

        ObjectId citaId = ordenServicio.getCitaId();
        if (citaId != null) {
            CitasModel cita = citaService.buscarCitasPorId(citaId);
            if (cita == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La cita con ID " + citaId + " no existe o es nulo.");
            }
            if (!usuarioId.equals(cita.getUsuarioId()) || !bicicletaId.equals(cita.getBicicletaId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La cita con ID " + citaId + " no corresponde a este cliente y esta bicicleta.");
            }
        }

        Validaciones.validarEnum("estado", ordenServicio.getEstado(), Constantes.ESTADOS_ORDEN);

        if (ordenServicio.getHistorialCambios() != null) {
            for (HistorialCambiosModel cambio : ordenServicio.getHistorialCambios()) {
                Validaciones.validarEnum("historialCambios.estado", cambio.getEstado(), Constantes.ESTADOS_ORDEN);
            }
        }

        if (ordenServicio.getDiagnostico() != null && ordenServicio.getDiagnostico().getHallazgos() != null) {
            for (HallazgosModel hallazgo : ordenServicio.getDiagnostico().getHallazgos()) {
                ObjectId tecnicoId = hallazgo.getTecnicoId();
                usuarioService.validarRol(tecnicoId, Constantes.ROL_TECNICO);
                if (ordenServicio.getTecnicosAsignados() == null || !ordenServicio.getTecnicosAsignados().contains(tecnicoId)) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                            "El tecnico con ID " + tecnicoId + " registro un hallazgo pero no esta en tecnicosAsignados de la orden.");
                }
            }
        }
    }

    @Override
    public OrdenServicioModel crearOrdenServicio(OrdenServicioModel ordenServicio) {
        validarOrden(ordenServicio);
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
        validarOrden(ordenServicio);
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
