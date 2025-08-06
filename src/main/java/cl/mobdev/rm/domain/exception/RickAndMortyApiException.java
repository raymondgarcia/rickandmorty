package cl.mobdev.rm.domain.exception;

import org.springframework.http.HttpStatusCode;

public class RickAndMortyApiException extends RuntimeException {

    public RickAndMortyApiException(HttpStatusCode statusCode, String body) {
        super("Error " + statusCode.toString() + " body " +  body);
    }
}
