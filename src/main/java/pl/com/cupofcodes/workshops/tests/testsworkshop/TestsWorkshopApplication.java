package pl.com.cupofcodes.workshops.tests.testsworkshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class TestsWorkshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestsWorkshopApplication.class, args);
    }

}
