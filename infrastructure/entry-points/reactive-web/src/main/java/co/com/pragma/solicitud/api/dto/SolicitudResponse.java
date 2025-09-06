package co.com.pragma.solicitud.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudResponse {
    private Long monto;
    private int plazo;
    private String email;
    private String nombre;
    private String tipoPrestamo;
    private Double tasaInteres;
    private String estadoSolicitud;
    private Long salarioBase;
    private Double montoMensualSolicitud;
}
