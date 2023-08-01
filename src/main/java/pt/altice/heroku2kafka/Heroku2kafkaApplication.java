package pt.altice.heroku2kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import pt.altice.heroku2kafka.service.Heroku2KafkaService;

@SpringBootApplication
public class Heroku2kafkaApplication implements ApplicationRunner {

	@Autowired
	Heroku2KafkaService service;

	public static void main(String[] args) {
		SpringApplication.run(Heroku2kafkaApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		service.run();
	}

}
