package co.com.pragma.solicitud.restclient;

import co.com.pragma.solicitud.model.usuario.Usuario;
import co.com.pragma.solicitud.model.usuario.gateway.UsuarioGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class UserRestClient implements UsuarioGateway {

    private final WebClient webClient;

    public UserRestClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:8081/api/v1/usuarios")
                .build();
    }

    @Override
    public Mono<Boolean> existeUsuarioPorEmail(String email, String token) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/exist")
                        .queryParam("email", email)
                        .build())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Map.class)
                .map(m -> (Boolean) m.get("exists"))
                .onErrorReturn(false);
    }

    @Override
    public Mono<Usuario> usuarioPorEmail(String email, String token) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search-by-email")
                        .queryParam("email", email)
                        .build())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Usuario.class);

    }
}
