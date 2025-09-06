package co.com.pragma.solicitud.usecase.solicitud;

import co.com.pragma.solicitud.model.estado.gateways.EstadoRepository;
import co.com.pragma.solicitud.model.solicitud.PageResult;
import co.com.pragma.solicitud.model.solicitud.Solicitud;
import co.com.pragma.solicitud.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.solicitud.model.usuario.gateway.UsuarioGateway;
import co.com.pragma.solicitud.model.tipoprestamo.gateways.TipoPrestamoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@RequiredArgsConstructor
public class SolicitudUseCase {
    private final SolicitudRepository solicitudRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final EstadoRepository estadoRepository;
    private final UsuarioGateway usuarioGateway;
    private static final Integer ESTADO_PENDIENTE_ID = 1;
    private static final Logger logger = Logger.getLogger(SolicitudUseCase.class.getName());

    public Mono<Solicitud> registrarSolicitud(Solicitud solicitud, String token, String sessionEmail) {
        logger.info(() -> "Iniciando registro de solicitud para email=" + solicitud.getEmail());

        if (!solicitud.getEmail().equalsIgnoreCase(sessionEmail)) {
            logger.warning(() -> "Intento de registrar solicitud con email distinto al de la sesión. " +
                    "sessionEmail=" + sessionEmail + ", solicitudEmail=" + solicitud.getEmail());
            return Mono.error(new SecurityException("No puedes crear solicitudes para otro usuario"));
        }

        if (solicitud.getMonto() == null || solicitud.getMonto() <= 0) {
            logger.warning(() -> "Monto inválido: " + solicitud.getMonto());
            return Mono.error(new IllegalArgumentException("El monto debe ser mayor a 0"));
        }

        if (solicitud.getPlazo() <= 0) {
            logger.warning(() -> "Plazo inválido: " + solicitud.getPlazo());
            return Mono.error(new IllegalArgumentException("El plazo debe ser mayor a 0"));
        }

        return usuarioGateway.existeUsuarioPorEmail(solicitud.getEmail(), token)
                .doOnNext(existe -> logger.info(() -> "Validación de usuario por email=" +
                        solicitud.getEmail() + ", existe=" + existe))
                .flatMap(existe -> {
                    if (!existe) {
                        logger.warning(() -> "Usuario no registrado: " + solicitud.getEmail());
                        return Mono.error(new IllegalArgumentException("El correo no esta registrado"));
                    }
                    return tipoPrestamoRepository.findByIdTipoPrestamo(solicitud.getIdTipoPrestamo())
                            .switchIfEmpty(Mono.defer(() -> {
                                logger.warning(() -> "Tipo de préstamo no encontrado: " + solicitud.getIdTipoPrestamo());
                                return Mono.error(new IllegalArgumentException("El tipo de prestamo no existe"));
                            }));
                })
                .flatMap(tipoPrestamo -> {
                    logger.info(() -> "Tipo de préstamo válido: " + tipoPrestamo.getNombre());
                    return estadoRepository.findByIdEstado(ESTADO_PENDIENTE_ID)
                            .switchIfEmpty(Mono.defer(() -> {
                                logger.warning("Estado inicial no encontrado: " + ESTADO_PENDIENTE_ID);
                                return Mono.error(new IllegalArgumentException("No se encontro el estado inicial"));
                            }));
                })
                .flatMap(estado -> {
                    solicitud.setIdEstado(ESTADO_PENDIENTE_ID);
                    logger.info(() -> "Asignando estado inicial=" + estado.getDescripcion() +
                            " a solicitud con email=" + solicitud.getEmail());
                    return solicitudRepository.save(solicitud)
                            .doOnSuccess(saved -> logger.info(() -> "Solicitud registrada con id=" + saved.getIdSolicitud()));
                });
    }

    public Mono<PageResult<Solicitud>> listarSolicitudesPendientes(Integer idEstado, int page, int size) {
        logger.info(() -> "Listando solicitudes pendientes. idEstado=" + idEstado + ", page=" + page + ", size=" + size);
        return solicitudRepository.getSolicitudesByIdEstado(idEstado, page, size).collectList()
                .zipWith(solicitudRepository.countSolicitudesPendientes(idEstado))
                .map(tuple -> {
                    logger.info(() -> "Solicitudes encontradas=" + tuple.getT1().size() + ", total=" + tuple.getT2());
                    return new PageResult<>(tuple.getT1(), tuple.getT2());
                });
    }

    public Mono<Solicitud> update(Solicitud solicitud) {
        logger.info(() -> "Actualizando solicitud id=" + solicitud.getIdSolicitud());
        return solicitudRepository.getByIdSolicitud(solicitud.getIdSolicitud())
                .switchIfEmpty(Mono.defer(() -> {
                    logger.warning(() -> "No se encontró solicitud con id=" + solicitud.getIdSolicitud());
                    return Mono.error(new IllegalArgumentException("La solicitud no existe"));
                }))
                .flatMap(existing -> {
                    logger.info(() -> "Solicitud encontrada, actualizando id=" + solicitud.getIdSolicitud());
                    return solicitudRepository.save(solicitud)
                            .doOnSuccess(saved -> logger.info(() -> "Solicitud actualizada id=" + saved.getIdSolicitud()));
                });
    }

}
