package co.com.pragma.solicitud.usecase.tipoprestamo;

import co.com.pragma.solicitud.model.tipoprestamo.TipoPrestamo;
import co.com.pragma.solicitud.model.tipoprestamo.gateways.TipoPrestamoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class TipoPrestamoUseCase {
    private final TipoPrestamoRepository tipoPrestamoRepository;
    private static final Logger logger = Logger.getLogger(TipoPrestamoUseCase.class.getName());

    public Mono<TipoPrestamo> findByIdTipoPrestamo(Integer idTipoPrestamo){
        logger.log(Level.INFO, "Buscando Prestamo por id {0}", idTipoPrestamo);
        return tipoPrestamoRepository.findByIdTipoPrestamo(idTipoPrestamo);
    }

    public Flux<TipoPrestamo> findAllTipoPrestamo(){
        return tipoPrestamoRepository.findAll();
    }

}
