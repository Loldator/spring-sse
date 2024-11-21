package org.example.app;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringSseApplication {

	@Bean
	public Publisher publisher() {
		return new Publisher();
	}

	public static void main(String[] args) {
		new SpringApplicationBuilder(SpringSseApplication.class)
			.bannerMode(Banner.Mode.OFF)
			.profiles()
			.build()
			.run(args);
	}

}
