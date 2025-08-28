package co.com.pragma.solicitud.r2dbcmysql.mapper;

import co.com.pragma.solicitud.model.estado.Estado;
import co.com.pragma.solicitud.r2dbcmysql.entity.R2dbcEstado;
import org.springframework.stereotype.Component;

@Component
public class EstadoMapper {

    public R2dbcEstado toEntity(Estado estado) {
        return new R2dbcEstado(
                estado.getIdEstado(),
                estado.getDescripcion()
        );
    }

    public Estado toModel(R2dbcEstado entity) {
        return new Estado(
                entity.getIdEstado(),
                entity.getDescripcion()
        );
    }

}
