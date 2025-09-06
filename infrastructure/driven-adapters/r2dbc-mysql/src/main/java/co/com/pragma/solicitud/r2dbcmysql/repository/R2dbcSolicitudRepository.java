package co.com.pragma.solicitud.r2dbcmysql.repository;


import co.com.pragma.solicitud.r2dbcmysql.entity.R2dbcSolicitud;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface R2dbcSolicitudRepository extends ReactiveCrudRepository<R2dbcSolicitud, Integer> {
    @Query("SELECT * FROM solicitud WHERE id_estado = :idEstado LIMIT :offset, :size")
    Flux<R2dbcSolicitud> getSolicitudesByIdEstado(Integer idEstado, int size, int offset);

    @Query("SELECT COUNT(*) FROM solicitud WHERE id_estado = :idEstado")
    Mono<Long> countByIdEstado(Integer idEstado);
}
