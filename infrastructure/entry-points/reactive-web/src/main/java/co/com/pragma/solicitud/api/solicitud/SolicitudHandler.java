package co.com.pragma.solicitud.api.solicitud;

import co.com.pragma.solicitud.model.solicitud.Solicitud;
import co.com.pragma.solicitud.usecase.solicitud.SolicitudUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SolicitudHandler {

    private final SolicitudUseCase solicitudUseCase;

    public Mono<ServerResponse> registrarSolicitud(ServerRequest request){
        return request.bodyToMono(Solicitud.class)
                .flatMap(solicitudUseCase::registrarSolicitud)
                .flatMap(solicitud -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(solicitud));
    }
}
