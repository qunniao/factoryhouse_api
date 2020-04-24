package com.baoge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//@SpringBootApplication
//public class BaogeApplication {
//    public static void main(String[] args) {
//        SpringApplication.run(BaogeApplication.class, args);
//    }
//
//}
@SpringBootApplication
public class BaogeApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

        return application.sources(BaogeApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(BaogeApplication.class, args);
    }


}
