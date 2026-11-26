package com.proyectoBD2.APIrest.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyectoBD2.APIrest.model.AutorizacionModel;
import com.proyectoBD2.APIrest.model.CatalogoServiciosModel;
import com.proyectoBD2.APIrest.model.OrdenServicioModel;
import com.proyectoBD2.APIrest.model.RepuestosModel;
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

    private void validarTrabajo(TrabajosModel trabajo, TrabajosModel existente) {
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
            CatalogoServiciosModel servicioCatalogo = catalogoServiciosService.buscarServiciosPorId(servicioId);
            if (servicioCatalogo == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El servicio con ID " + servicioId + " no existe en el catalogo.");
            }
            if (trabajo.getServicio().getNombre() == null) {
                trabajo.getServicio().setNombre(servicioCatalogo.getNombre());
            }
        }

        if (trabajo.getRepuestosPrevistos() != null) {
            for (RepuestosPrevistosModel repuestoPrevisto : trabajo.getRepuestosPrevistos()) {
                ObjectId repuestoId = repuestoPrevisto.getRepuestoId();
                RepuestosModel repuesto = repuestosService.buscarRepuestosPorId(repuestoId);
                if (repuesto == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El repuesto con ID " + repuestoId + " no existe.");
                }
                if (repuestoPrevisto.getNombre() == null) {
                    repuestoPrevisto.setNombre(repuesto.getNombre());
                }
                if (repuestoPrevisto.getEspecificaciones() == null) {
                    repuestoPrevisto.setEspecificaciones(repuesto.getEspecificaciones());
                }
                if (repuestoPrevisto.getPrecioUnitario() == null) {
                    repuestoPrevisto.setPrecioUnitario(repuesto.getPrecio());
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
        AutorizacionModel autorizacion = trabajo.getAutorizacion();
        Validaciones.validarEnum("autorizacion.decision", autorizacion.getDecision(), Constantes.DECISIONES_TRABAJO);

        if (existente == null && !Constantes.DECISION_PENDIENTE.equals(autorizacion.getDecision())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Un trabajo nuevo debe registrarse con autorizacion.decision = 'Pendiente'; el cliente decide despues.");
        }

        if (!Constantes.DECISION_PENDIENTE.equals(autorizacion.getDecision()) && autorizacion.getFecha() == null) {
            autorizacion.setFecha(new Date());
        }

        if (trabajo.getEjecucion() != null && !Constantes.DECISION_AUTORIZADO.equals(autorizacion.getDecision())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Solo los trabajos con autorizacion.decision = 'Autorizado' pueden registrar ejecucion.");
        }
    }

    @Override
    public TrabajosModel crearTrabajo(TrabajosModel trabajo) {
        validarTrabajo(trabajo, null);
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
        TrabajosModel existente = buscarTrabajosPorId(idTrabajo);
        if (existente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + idTrabajo + " en trabajos.");
        }
        validarTrabajo(trabajo, existente);
        trabajo.setId(idTrabajo);
        return trabajoRepository.save(trabajo);
    }

    @Override
    public void eliminarTrabajo(ObjectId idTrabajo) {
        TrabajosModel existente = buscarTrabajosPorId(idTrabajo);
        if (existente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + idTrabajo + " en trabajos.");
        }
        // Enunciado: los trabajos rechazados "permaneceran en la orden como parte del historial".
        // Solo se puede borrar un trabajo que el cliente aun no ha decidido.
        if (existente.getAutorizacion() != null
                && !Constantes.DECISION_PENDIENTE.equals(existente.getAutorizacion().getDecision())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede eliminar un trabajo con decision '" + existente.getAutorizacion().getDecision()
                            + "': forma parte del historial de la orden.");
        }
        trabajoRepository.deleteById(idTrabajo);
    }
}
