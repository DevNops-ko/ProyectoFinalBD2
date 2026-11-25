package com.proyectoBD2.APIrest.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyectoBD2.APIrest.model.CitaClienteModel;
import com.proyectoBD2.APIrest.model.CitasModel;
import com.proyectoBD2.APIrest.repository.ICitasRepository;

@Service
public class CitasServiceImp implements ICitasService {
    @Autowired ICitasRepository citaRepository;
    @Autowired IUsuarioService usuarioService;
    @Autowired IBicicletasService bicicletaService;
    @Autowired ICatalogoServiciosService catalogoServiciosService;

    @Override
    public CitasModel crearCita(CitasModel cita) {
        ObjectId usuarioId = cita.getUsuarioId();
        if (usuarioService.buscarUsuariosPorId(usuarioId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con ID " + usuarioId + " no existe o es nulo.");
        }
        ObjectId bicicletaId = cita.getBicicletaId();
        if (bicicletaService.buscarBicicletasPorId(bicicletaId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La bicicleta con ID " + bicicletaId + " no existe o es nulo.");
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
        if (buscarCitasPorId(idCita) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + idCita + " en citas.");
        }
        ObjectId usuarioId = cita.getUsuarioId();
        if (usuarioService.buscarUsuariosPorId(usuarioId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con ID " + usuarioId + " no existe o es nulo.");
        }
        ObjectId bicicletaId = cita.getBicicletaId();
        if (bicicletaService.buscarBicicletasPorId(bicicletaId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La bicicleta con ID " + bicicletaId + " no existe o es nulo.");
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