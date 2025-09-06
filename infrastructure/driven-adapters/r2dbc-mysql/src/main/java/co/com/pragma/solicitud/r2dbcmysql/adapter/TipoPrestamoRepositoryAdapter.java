package co.com.pragma.solicitud.r2dbcmysql.adapter;

import co.com.pragma.solicitud.model.tipoprestamo.TipoPrestamo;
import co.com.pragma.solicitud.model.tipoprestamo.gateways.TipoPrestamoRepository;
import co.com.pragma.solicitud.r2dbcmysql.mapper.TipoPrestamoMapper;
import co.com.pragma.solicitud.r2dbcmysql.repository.R2dbcTipoPrestamoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TipoPrestamoRepositoryAdapter implements TipoPrestamoRepository {

    private final R2dbcTipoPrestamoRepository tipoPrestamoRepository;
    private final TipoPrestamoMapper tipoPrestamoMapper;
    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<TipoPrestamo> save(TipoPrestamo tipoPrestamo) {
        return null;
    }

    @Override
    public Flux<TipoPrestamo> findAll() {
        return null;
    }

    @Override
    public Mono<TipoPrestamo> findByIdTipoPrestamo(Integer idTipoPrestamo) {
        return tipoPrestamoRepository.findById(idTipoPrestamo)
                .map(tipoPrestamoMapper::toModel).as(transactionalOperator::transactional);
    }

    @Override
    public Mono<TipoPrestamo> updateTipoPrestamo(TipoPrestamo tipoPrestamo) {
        return null;
    }
}
