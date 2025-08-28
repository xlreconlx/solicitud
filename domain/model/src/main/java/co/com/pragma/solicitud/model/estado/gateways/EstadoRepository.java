package co.com.pragma.solicitud.model.estado.gateways;

import co.com.pragma.solicitud.model.estado.Estado;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EstadoRepository {
    Mono<Estado> save(Estado estado);
    Mono<Estado> findByIdEstado(Integer idEstado);
    Flux<Estado> findAll();
}
