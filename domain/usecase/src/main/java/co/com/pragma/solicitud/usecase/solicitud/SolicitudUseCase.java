package co.com.pragma.solicitud.usecase.solicitud;

import co.com.pragma.solicitud.model.estado.gateways.EstadoRepository;
import co.com.pragma.solicitud.model.solicitud.Solicitud;
import co.com.pragma.solicitud.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.solicitud.model.tipoprestamo.gateways.TipoPrestamoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SolicitudUseCase {
    private final SolicitudRepository solicitudRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final EstadoRepository estadoRepository;
    private static final Integer ESTADO_PENDIENTE_ID = 1;

    public Mono<Solicitud> registrarSolicitud(Solicitud solicitud){
        if (solicitud.getMonto() == null || solicitud.getMonto() <= 0) {
            return Mono.error(new IllegalArgumentException("El monto debe ser mayor a 0"));
        }
        if (solicitud.getPlazo() <= 0) {
            return Mono.error(new IllegalArgumentException("El plazo debe ser mayor a 0"));
        }

        return tipoPrestamoRepository.findByIdTipoPrestamo(solicitud.getIdTipoPrestamo())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El tipo de préstamo no existe")))
                .flatMap(tipoPrestamo -> {
                    return estadoRepository.findByIdEstado(ESTADO_PENDIENTE_ID)
                            .switchIfEmpty(Mono.error(new IllegalArgumentException("No se encontró el estado inicial")))
                            .flatMap(estado -> {
                                solicitud.setIdEstado(ESTADO_PENDIENTE_ID);
                                return solicitudRepository.save(solicitud);
                            });
                });
    }

    public Mono<Solicitud> update(Solicitud solicitud) {
        return solicitudRepository.getByIdSolicitud(solicitud.getIdSolicitud())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("La solicitud no existe")))
                .flatMap(existing -> {
                    return solicitudRepository.save(solicitud);
                });
    }

}
