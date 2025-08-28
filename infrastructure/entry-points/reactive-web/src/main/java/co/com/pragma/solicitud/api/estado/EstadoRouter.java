package co.com.pragma.solicitud.api.estado;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class EstadoRouter {
    @Bean
    public RouterFunction<ServerResponse> estadoRoute(EstadoHandler handler) {
        return route(GET("/api/v1/solicitud/estados"), handler::findAllEstados);
    }
}
