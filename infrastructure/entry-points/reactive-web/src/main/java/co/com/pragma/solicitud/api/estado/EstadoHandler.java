package co.com.pragma.solicitud.api.estado;

import co.com.pragma.solicitud.model.estado.Estado;
import co.com.pragma.solicitud.usecase.estado.EstadoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class EstadoHandler {

    private  final EstadoUseCase estadoUseCase;

    public Mono<ServerResponse> findAllEstados(ServerRequest request) {
        Flux<Estado> estados = estadoUseCase.findAllEstados();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(estados, Estado.class);
    }

}
