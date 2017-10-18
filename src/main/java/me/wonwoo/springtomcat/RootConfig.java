package me.wonwoo.springtomcat;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(basePackageClasses = {SpringTomcatApplication.class})
@EnableWebMvc
public class RootConfig {

//    @Bean
//    ObjectMapper objectMapper() {
//        return new ObjectMapper();
//    }
}


