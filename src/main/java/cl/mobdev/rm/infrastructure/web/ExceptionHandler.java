package cl.mobdev.rm.infrastructure.web;

import cl.mobdev.rm.domain.exception.RickAndMortyApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(RickAndMortyApiException.class)
    public ResponseEntity<Void> handlerDomainException(RickAndMortyApiException ex) {
        return ResponseEntity.status(ex.getStatusCode()).build();
    }
}
