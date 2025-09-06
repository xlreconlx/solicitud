package co.com.pragma.solicitud.api.solicitud;

import co.com.pragma.solicitud.api.dto.PagedResponse;
import co.com.pragma.solicitud.api.mapper.SolicitudMapper;
import co.com.pragma.solicitud.model.estado.Estado;
import co.com.pragma.solicitud.model.solicitud.Solicitud;
import co.com.pragma.solicitud.model.tipoprestamo.TipoPrestamo;
import co.com.pragma.solicitud.model.usuario.Usuario;
import co.com.pragma.solicitud.usecase.estado.EstadoUseCase;
import co.com.pragma.solicitud.usecase.solicitud.SolicitudUseCase;
import co.com.pragma.solicitud.usecase.tipoprestamo.TipoPrestamoUseCase;
import co.com.pragma.solicitud.usecase.usuario.UsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class SolicitudHandler {

    private final SolicitudUseCase solicitudUseCase;
    private final EstadoUseCase estadoUseCase;
    private final UsuarioUseCase usuarioUseCase;
    private final TipoPrestamoUseCase tipoPrestamoUseCase;
    private static final Logger log = LoggerFactory.getLogger(SolicitudHandler.class);

    public Mono<ServerResponse> registrarSolicitud(ServerRequest request) {
        log.info("Iniciando proceso de registrarSolicitud");

        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> (JwtAuthenticationToken) ctx.getAuthentication())
                .flatMap(auth -> {
                    String sessionEmail = auth.getToken().getSubject();
                    String rol = auth.getToken().getClaim("rol");

                    log.debug("Token recibido para email={} con rol={}", sessionEmail, rol);

                    if (rol == null || !(rol.toUpperCase().equals("CLIENTE"))) {
                        log.warn("Acceso denegado. Usuario email={} con rol={} intentó registrar solicitud", sessionEmail, rol);
                        return ServerResponse.status(HttpStatus.FORBIDDEN)
                                .bodyValue(Map.of("error", "No tienes permisos para registrar Solicitudes"));
                    }

                    return request.bodyToMono(Solicitud.class)
                            .doOnNext(solicitud -> log.debug("Solicitud recibida: {}", solicitud))
                            .flatMap(solicitud -> {
                                if (!solicitud.getEmail().equalsIgnoreCase(sessionEmail)) {
                                    log.warn("El usuario de sesión {} intentó crear solicitud para {}", sessionEmail, solicitud.getEmail());
                                    return ServerResponse.status(HttpStatus.FORBIDDEN)
                                            .bodyValue(Map.of("error", "No puedes crear solicitudes a nombre de otro usuario"));
                                }

                                log.info("Registrando solicitud para email={}", solicitud.getEmail());
                                return solicitudUseCase.registrarSolicitud(solicitud, auth.getToken().getTokenValue(), sessionEmail)
                                        .doOnSuccess(saved -> log.info("Solicitud registrada exitosamente con id={}", saved.getIdSolicitud()))
                                        .doOnError(ex -> log.error("Error al registrar solicitud para email={}", solicitud.getEmail(), ex))
                                        .flatMap(saved -> ServerResponse.ok()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(saved));
                            });
                });
    }

    public Mono<ServerResponse> listarSolicitudes(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
        int idEstado = Integer.parseInt(request.queryParam("idEstado").orElse("1"));

        log.info("Listando solicitudes. page={}, size={}, idEstado={}", page, size, idEstado);

        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> (JwtAuthenticationToken) ctx.getAuthentication())
                .flatMap(auth -> {
                    String token = auth.getToken().getTokenValue();
                    String sessionEmail = auth.getToken().getSubject();

                    log.debug("Usuario autenticado para listar solicitudes. email={}", sessionEmail);

                    // caches por request: almacenan Monos cacheados
                    Map<String, Mono<Usuario>> usuarioCache = new ConcurrentHashMap<>();
                    Map<Integer, Mono<Estado>> estadoCache = new ConcurrentHashMap<>();
                    Map<Integer, Mono<TipoPrestamo>> tipoPrestamoCache = new ConcurrentHashMap<>();

                    return solicitudUseCase.listarSolicitudesPendientes(idEstado, page, size)
                            .doOnNext(result -> log.info("Total solicitudes encontradas={}", result.total()))
                            .flatMap(result -> Flux.fromIterable(result.items())
                                    .doOnNext(solicitud -> log.debug("Procesando solicitud id={}", solicitud.getIdSolicitud()))
                                    .flatMap(solicitud ->
                                            Mono.zip(
                                                    // Usuario cacheado por email
                                                    usuarioCache.computeIfAbsent(
                                                            solicitud.getEmail(),
                                                            email -> usuarioUseCase.usuarioPorEmail(email, token)
                                                                    .doOnSuccess(u -> log.info("Usuario cargado email={}", email))
                                                                    .cache()
                                                    ),
                                                    // Estado cacheado por id
                                                    estadoCache.computeIfAbsent(
                                                            solicitud.getIdEstado(),
                                                            id -> estadoUseCase.finByIdEstado(id)
                                                                    .doOnSuccess(e -> log.info("Estado cargado id={}", id))
                                                                    .cache()
                                                    ),
                                                    // TipoPrestamo cacheado por id
                                                    tipoPrestamoCache.computeIfAbsent(
                                                            solicitud.getIdTipoPrestamo(),
                                                            id -> tipoPrestamoUseCase.findByIdTipoPrestamo(id)
                                                                    .doOnSuccess(tp -> log.info("TipoPrestamo cargado id={}", id))
                                                                    .cache()
                                                    )
                                            ).map(tuple -> SolicitudMapper.toResponse(
                                                    solicitud,
                                                    tuple.getT1(), // Usuario
                                                    tuple.getT2(), // Estado
                                                    tuple.getT3()  // TipoPrestamo
                                            ))
                                    )
                                    .collectList()
                                    .doOnSuccess(list -> log.info("Total solicitudes mapeadas a DTO={}", list.size()))
                                    .map(responses -> new PagedResponse<>(responses, page, size, result.total()))
                            );
                })
                .flatMap(response ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(response)
                )
                .onErrorResume(ex -> {
                    log.error("Error al listar solicitudes", ex);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue(Map.of("error", "Ocurrió un error al listar solicitudes"));
                });
    }
}
