package co.com.pragma.solicitud.usecase.solicitud;

import co.com.pragma.solicitud.model.estado.Estado;
import co.com.pragma.solicitud.model.estado.gateways.EstadoRepository;
import co.com.pragma.solicitud.model.solicitud.Solicitud;
import co.com.pragma.solicitud.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.solicitud.model.usuario.gateway.UsuarioGateway;
import co.com.pragma.solicitud.model.tipoprestamo.TipoPrestamo;
import co.com.pragma.solicitud.model.tipoprestamo.gateways.TipoPrestamoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class SolicitudUseCaseTest {
    private SolicitudRepository solicitudRepository;
    private TipoPrestamoRepository tipoPrestamoRepository;
    private EstadoRepository estadoRepository;
    private UsuarioGateway usuarioGateway;
    private SolicitudUseCase solicitudUseCase;


    @BeforeEach
    void setUp() {
        solicitudRepository = mock(SolicitudRepository.class);
        tipoPrestamoRepository = mock(TipoPrestamoRepository.class);
        estadoRepository = mock(EstadoRepository.class);
        usuarioGateway = mock(UsuarioGateway.class);

        solicitudUseCase = new SolicitudUseCase(
                solicitudRepository,
                tipoPrestamoRepository,
                estadoRepository,
                usuarioGateway
        );
    }

    @Test
    void registrarSolicitud_exito() {
        Solicitud solicitud = new Solicitud();
        solicitud.setIdSolicitud(1);
        solicitud.setMonto(1000L);
        solicitud.setPlazo(12);
        solicitud.setEmail("test@correo.com");
        solicitud.setIdTipoPrestamo(1);

        Estado estado = new Estado();
        estado.setIdEstado(1);
        estado.setDescripcion("Pendiente");

        when(usuarioGateway.existeUsuarioPorEmail("test@correo.com")).thenReturn(Mono.just(true));
        when(tipoPrestamoRepository.findByIdTipoPrestamo(1)).thenReturn(Mono.just(new TipoPrestamo()));
        when(estadoRepository.findByIdEstado(1)).thenReturn(Mono.just(estado));
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(Mono.just(solicitud));

        StepVerifier.create(solicitudUseCase.registrarSolicitud(solicitud))
                .expectNextMatches(s -> s.getIdEstado().equals(1))
                .verifyComplete();

        verify(solicitudRepository).save(any(Solicitud.class));
    }

    @Test
    void registrarSolicitud_errorMontoInvalido() {
        Solicitud solicitud = new Solicitud();
        solicitud.setMonto(0L);
        solicitud.setPlazo(12);
        solicitud.setEmail("test@correo.com");
        solicitud.setIdTipoPrestamo(1);

        StepVerifier.create(solicitudUseCase.registrarSolicitud(solicitud))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("El monto debe ser mayor a 0"))
                .verify();
    }

    @Test
    void registrarSolicitud_errorPlazoInvalido() {
        Solicitud solicitud = new Solicitud();
        solicitud.setMonto(1000L);
        solicitud.setPlazo(0);
        solicitud.setEmail("test@correo.com");
        solicitud.setIdTipoPrestamo(1);

        StepVerifier.create(solicitudUseCase.registrarSolicitud(solicitud))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("El plazo debe ser mayor a 0"))
                .verify();
    }

    @Test
    void registrarSolicitud_errorUsuarioNoExiste() {
        Solicitud solicitud = new Solicitud();
        solicitud.setMonto(1000L);
        solicitud.setPlazo(12);
        solicitud.setEmail("noexiste@correo.com");
        solicitud.setIdTipoPrestamo(1);

        when(usuarioGateway.existeUsuarioPorEmail("noexiste@correo.com")).thenReturn(Mono.just(false));

        StepVerifier.create(solicitudUseCase.registrarSolicitud(solicitud))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("El correo no esta registrado"))
                .verify();
    }

    @Test
    void registrarSolicitud_errorTipoPrestamoNoExiste() {
        Solicitud solicitud = new Solicitud();
        solicitud.setMonto(1000L);
        solicitud.setPlazo(12);
        solicitud.setEmail("test@correo.com");
        solicitud.setIdTipoPrestamo(99);

        when(usuarioGateway.existeUsuarioPorEmail("test@correo.com")).thenReturn(Mono.just(true));
        when(tipoPrestamoRepository.findByIdTipoPrestamo(99)).thenReturn(Mono.empty());

        StepVerifier.create(solicitudUseCase.registrarSolicitud(solicitud))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("El tipo de prestamo no existe"))
                .verify();
    }

    @Test
    void registrarSolicitud_errorEstadoNoExiste() {
        Solicitud solicitud = new Solicitud();
        solicitud.setMonto(1000L);
        solicitud.setPlazo(12);
        solicitud.setEmail("test@correo.com");
        solicitud.setIdTipoPrestamo(1);

        when(usuarioGateway.existeUsuarioPorEmail("test@correo.com")).thenReturn(Mono.just(true));
        when(tipoPrestamoRepository.findByIdTipoPrestamo(1)).thenReturn(Mono.just(new TipoPrestamo()));
        when(estadoRepository.findByIdEstado(1)).thenReturn(Mono.empty());

        StepVerifier.create(solicitudUseCase.registrarSolicitud(solicitud))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("No se encontro el estado inicial"))
                .verify();
    }

    @Test
    void update_exito() {
        Solicitud solicitud = new Solicitud();
        solicitud.setIdSolicitud(1);
        solicitud.setMonto(2000L);
        solicitud.setPlazo(24);

        when(solicitudRepository.getByIdSolicitud(1)).thenReturn(Mono.just(new Solicitud()));
        when(solicitudRepository.save(solicitud)).thenReturn(Mono.just(solicitud));

        StepVerifier.create(solicitudUseCase.update(solicitud))
                .expectNextMatches(s -> s.getMonto() == 2000L && s.getPlazo() == 24)
                .verifyComplete();
    }

    @Test
    void update_errorSolicitudNoExiste() {
        Solicitud solicitud = new Solicitud();
        solicitud.setIdSolicitud(99);

        when(solicitudRepository.getByIdSolicitud(99)).thenReturn(Mono.empty());

        StepVerifier.create(solicitudUseCase.update(solicitud))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("La solicitud no existe"))
                .verify();
    }


}
