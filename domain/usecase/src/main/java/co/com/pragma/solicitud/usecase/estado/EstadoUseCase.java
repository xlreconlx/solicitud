package co.com.pragma.solicitud.usecase.estado;

import co.com.pragma.solicitud.model.estado.Estado;
import co.com.pragma.solicitud.model.estado.gateways.EstadoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.logging.Logger;

@RequiredArgsConstructor
public class EstadoUseCase {
    private final EstadoRepository estadoRepository;
    private static final Logger logger = Logger.getLogger(EstadoUseCase.class.getName());

    public Flux<Estado> findAllEstados() {
        logger.info("Listando Estados");
        return estadoRepository.findAll();
    }

}
