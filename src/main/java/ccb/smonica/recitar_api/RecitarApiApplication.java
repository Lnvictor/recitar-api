package ccb.smonica.recitar_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class RecitarApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(RecitarApiApplication.class, args);
	}

}
