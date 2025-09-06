package co.com.pragma.solicitud.model.solicitud;

import java.util.List;

public record PageResult<T>(List<T> items, long total) {
}
