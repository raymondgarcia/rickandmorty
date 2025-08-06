package cl.mobdev.rm.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class RickAndMortyApiException extends RuntimeException {
    private HttpStatusCode statusCode;
    private String body;

    public RickAndMortyApiException(HttpStatusCode statusCode, String body) {
        super("Error " + statusCode.toString() + " body " +  body);
        this.statusCode = statusCode;
        this.body = body;
    }
}
