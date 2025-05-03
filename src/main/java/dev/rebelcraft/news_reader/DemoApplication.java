package dev.rebelcraft.news_reader;

import dev.rebelcraft.news_reader.feeds.RssFeeds;
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
	CommandLineRunner commandLineRunner(RssFeeds rssFeeds) {
		return args -> {

			String robin = rssFeeds.add( "https://robintegg.com/feed.xml" );
			String owen = rssFeeds.add("https://blog.owenlacey.dev/feed.xml");


		};
	}

}
