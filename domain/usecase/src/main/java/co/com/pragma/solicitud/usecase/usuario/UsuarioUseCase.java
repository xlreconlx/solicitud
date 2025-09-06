package co.com.pragma.solicitud.usecase.usuario;

import co.com.pragma.solicitud.model.usuario.Usuario;
import co.com.pragma.solicitud.model.usuario.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UsuarioUseCase {
    private final UsuarioGateway usuarioGateway;

    public Mono<Usuario> usuarioPorEmail(String email, String token){
        return usuarioGateway.usuarioPorEmail(email, token);
    }

    public Mono<Boolean> existeUsuarioPorEmail(String email, String token){
        return usuarioGateway.existeUsuarioPorEmail(email, token);
    }
}
