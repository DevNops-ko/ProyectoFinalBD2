package com.proyectoBD2.APIrest.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyectoBD2.APIrest.model.AutorizacionModel;
import com.proyectoBD2.APIrest.model.OrdenServicioModel;
import com.proyectoBD2.APIrest.model.RepuestosPrevistosModel;
import com.proyectoBD2.APIrest.model.RepuestosUsadosModel;
import com.proyectoBD2.APIrest.model.TrabajosModel;
import com.proyectoBD2.APIrest.repository.ITrabajosRepository;
import com.proyectoBD2.APIrest.util.Constantes;
import com.proyectoBD2.APIrest.util.Validaciones;

@Service
public class TrabajosServiceImp implements ITrabajosService {

    @Autowired ITrabajosRepository trabajoRepository;
    @Autowired IOrdenServicioService ordenServicioService;
    @Autowired IUsuarioService usuarioService;
    @Autowired ICatalogoServiciosService catalogoServiciosService;
    @Autowired IRepuestosService repuestosService;


    private void validarTrabajo(TrabajosModel trabajo) {
        ObjectId ordenServicioId = trabajo.getOrdenServicioId();
        OrdenServicioModel orden = ordenServicioService.buscarOrdenesServicioPorId(ordenServicioId);
        if (orden == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La orden de servicio con ID " + ordenServicioId + " no existe o es nulo.");
        }

        ObjectId tecnicoId = trabajo.getTecnicoId();
        usuarioService.validarRol(tecnicoId, Constantes.ROL_TECNICO);
        if (orden.getTecnicosAsignados() == null || !orden.getTecnicosAsignados().contains(tecnicoId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "El tecnico con ID " + tecnicoId + " no esta asignado a la orden de servicio " + ordenServicioId + ".");
        }

        if (trabajo.getServicio() != null) {
            ObjectId servicioId = trabajo.getServicio().getServicioId();
            if (catalogoServiciosService.buscarServiciosPorId(servicioId) == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El servicio con ID " + servicioId + " no existe en el catalogo.");
            }
        }

        if (trabajo.getRepuestosPrevistos() != null) {
            for (RepuestosPrevistosModel repuestoPrevisto : trabajo.getRepuestosPrevistos()) {
                ObjectId repuestoId = repuestoPrevisto.getRepuestoId();
                if (repuestosService.buscarRepuestosPorId(repuestoId) == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El repuesto con ID " + repuestoId + " no existe.");
                }
            }
        }

        if (trabajo.getEjecucion() != null && trabajo.getEjecucion().getRepuestosUsados() != null) {
            for (RepuestosUsadosModel repuestoUsado : trabajo.getEjecucion().getRepuestosUsados()) {
                ObjectId repuestoId = repuestoUsado.getRepuestoId();
                if (repuestosService.buscarRepuestosPorId(repuestoId) == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El repuesto con ID " + repuestoId + " no existe.");
                }
            }
        }

        if (trabajo.getAutorizacion() == null) {
            trabajo.setAutorizacion(new AutorizacionModel(Constantes.DECISION_PENDIENTE, null, null));
        }
        Validaciones.validarEnum("autorizacion.decision", trabajo.getAutorizacion().getDecision(), Constantes.DECISIONES_TRABAJO);
    }

    @Override
    public TrabajosModel crearTrabajo(TrabajosModel trabajo) {
        validarTrabajo(trabajo);
        return trabajoRepository.save(trabajo);
    }

    @Override
    public List<TrabajosModel> listarTrabajos() {
        return trabajoRepository.findAll();
    }

    @Override
    public TrabajosModel buscarTrabajosPorId(ObjectId idTrabajo) {
        if (idTrabajo == null) {
            return null;
        }
        Optional<TrabajosModel> existente = trabajoRepository.findById(idTrabajo);
        return existente.orElse(null);
    }

    @Override
    public TrabajosModel actualizarTrabajo(ObjectId idTrabajo, TrabajosModel trabajo) {
        if (buscarTrabajosPorId(idTrabajo) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + idTrabajo + " en trabajos.");
        }
        validarTrabajo(trabajo);
        trabajo.setId(idTrabajo);
        return trabajoRepository.save(trabajo);
    }

    @Override
    public void eliminarTrabajo(ObjectId idTrabajo) {
        if (buscarTrabajosPorId(idTrabajo) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + idTrabajo + " en trabajos.");
        }
        trabajoRepository.deleteById(idTrabajo);
    }
}
