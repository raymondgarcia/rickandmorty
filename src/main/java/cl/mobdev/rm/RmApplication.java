package cl.mobdev.rm;

import cl.mobdev.rm.infrastructure.config.RestClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(RestClientConfig.class)
public class RmApplication {

	public static void main(String[] args) {
		SpringApplication.run(RmApplication.class, args);
	}



}
