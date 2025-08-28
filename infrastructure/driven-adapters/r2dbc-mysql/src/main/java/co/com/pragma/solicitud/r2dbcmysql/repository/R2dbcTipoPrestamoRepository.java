package co.com.pragma.solicitud.r2dbcmysql.repository;

import co.com.pragma.solicitud.r2dbcmysql.entity.R2dbcTipoPrestamo;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface R2dbcTipoPrestamoRepository extends ReactiveCrudRepository<R2dbcTipoPrestamo, Integer> {
}
