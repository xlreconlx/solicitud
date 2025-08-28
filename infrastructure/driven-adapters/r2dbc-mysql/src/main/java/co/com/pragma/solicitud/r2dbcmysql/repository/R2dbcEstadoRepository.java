package co.com.pragma.solicitud.r2dbcmysql.repository;

import co.com.pragma.solicitud.r2dbcmysql.entity.R2dbcEstado;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface R2dbcEstadoRepository extends ReactiveCrudRepository<R2dbcEstado, Integer> {
    Mono<R2dbcEstado> findByDescripcion(String descripcion);
}
