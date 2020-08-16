package com.spring.faq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FaqRestapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FaqRestapiApplication.class, args);
	}

}
