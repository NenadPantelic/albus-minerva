package rs.ac.kg.fin.albus.minerva;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@ConfigurationPropertiesScan
@SpringBootApplication
public class MinervaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinervaApplication.class, args);
    }

}
