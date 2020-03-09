package controller;

import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
public class MongoConfig {

    @Bean
    public MongoDbFactory mongoDbFactory() {
        return new SimpleMongoDbFactory(new MongoClientURI("mongodb+srv://sargon:sargon@cluster0-icdko.mongodb.net/research-engine"));
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());

        return mongoTemplate;
    }

}

