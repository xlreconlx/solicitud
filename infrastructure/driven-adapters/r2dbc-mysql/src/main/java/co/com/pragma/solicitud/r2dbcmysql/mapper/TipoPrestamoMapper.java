package co.com.pragma.solicitud.r2dbcmysql.mapper;

import co.com.pragma.solicitud.model.tipoprestamo.TipoPrestamo;
import co.com.pragma.solicitud.r2dbcmysql.entity.R2dbcTipoPrestamo;
import org.springframework.stereotype.Component;

@Component
public class TipoPrestamoMapper {

    public R2dbcTipoPrestamo toEntity(TipoPrestamo tipoPrestamo){
        return new R2dbcTipoPrestamo(
                tipoPrestamo.getIdTipoPrestamo(),
                tipoPrestamo.getNombre(),
                tipoPrestamo.getMontoMinimo(),
                tipoPrestamo.getMontoMaximo(),
                tipoPrestamo.getTasaInteres(),
                tipoPrestamo.isValidacionAutomatica()
        );
    }

    public TipoPrestamo toModel(R2dbcTipoPrestamo r2dbcTipoPrestamo){
        return new TipoPrestamo(
                r2dbcTipoPrestamo.getIdTipoPrestamo(),
                r2dbcTipoPrestamo.getNombre(),
                r2dbcTipoPrestamo.getMontoMinimo(),
                r2dbcTipoPrestamo.getMontoMaximo(),
                r2dbcTipoPrestamo.getTasaInteres(),
                r2dbcTipoPrestamo.isValidacionAutomatica()
        );
    }

}
