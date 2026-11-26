package com.proyectoBD2.APIrest.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyectoBD2.APIrest.model.BicicletasModel;
import com.proyectoBD2.APIrest.model.CitaClienteModel;
import com.proyectoBD2.APIrest.model.CitasModel;
import com.proyectoBD2.APIrest.repository.ICitasRepository;
import com.proyectoBD2.APIrest.util.Constantes;
import com.proyectoBD2.APIrest.util.Validaciones;

@Service
public class CitasServiceImp implements ICitasService {

    @Autowired ICitasRepository citaRepository;
    @Autowired IUsuarioService usuarioService;
    @Autowired IBicicletasService bicicletaService;
    @Autowired ICatalogoServiciosService catalogoServiciosService;

    private void validarCita(CitasModel cita, CitasModel existente) {
        ObjectId usuarioId = cita.getUsuarioId();
        usuarioService.validarRol(usuarioId, Constantes.ROL_CLIENTE);

        ObjectId bicicletaId = cita.getBicicletaId();
        BicicletasModel bicicleta = bicicletaService.buscarBicicletasPorId(bicicletaId);
        if (bicicleta == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La bicicleta con ID " + bicicletaId + " no existe o es nulo.");
        }
        if (!usuarioId.equals(bicicleta.getUsuarioId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "La bicicleta con ID " + bicicletaId + " no pertenece al cliente con ID " + usuarioId + ".");
        }

        if (cita.getCitaCliente() != null) {
            for (CitaClienteModel citaCliente : cita.getCitaCliente()) {
                if (citaCliente.getServiciosInteres() != null) {
                    for (ObjectId servicioId : citaCliente.getServiciosInteres()) {
                        if (catalogoServiciosService.buscarServiciosPorId(servicioId) == null) {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El servicio con ID " + servicioId + " no existe en el catalogo.");
                        }
                    }
                }
            }
        }

        Validaciones.validarEnum("estado", cita.getEstado(), Constantes.ESTADOS_CITA);

        if (Constantes.ESTADOS_CITA_ACTIVOS.contains(cita.getEstado()) && cita.getFechaHora() != null) {
            List<CitasModel> mismoHorario = citaRepository.findByBicicletaIdAndFechaHoraAndEstadoIn(
                    bicicletaId, cita.getFechaHora(), Constantes.ESTADOS_CITA_ACTIVOS);
            for (CitasModel otra : mismoHorario) {
                if (existente == null || !otra.getId().equals(existente.getId())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "La bicicleta con ID " + bicicletaId + " ya tiene una cita activa (ID " + otra.getId() + ") en ese mismo horario.");
                }
            }
        }
        
        if (existente != null && existente.getFechaHora() != null && !existente.getFechaHora().equals(cita.getFechaHora())) {
            String observacion = cita.getObservacionesCoordinador();
            if (observacion == null || observacion.isBlank() || observacion.equals(existente.getObservacionesCoordinador())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Al modificar el horario de la cita se debe registrar una nueva observacion en 'observacionesCoordinador'.");
            }
        }
    }

    @Override
    public CitasModel crearCita(CitasModel cita) {
        validarCita(cita, null);
        return citaRepository.save(cita);
    }

    @Override
    public List<CitasModel> listarCitas() {
        return citaRepository.findAll();
    }

    @Override
    public CitasModel buscarCitasPorId(ObjectId idCita) {
        if (idCita == null) {
            return null;
        }
        Optional<CitasModel> existente = citaRepository.findById(idCita);
        return existente.orElse(null);
    }

    @Override
    public CitasModel actualizarCita(ObjectId idCita, CitasModel cita) {
        CitasModel existente = buscarCitasPorId(idCita);
        if (existente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + idCita + " en citas.");
        }
        validarCita(cita, existente);
        cita.setId(idCita);
        return citaRepository.save(cita);
    }

    @Override
    public void eliminarCita(ObjectId idCita) {
        if (buscarCitasPorId(idCita) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + idCita + " en citas.");
        }
        citaRepository.deleteById(idCita);
    }
}
