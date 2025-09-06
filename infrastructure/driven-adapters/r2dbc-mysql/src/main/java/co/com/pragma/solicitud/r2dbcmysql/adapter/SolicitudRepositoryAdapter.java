package co.com.pragma.solicitud.r2dbcmysql.adapter;

import co.com.pragma.solicitud.model.solicitud.Solicitud;
import co.com.pragma.solicitud.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.solicitud.r2dbcmysql.entity.R2dbcSolicitud;
import co.com.pragma.solicitud.r2dbcmysql.mapper.SolicitudMapper;
import co.com.pragma.solicitud.r2dbcmysql.repository.R2dbcSolicitudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SolicitudRepositoryAdapter implements SolicitudRepository {
    private final SolicitudMapper solicitudMapper;
    private final R2dbcSolicitudRepository solicitudRepository;
    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<Solicitud> save(Solicitud solicitud) {
        R2dbcSolicitud entity = solicitudMapper.toEntity(solicitud);
        return solicitudRepository.save(entity)
                .map(solicitudMapper::toModel);
    }

    @Override
    public Flux<Solicitud> getSolicitudByEmail(String email) {
        return null;
    }

    @Override
    public Flux<Solicitud> getSolicitudesByIdEstado(Integer idEstado, int page, int size) {
        return solicitudRepository.getSolicitudesByIdEstado(idEstado, page, size)
                .map(solicitudMapper::toModel).as(transactionalOperator::transactional);
    }

    @Override
    public Mono<Solicitud> update(Solicitud solicitud) {
        R2dbcSolicitud entity = solicitudMapper.toEntity(solicitud);
        return solicitudRepository.save(entity)
                .map(solicitudMapper::toModel).as(transactionalOperator::transactional);
    }

    @Override
    public Mono<Solicitud> getByIdSolicitud(Integer idSolicitud) {
        return null;
    }

    @Override
    public Mono<Void> deleteByIdSolicitud(Integer idSolicitud) {
        return null;
    }

    @Override
    public Mono<Long> countSolicitudesPendientes(Integer idEstado) {
        return solicitudRepository.countByIdEstado(idEstado).as(transactionalOperator::transactional);
    }
}
