package co.com.pragma.solicitud.r2dbcmysql.repository;

import co.com.pragma.solicitud.r2dbcmysql.entity.R2dbcSolicitud;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface R2dbcSolicitudRepository extends ReactiveCrudRepository<R2dbcSolicitud, Integer> {
}
