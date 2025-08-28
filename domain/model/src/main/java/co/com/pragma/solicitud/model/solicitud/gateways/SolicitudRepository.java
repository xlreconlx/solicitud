package co.com.pragma.solicitud.model.solicitud.gateways;

import co.com.pragma.solicitud.model.solicitud.Solicitud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SolicitudRepository {
    Mono<Solicitud> save(Solicitud solicitud);
    Flux<Solicitud> getSolicitudByEmail(String email);
    Mono<Solicitud> update(Solicitud solicitud);
    Mono<Solicitud> getByIdSolicitud(Integer idSolicitud);
    Mono<Void> deleteByIdSolicitud(Integer idSolicitud);
}
