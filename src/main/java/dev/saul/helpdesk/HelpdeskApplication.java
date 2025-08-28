package dev.saul.helpdesk;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import dev.saul.helpdesk.model.Topic;
import dev.saul.helpdesk.repository.TopicRepository;

@SpringBootApplication
public class HelpdeskApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelpdeskApplication.class, args);
	}

	@Bean
CommandLineRunner initDatabase(TopicRepository repo) {
    return args -> {
        if (repo.count() == 0) {
            repo.save(new Topic("Infraestructura"));
            repo.save(new Topic("Aplicaciones"));
            repo.save(new Topic("Soporte"));
        }
    };
}


}
