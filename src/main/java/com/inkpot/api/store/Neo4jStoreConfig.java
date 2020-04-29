package com.inkpot.api.store;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Neo4jStoreConfig {

    @Value("${store.neo4j.uri:bolt://localhost:7687}")
    private String uri;

    @Value("${store.neo4j.username:neo4j}")
    private String username;

    @Value("${store.neo4j.password:neo4j}")
    private String password;

    @Bean
    public Driver driver() {
        return GraphDatabase.driver(uri, AuthTokens.basic(username, password));
    }

}
