package co.com.pragma.solicitud.model.tipoprestamo.gateways;

import co.com.pragma.solicitud.model.tipoprestamo.TipoPrestamo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TipoPrestamoRepository {
    Mono<TipoPrestamo> save(TipoPrestamo tipoPrestamo);
    Flux<TipoPrestamo> findAll();
    Mono<TipoPrestamo> findByIdTipoPrestamo(Integer idTipoPrestamo);
    Mono<TipoPrestamo> updateTipoPrestamo(TipoPrestamo tipoPrestamo);
}
