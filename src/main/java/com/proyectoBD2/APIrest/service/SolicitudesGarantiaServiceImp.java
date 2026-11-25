package com.proyectoBD2.APIrest.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyectoBD2.APIrest.model.GarantiaServicioModel;
import com.proyectoBD2.APIrest.model.SolicitudesGarantiaModel;
import com.proyectoBD2.APIrest.model.TrabajosModel;
import com.proyectoBD2.APIrest.repository.ISolicitudesGarantiaRepository;

@Service
public class SolicitudesGarantiaServiceImp implements ISolicitudesGarantiaService {

    private static final long MILISEGUNDOS_POR_DIA = 24L * 60 * 60 * 1000;

    @Autowired ISolicitudesGarantiaRepository solicitudGarantiaRepository;
    @Autowired ITrabajosService trabajoService;


    private void validarSolicitud(SolicitudesGarantiaModel solicitud) {
        ObjectId trabajoId = solicitud.getTrabajoId();
        TrabajosModel trabajo = trabajoService.buscarTrabajosPorId(trabajoId);
        if (trabajo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El trabajo con ID " + trabajoId + " no existe o es nulo.");
        }

        GarantiaServicioModel garantia = trabajo.getGarantiaServicio();
        if (garantia == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El trabajo con ID " + trabajoId + " no tiene una garantia asociada.");
        }

        if (solicitud.getFechaSV() == null) {
            solicitud.setFechaSV(new Date());
        }

        if (garantia.getFechaInicioGS() != null && garantia.getDuracion() != null) {
            long inicio = garantia.getFechaInicioGS().getTime();
            long fin = inicio + garantia.getDuracion() * MILISEGUNDOS_POR_DIA;
            long fechaSolicitud = solicitud.getFechaSV().getTime();
            if (fechaSolicitud < inicio || fechaSolicitud > fin) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "La solicitud esta fuera de la vigencia de la garantia (" + garantia.getDuracion() + " dias desde su inicio).");
            }
        }
    }

    @Override
    public SolicitudesGarantiaModel crearSolicitudGarantia(SolicitudesGarantiaModel solicitudGarantia) {
        validarSolicitud(solicitudGarantia);
        return solicitudGarantiaRepository.save(solicitudGarantia);
    }

    @Override
    public List<SolicitudesGarantiaModel> listarSolicitudesGarantia() {
        return solicitudGarantiaRepository.findAll();
    }

    @Override
    public SolicitudesGarantiaModel buscarSolicitudesGarantiaPorId(ObjectId idSolicitudGarantia) {
        if (idSolicitudGarantia == null) {
            return null;
        }
        Optional<SolicitudesGarantiaModel> existente = solicitudGarantiaRepository.findById(idSolicitudGarantia);
        return existente.orElse(null);
    }

    @Override
    public SolicitudesGarantiaModel actualizarSolicitudGarantia(ObjectId idSolicitudGarantia, SolicitudesGarantiaModel solicitudGarantia) {
        if (buscarSolicitudesGarantiaPorId(idSolicitudGarantia) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + idSolicitudGarantia + " en solicitudesGarantia.");
        }
        validarSolicitud(solicitudGarantia);
        solicitudGarantia.setId(idSolicitudGarantia);
        return solicitudGarantiaRepository.save(solicitudGarantia);
    }

    @Override
    public void eliminarSolicitudGarantia(ObjectId idSolicitudGarantia) {
        if (buscarSolicitudesGarantiaPorId(idSolicitudGarantia) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + idSolicitudGarantia + " en solicitudesGarantia.");
        }
        solicitudGarantiaRepository.deleteById(idSolicitudGarantia);
    }
}
