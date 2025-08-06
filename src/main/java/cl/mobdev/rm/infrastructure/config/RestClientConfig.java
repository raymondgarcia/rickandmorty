package cl.mobdev.rm.infrastructure.config;

import cl.mobdev.rm.domain.exception.RickAndMortyApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;

@Configuration
public class RestClientConfig {

    @Value("${external.rickandmorty.base-url}")
    private String baseUrl;

    @Bean
    RestClient rickAndMortyRestClient() {
        return  RestClient.builder()
                .baseUrl(baseUrl)
                .defaultStatusHandler(HttpStatusCode::isError,
                        (req, res) -> {
                            String body = StreamUtils.copyToString(res.getBody(), StandardCharsets.UTF_8);
                            throw new RickAndMortyApiException(res.getStatusCode(), body);
                        }).build();
    }
}
