package co.com.pragma.solicitud.restclient;

import co.com.pragma.solicitud.model.solicitud.gateways.UsuarioGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserRestClient implements UsuarioGateway {

    private final WebClient webClient;

    public UserRestClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:8081/api/v1/usuarios")
                .build();
    }

    @Override
    public Mono<Boolean> existeUsuarioPorEmail(String email) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/exist")
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorReturn(false);
    }
}
