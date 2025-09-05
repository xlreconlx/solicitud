package co.com.pragma.solicitud.api.solicitud;

import co.com.pragma.solicitud.model.solicitud.Solicitud;
import co.com.pragma.solicitud.usecase.solicitud.SolicitudUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SolicitudHandler {

    private final SolicitudUseCase solicitudUseCase;

    public Mono<ServerResponse> registrarSolicitud(ServerRequest request){
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> (JwtAuthenticationToken) ctx.getAuthentication())
                .flatMap(auth -> {
                    String sessionEmail = auth.getToken().getSubject();
                    String rol = auth.getToken().getClaim("rol");

                    if (rol == null || !(rol.toUpperCase().equals("CLIENTE"))) {
                        return ServerResponse.status(HttpStatus.FORBIDDEN)
                                .bodyValue(Map.of("error", "No tienes permisos para registrar Solicitudes"));
                    }


                    return request.bodyToMono(Solicitud.class)
                            .flatMap(solicitud -> {
                                if (!solicitud.getEmail().equalsIgnoreCase(sessionEmail)) {
                                    return ServerResponse.status(HttpStatus.FORBIDDEN)
                                            .bodyValue(Map.of("error", "No puedes crear solicitudes a nombre de otro usuario"));
                                }

                                return solicitudUseCase.registrarSolicitud(solicitud, auth.getToken().getTokenValue(), sessionEmail)
                                        .flatMap(saved -> ServerResponse.ok()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(saved));
                            });
                });
    }
}
