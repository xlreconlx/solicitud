package co.com.pragma.solicitud.api.solicitud;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SolicitudRouter {
    @Bean
    public RouterFunction<ServerResponse> solicitudRoutes(SolicitudHandler handler) {
        return route(POST("api/v1/solicitud"), handler::registrarSolicitud);
    }
}
