package co.com.pragma.solicitud.model.usuario.gateway;

import co.com.pragma.solicitud.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioGateway {
    Mono<Boolean> existeUsuarioPorEmail(String email, String token);
    Mono<Usuario> usuarioPorEmail(String email, String token);
}
