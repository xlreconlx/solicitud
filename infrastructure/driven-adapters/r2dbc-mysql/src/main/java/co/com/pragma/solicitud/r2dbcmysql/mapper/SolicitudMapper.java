package co.com.pragma.solicitud.r2dbcmysql.mapper;

import co.com.pragma.solicitud.model.solicitud.Solicitud;
import co.com.pragma.solicitud.r2dbcmysql.entity.R2dbcSolicitud;
import org.springframework.stereotype.Component;

@Component
public class SolicitudMapper {

    public R2dbcSolicitud toEntity(Solicitud solicitud){
        return new R2dbcSolicitud(
                solicitud.getIdSolicitud(),
                solicitud.getMonto(),
                solicitud.getPlazo(),
                solicitud.getEmail(),
                solicitud.getIdEstado(),
                solicitud.getIdTipoPrestamo()
        );
    }

    public Solicitud toModel(R2dbcSolicitud r2dbcSolicitud){
        return new Solicitud(
                r2dbcSolicitud.getIdSolicitud(),
                r2dbcSolicitud.getMonto(),
                r2dbcSolicitud.getPlazo(),
                r2dbcSolicitud.getEmail(),
                r2dbcSolicitud.getIdEstado(),
                r2dbcSolicitud.getIdTipoPrestamo()
        );
    }
}
