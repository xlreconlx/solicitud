package co.com.pragma.solicitud.model.tipoprestamo;
import lombok.*;
//import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TipoPrestamo {
    private Integer idTipoPrestamo;
    private String nombre;
    private Long montoMinimo;
    private Long montoMaximo;
    private int tasaInteres;
    private boolean validacionAutomatica;
}
