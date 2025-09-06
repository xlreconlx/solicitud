package co.com.pragma.solicitud.api.solicitud;

import co.com.pragma.solicitud.model.solicitud.Solicitud;
import io.swagger.v3.oas.annotations.media.Content;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SolicitudRouter {
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "api/v1/solicitud",
                    beanClass = SolicitudHandler.class,
                    beanMethod = "registrarSolicitud",
                    method = RequestMethod.POST,
                    operation = @io.swagger.v3.oas.annotations.Operation(
                            summary = "Registrar una solicitud",
                            operationId = "registrarSolicitud",
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Datos de la solicitud",
                                    required = true,
                                    content = @io.swagger.v3.oas.annotations.media.Content(
                                            mediaType = "application/json",
                                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                                    implementation = Solicitud.class
                                            )
                                    )
                            ),
                            responses = {
                                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                                            responseCode = "201",
                                            description = "Solicitud registrada correctamente",
                                            content = @io.swagger.v3.oas.annotations.media.Content(
                                                    mediaType = "application/json",
                                                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                                                            implementation = Solicitud.class
                                                    )
                                            )
                                    ),
                                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                                            responseCode = "400",
                                            description = "Datos invalidos",
                                            content = @io.swagger.v3.oas.annotations.media.Content(
                                                    mediaType = "application/json"
                                            )
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> solicitudRoutes(SolicitudHandler handler) {
        return route(POST("api/v1/solicitud"), handler::registrarSolicitud)
                .andRoute(GET("api/v1/solicitud"), handler::listarSolicitudes);
    }
}
