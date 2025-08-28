package co.com.pragma.solicitud.model.solicitud;
import co.com.pragma.solicitud.model.estado.Estado;
import co.com.pragma.solicitud.model.tipoprestamo.TipoPrestamo;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Solicitud {
    private Integer idSolicitud;
    private Long monto;
    private int plazo;
    private String email;
    private Integer idEstado;
    private Integer idTipoPrestamo;

}
