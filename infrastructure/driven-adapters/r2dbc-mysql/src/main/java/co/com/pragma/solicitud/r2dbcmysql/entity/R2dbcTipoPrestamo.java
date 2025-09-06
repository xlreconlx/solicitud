package co.com.pragma.solicitud.r2dbcmysql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("tipo_prestamo")
@NoArgsConstructor
@AllArgsConstructor
public class R2dbcTipoPrestamo {
    @Id
    private Integer idTipoPrestamo;
    private String nombre;
    private Long montoMinimo;
    private Long montoMaximo;
    private Double tasaInteres;
    private boolean validacionAutomatica;
}
