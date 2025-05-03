package dev.rebelcraft.news_reader;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.BeanNameViewResolver;

@Configuration
public class J2HtmlConfiguration {

    @Bean
    BeanNameViewResolver viewResolver() {
        return new BeanNameViewResolver();
    }

}
