package co.com.pragma.solicitud.api.mapper;

import co.com.pragma.solicitud.api.dto.SolicitudResponse;
import co.com.pragma.solicitud.model.estado.Estado;
import co.com.pragma.solicitud.model.solicitud.Solicitud;
import co.com.pragma.solicitud.model.tipoprestamo.TipoPrestamo;
import co.com.pragma.solicitud.model.usuario.Usuario;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SolicitudMapper {
    public static SolicitudResponse toResponse(Solicitud solicitud, Usuario usuario, Estado estado, TipoPrestamo tipoPrestamo) {
        BigDecimal monto = BigDecimal.valueOf(solicitud.getMonto());

        BigDecimal tasaInteres = BigDecimal.valueOf(tipoPrestamo.getTasaInteres())
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP); // si es porcentaje

        BigDecimal plazo = BigDecimal.valueOf(solicitud.getPlazo());

        BigDecimal montoMensual = monto.add(monto.multiply(tasaInteres))
                .divide(plazo, 2, RoundingMode.HALF_UP);

        return new SolicitudResponse(
                solicitud.getMonto(),
                solicitud.getPlazo(),
                usuario.getEmail(),
                usuario.getNombre() + " " + usuario.getApellido(),
                tipoPrestamo.getNombre(),
                tipoPrestamo.getTasaInteres(),
                estado.getDescripcion(),
                usuario.getSalarioBase(),
                montoMensual.doubleValue()
        );
    }
}
