package co.com.pragma.solicitud.r2dbcmysql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("estados")
@NoArgsConstructor
@AllArgsConstructor
public class R2dbcEstado {
    @Id
    private Integer idEstado;
    private String descripcion;
}
