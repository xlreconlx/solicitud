package co.com.pragma.solicitud.model.solicitud.gateways;

import reactor.core.publisher.Mono;

public interface UsuarioGateway {
    Mono<Boolean> existeUsuarioPorEmail(String email);
}
