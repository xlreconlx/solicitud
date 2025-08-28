package co.com.pragma.solicitud.r2dbcmysql.adapter;

import co.com.pragma.solicitud.model.estado.Estado;
import co.com.pragma.solicitud.model.estado.gateways.EstadoRepository;
import co.com.pragma.solicitud.r2dbcmysql.entity.R2dbcEstado;
import co.com.pragma.solicitud.r2dbcmysql.mapper.EstadoMapper;
import co.com.pragma.solicitud.r2dbcmysql.repository.R2dbcEstadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class EstadoRepositoryAdapter implements EstadoRepository {

    private final EstadoMapper estadoMapper;
    private final R2dbcEstadoRepository estadoRepository;

    @Override
    public Mono<Estado> save(Estado estado) {
        R2dbcEstado entity = estadoMapper.toEntity(estado);
        return estadoRepository.save(entity).map(estadoMapper ::toModel);
    }

    @Override
    public Mono<Estado> findByIdEstado(Integer idEstado) {
        return estadoRepository.findById(idEstado).map(estadoMapper::toModel);
    }

    @Override
    public Flux<Estado> findAll() {
        return estadoRepository.findAll().map(estadoMapper::toModel);
    }
}
