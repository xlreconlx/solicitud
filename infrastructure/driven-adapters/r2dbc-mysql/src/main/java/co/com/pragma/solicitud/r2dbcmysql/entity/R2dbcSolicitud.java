package co.com.pragma.solicitud.r2dbcmysql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("solicitud")
@NoArgsConstructor
@AllArgsConstructor
public class R2dbcSolicitud {

    @Id
    private Integer idSolicitud;
    private Long monto;
    private int plazo;
    private String email;
    private Integer idEstado;
    private Integer idTipoPrestamo;

}
