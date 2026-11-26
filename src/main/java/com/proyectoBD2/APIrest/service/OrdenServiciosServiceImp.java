package com.proyectoBD2.APIrest.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyectoBD2.APIrest.model.BicicletasModel;
import com.proyectoBD2.APIrest.model.CitasModel;
import com.proyectoBD2.APIrest.model.HallazgosModel;
import com.proyectoBD2.APIrest.model.HistorialCambiosModel;
import com.proyectoBD2.APIrest.model.OrdenServicioModel;
import com.proyectoBD2.APIrest.model.TrabajosModel;
import com.proyectoBD2.APIrest.repository.IOrdenServicioRepository;
import com.proyectoBD2.APIrest.repository.ITrabajosRepository;
import com.proyectoBD2.APIrest.util.Constantes;
import com.proyectoBD2.APIrest.util.Validaciones;

@Service
public class OrdenServiciosServiceImp implements IOrdenServicioService {

    @Autowired IOrdenServicioRepository ordenServicioRepository;
    @Autowired IBicicletasService bicicletaService;
    @Autowired IUsuarioService usuarioService;
    @Autowired ICitasService citaService;

    @Autowired ITrabajosRepository trabajoRepository;
    @Autowired MongoTemplate mongoTemplate;

    private void validarOrden(OrdenServicioModel ordenServicio, OrdenServicioModel existente) {
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

        String estado = ordenServicio.getEstado();
        Validaciones.validarEnum("estado", estado, Constantes.ESTADOS_ORDEN);

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
        if (Constantes.ESTADOS_ORDEN_ACTIVOS.contains(estado)) {
            for (OrdenServicioModel otra : ordenServicioRepository.findByBicicletaIdAndEstadoIn(bicicletaId, Constantes.ESTADOS_ORDEN_ACTIVOS)) {
                if (existente == null || !otra.getId().equals(existente.getId())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "La bicicleta con ID " + bicicletaId + " ya tiene una orden activa (ID " + otra.getId()
                                    + ", estado '" + otra.getEstado() + "').");
                }
            }
        }
        if (ordenServicio.getValoracion() != null && !Constantes.ESTADO_ENTREGADO.equals(estado)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Solo se puede registrar la valoracion de una orden en estado '" + Constantes.ESTADO_ENTREGADO + "'.");
        }
        if (Constantes.ESTADO_LISTA_ENTREGA.equals(estado) || Constantes.ESTADO_ENTREGADO.equals(estado)) {
            validarListaParaEntrega(ordenServicio, existente);
        }
        if (Constantes.ESTADO_ENTREGADO.equals(estado)) {
            if (ordenServicio.getEntrega() == null || ordenServicio.getEntrega().getFecha() == null
                    || !Boolean.TRUE.equals(ordenServicio.getEntrega().getConfirmacionRecepcionCliente())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Para marcar la orden como '" + Constantes.ESTADO_ENTREGADO
                                + "' se requiere entrega.fecha y entrega.confirmacionRecepcionCliente = true.");
            }
        }

        prepararHistorial(ordenServicio, existente);
    }

    private void validarListaParaEntrega(OrdenServicioModel ordenServicio, OrdenServicioModel existente) {
        if (ordenServicio.getRevisionEntrega() == null || !Boolean.TRUE.equals(ordenServicio.getRevisionEntrega().getAprobada())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La orden no puede estar lista para entrega sin una revisionEntrega con aprobada = true.");
        }
        if (existente != null) {
            for (TrabajosModel trabajo : trabajoRepository.findByOrdenServicioId(existente.getId())) {
                boolean autorizado = trabajo.getAutorizacion() != null
                        && Constantes.DECISION_AUTORIZADO.equals(trabajo.getAutorizacion().getDecision());
                boolean finalizado = trabajo.getEjecucion() != null && trabajo.getEjecucion().getFechaFinalizacion() != null;
                if (autorizado && !finalizado) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "El trabajo autorizado con ID " + trabajo.getId()
                                    + " aun no esta finalizado (falta ejecucion.fechaFinalizacion).");
                }
            }
        }
    }
    private void prepararHistorial(OrdenServicioModel ordenServicio, OrdenServicioModel existente) {
        String estado = ordenServicio.getEstado();
        List<HistorialCambiosModel> enviado = ordenServicio.getHistorialCambios();

        if (existente == null && (enviado == null || enviado.isEmpty())) {
            Date fecha = ordenServicio.getFechaRecepcion() != null ? ordenServicio.getFechaRecepcion() : new Date();
            List<HistorialCambiosModel> inicial = new ArrayList<>();
            inicial.add(new HistorialCambiosModel(fecha, ordenServicio.getCoordinadorResponsable(),
                    "Orden registrada en estado '" + estado + "'.", estado));
            ordenServicio.setHistorialCambios(inicial);
            return;
        }

        List<HistorialCambiosModel> anterior = new ArrayList<>();
        if (existente != null && existente.getHistorialCambios() != null) {
            anterior.addAll(existente.getHistorialCambios());
        }
        if (enviado == null) {
            enviado = anterior;
        }
        if (enviado.size() < anterior.size()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se pueden eliminar entradas de historialCambios: envie el historial completo y agregue las nuevas al final.");
        }

        List<HistorialCambiosModel> nuevas = new ArrayList<>(enviado.subList(anterior.size(), enviado.size()));
        for (HistorialCambiosModel cambio : nuevas) {
            Validaciones.validarEnum("historialCambios.estado", cambio.getEstado(), Constantes.ESTADOS_ORDEN);
            if (usuarioService.buscarUsuariosPorId(cambio.getUsuarioId()) == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "El usuario con ID " + cambio.getUsuarioId() + " del historialCambios no existe o es nulo.");
            }
            if (cambio.getObservacionesHC() == null || cambio.getObservacionesHC().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Cada entrada nueva de historialCambios debe incluir observacionesHC.");
            }
            if (cambio.getFecha() == null) {
                cambio.setFecha(new Date());
            }
        }

        boolean cambioEstado = existente == null || !Objects.equals(existente.getEstado(), estado);
        if (cambioEstado && (nuevas.isEmpty() || !estado.equals(nuevas.get(nuevas.size() - 1).getEstado()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Al cambiar el estado a '" + estado + "' debe agregar al final de historialCambios una entrada con"
                            + " estado '" + estado + "', usuarioId y observacionesHC.");
        }

        List<HistorialCambiosModel> resultado = new ArrayList<>(anterior);
        resultado.addAll(nuevas);
        ordenServicio.setHistorialCambios(resultado);
    }

    @Override
    public OrdenServicioModel crearOrdenServicio(OrdenServicioModel ordenServicio) {
        validarOrden(ordenServicio, null);
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
        OrdenServicioModel existente = buscarOrdenesServicioPorId(idOrdenServicio);
        if (existente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + idOrdenServicio + " en ordenesServicio.");
        }
        validarOrden(ordenServicio, existente);
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

    @Override
    public List<OrdenServicioModel> listarOrdenesPorBicicleta(ObjectId bicicletaId) {
        if (bicicletaService.buscarBicicletasPorId(bicicletaId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La bicicleta con ID " + bicicletaId + " no existe.");
        }
        return ordenServicioRepository.findByBicicletaIdOrderByFechaRecepcionDesc(bicicletaId);
    }

    @Override
    public List<OrdenServicioModel> listarOrdenesPorCliente(ObjectId usuarioId) {
        usuarioService.validarRol(usuarioId, Constantes.ROL_CLIENTE);
        return ordenServicioRepository.findByUsuarioIdOrderByFechaRecepcionDesc(usuarioId);
    }

    @Override
    public Document calcularValorEstimado(ObjectId ordenServicioId) {
        if (buscarOrdenesServicioPorId(ordenServicioId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un registro con ID " + ordenServicioId + " en ordenesServicio.");
        }
        List<Document> pipeline = List.of(
            new Document("$match", new Document("ordenServicioId", ordenServicioId)),
            Document.parse("{ $project: {"
                + " descripcionIntervencion: 1,"
                + " decision: '$autorizacion.decision',"
                + " manoObra: { $ifNull: ['$costoManoObra', 0] },"
                + " repuestos: { $sum: { $map: { input: { $ifNull: ['$repuestosPrevistos', []] }, as: 'r',"
                + "   in: { $multiply: [ { $ifNull: ['$$r.cantidad', 0] }, { $ifNull: ['$$r.precioUnitario', 0] } ] } } } } } }"),
            Document.parse("{ $addFields: { subtotal: { $add: ['$manoObra', '$repuestos'] } } }"),
            Document.parse("{ $group: { _id: null,"
                + " valorEstimadoTotal: { $sum: '$subtotal' },"
                + " valorAutorizado: { $sum: { $cond: [ { $eq: ['$decision', 'Autorizado'] }, '$subtotal', 0 ] } },"
                + " trabajos: { $push: { trabajoId: { $toString: '$_id' }, descripcion: '$descripcionIntervencion',"
                + "   decision: '$decision', manoObra: '$manoObra', repuestos: '$repuestos', subtotal: '$subtotal' } } } }"),
            Document.parse("{ $project: { _id: 0 } }"));

        List<Document> resultado = mongoTemplate.getCollection(mongoTemplate.getCollectionName(TrabajosModel.class))
                .aggregate(pipeline).into(new ArrayList<>());

        Document respuesta = new Document("ordenServicioId", ordenServicioId.toHexString());
        if (resultado.isEmpty()) {
            respuesta.append("valorEstimadoTotal", 0).append("valorAutorizado", 0).append("trabajos", List.of());
        } else {
            respuesta.putAll(resultado.get(0));
        }
        return respuesta;
    }
}
