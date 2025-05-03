package dev.rebelcraft.news_reader;

import dev.rebelcraft.news_reader.feeds.Feeds;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(Feeds feeds) {
		return args -> {

			String robin = feeds.add( "https://robintegg.com/feed.xml" );
			String owen = feeds.add("https://blog.owenlacey.dev/feed.xml");


		};
	}

}
